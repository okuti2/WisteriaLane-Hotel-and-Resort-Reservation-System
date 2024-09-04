package controllers;

import database.DataAccess;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Reservation;
import models.Room;

import java.io.IOException;

public class GuestInformation {

    private Stage st;
    private Scene sc;
    private Parent root;
    private Reservation reservation;
    private models.GuestInformation guest;
    private DataAccess da;
    public GuestInformation() {da = new DataAccess();}

    @FXML
    private TextField address;

    @FXML
    private TextField email;

    @FXML
    private TextField firstName;

    @FXML
    private TextField lastName;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField title;

    public void setReservation(Reservation reservation) {
        this.reservation = reservation;
    }


    @FXML
    void back(ActionEvent event) throws Exception {
        for (Room room : reservation.getRoom()) {
            da.updateRoom(true, room.getRoomNumber());
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BookRoom.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Book Room");
        st.show();

    }

    @FXML
    void bookRoom(ActionEvent event) throws Exception{
        if (validateInput()) {
            guest = selectGuest(phoneNumber.getText().replaceAll("[^0-9]", ""));
            if (guest.getGuestID() == 0) {
                guest.setAddress(address.getText());
                guest.setEmail(email.getText());
                guest.setFirstName(firstName.getText());
                guest.setLastName(lastName.getText());
                guest.setPhone(phoneNumber.getText().replaceAll("[^0-9]", ""));
                guest.setTitle(title.getText());
                guest.setGuestID(da.insertGuest(guest));
                guest.setReservation(FXCollections.observableArrayList());
            } else {
                address.setText(guest.getAddress());
                email.setText(guest.getEmail());
                firstName.setText(guest.getFirstName());
                lastName.setText(guest.getLastName());
                phoneNumber.setText(guest.getPhone());
                title.setText(guest.getTitle());
            }
            reservation.setGuest(guest);
            guest.getReservation().add(reservation);
            reservation.toString();
            reservation.setReservationID(da.insertReservation(reservation));
            da.setRoomsForReservation(reservation);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Please confirm information is correct", ButtonType.YES, ButtonType.NO);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Confirm Booking?");
            alert.showAndWait();
            if (alert.getResult() == ButtonType.YES) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/ConfirmationPage.fxml"));
                Parent root = loader.load();
                st = (Stage) ((Node) event.getSource()).getScene().getWindow();
                sc = new Scene(root);
                st.setScene(sc);
                st.setTitle("Confirmation Page");
                ConfirmationPage controller = loader.getController();
                controller.setReservation(reservation);
                st.show();
            }
        }
    }

    // This function searches the guest database for information matching the phone number, shows the user the possible guests, and allows the user to select the correct guest, it returns the selected guest
    public models.GuestInformation selectGuest(String phoneNumber) throws IOException {
        ObservableList<models.GuestInformation> guests = da.getGuests(phoneNumber);
        if(guests.size() == 0){
            return new models.GuestInformation();
        }
        models.GuestInformation selectedGuest = null;
        // Show the user the possible guests
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/SelectGuests.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Select Guest");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(st);
        Scene ssc = new Scene(page);
        dialogStage.setScene(ssc);

        SelectGuests controller = loader.getController();
        //controller.setGuestsAndRooms(SingleRoom, DoubleRoom, DeluxeRoom, PenthouseRoom, noOfGuests);
        controller.setGuest(guests);
        controller.setDialogStage(dialogStage);
        dialogStage.setResizable(false);
        dialogStage.showAndWait();
        selectedGuest = controller.selectedGuest;
        if(selectedGuest == null){
            selectedGuest = new models.GuestInformation();
        }


        // Allow the user to select the correct guest

        return selectedGuest;
    }

    public boolean validateInput() {
        String errorMessage = "";
        if (firstName.getText() == null || firstName.getText().length() == 0) {
            errorMessage += "No valid first name!\n";
        }
        if (lastName.getText() == null || lastName.getText().length() == 0) {
            errorMessage += "No valid last name!\n";
        }
        if (phoneNumber.getText() == null || phoneNumber.getText().length() == 0 || phoneNumber.getText().replaceAll("[^0-9]", "").length() != 10) {
            errorMessage += "No valid phone number!\n";
        }
        if (address.getText() == null || address.getText().length() == 0) {
            errorMessage += "No valid address!\n";
        }
        if (email.getText() == null || email.getText().length() == 0 || !email.getText().matches("^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            errorMessage += "No valid email!\n";
        }
        if (errorMessage.isEmpty()) {
            return true;
        } else {
            showAlert(errorMessage);
            return false;
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
