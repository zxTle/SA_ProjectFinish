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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class SalesController {
    private String styleHover = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #081F37;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #61BDF6;";
    private String styleNormal = "-fx-font-family: 'Kanit';\n" + "-fx-font-size: 20px;\n" + "-fx-background-color: #61BDF6;\n" +
            "-fx-background-radius : 0;\n" + "-fx-text-fill : #081F37;";
    private ReqList rqList;
    private ProductsDocList prList;
    private ProductsDocList createList = new ProductsDocList();
    private reqService service;
    private NumberFormat rqNumFormat = new DecimalFormat("0000");
    private ReqForm rqselect;
    @FXML Label usernameLabel, dateLabel,nameLabel,rqNumFm,rqEmpFm,rqDateFm;
    @FXML Button logoutBtn,listRQBtn,createRQMenuBtn;
    @FXML Button CancelReqBtn,Backbtn;
    @FXML Button DiscardReqBtn,CreateReq,AddBtn;
    @FXML Pane reqDetails,reqForm,ReqList;
    @FXML TextField inputSearch,OrderNumInput;
    @FXML DatePicker datePick;
    @FXML private TableView<ReqForm> reqTable;
    @FXML private TableColumn<ReqForm,String> reqNo;
    @FXML private TableColumn<ReqForm,String> reqEmp;
    @FXML private TableColumn<ReqForm,String> status;
    @FXML private TableColumn<ReqForm,Date> reqDate;
    @FXML private TableColumn<ReqForm,Date> reqDueDate;
    @FXML Label rqNum,orNum,empName,rqDate,rqDue,rqShipDate;
    //Details
    @FXML private TableView<ProductDoc> saleTable1;
    @FXML private TableColumn<ProductDoc,Integer> itemNum;
    @FXML private TableColumn<ProductDoc,String> product;
    @FXML private TableColumn<ProductDoc,String> description;
    @FXML private TableColumn<ProductDoc,Integer> qty;
    //Create
    @FXML private TableView<ProductDoc> saleTable;
    @FXML private TableColumn<ProductDoc,Integer> number;
    @FXML private TableColumn<ProductDoc,String> productN;
    @FXML private TableColumn<ProductDoc,String> descrip;
    @FXML private TableColumn<ProductDoc,Integer> qtyRq;



    public Account account;
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                service = new reqService();
                rqList = service.getRqList("SELECT RQ_no,RQ_date,RQ_due_date,Deliveried_date,RQ_status,OR_no,Emp_id,Emp_name FROM req_forms NATURAL JOIN employees");
                showData();
                usernameLabel.setText(account.getUsername());
                nameLabel.setText(account.getName());
                listRQBtn.setStyle(styleHover);
            }
        });
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm", new Locale("en"));
            dateLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    private void resetData(){
        rqList = service.getRqList("SELECT RQ_no,RQ_date,RQ_due_date,Deliveried_date,RQ_status,OR_no,Emp_id,Emp_name FROM req_forms NATURAL JOIN employees");
        showData();
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
    @FXML private void handleLogOutBtn(ActionEvent event) throws IOException {
        logoutBtn = (Button) event.getSource();
        Stage stage = (Stage) logoutBtn.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Home.fxml"));
        stage.setScene(new Scene(loader.load(), 1280, 768));

        HomeController c = new HomeController();

        stage.show();
    }

    @FXML MenuButton productSelect;
    @FXML TextField rqQtyF;
    private ProductsList productsList;
    private productService productService;
    private void setSelectLabel(ActionEvent event){
        MenuItem source = (MenuItem) event.getSource();
        productSelect.setText(source.getText());

    }
    @FXML private void handleSidemenu(ActionEvent menu) throws SQLException {
        if(menu.getSource() == createRQMenuBtn){
            EventHandler<ActionEvent> handler = this::setSelectLabel;
            productService =  new productService();
            productsList = productService.getProductsList("SELECT * FROM product_stocks;");
            productsList.setMenuItem(productSelect,handler);
            createRQMenuBtn.setStyle(styleHover);
            createRQMenuBtn.setOnMouseExited(event -> createRQMenuBtn.setStyle(styleHover));
            reqForm.toFront();
            listRQBtn.setStyle(styleNormal);
            listRQBtn.setOnMouseEntered(event -> listRQBtn.setStyle(styleHover));
            listRQBtn.setOnMouseExited(event -> listRQBtn.setStyle(styleNormal));
            rqNumFm.setText("เลขที่ใบเบิก : "+"RQ"+rqNumFormat.format(rqList.toList().size()+1));
            rqEmpFm.setText("ผู้ออกใบเบิก : "+account.getName());
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd", new Locale("en"));
            rqDateFm.setText("วันที่ออกใบเบิก : "+(LocalDateTime.now().format(formatter)));
        }
        else if(menu.getSource() == listRQBtn){
            listRQBtn.setStyle(styleHover);
            listRQBtn.setOnMouseExited(event -> listRQBtn.setStyle(styleHover));
            ReqList.toFront();
            showData();
            createRQMenuBtn.setStyle(styleNormal);
            createRQMenuBtn.setOnMouseEntered(event -> createRQMenuBtn.setStyle(styleHover));
            createRQMenuBtn.setOnMouseExited(event -> createRQMenuBtn.setStyle(styleNormal));
        }

    }
    @FXML public void tableRowOnMouseClick(MouseEvent mouseEvent)  {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(mouseEvent.getClickCount() == 2 && !reqTable.getSelectionModel().getSelectedCells().isEmpty()) {
                ReqForm clickedReq = reqTable.getSelectionModel().getSelectedItem();
                rqselect = clickedReq;
                prList = service.getProductList("SELECT RQ_item_num,Product_id,RQ_qty,Product_name,Description,Qty_onhand,(Qty_onhand-Total_qty_req) AS amount FROM req_product_list NATURAL JOIN product_stocks WHERE RQ_no = "+ "'"+clickedReq.getRqNumber() + "'");
                reqDetails.toFront();
                if(rqselect.getRqStatus().equals("Cancelled")||rqselect.getRqStatus().equals("Deliveried")) {CancelReqBtn.setDisable(true);}
                else {CancelReqBtn.setDisable(false);}
                rqNum.setText("เลขที่ใบเบิก : "+clickedReq.getRqNumber());
                orNum.setText("เลขออเดอร์  : "+clickedReq.getOrderNum());
                empName.setText("ผู้ออกใบเบิก : Id "+clickedReq.getEmpId()+" , "+clickedReq.getEmpName());
                rqDate.setText("วันที่ออกใบเบิก : "+clickedReq.getRqDate());
                rqDue.setText("วันกำหนดส่ง : "+clickedReq.getRqDueDate());
                rqShipDate.setText("วันนำส่งสินค้า : "+clickedReq.getDeliveriedDate());
                ObservableList<ProductDoc> productList = FXCollections.observableArrayList(prList.toList());
                itemNum.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
                product.setCellValueFactory(new PropertyValueFactory<>("productName"));
                description.setCellValueFactory(new PropertyValueFactory<>("description"));
                qty.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                saleTable1.setItems(productList);
            }
        }
    }
    @FXML private  void handletopBtn(ActionEvent btn) throws SQLException {
        if(btn.getSource() == Backbtn){
            ReqList.toFront();
        }
        else if(btn.getSource() == CancelReqBtn){
            rqList.setStatus(rqselect,"Cancelled");
            service.updateRqForm("UPDATE req_forms SET RQ_Status =" + "'"+rqselect.getRqStatus()+ "'"+
                    " WHERE RQ_no = ",rqselect);
            ReqList.toFront();
            resetData();
        }
    }
    @FXML private void handleCreateRqPage(ActionEvent e) throws SQLException {
        if(e.getSource() == DiscardReqBtn){
            clearForm();
        }
        if(e.getSource() == AddBtn){
            ObservableList<ProductDoc> createListDoc = FXCollections.observableArrayList(createList.toList());
            number.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
            productN.setCellValueFactory(new PropertyValueFactory<>("productName"));
            descrip.setCellValueFactory(new PropertyValueFactory<>("description"));
            qtyRq.setCellValueFactory(new PropertyValueFactory<>("quantity"));
            saleTable.setItems(createListDoc);
            if(productSelect.getText().equals("สินค้า") && (rqQtyF.getText().equals("") || Integer.parseInt(rqQtyF.getText()) == 0)){
                alert2("");
            }
            else if (productSelect.getText().equals("สินค้า") && (!(rqQtyF.getText().equals(""))  || Integer.parseInt(rqQtyF.getText()) != 0)){
                alert2("สินค้า");
            }
            else if(!(productSelect.getText().equals("สินค้า")) && (rqQtyF.getText().equals("")  || Integer.parseInt(rqQtyF.getText()) == 0)){
                alert2("จำนวน");
            }
            else{
                String id = productSelect.getText().split(":")[0];
                String name = productSelect.getText().split(":")[1];
                Integer qty = Integer.valueOf(rqQtyF.getText());
                String des = productsList.getDescription(id);
                ProductDoc product = new ProductDoc(createList.toList().size()+1,id,name,des,qty,"",0,0,"");
                createList.addProduct(product);
                saleTable.getItems().add(product);
                rqQtyF.clear();
                productSelect.setText("สินค้า");
            }
        }
        if(e.getSource() == CreateReq){
            if(OrderNumInput.getText().equals("") && datePick.getEditor().getText().equals("") && createList.toList().size()==0){
                alert1("");
            }
            else if(OrderNumInput.getText().equals("") && !(datePick.getEditor().getText().equals("")) && createList.toList().size()!=0){
                alert1("ออเดอร์");
            }
            else if(!(OrderNumInput.getText().equals("")) && datePick.getEditor().getText().equals("") && createList.toList().size()!=0){
                alert1("วันที่");
            }
            else if(!(OrderNumInput.getText().equals("")) && !(datePick.getEditor().getText().equals("")) && createList.toList().size()==0){
                alert1("สินค้า");
            }
            else if(OrderNumInput.getText().equals("") && datePick.getEditor().getText().equals("") && createList.toList().size()!=0){
                alert1("ออเดอร์-วันที่");
            }
            else if(!(OrderNumInput.getText().equals("")) && datePick.getEditor().getText().equals("") && createList.toList().size()==0){
                alert1("สินค้า-วันที่");
            }
            else if(OrderNumInput.getText().equals("") && !(datePick.getEditor().getText().equals("")) && createList.toList().size()==0){
                alert1("สินค้า-ออเดอร์");
            }
            else {
            String rqNo = "RQ"+rqNumFormat.format(rqList.toList().size()+1);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("en"));
            String rqDate = LocalDateTime.now().format(formatter);
            String dueDate = datePick.getValue().toString();
            ReqForm createReq = new ReqForm(rqNo,rqDate,dueDate,"","Waiting",OrderNumInput.getText(),account.getUsername(), account.getName());
            service.addRqForm(createReq);
            service.addRqList(rqNo,createList);
            productService.updateTotalReq(createList);
            clearForm();
            ReqList.toFront();
            showSuccess(rqNo);
            initialize();
            }
        }
    }

    private void clearForm(){
        OrderNumInput.clear();
        datePick.getEditor().setText("");
        saleTable.getItems().clear();
        createList.toList().clear();
        rqQtyF.clear();
        productSelect.setText("สินค้า");
    }
    private void showSuccess(String rqNum){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("สร้างใบเบิกเรียบร้อย!");
        alert.setHeaderText("สร้างใบเบิกเลขที่ "+rqNum+ " สำเร็จ");
        alert.showAndWait();
    }
    private void alert1(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ไม่สามารถสร้างใบเบิกได้!");
        alert.setHeaderText("กรุณาตรวจกรอกข้อมูลให้ครบถ้วน");
        if (message == "สินค้า"){
            alert.setContentText("กรุณาเลือกสินค้าที่ต้องการเบิก");
        }
        else if(message == "ออเดอร์"){
            alert.setContentText("กรุณากรอกเลขออเดอร์");
        }
        else if(message == "วันที่"){
            alert.setContentText("กรุณาเลือกวันนำส่ง");
        }
        else if(message == "ออเดอร์-วันที่"){
            alert.setContentText("กรุณากรอกเลขออเดอร์\n กรุณาเลือกวันนำส่ง");
        }
        else if(message == "สินค้า-วันที่"){
            alert.setContentText("กรุณาเลือกวันนำส่ง \n กรุณาเพิ่มรายการสินค้าที่ต้องการเบิก");
        }
        else if(message == "สินค้า-ออเดอร์"){
            alert.setContentText("กรุณากรอกเลขออเดอร์\n กรุณาเพิ่มรายการสินค้าที่ต้องการเบิก");
        }
        else {
            alert.setContentText("กรุณากรอกเลขออเดอร์\n กรุณาเลือกวันนำส่ง \n กรุณาเพิ่มรายการสินค้าที่ต้องการเบิก");
        }
        alert.showAndWait();
    }

    private void alert2(String message){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ไม่สามารถเพิ่มสินค้าได้!");
        alert.setHeaderText("กรุณาตรวจกรอกข้อมูลให้ครบถ้วน");
        if (message == "สินค้า"){
            alert.setContentText("กรุณาเลือกสินค้าที่จะขอเบิก");
        }
        else if(message == "จำนวน"){
            alert.setContentText("กรุณากรอกจำนวนที่จะขอเบิก");
        }
        else {
            alert.setContentText("กรุณาเลือกสินค้าที่จะขอเบิก\n"+"กรุณากรอกจำนวนที่จะขอเบิก");
        }
        alert.showAndWait();
    }
    public void setAccount(Account account){
        this.account = account;
    }

}
