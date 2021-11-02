package sa_project.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sa_project.DatabaseConnection;
import sa_project.model.Account;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class HomeController {

    @FXML private Button loginBtn;
    @FXML private Label status;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    public Account account;
    public Account checkLogin = new Account("","");
    public void Login(ActionEvent event){
        if(usernameField.getText().isEmpty() && passwordField.getText().isEmpty()){
            status.setText("Please enter username and password");
        }
        else{
            DatabaseConnection login = new DatabaseConnection();
            Connection connectDB = login.getConnection();

            String verifyLogin = "SELECT Emp_name,Position FROM employees WHERE Emp_id = '" + usernameField.getText() + "' AND Pass = '" + passwordField.getText() + "'";
            try {
                Statement statement = connectDB.createStatement();
                ResultSet queryResult = statement.executeQuery(verifyLogin);

                while(queryResult.next()){
                    if(queryResult.getString(2).equals("Sale")){
                        account = new Account(usernameField.getText(),queryResult.getString(1));
                        checkLogin =account;
                        loginBtn = (Button) event.getSource();
                        Stage stage = (Stage) loginBtn.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader((getClass().getResource("/Sales1.fxml")));
                        stage.setScene(new Scene(loader.load(), 1280, 768));
                        SalesController controller = loader.getController();
                        controller.setAccount(account);

                        stage.show();
                    }
                    if(queryResult.getString(2).equals("Inventory")){
                        account = new Account(usernameField.getText(),queryResult.getString(1));
                        checkLogin =account;
                        loginBtn = (Button) event.getSource();
                        Stage stage = (Stage) loginBtn.getScene().getWindow();
                        FXMLLoader loader = new FXMLLoader((getClass().getResource("/InventoryProducts.fxml")));
                        stage.setScene(new Scene(loader.load(), 1280, 768));
                        InventoryProductController controller = loader.getController();
                        controller.setAccount(account);

                        stage.show();
                    }
                }
                if(checkLogin.getUsername().equals("")){
                    status.setText("Username หรือ Password ไม่ถูกต้อง");
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
    public void setAccount(Account account){
    this.account = account;
}
}
