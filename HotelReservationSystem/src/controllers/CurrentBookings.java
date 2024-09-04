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
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.Reservation;
import models.Room;

public class CurrentBookings {

    private DataAccess da;
    public CurrentBookings() {da = new DataAccess();}
    private Stage st;
    private Scene sc;
    private Parent root;

    @FXML
    private TableColumn<Reservation, Integer> bookingID;

    @FXML
    private TableColumn<Reservation, String> customerName;

    @FXML
    private Label noOfBookings;

    @FXML
    private TableColumn<Reservation, Integer> noOfDays;

    @FXML
    private TableColumn<Reservation, Integer> noOfRooms;

    @FXML
    private TableView<Reservation> reservationTable;

    @FXML
    private TableColumn<Reservation, String> roomInfo;

    @FXML
    void back(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/AdminHomePage.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Admin Home Page");
        st.show();

    }

    @FXML
    void initialize() {
        ObservableList<Reservation> reservations = da.getReservations(null, null, false);

        bookingID.setCellValueFactory(cellData -> cellData.getValue().bookIDProperty().asObject());
        customerName.setCellValueFactory(cellData -> Bindings.createStringBinding(
                () -> cellData.getValue().getGuest().getFirstName() + " " + cellData.getValue().getGuest().getLastName(),
                cellData.getValue().getGuest().firstNameProperty(),
                cellData.getValue().getGuest().lastNameProperty()
        ));
        roomInfo.setCellValueFactory(cellData -> Bindings.createStringBinding(
                () -> getRoomString(cellData.getValue().getRoom()),
                cellData.getValue().roomProperty()
        ));
        noOfDays.setCellValueFactory(cellData -> cellData.getValue().lengthOfStayProperty().asObject());
        noOfRooms.setCellValueFactory(cellData -> cellData.getValue().noOfRoomsProperty().asObject());
        noOfBookings.setText(String.valueOf(reservations.size()));

        reservationTable.setItems(reservations);

        reservationTable.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && (!row.isEmpty())) {
                    Reservation rowData = row.getItem();
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BillService.fxml"));
                        Parent root = loader.load();
                        BillService controller = loader.getController();
                        controller.find(rowData.getBookID());
                        st = (Stage)((Node)event.getSource()).getScene().getWindow();
                        sc = new Scene(root);
                        st.setScene(sc);
                        st.setTitle("View Booking");
                        st.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            return row;
        });


    }

    public String getRoomString(ObservableList<Room> rooms){
        String roomString = "";
        for(Room room : rooms){
            roomString += room.getRoomNumber() + " " + room.getRoomType() +",";
        }
        return roomString;

    }

}
