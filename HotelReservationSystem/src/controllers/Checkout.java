package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import models.Bill;
import models.Reservation;

import java.io.IOException;

public class Checkout {
    private Bill bill;

    @FXML
    private TextArea paymentConfirm;

    public void setBill(Bill bill) {
        this.bill = bill;
    }

    public void setReservation(Reservation reservation) {
        String ReservationString =
                "Reservation Checkout\n\n"+
                "Reservation ID: [#" + reservation.getBookID() + "]\n" +
                        "Guest Name: " + reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName() + "\n" +
                        "Check In: " + reservation.getCheckIn() + "\n" +
                        "Check Out: " + reservation.getCheckOut() + "\n" +
                        "Number of Guests: " + reservation.getNoOfGuests() + "\n" +
                        "Total Cost: $" + reservation.getTotalCost() + "\n\n" +
                        "Payment Information\n\n"+
                        "Payment Method: MasterCard" + "\n" +
                        "Card Number: **** **** **** 1234" + "\n" +
                        "Expiry Date: 12/2024" + "\n" +
                        "CVV: 123" + "\n\n"+
                        "Operator: " + bill.getAdminID() + "\n";

        paymentConfirm.setText(ReservationString);
    }

    @FXML
    void exit(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BillService.fxml"));
        Parent root = loader.load();
        Stage st = (Stage) ((Node) event.getSource()).getScene().getWindow();
        Scene sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Bill Service");
        st.show();

    }

}
