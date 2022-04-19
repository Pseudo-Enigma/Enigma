package enigma;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
//        Parent root = FXMLLoader.load(getClass().getResource("EnigmaUI.fxml"));
//        primaryStage.setTitle("Enigma");
//        primaryStage.setScene(new Scene(root, 801, 570));
//        primaryStage.show();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/login.fxml"));
        System.out.println(fxmlLoader);
        Scene scene = new Scene(fxmlLoader.load(), 620, 400);
        primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
    	launch(args);
    }

}
