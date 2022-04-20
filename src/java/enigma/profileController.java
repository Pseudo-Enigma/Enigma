package enigma;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.commons.codec.digest.DigestUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;


public class profileController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private Label lbl_username;
    @FXML
    private Label lbl_theme;
    @FXML
    public PasswordField pf_oldPass;
    @FXML
    public PasswordField pf_newPass;
    @FXML
    public Button btn_logOut;
    @FXML
    public Label lbl_messege;
    @FXML
    public Button btn_changePass;
    @FXML
    public ImageView img_logoWhite;
    @FXML
    private Button btn_back;

    public String profilename;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File ("src/resources/icons/logo_white.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        img_logoWhite.setImage(brandingImage);

        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();
        System.out.println(profilename);
        String query = "SELECT theme FROM Enigma.user_account WHERE username = '" + profilename + "'";
        lbl_username.setText(profilename);
        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(query);
            while (queryResult.next()) {
                if(Objects.equals(queryResult.getString("theme"), "light")) {
                    lbl_theme.setText("Light");
                } else if (Objects.equals(queryResult.getString("theme"), "dark")) {
                    lbl_theme.setText("dark");
                } else {
                    lbl_messege.setText("None");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }

    }

    public void btn_logOutOnClink (ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void btn_exitOnAction (ActionEvent event) throws IOException {
        //going to landing page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EnigmaUI.fxml"));
        root = loader.load();
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

//    public void validateSignIn() {
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.getConnection();
//
//        String hashedPass = DigestUtils.sha256Hex(pf_password.getText().toString());
//        String verifyLogin = "select count(1) from Enigma.user_account where username='" + tf_username.getText() + "' AND password='" + hashedPass + "'";
//
//        try {
//            Statement statement = connectDB.createStatement();
//            ResultSet queryResult = statement.executeQuery(verifyLogin);
//            while (queryResult.next()) {
//                if(queryResult.getInt(1) == 1) {
//                    lbl_messege.setText("login successful!");
//                    lbl_messege.setAlignment(Pos.CENTER);
//
//                } else {
//                    lbl_messege.setText("Invalid Username or Password!");
//                    lbl_messege.setAlignment(Pos.CENTER);
//                }
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            e.getCause();
//        }
//    }
//    public void validateSignUP() {
//        DatabaseConnection connectNow = new DatabaseConnection();
//        Connection connectDB = connectNow.getConnection();
//
//        String hashedPass = DigestUtils.sha256Hex(pf_password.getText().toString());
//        String verifyRegister = "INSERT INTO Enigma.user_account (username, password) VALUES ('" + tf_username.getText() + "', '" + hashedPass + "')";
//
//        try {
//            Statement statement = connectDB.createStatement();
//            statement.executeUpdate(verifyRegister);
//
//            lbl_messege.setText("Sign Up successful!");
//            lbl_messege.setAlignment(Pos.CENTER);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            e.getCause();
//        }
//    }
    public void getUsername(String name)
    {
        profilename = name;
    }
}