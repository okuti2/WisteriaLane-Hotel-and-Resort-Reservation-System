package controllers;

import database.DataAccess;
import enumarators.RoomType;
import javafx.collections.FXCollections;
import javafx.collections.ObservableArray;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Reservation;
import models.Room;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Collections;


public class BookRoom {
    private DataAccess da;
    public BookRoom() {da = new DataAccess();}
    @FXML
    private CheckBox deluxeR;

    @FXML
    private CheckBox doubleR;

    @FXML
    private CheckBox penthouseR;

    @FXML
    private CheckBox singleR;

    @FXML
    private DatePicker checkIn;

    @FXML
    private DatePicker checkOut;

    @FXML
    private TextField rate;

    @FXML
    private TextField selectedRooms;


    @FXML
    private Spinner<Integer> stayLength;

    private Stage st;
    private Scene sc;
    private Parent root;

    private int noOfRooms;
    private int noOfGuests;
    private final RoomType singleRoom = RoomType.SINGLE;
    private final RoomType doubleRoom = RoomType.DOUBLE;
    private final RoomType deluxeRoom = RoomType.DELUXE;
    private final RoomType penthouse = RoomType.PENTHOUSE;

    @FXML
    void initialize() {
        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, 1);
        stayLength.setValueFactory(valueFactory);
        // Add listener to checkIn DatePicker
        checkIn.valueProperty().addListener((observable, oldValue, newValue) -> calculateStayLength());
        // Add listener to checkOut DatePicker
        checkOut.valueProperty().addListener((observable, oldValue, newValue) -> calculateStayLength());


