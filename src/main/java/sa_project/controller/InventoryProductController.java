package sa_project.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sa_project.DatabaseConnection;
import sa_project.model.*;
import sa_project.service.productService;
import sa_project.service.reqService;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;


public class InventoryProductController {
    private String styleHover = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #081F37;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #61BDF6;";
    private String styleNormal = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #61BDF6;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #081F37;";
    @FXML private Label dateLabel,usernameLabel,nameLabel,productCode,productType,productID;
    @FXML private Button listRQBtn,createRQBtn,logoutBtn,purchaseProductBtn,EditBtn,BackBtn,searchBtn,addItemBtn,cancelAddProductBtn;
    @FXML private TextField inputSearch,productName,pNameField;
    @FXML private TextArea productSpec,desField;
    @FXML private TableView<ProductDoc> productTable;
    @FXML private TableColumn<ProductDoc, String> productCodeTb;
    @FXML private TableColumn<ProductDoc, String> productNameTb;
    @FXML private TableColumn<ProductDoc, String> productSpecTb;
    @FXML private TableColumn<ProductDoc, Integer> productInTb;
    @FXML private TableColumn<ProductDoc, Integer> productWantTb;
    @FXML private TableColumn<ProductDoc, Integer> productAvailTb;
    @FXML private MenuButton typeChoice;
    @FXML private ImageView icon;
    @FXML private Pane editProductPage, RqList, productList, createProductPage;
    public Account account;
    private reqService reqService;
    private productService productService;
    private ProductsDocList prList;
    private ProductDoc thisProduct;
    private CategoryList caList;
    private NumberFormat prNumFormat = new DecimalFormat("000");
    private boolean checkClick=false;
    @FXML private MenuButton receiveProductBtn;
    @FXML private MenuItem receiveMenuBtn,claimsMenuBtn;


    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                reqService = new reqService();
                productService = new productService();
                usernameLabel.setText(account.getUsername());
                nameLabel.setText(account.getName());
                prList = reqService.getSpecificProductList("SELECT Product_id, Product_name, Product_type, Description, Qty_onhand, Total_qty_req,(Qty_onhand-Total_qty_req) AS amount FROM product_stocks");
                listRQBtn.setStyle(styleHover);
                productName.setDisable(true);
                productSpec.setDisable(true);
                Image ic = new Image("image/pencil-alt-solid.png");
                icon.setImage(ic);
                EditBtn.setText("เเก้ไข");
                checkClick = false;
                showData();
            }
        });
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm", new Locale("en"));
            dateLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    @FXML
    private void handleEditPageClick(ActionEvent click) throws SQLException {
        if(click.getSource() == EditBtn && checkClick){
            if(productName.getText().isEmpty() && !productSpec.getText().isEmpty()){
                alert("ชื่อสินค้า");
            }
            if(productSpec.getText().isEmpty() && !productName.getText().isEmpty()){
                alert("คุณสมบัติ");
            }
            if(productSpec.getText().isEmpty() && productName.getText().isEmpty()){
                alert("คุณสมบัติ-ชื่อสินค้า");
            }
            else{
                productService.updateProductForm("UPDATE product_stocks SET Product_name=" + "'"+productName.getText()+ "'"+",Description=" + "'"+productSpec.getText()+"'"+
                        " WHERE Product_id=" + "'"+thisProduct.getProductId()+ "'");
                productList.toFront();
                showSuccess2();
                initialize();
            }
        }
        if(click.getSource() == EditBtn){
            productName.setDisable(false);
            productSpec.setDisable(false);
            EditBtn.setText("บันทึก");
            Image ic = new Image("image/file-download-solid.png");
            icon.setImage(ic);
            checkClick = true;
        }
    }
    private void setSelectLabel(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        typeChoice.setText(source.getText());
    }

    @FXML public void showData(){
        ObservableList<ProductDoc> productObservableList =  FXCollections.observableArrayList(prList.toList());
        productCodeTb.setCellValueFactory(new PropertyValueFactory<>("productId"));
        productNameTb.setCellValueFactory(new PropertyValueFactory<>("productName"));
        productSpecTb.setCellValueFactory(new PropertyValueFactory<>("description"));
        productInTb.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
        productWantTb.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        productAvailTb.setCellValueFactory(new PropertyValueFactory<>("itemNumForecast"));

        productTable.setItems(productObservableList);

        FilteredList<ProductDoc> searchFilter = new FilteredList<>(productObservableList, b -> true);
        inputSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchFilter.setPredicate(product -> {
                if(newValue == null || newValue.isEmpty()) return true;
                if(product.getProductName().toLowerCase().indexOf(newValue) != -1
                        || product.getDescription().toLowerCase().indexOf(newValue) != -1
                        || product.getProductId().toLowerCase().indexOf(newValue) != -1) return true;
                else return false;
            });
        });
        SortedList<ProductDoc> sortedReq = new SortedList<>(searchFilter);
        sortedReq.comparatorProperty().bind(productTable.comparatorProperty());
        productTable.setItems(sortedReq);
    }
    @FXML public void tableRowOnMouseClick(MouseEvent mouseEvent){
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
            if(mouseEvent.getClickCount() == 2 && !productTable.getSelectionModel().getSelectedCells().isEmpty()){
                ProductDoc selectProduct = productTable.getSelectionModel().getSelectedItem();
                thisProduct = selectProduct;
                prList = reqService.getSpecificProductList("SELECT Product_id, Product_type, Product_name, Description, Qty_onhand, Total_qty_req,(Qty_onhand-Total_qty_req) AS amount FROM product_stocks");
                editProductPage.toFront();
                productID.setText(selectProduct.getProductId());
                productName.setText(selectProduct.getProductName());
                productType.setText(selectProduct.getProductType());
                productSpec.setText(selectProduct.getDescription());
            }
        }
    }
    @FXML public void handleBackBtn(ActionEvent event){
        productList.toFront();
        initialize();
    }
    @FXML public void handleAddProduct(ActionEvent event) throws SQLException {
        createProductPage.toFront();
        EventHandler<ActionEvent> handler = this::setSelectLabel;
        caList = productService.getCategoryList("SELECT Ctg_name, Initials FROM category");
        caList.setMenuItem(typeChoice,handler);
    }
    @FXML public void handleAddItemBtn(ActionEvent event) throws SQLException {
        if(pNameField.getText().isEmpty() && !desField.getText().isEmpty()){
            alert("ชื่อสินค้า");
        }
        else if(desField.getText().isEmpty() && !pNameField.getText().isEmpty() && !typeChoice.getText().equals("ประเภท")){
            alert("คุณสมบัติ");
        }
        else if(desField.getText().isEmpty() && pNameField.getText().isEmpty() && !typeChoice.getText().equals("ประเภท")){
            alert("คุณสมบัติ-ชื่อสินค้า");
        }
        else if(desField.getText().isEmpty() && pNameField.getText().isEmpty() && typeChoice.getText().equals("ประเภท")){
            alert("คุณสมบัติ-ชื่อสินค้า-ประเภท");
        }
        else{
            String id = typeChoice.getText().split(":")[0];
            String type = typeChoice.getText().split(":")[1];
            DatabaseConnection dbConnect = new DatabaseConnection();
            Connection connectDBSales = dbConnect.getConnection();
            String query = "SELECT count(Product_type)+1 AS name FROM product_stocks WHERE Product_type='"+type+"'";
            Statement statement = connectDBSales.createStatement();
            ResultSet idProduct = statement.executeQuery(query);
            idProduct.next();
            int partTwo = idProduct.getInt("name");
            String idFull = id+prNumFormat.format(partTwo);
            String name = pNameField.getText();
            String description = desField.getText();
            ProductForm addProduct = new ProductForm(idFull,type,name,description);
            productService.addProductForm(addProduct);
            pNameField.clear();
            desField.clear();
            typeChoice.setText("ประเภท");
            productList.toFront();
            showSuccess(name);
            initialize();
        }
    }
    @FXML public void handleCancelAddProductBtn(ActionEvent event){
        pNameField.clear();
        desField.clear();
        typeChoice.setText("ประเภท");
    }
    @FXML private void handleSidemenu(ActionEvent menu) throws IOException {
        if(menu.getSource() == listRQBtn){
            listRQBtn.setStyle(styleHover);
            listRQBtn.setOnMouseExited(event -> listRQBtn.setStyle(styleHover));
            productList.toFront();
            initialize();
        }
        else if(menu.getSource() == createRQBtn){
            createRQBtn = (Button) menu.getSource();
            Stage stage = (Stage) createRQBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StockOut.fxml"));
            stage.setScene(new Scene(loader.load(),1280,768));
            InventoryStockOutController controller = loader.getController();
            controller.setAccount(account);

            stage.show();
        }
        else if(menu.getSource() == purchaseProductBtn){
            purchaseProductBtn = (Button) menu.getSource();
            Stage stage = (Stage) purchaseProductBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InventoryPurchaseProduct.fxml"));
            stage.setScene(new Scene(loader.load(),1280,768));
            InventoryPurchaseProductController controller = loader.getController();
            controller.setAccount(account);

            stage.show();
        }
        else if(menu.getSource() == receiveMenuBtn){
            receiveProductBtn.setText(receiveMenuBtn.getText());
            Stage stage = (Stage) receiveProductBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InventoryReceiveAndClaim.fxml"));
            stage.setScene(new Scene(loader.load(),1280,768));
            InventoryReceiveAndClaimController controller = loader.getController();
            controller.setAccount(account);
            controller.setReceiveMenuBtn();
            stage.show();

        }
        else if(menu.getSource() == claimsMenuBtn){
            receiveProductBtn.setText(claimsMenuBtn.getText());
            Stage stage = (Stage) receiveProductBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InventoryReceiveAndClaim.fxml"));
            stage.setScene(new Scene(loader.load(),1280,768));
            InventoryReceiveAndClaimController controller = loader.getController();
            controller.setAccount(account);
            controller.setPaneClaim();
            stage.show();
        }
    }
    @FXML public void handleLogOutBtn(ActionEvent event) throws IOException {
        logoutBtn = (Button) event.getSource();
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
        stage.setScene(new Scene(loader.load(), 1280, 768));

        HomeController c = new HomeController();

        stage.show();
    }
    public void alert(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ไม่สามารถเพิ่มสินค้าได้");
        alert.setHeaderText("กรุณาตรวจกรอกข้อมูลให้ครบถ้วน");
        if(message.equals("ชื่อสินค้า")){
            alert.setContentText("กรุณากรอกชื่อสินค้า");
        }
        else if(message.equals("คุณสมบัติ")){
            alert.setContentText("กรุณากรอกคุณสมบัติของสินค้า");
        }
        else if(message.equals("คุณสมบัติ-ชื่อสินค้า")){
            alert.setContentText("กรุณากรอกคุณสมบัติของสินค้า\nกรุณากรอกชื่อสินค้า");
        }
        else{
            alert.setContentText("กรุณากรอกคุณสมบัติของสินค้า\nกรุณากรอกชื่อสินค้า\nกรุณาเลือกประเภทของสินค้า");
        }
        alert.showAndWait();
    }
    private void showSuccess(String prName){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("เพิ่มสินค้าสำเร็จ");
        alert.setHeaderText("เพิ่มสินค้า "+prName+ " สำเร็จ");
        alert.showAndWait();
    }
    private void showSuccess2(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("เเก้ไขสำเร็จ");
        alert.setHeaderText("เเก้ไขข้อมูลสินค้าสำเร็จ");
        alert.showAndWait();
    }

    public void setAccount(Account account){
        this.account = account;
    }
}
