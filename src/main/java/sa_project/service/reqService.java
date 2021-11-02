package sa_project.service;

import sa_project.DatabaseConnection;
import sa_project.model.ProductDoc;
import sa_project.model.ProductsDocList;
import sa_project.model.ReqForm;
import sa_project.model.ReqList;

import java.sql.*;


public class reqService {
    private ReqList reqForms;
    private ProductsDocList products;

    public reqService() {
        reqForms = new ReqList();
        products = new ProductsDocList();
    }

    private void readReqFormData(String query) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            String rqNumber = queryResult.getString("RQ_no");
            String rqDate = queryResult.getString("RQ_date");
            String rqDueDate = queryResult.getString("RQ_due_date");
            String deliveriedDate = queryResult.getString("Deliveried_date");
            String rqStatus = queryResult.getString("RQ_status");
            String orderNum = queryResult.getString("OR_no");
            String empId = queryResult.getString("Emp_id");
            String empName = queryResult.getString("Emp_name");
            ReqForm req = new ReqForm(rqNumber,rqDate,rqDueDate,deliveriedDate,rqStatus,orderNum,empId,empName);
            reqForms.addReqList(req);
        }
    }
    public ReqList getRqList(String query){
        try {
            reqForms = new ReqList();
            readReqFormData(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return reqForms;
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
    public ProductsDocList getProductList(String query){
        try {
            products = new ProductsDocList();
            readProductList(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return products;
    }

    private void readSpecificProductList(String query) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        ResultSet queryResult = statement.executeQuery(query);
        while (queryResult.next()){
            int itemNo = queryResult.getInt("Qty_onhand");
            String prId = queryResult.getString("Product_id");
            String prName = queryResult.getString("Product_name");
            String des = queryResult.getString("Description");
            int qty = queryResult.getInt("Total_qty_req");
            String prType = queryResult.getString("Product_type");
            int inventory = queryResult.getInt("Qty_onhand");
            int itemForecast = queryResult.getInt("amount");
            ProductDoc product = new ProductDoc(itemNo,prId,prName,des,qty,prType,inventory,itemForecast,"");
            products.addProduct(product);
        }
    }
    public ProductsDocList getSpecificProductList(String query){
        try {
            products = new ProductsDocList();
            readSpecificProductList(query);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return products;
    }

    public void updateRqForm(String query,ReqForm rqUpdate) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        String num = "'"+rqUpdate.getRqNumber()+"'";
        Statement statement = connectDBSales.createStatement();
        statement.executeUpdate(query+num+";");
    }
    public void updateRqStatus(String query,ReqForm rqUpdate) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        String num = "'"+rqUpdate.getRqNumber()+"'";
        Statement statement = connectDBSales.createStatement();
        statement.executeUpdate(query+num+";");
    }
    public void addRqForm(ReqForm rqform) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        String rqNo ="'" + rqform.getRqNumber()+"'" ;
        String rqDate = "'" +rqform.getRqDate()+"'" ;
        String rqDue = "'" +rqform.getRqDueDate()+"'" ;
        String status = "'" +rqform.getRqStatus()+"'" ;
        String order = "'" +rqform.getOrderNum()+"'" ;
        String emp = "'" +rqform.getEmpId()+"'" ;
        String toInsert ="("+rqNo+","+rqDate+","+rqDue+","+"NULL"+","+status+","+order+","+emp+")";
        String query = "INSERT INTO req_forms (RQ_no, RQ_date, RQ_due_date, Deliveried_date, RQ_status, OR_no, Emp_id) VALUES " +toInsert;
        statement.executeUpdate(query);
    }

    public void addRqList(String rqNo,ProductsDocList products) throws SQLException {
        DatabaseConnection dbConnect = new DatabaseConnection();
        Connection connectDBSales = dbConnect.getConnection();
        Statement statement = connectDBSales.createStatement();
        String query = "INSERT INTO req_product_list (RQ_item_num, RQ_no, Product_id, RQ_qty) VALUES "+ products.toInsert(rqNo);
        statement.executeUpdate(query);
    }

}
