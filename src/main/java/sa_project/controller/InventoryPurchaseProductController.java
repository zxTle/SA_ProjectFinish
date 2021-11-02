package sa_project.controller;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sa_project.model.*;
import sa_project.service.prService;
import sa_project.service.productService;
import sa_project.service.reqService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InventoryPurchaseProductController {
    private String styleHover = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #081F37;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #61BDF6;";
    private String styleNormal = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #61BDF6;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #081F37;";
    @FXML
    private Label dateLabel,usernameLabel,nameLabel
            ,rqNum,orNum,empName,rqDate,rqDue,rqShipDate
            ,prNum,prDate;
    @FXML private DatePicker prDue;
    @FXML private Button rqListBtn, listRQBtn, logoutBtn, purchaseProductBtn,discardBtn,AddBtn,createPrBtn;
    @FXML private Pane reqList,purchaseProduct,reqDetails;
    @FXML private MenuButton typeChoice;
    @FXML private TextField orderNum;
    @FXML private TableView<ProductDoc> prTable;
    @FXML private TableColumn<ProductDoc,Integer> prOrder;
    @FXML private TableColumn<ProductDoc,String> prProduct;
    @FXML private TableColumn<ProductDoc,String> prProductSpec;
    @FXML private TableColumn<ProductDoc,Integer> numOrder;
    @FXML private MenuButton receiveProductBtn;
    @FXML private MenuItem receiveMenuBtn,claimsMenuBtn;
    private CategoryList caList;
    private productService productService;
    private prService prService;
    private reqService service;
    private NumberFormat prNumFormat = new DecimalFormat("0000");
    private ReqList rqList;
    private ProductsList productsList;
    private ReqForm rqselect;
    private PrList prList;
    private PrForm prForm;
    private Account account;
    private ProductsDocList createList = new ProductsDocList();



    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                service = new reqService();
                productService = new productService();
                usernameLabel.setText(account.getUsername());
                nameLabel.setText(account.getName());
                purchaseProductBtn.setStyle(styleHover);
                EventHandler<ActionEvent> handler = this::setSelectLabel;
                prService =  new prService();
                try {
                    prList = prService.getAllPrFrom("SELECT PR_no,PR_date,PR_status,IN_due_date,Emp_id,Emp_name FROM pr_forms NATURAL JOIN employees");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    productsList = productService.getProductsList("SELECT * FROM product_stocks");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                prNum.setText("PR"+prNumFormat.format(prList.toList().size()+1));
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", new Locale("en"));
                prDate.setText(LocalDateTime.now().format(formatter));
                empName.setText(account.getName());
                productsList.setMenuItem(typeChoice,handler);
            }

            private void setSelectLabel(ActionEvent event){
                MenuItem source = (MenuItem) event.getSource();
                typeChoice.setText(source.getText());
            }
        });
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm", new Locale("en"));
            dateLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    @FXML private void handleCreatePrPage(ActionEvent e) throws SQLException, IOException {
        if(e.getSource() == discardBtn){
            prDue.getEditor().clear();
            typeChoice.setText("ชื่อสินค้า");
            orderNum.clear();
            prTable.getItems().clear();

        }
        if(e.getSource() == AddBtn){
            ObservableList<ProductDoc> createPrList = FXCollections.observableArrayList(createList.toList());
            prOrder.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
            prProduct.setCellValueFactory(new PropertyValueFactory<>("productName"));
            prProductSpec.setCellValueFactory(new PropertyValueFactory<>("description"));
            numOrder.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            prTable.setItems(createPrList);
            if(typeChoice.getText().equals("สินค้า") && (orderNum.getText().equals("") || Integer.parseInt(orderNum.getText()) <= 0)){
                alert2("");
            }
            else if (typeChoice.getText().equals("สินค้า") && (!(orderNum.getText().equals(""))  || Integer.parseInt(orderNum.getText()) <= 0)){
                alert2("สินค้า");
            }
            else if(!(typeChoice.getText().equals("สินค้า")) && (orderNum.getText().equals("")  || Integer.parseInt(orderNum.getText()) <= 0)){
                alert2("จำนวน");
            }
            else{
                String id = typeChoice.getText().split(":")[0];
                String name = typeChoice.getText().split(":")[1];
                Integer qty = Integer.valueOf(orderNum.getText());
                String des = productsList.getDescription(id);
                ProductDoc product = new ProductDoc(createList.toList().size()+1,id,name,des,qty,"",0,0);
                createList.addProduct(product);
                prTable.getItems().add(product);
                orderNum.clear();
                typeChoice.setText("สินค้า");
            }
        }
    }
    @FXML private void handleCreatePrBtn(ActionEvent event) throws SQLException, IOException {
        if(prDue.getEditor().getText().equals("") && createList.toList().size()==0){
            alert1("");
        }
        else if(prDue.getEditor().getText().equals("") && createList.toList().size()!=0){
            alert1("วันที่");
        }
        else if(!(prDue.getEditor().getText().equals("")) && createList.toList().size()==0){
            alert1("สินค้า");
        }
        else if(prDue.getEditor().getText().equals("") && createList.toList().size()==0){
            alert1("สินค้า-วันที่");
        }
        else {
            String prNo = "PR"+prNumFormat.format(prList.toList().size()+1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("en"));
            String prDate = LocalDateTime.now().format(formatter);
            String dueDate = prDue.getValue().toString();
            PrForm createPr = new PrForm(prNo,prDate,"Waiting",dueDate,account.getUsername(), account.getName());
            prService.addPrForm(createPr);
            prService.addPrList(prNo,createList);
            showSuccess(prNo);

            createPrBtn = (Button) event.getSource();
            Stage stage = (Stage) createPrBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StockOut.fxml"));
            stage.setScene(new Scene(loader.load(),1280,768));
            InventoryStockOutController controller = loader.getController();
            controller.setAccount(account);

            stage.show();
        }
    }
    private void alert1(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ไม่สามารถสร้างใบขอสั่งซื้อได้!");
        alert.setHeaderText("กรุณาตรวจกรอกข้อมูลให้ครบถ้วน");
        if (message == "สินค้า"){
            alert.setContentText("กรุณาเลือกสินค้าที่ต้องการสั่งซื้อ");
        }
        else if(message == "วันที่"){
            alert.setContentText("กรุณาเลือกวันกำหนดรับ");
        }
        else if(message == "สินค้า-วันที่"){
            alert.setContentText("กรุณาเลือกวันกำหนดรับ\nกรุณาเพิ่มรายการสินค้าที่ต้องการเบิก");
        }
        else {
            alert.setContentText("กรุณาเลือกวันกำหนดรับ\nกรุณาเพิ่มรายการสินค้าที่ต้องการเบิก");
        }
        alert.showAndWait();
    }

    private void alert2(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ไม่สามารถเพิ่มสินค้าได้!");
        alert.setHeaderText("กรุณาตรวจกรอกข้อมูลให้ครบถ้วน");
        if (message == "สินค้า"){
            alert.setContentText("กรุณาเลือกสินค้าที่จะสั่งซื้อ");
        }
        else if(message == "จำนวน"){
            alert.setContentText("กรุณากรอกจำนวนสินค้า โดยจำนวนสินค้าจะต้องมีค่ามากกว่า 0");
        }
        else {
            alert.setContentText("กรุณาเลือกสินค้าที่จะสั่งซื้อ\n"+"กรุณากรอกจำนวนสินค้า โดยจำนวนสินค้าจะต้องมีค่ามากกว่า 0");
        }
        alert.showAndWait();
    }
    private void showSuccess(String prNum){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("สร้างใบขอสั่งซื้อสินค้าสำเร็จ!");
        alert.setHeaderText("สร้างใบขอสั่งซื้อสินค้าเลขที่ "+prNum+ " สำเร็จ");
        alert.showAndWait();
    }
    @FXML private void handleSidemenu(ActionEvent menu) throws IOException {
        if(menu.getSource() == listRQBtn){
            listRQBtn = (Button) menu.getSource();
            Stage stage = (Stage) listRQBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/InventoryProducts.fxml"));
            stage.setScene(new Scene(loader.load(),1280,768));
            InventoryProductController controller = loader.getController();
            controller.setAccount(account);

            stage.show();
        }
        else if(menu.getSource() == rqListBtn){
            rqListBtn = (Button) menu.getSource();
            Stage stage = (Stage) rqListBtn.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/StockOut.fxml"));
            stage.setScene(new Scene(loader.load(),1280,768));
            InventoryStockOutController controller = loader.getController();
            controller.setAccount(account);

            stage.show();
        }
        else if(menu.getSource() == purchaseProductBtn){
            purchaseProduct.toFront();
            purchaseProductBtn.setStyle(styleHover);
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
    public void setAccount(Account account){
        this.account = account;
    }
}
