package controllers;

import enumarators.RoomType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Reservation;
import models.Room;
import controllers.LoginPage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ConfirmationPage {

    private Stage st;
    private Scene sc;

    private Reservation reservation;
    @FXML
    private Button homePagebtn;


    @FXML
    private TextArea reservationInfo;

    public void initialize() {
        reservationInfo.setEditable(false);
        homePagebtn.setVisible(LoginPage.loggedInUser);
    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        setReservationInfo();
    }

    public void setReservationInfo() {
        String ReservationString =
                "Booking ID: #" + reservation.getBookID() + "\n" +
                "Guest Name: " + reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName() + "\n" +
                "Check In: " + reservation.getCheckIn() + "\n" +
                "Check Out: " + reservation.getCheckOut() + "\n" +
                "Number of Guests: " + reservation.getNoOfGuests() + "\n" +
                "Total Cost: $" + reservation.getTotalCost() + "0\n" +
                "Rooms: \n";

        // Create a map to store the count of each room type
        Map<RoomType, Integer> roomTypeCount = new HashMap<>();

        // Count the number of rooms for each room type
        for (Room room : reservation.getRoom()) {
            roomTypeCount.put(room.getRoomType(), roomTypeCount.getOrDefault(room.getRoomType(), 0) + 1);
        }

        // Append room types with counts to the ReservationString
        for (Map.Entry<RoomType, Integer> entry : roomTypeCount.entrySet()) {
            switch (entry.getKey()) {
                case RoomType.SINGLE:
                    ReservationString += entry.getValue() + " Single Rooms\n";
                    break;
                case RoomType.DOUBLE:
                    ReservationString += entry.getValue() + " Double Rooms\n";
                    break;
                case RoomType.DELUXE:
                    ReservationString += entry.getValue() + " Deluxe Rooms\n";
                    break;
                case RoomType.PENTHOUSE:
                    ReservationString += entry.getValue() + " Penthouse Rooms\n";
                    break;
                default:
                    // Handle unknown room types, if needed
                    break;
            }
        }

        reservationInfo.setText(ReservationString);
    }

    @FXML
    void HomePage(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminHomePage.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Admin Home Page");
        st.show();
    }


    @FXML
    void exit(ActionEvent event) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/WelcomePage.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Welcome Page");
        st.show();
    }

}
