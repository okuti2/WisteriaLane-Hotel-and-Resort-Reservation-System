package controllers;

import application.Main;
import database.DataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginPage {
    public static boolean loggedInUser;
    public static int adminNo;
    private DataAccess da;
    public LoginPage() {da = new DataAccess();}

    private Stage st;
    private Scene sc;
    private final int maxAttempts = 3;
    private int attemptCount = 0;


    @FXML
    private PasswordField password;

    @FXML
    private TextField userId;

    @FXML
    void adminLogin(ActionEvent event) throws Exception {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminHomePage.fxml"));
            Parent root = loader.load();
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setTitle("Admin Home Page");

            adminNo = da.validation(userId.getText(), password.getText());
            if (adminNo != -1) {
                loggedInUser = true;
                Main mainInstance = Main.getInstance();
                if (mainInstance != null) {
                    mainInstance.sendDataToServer("Logged in as Admin " + adminNo);
                }
                st.setScene(sc);
                st.show();

            } else {
                attemptCount++;
                if(attemptCount >= maxAttempts) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error");
                    alert.setHeaderText(null);
                    alert.setContentText("You have exceeded the maximum number of attempts. Please try again later.");
                    alert.showAndWait();
                    System.exit(0);
                }
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText("Invalid User ID or Password. You have " + (maxAttempts - attemptCount) + " attempts left.");
                alert.showAndWait();
                password.clear();

            }

    }

    @FXML
    void back (ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/WelcomePage.fxml"));
        Parent root = loader.load();
        st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Welcome Page");
        st.show();

    }

}
