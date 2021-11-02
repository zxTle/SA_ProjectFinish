package sa_project.service;

import sa_project.DatabaseConnection;
import sa_project.model.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class retService {
    private RtFormList returnList;
    private ProductsDocList products;

    public retService() {
        returnList = new RtFormList();
        products = new ProductsDocList();
    }

    private void readRtForm(String query) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBInvent = dbConnect.getConnection();
        Statement statement = connectDBInvent.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            String rtNum = queryResult.getString("RT_no");
            String inNum = queryResult.getString("IN_no");
            String rtStatus = queryResult.getString("RT_status");
            RtForm retForm = new RtForm(rtNum,inNum,rtStatus);
            returnList.addRtForm(retForm);
        }
    }

    public RtFormList getReturnList(String query) throws SQLException {
        readRtForm(query);
        return returnList;
    }

    private void readProductList(String query) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBInvent = dbConnect.getConnection();
        Statement statement = connectDBInvent.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            int itemNo = queryResult.getInt("RT_item_num");
            String prId = queryResult.getString("Product_id");
            String prName = queryResult.getString("Product_name");
            String des = queryResult.getString("Description");
            int qty = queryResult.getInt("RT_qty");
            String reason = queryResult.getString("RT_reason");
            ProductDoc product = new ProductDoc(itemNo,prId,prName,des,qty, "",0,0,reason);
            products.addProduct(product);
        }
    }
    public ProductsDocList getProductList(String query){
        try {
            products = new ProductsDocList();
            readProductList(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return products;
    }

    public void updateRetStatus(RtForm retUpdate) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        String query = "UPDATE ret_forms SET RT_status = 'Received' WHERE RT_no = "+"'"+retUpdate.getRtNum()+"';";
        Statement statement = connectDBSales.createStatement();
        statement.executeUpdate(query);
    }

    public String getRTNumber() throws SQLException {
        String rtNum = "";
        NumberFormat rtNumFormat = new DecimalFormat("0000");
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        String query = "SELECT count(RT_no) +1 AS amount FROM ret_forms";
        Statement statement = connectDBSales.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            rtNum = "RT"+rtNumFormat.format(queryResult.getInt("amount"));
        }
        return rtNum;
    }

    public void addRtForm(RtForm rtform) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        String rtNo ="'" + rtform.getRtNum()+"'" ;
        String inNo = "'"+ rtform.getInNum()+"'";
        String status = "'"+rtform.getRtStatus()+"'";
        String toInsert ="("+rtNo+","+inNo+","+status+")";
        String query = "INSERT INTO ret_forms (RT_no,IN_no,RT_status) VALUES " +toInsert;
        statement.executeUpdate(query);
    }

    public void addRtList(String rtNo,ProductsDocList products) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        String query = "INSERT INTO ret_product_list (RT_item_num, RT_no, Product_id, RT_qty,RT_reason) VALUES "+ products.toInsertClaim(rtNo);
        statement.executeUpdate(query);
    }

    public void updateReason(String rtNum,ProductsDocList upReason) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        rtNum = "'"+rtNum+"'"+";";
        for(int i=0;i<upReason.toList().size();i++){
            String reason = "'"+upReason.toList().get(i).getClaimsReason()+"'";
            System.out.println(rtNum);
            String query = "UPDATE ret_product_list SET RT_reason = " +reason +" WHERE RT_item_num  = "+upReason.toList().get(i).getItemNum()+" AND RT_no = "+rtNum;
            statement.executeUpdate(query);
        }
    }
}
