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
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.util.Duration;
import sa_project.model.*;
import sa_project.service.inService;
import sa_project.service.prService;
import sa_project.service.productService;
import sa_project.service.retService;

import java.io.IOException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class InventoryReceiveAndClaimController {
    @FXML private Pane purchaseList,claimList,receiveList, receivePage;
    @FXML private MenuButton receiveProductBtn;
    @FXML private MenuItem receiveMenuBtn,claimsMenuBtn;
    @FXML private Label usernameLabel,nameLabel,dateLabel;
    @FXML private Button createRQBtn,listRQBtn,purchaseProductBtn,logoutBtn,viewReceiveBtn,backRBtn;

    //PRList
    @FXML private TableView<PrForm> prFormTable;
    @FXML private TableColumn<PrForm,String> prNum;
    @FXML private TableColumn<PrForm,String> prDate;
    @FXML private TableColumn<PrForm,String> prEmp;
    @FXML private TableColumn<PrForm,String> prDuedate;
    @FXML private TableColumn<PrForm,String> prStatus;
    @FXML private TextField searchText;

    //RetList
    @FXML private TableView<RtForm> claimsTable;
    @FXML private TableColumn<RtForm,String> rtNumCol;
    @FXML private TableColumn<RtForm,String> inNumCol;
    @FXML private TableColumn<RtForm,String>rtStatus;
    @FXML private TextField searchClaim;
    @FXML private Pane claimDetails;
    @FXML private Label claimNum,inNumc;
    //RetProductList
    @FXML private TableView<ProductDoc> claimListTb;
    @FXML private TableColumn<ProductDoc,Integer> itemNumc;
    @FXML private TableColumn<ProductDoc,String> productc;
    @FXML private TableColumn<ProductDoc,String> desC;
    @FXML private TableColumn<ProductDoc,Integer> qtyC;
    @FXML private TableColumn<ProductDoc,String> reasonC;
    @FXML private Button  cBackBtn,claimBtn;

    //InList
    @FXML TextField searchReceive;
    @FXML private TableView<InForm> InTb;
    @FXML private TableColumn<InForm,String> inNumT;
    @FXML private TableColumn<InForm,String>prNumT;
    @FXML private TableColumn<InForm,String> inDateT;
    @FXML private TableColumn<InForm,String> empReceive;
    @FXML private Label inNo, prNo, receiver, inDueDate, inDate;
    @FXML private Button inClose, inConfirm;
    @FXML private TableView<ProductDoc> inPdTb;
    @FXML private TableColumn<ProductDoc,Integer> itemIn;
    @FXML private TableColumn<ProductDoc,String> productIn;
    @FXML private TableColumn<ProductDoc,String> desIn;
    @FXML private TableColumn<ProductDoc,Integer> qtyPr;
    @FXML private TableColumn<ProductDoc,String> receiveIn;
    @FXML private TableColumn<ProductDoc,Integer> scrapC;


    private Account account;
    private RtForm rtSelect;
    private InForm inSelect;
    private PrForm prSelect;
    private ProductsDocList prList,inProductList;
    private ProductsDocList toReceiveList = new ProductsDocList();
    private ProductsDocList claimsProList = new ProductsDocList();
    private PrList prFormList;
    private InList inFormList;
    private inService in_service;
    private RtFormList retFormList;
    private retService rt_service;
    private prService pr_service;
    private productService productService;
    private NumberFormat inNumFormat = new DecimalFormat("0000");
    public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                in_service = new inService();
                productService = new productService();
                pr_service = new prService();
                try {
                    inFormList = in_service.getAllInForm("SELECT IN_no,PR_no,IN_date,Emp_id,Emp_name FROM in_forms NATURAL JOIN employees;");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                try {
                    prFormList = pr_service.getAllPrFrom("SELECT PR_no,Emp_id,PR_date,PR_status,IN_due_date,Emp_name FROM pr_forms NATURAL JOIN employees;");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                rt_service = new retService();
                try {
                    retFormList = rt_service.getReturnList("SELECT * FROM ret_forms");
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                usernameLabel.setText(account.getUsername());
                nameLabel.setText(account.getName());
                showRetList();
                showPrList();
                showInList();
            }
        });
        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy HH:mm", new Locale("en"));
            dateLabel.setText(LocalDateTime.now().format(formatter));
        }), new KeyFrame(Duration.seconds(1)));
        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
    }
    private void showPrList(){
        ObservableList<PrForm> prList = FXCollections.observableList(prFormList.toList());
        prNum.setCellValueFactory(new PropertyValueFactory<>("prNumber"));
        prDate.setCellValueFactory(new PropertyValueFactory<>("prDate"));
        prEmp.setCellValueFactory(new PropertyValueFactory<>("empId"));
        prDuedate.setCellValueFactory(new PropertyValueFactory<>("prDueDate"));
        prStatus.setCellValueFactory(new PropertyValueFactory<>("prStatus"));
        prFormTable.setItems(prList);
        FilteredList<PrForm> searchFilter = new FilteredList<>(prList, b -> true);
        searchText.textProperty().addListener((observable, oldValue, newValue) -> {
            searchFilter.setPredicate(pr -> {
                if(newValue == null || newValue.isEmpty()) return true;
                if(pr.getPrNumber().indexOf(newValue) != -1 || pr.getPrStatus().indexOf(newValue) != -1
                        || pr.getEmpName().indexOf(newValue) != -1 || pr.getEmpId().indexOf(newValue) != -1
                        || pr.getPrNumber().toLowerCase().indexOf(newValue) != -1 || pr.getPrStatus().toLowerCase().indexOf(newValue) != -1
                        || pr.getEmpName().toLowerCase().indexOf(newValue) != -1 || pr.getEmpId().toLowerCase().indexOf(newValue) != -1) return true;
                else return false;
            });
        });
        SortedList<PrForm> sortedPr = new SortedList<>(searchFilter);
        sortedPr.comparatorProperty().bind(prFormTable.comparatorProperty());
        prFormTable.setItems(sortedPr);
    }

    private void showRetList(){
        ObservableList<RtForm> rtList = FXCollections.observableList(retFormList.toList());
        rtNumCol.setCellValueFactory(new PropertyValueFactory<>("rtNum"));
        inNumCol.setCellValueFactory(new PropertyValueFactory<>("inNum"));
        rtStatus.setCellValueFactory(new PropertyValueFactory<>("rtStatus"));
        claimsTable.setItems(rtList);
        FilteredList<RtForm> searchFilter = new FilteredList<>(rtList, b -> true);
        searchClaim.textProperty().addListener((observable, oldValue, newValue) -> {
            searchFilter.setPredicate(rt -> {
                if(newValue == null || newValue.isEmpty()) return true;
                if(rt.getRtNum().indexOf(newValue) != -1 || rt.getRtStatus().indexOf(newValue) != -1
                        || rt.getInNum().indexOf(newValue) != -1 || rt.getInNum().toLowerCase().indexOf(newValue) != -1
                        || rt.getRtNum().toLowerCase().indexOf(newValue) != -1 || rt.getRtStatus().toLowerCase().indexOf(newValue) != -1) return true;
                else return false;
            });
        });
        SortedList<RtForm> sortedRt = new SortedList<>(searchFilter);
        sortedRt.comparatorProperty().bind(claimsTable.comparatorProperty());
        claimsTable.setItems(sortedRt);
    }
    private void showInList(){
        ObservableList<InForm> inList = FXCollections.observableList(inFormList.toList());
        inNumT.setCellValueFactory(new PropertyValueFactory<>("inNum"));
        prNumT.setCellValueFactory(new PropertyValueFactory<>("prNum"));
        inDateT.setCellValueFactory(new PropertyValueFactory<>("inDate"));
        empReceive.setCellValueFactory(new PropertyValueFactory<>("empId"));
        InTb.setItems(inList);
        FilteredList<InForm> searchFilter = new FilteredList<>(inList, b -> true);
        searchReceive.textProperty().addListener((observable, oldValue, newValue) -> {
            searchFilter.setPredicate(in -> {
                if(newValue == null || newValue.isEmpty()) return true;
                if(in.getInNum().indexOf(newValue) != -1 || in.getPrNum().indexOf(newValue) != -1
                        || in.getEmpId().indexOf(newValue) != -1 || in.getEmpId().toLowerCase().indexOf(newValue) != -1
                        || in.getInNum().toLowerCase().indexOf(newValue) != -1 || in.getPrNum().toLowerCase().indexOf(newValue) != -1) return true;
                else return false;
            });
        });
        SortedList<InForm> sortedIn = new SortedList<>(searchFilter);
        sortedIn.comparatorProperty().bind(InTb.comparatorProperty());
        InTb.setItems(sortedIn);
    }

    @FXML public void tableRowOnMouseClick(MouseEvent mouseEvent) throws SQLException {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(mouseEvent.getClickCount() == 2 && !claimsTable.getSelectionModel().getSelectedCells().isEmpty()) {
                RtForm clickedRet = claimsTable.getSelectionModel().getSelectedItem();
                rtSelect = clickedRet;
                prList = rt_service.getProductList("SELECT RT_item_num,RT_no,Product_id,RT_qty,RT_reason,Product_name,Description FROM ret_product_list NATURAL JOIN product_stocks WHERE RT_no ="+ "'"+ clickedRet.getRtNum()+"'");
                claimDetails.toFront();
                if(rtSelect.getRtStatus().equals("Received")) {claimBtn.setDisable(true);}
                else {claimBtn.setDisable(false);}
                claimNum.setText("เลขที่ใบเคลม : "+clickedRet.getRtNum());
                inNumc.setText("เลขที่ใบรับของ : "+clickedRet.getInNum());
                ObservableList<ProductDoc> productList = FXCollections.observableArrayList(prList.toList());
                itemNumc.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
                productc.setCellValueFactory(new PropertyValueFactory<>("productName"));
                desC.setCellValueFactory(new PropertyValueFactory<>("description"));
                qtyC.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                reasonC.setCellValueFactory(new PropertyValueFactory<>("claimsReason"));
                claimListTb.setItems(productList);
            }
        }
    }

    @FXML public void tableRowOnMouseClickToReceive(MouseEvent mouseEvent) throws SQLException{
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(mouseEvent.getClickCount() == 2 && !prFormTable.getSelectionModel().getSelectedCells().isEmpty()) {
                PrForm clickedIn = prFormTable.getSelectionModel().getSelectedItem();
                prSelect = clickedIn;
                toReceiveList = pr_service.getPrProductList("SELECT PR_item_num,PR_no,Product_id,PR_qty,Product_name,Description " +
                        "FROM PR_product_list NATURAL JOIN product_stocks WHERE PR_no ="+ "'"+ clickedIn.getPrNumber()+"'");
                inProductList = in_service.getProductList("SELECT IN_item_num,In_no,PR_qty,Product_id,Product_name,Description,IN_qty FROM in_product_list NATURAL JOIN product_stocks NATURAL JOIN in_forms NATURAL JOIN pr_product_list WHERE PR_no = "+"'"+clickedIn.getPrNumber()+"';");
                inSelect = in_service.getInNoFromPrNo("'"+prSelect.getPrNumber()+"'");
                inProductList.setToTableIn();
                receivePage.toFront();
                // เข้าหน้าโชวใบสั่งซื้อที่สถานะ Received และใบรับของ
                if(prSelect.getPrStatus().equals("Received")) {
                    inConfirm.setDisable(true);
                    prNo.setText("เลขที่ใบสั่งซื้อ : " + prSelect.getPrNumber());
                    inDueDate.setText("วันกำหนดรับ : "+prSelect.getPrDueDate());
                    inNo.setText("เลขที่ใบรับของ : "+inSelect.getInNum());
                    receiver.setText("ผู้รับของ : "+inSelect.getEmpName());
                    inDate.setText("วันที่รับสินค้า : "+inSelect.getInDate());
                    ObservableList<ProductDoc> productList = FXCollections.observableArrayList(inProductList.toList());
                    itemIn.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
                    productIn.setCellValueFactory(new PropertyValueFactory<>("productName"));
                    desIn.setCellValueFactory(new PropertyValueFactory<>("description"));
                    receiveIn.setCellValueFactory(new PropertyValueFactory<>("receiveStr"));
                    qtyPr.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                    scrapC.setCellValueFactory(new PropertyValueFactory<>("badQty"));
                    scrapC.setText("เคลม");
                    inPdTb.setItems(productList);
                }
                // เข้าหน้าสร้างใบรับของ มีการบันทึกข้อมูลจาก pr_product_list ไปยัง in_product_list
                else {
                    inPdTb.setEditable(true);
                    inConfirm.setDisable(false);
                    inNo.setText("เลขที่ใบรับของ : IN"+inNumFormat.format(inFormList.toList().size()+1));
                    prNo.setText("เลขที่ใบสั่งซื้อ : " + prSelect.getPrNumber());
                    receiver.setText("ผู้รับของ : "+account.getName());
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy", new Locale("en"));
                    inDate.setText("วันรับของ : "+LocalDateTime.now().format(formatter));
                    inDueDate.setText("วันกำหนดรับ : "+prSelect.getPrDueDate());

                    ObservableList<ProductDoc> productList = FXCollections.observableArrayList(toReceiveList.toList());
                    itemIn.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
                    productIn.setCellValueFactory(new PropertyValueFactory<>("productName"));
                    desIn.setCellValueFactory(new PropertyValueFactory<>("description"));
                    qtyPr.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                    receiveIn.setCellFactory(TextFieldTableCell.forTableColumn());
                    receiveIn.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductDoc, String>>() {
                                                  @Override
                                                  public void handle(TableColumn.CellEditEvent<ProductDoc, String> event) {
                                                      toReceiveList.toReceive(event.getRowValue().getItemNum(), event.getNewValue());
                                                      scrapC.setCellValueFactory(new PropertyValueFactory<>("badQty"));
                                                      inPdTb.refresh();
                                                      receiveIn.setCellValueFactory(new PropertyValueFactory<>("receiveStr"));
                                                  }
                                              });
                    inPdTb.setItems(productList);
                }
            }
        }
    }

    @FXML public void tableRowOnMouseClickReceiveFrom(MouseEvent mouseEvent) throws SQLException {
        if(mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
            if(mouseEvent.getClickCount() == 2 && !InTb.getSelectionModel().getSelectedCells().isEmpty()) {
                InForm clickedIn = InTb.getSelectionModel().getSelectedItem();
                String dueDate = in_service.getInDueDate(clickedIn.getInNum());
                inProductList = in_service.getProductList("SELECT IN_item_num,In_no,PR_qty,Product_id,Product_name,Description,IN_qty FROM in_product_list NATURAL JOIN product_stocks NATURAL JOIN in_forms NATURAL JOIN pr_product_list WHERE PR_no = "+"'"+clickedIn.getPrNum()+"';");
                inProductList.setToTableIn();
                receivePage.toFront();
                inConfirm.setDisable(true);
                inNo.setText("เลขที่ใบรับของ : "+clickedIn.getInNum());
                prNo.setText("เลขที่ใบสั่งซื้อ : " +clickedIn.getPrNum());
                inDate.setText("วันที่รับสินค้า : "+clickedIn.getInDate());
                receiver.setText("ผู้รับของ : "+clickedIn.getEmpName());
                inDueDate.setText("วันกำหนดรับ : "+dueDate);
                ObservableList<ProductDoc> productList = FXCollections.observableArrayList(inProductList.toList());
                itemIn.setCellValueFactory(new PropertyValueFactory<>("itemNum"));
                productIn.setCellValueFactory(new PropertyValueFactory<>("productName"));
                desIn.setCellValueFactory(new PropertyValueFactory<>("description"));
                receiveIn.setCellValueFactory(new PropertyValueFactory<>("receiveStr"));
                qtyPr.setCellValueFactory(new PropertyValueFactory<>("quantity"));
                scrapC.setCellValueFactory(new PropertyValueFactory<>("badQty"));
                scrapC.setText("เคลม");
                inPdTb.setItems(productList);

            }
        }
    }
    @FXML public void handleInConfirm(ActionEvent actionEvent) throws SQLException, IOException {
        String inNum = inNo.getText().split(": ")[1];
        String prNum = prNo.getText().split(": ")[1];
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd", new Locale("en"));
        String inD = LocalDateTime.now().format(formatter);
        String empId = account.getUsername();
        String empName = account.getName();
        InForm inForm = new InForm(inNum,prNum,inD,empId,empName);
        prSelect.setPrStatus("Received");
        pr_service.updatePrForm("UPDATE pr_forms SET PR_Status =" + "'"+prSelect.getPrStatus()+ "'"+
                " WHERE PR_no = ",prSelect);
        productService.updateQtyStockForm(toReceiveList);
        in_service.addInForm(inForm);
        in_service.addInList(inNum,toReceiveList);
        if(toReceiveList.checkIfScrap()){
            String returnNum = rt_service.getRTNumber();
            RtForm rtForm = new RtForm(returnNum,inNum,"Waiting");
            rt_service.addRtForm(rtForm);
            claimsProList = toReceiveList.getrtFormList();
            rt_service.addRtList(returnNum,claimsProList);
            Stage stage = new Stage();
            Parent root = null;
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/PopUpClaim.fxml"));
            root = loader.load();
            PopUpClaimController popUpClaimController = loader.getController();
            popUpClaimController.setClaimNum(returnNum);
            if(root!=null){
                Scene scene = new Scene(root);
                stage.setScene(scene);
                stage.setResizable(false);
                stage.showAndWait();
                initialize();
            }
            receiveList.toFront();
        }
        else {
           receiveList.toFront();
           initialize();
        }
    }

    @FXML private  void handleTop(ActionEvent topBtn) throws SQLException {
        if(topBtn.getSource() == claimBtn){
            rt_service.updateRetStatus(rtSelect);
            productService.updateQtyStockFormClaims(prList);
            initialize();
            receiveProductBtn.setText(claimBtn.getText());
            claimList.toFront();
        }
        if(topBtn.getSource() == cBackBtn) claimList.toFront();
        if(topBtn.getSource() == viewReceiveBtn){
            showInList();
            receiveList.toFront();
        }
        if(topBtn.getSource() == backRBtn || topBtn.getSource() ==inClose){
            purchaseList.toFront();}
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
            purchaseList.toFront();


        }
        else if(menu.getSource() == claimsMenuBtn){
            receiveProductBtn.setText(claimsMenuBtn.getText());
            claimList.toFront();
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
    public void setPaneClaim() {
        receiveProductBtn.setText(claimsMenuBtn.getText());
        claimList.toFront();
    }
    public void setReceiveMenuBtn(){
        receiveProductBtn.setText(receiveMenuBtn.getText());
    }
}
