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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sa_project.model.*;
import sa_project.service.productService;
import sa_project.service.reqService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class InventoryStockOutController {

    private String styleHover = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #081F37;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #61BDF6;";
    private String styleNormal = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #61BDF6;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #081F37;";
    @FXML private Label dateLabel,usernameLabel,nameLabel,rqNum,orNum,empName,rqDate,rqDue,rqShipDate;
    @FXML private Button rqListBtn, listRQBtn, logoutBtn, purchaseProductBtn,orderProduct,deliveryBtn;
    @FXML private TextField inputSearch,reqStatus;
    @FXML private TableView<ReqForm> reqTable;
    @FXML private TableColumn<ReqForm,String> reqNo;
    @FXML private TableColumn<ReqForm,String> reqEmp;
    @FXML private TableColumn<ReqForm,String> status;
    @FXML private TableColumn<ReqForm,Date> reqDate;
    @FXML private TableColumn<ReqForm,Date> reqDueDate;

    @FXML private TableView<ProductDoc> reqTableDetail;
    @FXML private TableColumn<ProductDoc,Integer> itemNum;
    @FXML private TableColumn<ProductDoc,String> product;
    @FXML private TableColumn<ProductDoc,String> description;
    @FXML private TableColumn<ProductDoc,Integer> qty;
    @FXML private TableColumn<ProductDoc,Integer> inventory;
    @FXML private TableColumn<ProductDoc,Integer> productLeft;
    @FXML private ChoiceBox<String> typeChoice;
    @FXML private Pane reqList,purchaseProduct,reqDetails;
    private reqService service;
    private productService productService;
    private NumberFormat rqNumFormat = new DecimalFormat("0000");
    private ReqList rqList;
    private ReqForm rqselect;
    private ProductsDocList prList;
    private Account account;
    @FXML private MenuButton receiveProductBtn;
    @FXML private MenuItem receiveMenuBtn,claimsMenuBtn;

    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                service = new reqService();
                productService = new productService();
                rqList = service.getRqList("SELECT RQ_no,RQ_date,RQ_due_date,Deliveried_date,RQ_status,OR_no,Emp_id,Emp_name FROM req_forms NATURAL JOIN employees");
                usernameLabel.setText(account.getUsername());
                nameLabel.setText(account.getName());
                rqListBtn.setStyle(styleHover);
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
    private void showData() {
        ObservableList<ReqForm> reqList = FXCollections.observableArrayList(rqList.toList());
        reqNo.setCellValueFactory(new PropertyValueFactory<>("rqNumber"));
        reqEmp.setCellValueFactory(new PropertyValueFactory<>("empId"));
        reqDate.setCellValueFactory(new PropertyValueFactory<>("rqDate"));
        reqDueDate.setCellValueFactory(new PropertyValueFactory<>("rqDueDate"));
        status.setCellValueFactory(new PropertyValueFactory<>("rqStatus"));
        reqTable.setItems(reqList);

        FilteredList<ReqForm> searchFilter = new FilteredList<>(reqList, b -> true);
        inputSearch.textProperty().addListener((observable, oldValue, newValue) -> {
            searchFilter.setPredicate(req -> {
                if(newValue == null || newValue.isEmpty()) return true;
                if(req.getRqNumber().indexOf(newValue) != -1 || req.getRqStatus().indexOf(newValue) != -1
                        || req.getEmpName().indexOf(newValue) != -1 || req.getEmpId().indexOf(newValue) != -1
                        || req.getRqNumber().toLowerCase().indexOf(newValue) != -1 || req.getRqStatus().toLowerCase().indexOf(newValue) != -1
                        || req.getEmpName().toLowerCase().indexOf(newValue) != -1 || req.getEmpId().toLowerCase().indexOf(newValue) != -1) return true;
                else return false;
            });
        });
        SortedList<ReqForm> sortedReq = new SortedList<>(searchFilter);
        sortedReq.comparatorProperty().bind(reqTable.comparatorProperty());
        reqTable.setItems(sortedReq);
    }
    @FXML public void tableRowOnMouseClick(MouseEvent mouseEvent)  {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(mouseEvent.getClickCount() == 2 && !reqTable.getSelectionModel().getSelectedCells().isEmpty()) {
                ReqForm clickedReq = reqTable.getSelectionModel().getSelectedItem();
                rqselect = clickedReq;
                prList = service.getProductList("SELECT RQ_item_num,Product_id,RQ_qty,Product_name,Description,Qty_onhand,(Qty_onhand-Rq_qty) AS amount FROM req_product_list NATURAL JOIN product_stocks WHERE RQ_no = "+ "'"+clickedReq.getRqNumber() + "'");
                reqDetails.toFront();
                rqNum.setText(clickedReq.getRqNumber());
                orNum.setText(clickedReq.getOrderNum());
                empName.setText(clickedReq.getEmpId()+" , "+clickedReq.getEmpName());
                rqDate.setText(clickedReq.getRqDate());
                rqDue.setText(clickedReq.getRqDueDate());
                rqShipDate.setText(clickedReq.getDeliveriedDate());
                if(rqselect.getRqStatus().equals("Waiting")){
                    reqStatus.setText("Waiting");
                    reqStatus.setDisable(true);
                }
                else if(rqselect.getRqStatus().equals("Deliveried")){
                    reqStatus.setText("Deliveried");
                    reqStatus.setDisable(true);
                }
                else if(rqselect.getRqStatus().equals("Cancelled")){
                    reqStatus.setText("Cancelled");
                    reqStatus.setDisable(true);
                }
                ObservableList<ProductDoc> productList = FXCollections.observableArrayList(prList.toList());
                itemNum.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
                product.setCellValueFactory(new PropertyValueFactory<>("productName"));
                description.setCellValueFactory(new PropertyValueFactory<>("description"));
                qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                inventory.setCellValueFactory(new PropertyValueFactory<>("onHand"));
                productLeft.setCellValueFactory(new PropertyValueFactory<>("itemNumForecast"));
                reqTableDetail.setItems(productList);
            }
        }
    }
    @FXML private void handleOrderProduct(ActionEvent event) throws IOException {
        orderProduct = (Button) event.getSource();
        Stage stage = (Stage) orderProduct.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/InventoryPurchaseProduct.fxml"));
        stage.setScene(new Scene(loader.load(),1280,768));
        InventoryPurchaseProductController controller = loader.getController();
        controller.setAccount(account);

        stage.show();
    }
    @FXML private void handleDeliveryBtn(ActionEvent event) throws SQLException {
        if(!prList.checkIfEnough() && rqselect.getRqStatus().equals("Waiting")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ไม่สามารถส่งสินค้าได้!");
            alert.setHeaderText("สินค้าไม่เพียงพอ กรุณาสั่งสินค้าเพิ่มเติมเพื่อส่งสินค้า");
            alert.showAndWait();
        }
        else if(prList.checkIfEnough() && rqselect.getRqStatus().equals("Deliveried")){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ไม่สามารถส่งสินค้าได้!");
            alert.setHeaderText("สินค้าดังกล่าวได้ถูกส่งมอบเเล้ว");
            alert.showAndWait();
        }
        else{
            service.updateRqStatus("UPDATE req_forms SET RQ_Status = 'Deliveried' WHERE RQ_no = ",rqselect);
            productService.updateQtyStockFormReq(prList);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("ส่งมอบสินค้าสำเร็จ!");
            alert.setHeaderText("ส่งมอบสินค้าของใบเบิกสินค้าหมายเลข " + rqselect.getRqNumber() + " สำเร็จ");
            alert.showAndWait();
            reqList.toFront();
            initialize();
        }
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
            rqListBtn.setStyle(styleHover);
            purchaseProductBtn.setStyle(styleNormal);
            rqListBtn.setOnMouseExited(event -> rqListBtn.setStyle(styleHover));
            reqList.toFront();
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
    public void setAccount(Account account){
        this.account = account;
    }
}
