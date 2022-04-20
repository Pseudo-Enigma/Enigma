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
import java.util.ResourceBundle;


public class LoginController implements Initializable {
    private Stage stage;
    private Scene scene;
    private Parent root;

    public boolean flag = false;
    @FXML
    public Line line_r;
    @FXML
    public Line line_l;
    @FXML
    private Label lbl_login;
    @FXML
    private Label lbl_register;
    @FXML
    public TextField tf_username;
    @FXML
    public PasswordField pf_password;
    public Button btn_login;
    @FXML
    public Label lbl_messege;
    public Button btn_continuewithout;
    @FXML
    public ImageView img_logoWhite;
    @FXML
    private Button btn_exit;

    @Override
    public void initialize (URL url, ResourceBundle resourceBundle) {
        File brandingFile = new File ("src/resources/icons/logo_white.png");
        Image brandingImage = new Image(brandingFile.toURI().toString());
        img_logoWhite.setImage(brandingImage);
        btn_login.setText("Sign In");
        lbl_login.setTextFill(Paint.valueOf("#286090"));
        line_l.setVisible(true);
        line_r.setVisible(false);
        lbl_register.setTextFill(Paint.valueOf("#111111"));
    }

    public void lbl_loginOnClink (MouseEvent event) {
        btn_login.setText("Sign In");
        lbl_login.setTextFill(Paint.valueOf("#286090"));
        line_l.setVisible(true);
        line_r.setVisible(false);
        lbl_register.setTextFill(Paint.valueOf("#111111"));
        lbl_messege.setText("");
        tf_username.setText("");
        pf_password.setText("");
    }
    public void lbl_registerOnClink (MouseEvent event) {
        btn_login.setText("Sign Up");
        lbl_register.setTextFill(Paint.valueOf("#286090"));
        line_r.setVisible(true);
        line_l.setVisible(false);
        lbl_login.setTextFill(Paint.valueOf("#111111"));
        lbl_messege.setText("");
        tf_username.setText("");
        pf_password.setText("");
    }
    public void btn_loginOnAction (ActionEvent event) throws IOException {
        if (!tf_username.getText().isBlank() && !pf_password.getText().isBlank()) {
            if (btn_login.getText().toString().equals("Sign In")) {
                if (validateSignIn())
                {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("EnigmaUI.fxml"));
                    root = loader.load();
                    flag = true;
                    EnigmaUIController enigmaUIController = loader.getController();
                    enigmaUIController.updateFlag(flag);
                    enigmaUIController.getUsername(tf_username.getText());
                    stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root);
                    stage.setScene(scene);
                    stage.show();
                }
            } else {
                validateSignUP();
            }
        } else {
            lbl_messege.setText("Please Enter Username and Password");
            lbl_messege.setAlignment(Pos.CENTER);
        }
    }
    public void btn_exitOnAction (ActionEvent event) throws IOException {
        //going to landing page
        FXMLLoader loader = new FXMLLoader(getClass().getResource("EnigmaUI.fxml"));
        root = loader.load();
        EnigmaUIController enigmaUIController = loader.getController();
        enigmaUIController.updateFlag(flag);
        enigmaUIController.getUsername(tf_username.getText());
        stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public boolean validateSignIn() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String hashedPass = DigestUtils.sha256Hex(pf_password.getText().toString());
        String verifyLogin = "select count(1) from Enigma.user_account where username='" + tf_username.getText() + "' AND password='" + hashedPass + "'";

        try {
            Statement statement = connectDB.createStatement();
            ResultSet queryResult = statement.executeQuery(verifyLogin);
            while (queryResult.next()) {
                if(queryResult.getInt(1) == 1) {
                    lbl_messege.setText("login successful!");
                    lbl_messege.setAlignment(Pos.CENTER);
                    flag = true;
                    return true;
                } else {
                    lbl_messege.setText("Invalid Username or Password!");
                    lbl_messege.setAlignment(Pos.CENTER);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return false;
    }
    public void validateSignUP() {
        DatabaseConnection connectNow = new DatabaseConnection();
        Connection connectDB = connectNow.getConnection();

        String hashedPass = DigestUtils.sha256Hex(pf_password.getText().toString());
        String verifyRegister = "INSERT INTO Enigma.user_account (username, password) VALUES ('" + tf_username.getText() + "', '" + hashedPass + "')";

        try {
            Statement statement = connectDB.createStatement();
            statement.executeUpdate(verifyRegister);

            lbl_messege.setText("Sign Up successful!");
            lbl_messege.setAlignment(Pos.CENTER);

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }
}