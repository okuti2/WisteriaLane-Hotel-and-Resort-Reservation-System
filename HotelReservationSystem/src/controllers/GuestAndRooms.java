package controllers;

import enumarators.RoomType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.stage.Stage;
import models.Room;
import database.DataAccess;

public class GuestAndRooms {

    private DataAccess da;
    public GuestAndRooms() {da = new DataAccess();}

    @FXML
    private Spinner<Integer> DeluxeRoom;

    @FXML
    private Spinner<Integer> DoubleRoom;

    @FXML
    private Spinner<Integer> Penthouse;

    @FXML
    private Spinner<Integer> SingleRoom;

    @FXML
    private Spinner<Integer> guestNo;

    private Stage dialogStage;
    public int noOfSingleRooms;
    public int noOfDoubleRooms;
    public int noOfDeluxeRooms;
    public int noOfPenthouseRooms;
    public int noOfGuests;




// Issue with old values being displayed in the spinners even after booking has been made and the dialog is opened again
    public void setGuestsAndRooms(int noOfSingleRooms, int noOfDoubleRooms, int noOfDeluxeRooms, int noOfPenthouseRooms, int noOfGuests){
        this.noOfSingleRooms = noOfSingleRooms;
        this.noOfDoubleRooms = noOfDoubleRooms;
        this.noOfDeluxeRooms = noOfDeluxeRooms;
        this.noOfPenthouseRooms = noOfPenthouseRooms;
        this.noOfGuests = noOfGuests;

        // mAX VALUE SHOULD BE THE NUMBER OF AVAILABLE ROOMS OF EACH TYPE, MAKE SUCH FUNCTION IN DATABASE CLASS
        SpinnerValueFactory<Integer> singleValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, da.getAvailableRooms(RoomType.SINGLE), this.noOfSingleRooms); // min, max, initialValue
        SingleRoom.setValueFactory(singleValueFactory);

        SpinnerValueFactory<Integer> doubleValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, da.getAvailableRooms(RoomType.DOUBLE), this.noOfDoubleRooms); // min, max, initialValue
        DoubleRoom.setValueFactory(doubleValueFactory);

        SpinnerValueFactory<Integer> deluxeValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, da.getAvailableRooms(RoomType.DELUXE), this.noOfDeluxeRooms); // min, max, initialValue
        DeluxeRoom.setValueFactory(deluxeValueFactory);

        SpinnerValueFactory<Integer> penthouseValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, da.getAvailableRooms(RoomType.PENTHOUSE), this.noOfPenthouseRooms); // min, max, initialValue
        Penthouse.setValueFactory(penthouseValueFactory);

        SpinnerValueFactory<Integer> guestValueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, Integer.MAX_VALUE, this.noOfGuests); // min, max, initialValue
        guestNo.setValueFactory(guestValueFactory);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    @FXML
    void cancel(ActionEvent event) {
        dialogStage.close();

    }

    @FXML
    void confirm(ActionEvent event) {
        int totalCapacity = 0;
        noOfSingleRooms = SingleRoom.getValue();
        totalCapacity += SingleRoom.getValue() * RoomType.SINGLE.getMax();
        noOfDoubleRooms = DoubleRoom.getValue();
        totalCapacity += DoubleRoom.getValue() * RoomType.DOUBLE.getMax();
        noOfDeluxeRooms = DeluxeRoom.getValue();
        totalCapacity += DeluxeRoom.getValue() * RoomType.DELUXE.getMax();
        noOfPenthouseRooms = Penthouse.getValue();
        totalCapacity += Penthouse.getValue() * RoomType.PENTHOUSE.getMax();

        noOfGuests = guestNo.getValue();

        if(totalCapacity < noOfGuests){
            // Notify the user that there are not enough rooms to accommodate the guests
            Alert alert = new Alert(Alert.AlertType.ERROR, "Not enough rooms to accommodate the guests", javafx.scene.control.ButtonType.OK);
            alert.showAndWait();

        } else {
            // Notify the user that the rooms have been successfully booked
            dialogStage.close();

        }


    }

}
