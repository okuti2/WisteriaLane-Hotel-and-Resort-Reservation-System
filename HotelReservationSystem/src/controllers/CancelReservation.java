package controllers;

import database.DataAccess;
import enumarators.RoomType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Reservation;
import models.Room;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CancelReservation {
    // This class will be used to cancel a reservation, deleting it from the database. Tables affected will be the reservation table, the room table and the room_reservation table
    // Still debating on if guest information should be deleted as well
    private Stage st;
    private Scene sc;
    private Reservation reservation;
    @FXML
    private TextArea cancelMessage;
    private DataAccess da;
    public CancelReservation() {da = new DataAccess();}

    @FXML
    void cancel(ActionEvent event) throws IOException {
        // This method will delete the reservation from the database
        // The reservation will be deleted from the reservation table
        // The rooms will be marked as available in the room table
        // The room_reservation table will be updated to reflect the changes
        // The guest information will be deleted as well
        da.deleteReservation(reservation);

        // The user will be shown a message that the reservation has been cancelled
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Reservation Cancelled");
        alert.setHeaderText("Reservation has been cancelled");
        alert.setContentText("The reservation has been cancelled and the rooms have been marked as available");
        alert.showAndWait();


        // The user will be redirected to the admin home page
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
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BillService.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Bill Service");
        st.show();

    }

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
        String ReservationString =
                "Reservation ID: [#" + reservation.getBookID() + "]\n" +
                        "Guest Name: " + reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName() + "\n" +
                        "Check In: " + reservation.getCheckIn() + "\n" +
                        "Check Out: " + reservation.getCheckOut() + "\n" +
                        "Number of Guests: " + reservation.getNoOfGuests() + "\n" +
                        "Room Details: \n";

        // Count the number of rooms for each room type
        int num = 1;
        for (Room room : reservation.getRoom()) {
            ReservationString += "Room " + num + ": " + room.getRoomType() +" " + room.getRoomNumber() + "\n";
            num++;
        }

        cancelMessage.setText(ReservationString);
    }
}