        // Add listener to stayLength Spinner
        stayLength.valueProperty().addListener((observable, oldValue, newValue) -> {
            // Calculate the new checkout date based on the stay length
            if (checkIn.getValue() != null && newValue != null) {
                LocalDate newCheckOutDate = checkIn.getValue().plusDays(newValue);
                checkOut.setValue(newCheckOutDate);
            }
        });

    }

    // Method to calculate the length of stay and update stayLength Spinner
    private void calculateStayLength() {
        if (checkIn.getValue() != null && checkOut.getValue() != null) {
            long days = ChronoUnit.DAYS.between(checkIn.getValue(), checkOut.getValue());
            stayLength.getValueFactory().setValue((int) days);
        }
    }

    @FXML
    void back(ActionEvent event) throws Exception {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/WelcomePage.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Welcome Page");
        st.show();

    }

    @FXML
    void checkInGuest(ActionEvent event) throws Exception{
        if(validation()) {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GuestInformation.fxml"));
            Parent root = loader.load();
            st = (Stage) ((Node) event.getSource()).getScene().getWindow();
            sc = new Scene(root);
            st.setScene(sc);
            st.setTitle("Guest Information");
            GuestInformation controller = loader.getController();

            Reservation reservation = new Reservation();
            reservation.setCheckIn(java.sql.Date.valueOf(checkIn.getValue()));
            reservation.setCheckOut(java.sql.Date.valueOf(checkOut.getValue()));
            reservation.setLengthOfStay(stayLength.getValue());
            reservation.setRoom(bookRooms());
            reservation.setNoOfRooms(noOfRooms);
            reservation.setNoOfGuests(noOfGuests);
            reservation.setTotalCost(BigDecimal.valueOf(Double.parseDouble(rate.getText()) * stayLength.getValue()));

            controller.setReservation(reservation);
            singleRoom.setNoOfRooms(0);
            doubleRoom.setNoOfRooms(0);
            deluxeRoom.setNoOfRooms(0);
            penthouse.setNoOfRooms(0);

            st.show();
        }

    }

    @FXML
    void exit(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    void selectRooms(MouseEvent event) throws Exception{  // Add validation so the number of people and rooms add up. Also make sure when the textfield is selected the already chosen options are saved and displayed
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/GuestAndRooms.fxml"));
        AnchorPane page = (AnchorPane) loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle("Select Room");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(st);
        Scene ssc = new Scene(page);
        dialogStage.setScene(ssc);

        GuestAndRooms controller = loader.getController();
        //controller.setGuestsAndRooms(SingleRoom, DoubleRoom, DeluxeRoom, PenthouseRoom, noOfGuests);
        controller.setGuestsAndRooms(singleRoom.getNoOfRooms(), doubleRoom.getNoOfRooms(), deluxeRoom.getNoOfRooms(), penthouse.getNoOfRooms(), noOfGuests);
        controller.setDialogStage(dialogStage);
        dialogStage.setResizable(false);
        dialogStage.showAndWait();

        noOfGuests = controller.noOfGuests;
        singleRoom.setNoOfRooms(controller.noOfSingleRooms);
        doubleRoom.setNoOfRooms(controller.noOfDoubleRooms);
        deluxeRoom.setNoOfRooms(controller.noOfDeluxeRooms);
        penthouse.setNoOfRooms(controller.noOfPenthouseRooms);
        noOfRooms = singleRoom.getNoOfRooms() + doubleRoom.getNoOfRooms() + deluxeRoom.getNoOfRooms() + penthouse.getNoOfRooms();

        singleR.setSelected(singleRoom.getNoOfRooms() > 0);
        doubleR.setSelected(doubleRoom.getNoOfRooms() > 0);
        deluxeR.setSelected(deluxeRoom.getNoOfRooms() > 0);
        penthouseR.setSelected(penthouse.getNoOfRooms() > 0);

        if(noOfRooms == 0 && noOfGuests != 0) {
            selectedRooms.setText("No Rooms Selected");
        }else if(noOfRooms !=0 && noOfGuests == 0) {
            selectedRooms.setText("No Guests Selected");
        } else if (noOfRooms == 0 && noOfGuests == 0) {
            selectedRooms.setText("No Rooms and Guests Selected");
        } else if (noOfRooms  == 1 && noOfGuests == 1) {
            selectedRooms.setText(String.valueOf(noOfRooms)+ " Room - " + String.valueOf(noOfGuests) + " Guest");
        } else if (noOfRooms == 1 && noOfGuests > 1) {
            selectedRooms.setText(String.valueOf(noOfRooms)+ " Room - " + String.valueOf(noOfGuests) + " Guests");
        } else if (noOfRooms > 1 && noOfGuests == 1) {
            selectedRooms.setText(String.valueOf(noOfRooms)+ " Rooms - " + String.valueOf(noOfGuests) + " Guest");
        } else if (noOfRooms > 1 && noOfGuests > 1) {
            selectedRooms.setText(String.valueOf(noOfRooms)+ " Rooms - " + String.valueOf(noOfGuests) + " Guests");
        }

        //Calculate the total rate of selected rooms
        double totalRate = 0;
        totalRate += singleRoom.getNoOfRooms() * singleRoom.getRate();
        totalRate += doubleRoom.getNoOfRooms() * doubleRoom.getRate();
        totalRate += deluxeRoom.getNoOfRooms() * deluxeRoom.getRate();
        totalRate += penthouse.getNoOfRooms() * penthouse.getRate();
        rate.setText(String.valueOf(totalRate));

    }

    public ObservableList<Room> bookRooms(){
        ObservableList<Room> rooms = FXCollections.observableArrayList();

        rooms.addAll(da.bookRoom(singleRoom, singleRoom.getNoOfRooms()));
        rooms.addAll(da.bookRoom(doubleRoom, doubleRoom.getNoOfRooms()) );
        rooms.addAll(da.bookRoom(deluxeRoom, deluxeRoom.getNoOfRooms()));
        rooms.addAll(da.bookRoom(penthouse, penthouse.getNoOfRooms()));

        return rooms;
    }

    public boolean validation(){
        // Check if check-in date is selected
        if (checkIn.getValue() == null) {
            showAlert("Check-In Date is required.");
            return false;
        }

        // Check if check-out date is selected
        if (checkOut.getValue() == null) {
            showAlert("Check-Out Date is required.");
            return false;
        }

        // Check if check-out date is after the check-in date
        LocalDate currentDate = LocalDate.now();
        if(checkIn.getValue().isBefore(currentDate)){
            showAlert("Check-In Date must be on or after the current date.");
            return false;
        }

        if (checkOut.getValue().isBefore(checkIn.getValue()) || checkOut.getValue().isEqual(checkIn.getValue())) {
            showAlert("Check-Out Date must be after the Check-In Date.");
            return false;
        }

        if(noOfRooms == 0){
            showAlert("Please select at least one room.");
            return false;
        }
        if(noOfGuests == 0){
            showAlert("Please select at least one guest.");
            return false;
        }

        // Add additional validations for other fields if needed

        return true;

    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Validation Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
