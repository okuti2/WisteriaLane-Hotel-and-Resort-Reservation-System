package controllers;

import application.Main;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class WelcomePage {

    private Stage st;
    private Scene sc;
    private Parent root;
    @FXML
    void bookARoom(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BookRoom.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Book Room");
        Main mainInstance = Main.getInstance();
        if(mainInstance != null){
            mainInstance.sendDataToServer("Booking room as a guest");
        }
        st.show();
    }

    @FXML
    void login(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginPage.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Login");
        st.show();
    }

}
