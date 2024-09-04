package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class AdminHomePage {

    private Stage st;
    private Scene sc;
    private Parent root;

    @FXML
    private Button availableRoomsBtn;

    @FXML
    private Button billServiceBtn;

    @FXML
    private Button bookRoomBtn;

    @FXML
    private Button currentBookingsBtn;

    @FXML
    void availableRooms(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AvailableRooms.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Available Rooms");
        st.show();

    }

    @FXML
    void back(ActionEvent event) throws Exception {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/LoginPage.fxml"));
         Parent root = loader.load();
         st = (Stage)((Node)event.getSource()).getScene().getWindow();
         sc = new Scene(root);
         st.setScene(sc);
         st.show();

    }

    @FXML
    void billService(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BillMenu.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.show();

    }

    @FXML
    void bookRoom(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BookRoom.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.show();

    }

    @FXML
    void currentBookings(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CurrentBookings.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.show();

    }

    @FXML
    void guestLookup(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GuestLookup.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Guest Lookup");
        st.show();

    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

}
