package controllers;

import database.DataAccess;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ObservableValue;
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
import models.Bill;
import models.Reservation;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Date;

public class BillLookup {
    private Stage st;
    private Scene sc;
    private DataAccess da;
    public BillLookup() {da = new DataAccess();}

    @FXML
    private TableView<Bill> BillTable;

    @FXML
    private TableColumn<Bill, Integer> admin;

    @FXML
    private TableColumn<Bill, Integer> bookID;

    @FXML
    private TableColumn<Bill, Date> checkIn;

    @FXML
    private TableColumn<Bill, Date> checkOut;

    @FXML
    private TextField firstandlast;

    @FXML
    private TableColumn<Bill, String> guestName;

    @FXML
    private TableColumn<Bill, BigDecimal> paidAmount;

    @FXML
    private TextField phoneNumber;

    @FXML
    void initialize() {
        admin.setCellValueFactory(cellData -> cellData.getValue().adminIDProperty().asObject());
        bookID.setCellValueFactory(cellData -> cellData.getValue().getReservation().bookIDProperty().asObject());
//        checkIn.setCellValueFactory(cellData -> {
//            Reservation reservation = cellData.getValue().getReservation();
//            ObservableValue<Date> observableDate = Bindings.createObjectBinding(() -> reservation.getCheckIn(), reservation.checkInProperty());
//            return observableDate;
//        });
        checkIn.setCellValueFactory(cellData -> cellData.getValue().getReservation().checkInProperty());
//        checkOut.setCellValueFactory(cellData -> {
//            Reservation reservation = cellData.getValue().getReservation();
//            ObservableValue<Date> observableDate = Bindings.createObjectBinding(() -> reservation.getCheckOut(), reservation.checkOutProperty());
//            return observableDate;
//        });
        checkOut.setCellValueFactory(cellData -> cellData.getValue().getReservation().checkOutProperty());
        guestName.setCellValueFactory(cellData -> Bindings.createStringBinding(
                () -> cellData.getValue().getReservation().getGuest().getFirstName() + " " + cellData.getValue().getReservation().getGuest().getLastName(),
                cellData.getValue().getReservation().getGuest().firstNameProperty(),
                cellData.getValue().getReservation().getGuest().lastNameProperty()
        ));
        //paidAmount.setCellValueFactory(cellData -> (ObservableValue<BigDecimal>) cellData.getValue().totalCostProperty().getValue());
        paidAmount.setCellValueFactory(cellData -> cellData.getValue().totalCostProperty());
    }


    @FXML
    void searchBills(ActionEvent event) {
        ObservableList<Bill> bills = da.getBills(firstandlast.getText(), phoneNumber.getText());
        BillTable.setItems(bills);

    }

    @FXML
    void back(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/BillMenu.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Bill Menu");
        st.show();
    }
}
