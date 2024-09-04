package controllers;

import database.DataAccess;
import javafx.beans.binding.Bindings;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Reservation;
import models.Room;

import java.sql.Date;

public class GuestLookup {

    private DataAccess da;
    public GuestLookup() {da = new DataAccess();}
    private Stage st;
    private Scene sc;
    private Reservation reservation;
    @FXML
    private TableColumn<Reservation, String> Rooms;

    @FXML
    private TableColumn<Reservation, Integer> bookingID;

    @FXML
    private TableColumn<Reservation, String> custName;

    @FXML
    private TableColumn<Reservation, Integer> days;
    @FXML
    private TableColumn<Reservation, Date> checkin;

    @FXML
    private TableColumn<Reservation, Date> checkout;



    @FXML
    private TableView<Reservation> guestTable;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField searchName;

    @FXML
    private TableColumn<Reservation, String> status;

    @FXML
    void initialize() {
        bookingID.setCellValueFactory(cellData -> cellData.getValue().bookIDProperty().asObject());
        custName.setCellValueFactory(cellData -> Bindings.createStringBinding(
                () -> cellData.getValue().getGuest().getFirstName() + " " + cellData.getValue().getGuest().getLastName(),
                cellData.getValue().getGuest().firstNameProperty(),
                cellData.getValue().getGuest().lastNameProperty()
        ));
        //Rooms.setCellValueFactory(cellData -> cellData.getValue().RoomsProperty());
        Rooms.setCellValueFactory(cellData -> Bindings.createStringBinding(
                () -> getRoomString(cellData.getValue().getRoom()),
                cellData.getValue().roomProperty()
        ));
        //days.setCellValueFactory(cellData -> cellData.getValue().lengthOfStayProperty().asObject());
        checkin.setCellValueFactory(cellData -> cellData.getValue().checkInProperty());
        checkout.setCellValueFactory(cellData -> cellData.getValue().checkOutProperty());
        status.setCellValueFactory(cellData -> Bindings.createStringBinding(
                () -> cellData.getValue().getIsCancelled() ? "Cancelled" :  (cellData.getValue().getIsPaid() ? "Checked Out" : "Booked"),
                cellData.getValue().isPaidProperty()
        ));
    }

    @FXML
    void back(ActionEvent event) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminHomePage.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Admin Home Page");
        st.show();
    }

    @FXML
    void searchGuests(ActionEvent event) {
        ObservableList<Reservation> reservations = da.getReservations(searchName.getText(), phoneNumber.getText(), false);
        guestTable.setItems(reservations);
    }

    public String getRoomString(ObservableList<Room> rooms){
        String roomString = "";
        for(Room room : rooms){
            roomString += room.getRoomNumber() + " " + room.getRoomType() +",";
        }
        return roomString;

    }

}
