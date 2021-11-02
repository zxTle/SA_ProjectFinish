package sa_project;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/Home.fxml"));
        root.getStylesheets().add("https://fonts.googleapis.com/css2?family=Barlow&family=Kanit:wght@200;300;400&display=swap");
        primaryStage.setTitle("MICRO SCIENCE");
        primaryStage.setScene(new Scene(root, 1280, 768));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
