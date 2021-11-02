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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.Duration;
import sa_project.model.ProductDoc;
import sa_project.model.ProductsDocList;
import sa_project.model.RtForm;
import sa_project.service.reqService;
import sa_project.service.retService;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PopUpClaimController {
    @FXML private TableView<ProductDoc> ClaimsList;
    @FXML private TableColumn<ProductDoc,String> ProductName;
    @FXML private TableColumn<ProductDoc,String> Reason;
    @FXML private Button confirmBtn;
    private ProductsDocList rtProductList;
    private retService service;
    private String rtNum;
  public void initialize(){
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                service = new retService();
                rtProductList = service.getProductList("SELECT RT_item_num,RT_no,Product_id,RT_qty,RT_reason,Product_name,Description FROM ret_product_list NATURAL JOIN product_stocks WHERE RT_no ="+ "'"+ rtNum+"'");
                showData();
            }
        });
    }
    private void showData(){
    ClaimsList.setEditable(true);
        ObservableList<ProductDoc> list = FXCollections.observableArrayList(rtProductList.toList());
        ProductName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        //Reason.setCellValueFactory(new PropertyValueFactory<>("claimsReason"));
        Reason.setCellFactory(TextFieldTableCell.forTableColumn());
        Reason.setOnEditCommit(new EventHandler<TableColumn.CellEditEvent<ProductDoc, String>>() {
            @Override
            public void handle(TableColumn.CellEditEvent<ProductDoc, String> event) {
                rtProductList.toSetReason(event.getRowValue().getItemNum(), event.getNewValue());
                Reason.setCellValueFactory(new PropertyValueFactory<>("claimsReason"));
                ClaimsList.refresh();
            }
        });
        ClaimsList.setItems(list);

    }
    @FXML private void handleConfirm(ActionEvent event) throws SQLException {
      service.updateReason(rtNum,rtProductList);
        confirmBtn = (Button)event.getSource();
        Stage stage = (Stage) confirmBtn.getScene().getWindow();
        stage.close();
    }
    public void setClaimNum(String rtNo){rtNum = rtNo;}
}
