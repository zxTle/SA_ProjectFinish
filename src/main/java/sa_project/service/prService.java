package sa_project.service;

import sa_project.DatabaseConnection;
import sa_project.model.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class prService {
    private PrList prList;
    private ProductsDocList products;

    public prService() {
        prList = new PrList();
        products =  new ProductsDocList();
    }

    private void readPrForm(String query) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBInvent = dbConnect.getConnection();
        Statement statement = connectDBInvent.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            String prNum = queryResult.getString("PR_no");
            String prDate = queryResult.getString("PR_date");
            String prStatus = queryResult.getString("PR_status");
            String prDue = queryResult.getString("IN_due_date");
            String empId = queryResult.getString("Emp_id");
            String empName = queryResult.getString("Emp_name");
            PrForm prForm = new PrForm(prNum,prDate,prStatus,prDue,empId,empName);
            prList.addPrList(prForm);
        }
    }
    public PrList getAllPrFrom(String query) throws SQLException {
        readPrForm(query);
        return prList;
    }

    private void readProductList(String query) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            int itemNo = queryResult.getInt("RQ_item_num");
            String prId = queryResult.getString("Product_id");
            String prName = queryResult.getString("Product_name");
            String des = queryResult.getString("Description");
            int qty = queryResult.getInt("RQ_qty");
            int inventory = queryResult.getInt("Qty_onhand");
            int itemForecast = queryResult.getInt("amount");
            ProductDoc product = new ProductDoc(itemNo,prId,prName,des,qty, "",inventory,itemForecast,"");
            products.addProduct(product);
        }
    }

    private void readPrProductList(String query) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            int itemNo = queryResult.getInt("PR_item_num");
            String prId = queryResult.getString("Product_id");
            String prName = queryResult.getString("Product_name");
            String des = queryResult.getString("Description");
            int qty = queryResult.getInt("PR_qty");
            ProductDoc product = new ProductDoc(itemNo,prId,prName,des,qty, 0,0);
            products.addProduct(product);
        }
    }

    public ProductsDocList getPrProductList(String query){
        try {
            products = new ProductsDocList();
            readPrProductList(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return products;
    }

    public void updatePrForm(String query, PrForm prUpdate) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        String num = "'"+prUpdate.getPrNumber()+"'";
        Statement statement = connectDBSales.createStatement();
        statement.executeUpdate(query+num+";");
    }

    public void addPrForm(PrForm prForm) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        String prNo ="'" + prForm.getPrNumber()+"'" ;
        String prDate = "'" +prForm.getPrDate()+"'" ;
        String prDue = "'" +prForm.getPrDueDate()+"'" ;
        String status = "'" +prForm.getPrStatus()+"'" ;
        String emp = "'" +prForm.getEmpId()+"'" ;
        String toInsert ="("+prNo+","+prDate+","+prDue+","+status+","+emp+")";
        String query = "INSERT INTO pr_forms (PR_no, PR_date, IN_due_date,PR_status,Emp_id) VALUES " +toInsert;
        statement.executeUpdate(query);
    }
    public void addPrList(String prNo,ProductsDocList products) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        String query = "INSERT INTO pr_product_list (PR_item_num, PR_no, Product_id, PR_qty) VALUES "+ products.toInsert(prNo);
        statement.executeUpdate(query);
    }
}
