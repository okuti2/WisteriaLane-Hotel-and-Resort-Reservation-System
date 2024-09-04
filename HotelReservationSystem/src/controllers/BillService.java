package controllers;

import database.DataAccess;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Bill;
import models.Reservation;
import models.Room;

import java.io.IOException;
import java.math.BigDecimal;
import java.security.interfaces.RSAKey;

public class BillService {
    private Stage st;
    private Scene sc;
    private Parent root;
    private DataAccess da;
    public BillService() {da = new DataAccess();}
    private Reservation reservation;

    @FXML
    private TextField bookedRoomsNo;

    @FXML
    private TextField bookingId;

    @FXML
    private CheckBox deluxe;

    @FXML
    private Slider discounts;

    @FXML
    private CheckBox doubleR;

    @FXML
    private TextField guestName;

    @FXML
    private CheckBox pentHouse;

    @FXML
    private TextField ratePerNight;

    @FXML
    private TextField searchReservationID;

    @FXML
    private CheckBox single;

    @FXML
    private Label slider_lbl;

    @FXML
    private TextField total;



    @FXML
    void initialize() {
        discounts.valueProperty().addListener((obs, oldval, newVal) -> {
            discounts.setValue(Math.round(newVal.doubleValue()));
            slider_lbl.setText(String.valueOf((int) discounts.getValue()) + "%");
            if(reservation != null)
                total.setText("$"+String.valueOf(reservation.getTotalCost().subtract(reservation.getTotalCost().multiply(BigDecimal.valueOf((double) discounts.getValue()/100))).setScale(2, BigDecimal.ROUND_HALF_UP)));
        });
    }

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
    void cancelBooking(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CancelReservation.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Cancel Reservation");
        CancelReservation cancelReservation = loader.getController();
        cancelReservation.setReservation(reservation);
        st.show();

    }

    @FXML
    void checkOut(ActionEvent event) throws IOException {
        //reservation.setTotalCost(new BigDecimal(total.getText().substring(1)));
        // It should set paid to true and make each room in the reservation available
        reservation.setTotalCost(new BigDecimal(total.getText().substring(1)));
        da.checkoutReservation(reservation);
        // Create a bill for the guest
        Bill bill = new Bill();
        bill.setDiscount(BigDecimal.valueOf((int) discounts.getValue()));
        bill.setTotalCost(new BigDecimal(total.getText().substring(1)));
        bill.setReservation(reservation);
        bill.setAdminID(LoginPage.adminNo);
        da.insertBill(bill);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Checkout.fxml"));
        Parent root = loader.load();
        st = (Stage)((Node)event.getSource()).getScene().getWindow();
        sc = new Scene(root);
        st.setScene(sc);
        st.setTitle("Checkout");
        Checkout checkout = loader.getController();
        checkout.setBill(bill);
        checkout.setReservation(reservation);
        st.show();


    }

    @FXML
    void findReservation(ActionEvent event) {
        find(Integer.parseInt(searchReservationID.getText()));
    }

    public void find(int id){
        reservation = da.selectReservation(id);
        if(reservation != null){
            bookingId.setText(String.valueOf(reservation.getBookID()));
            guestName.setText(reservation.getGuest().getFirstName() + " " + reservation.getGuest().getLastName());
            bookedRoomsNo.setText(String.valueOf(reservation.getRoom().size()));
            total.setText("$"+String.valueOf(reservation.getTotalCost()));

            int rate = 0;
            for (Room room : reservation.getRoom()){
                switch (room.getRoomType()){
                    case SINGLE:
                        single.setSelected(true);
                        break;
                    case DOUBLE:
                        doubleR.setSelected(true);
                        break;
                    case DELUXE:
                        deluxe.setSelected(true);
                        break;
                    case PENTHOUSE:
                        pentHouse.setSelected(true);
                        break;
                }
                rate += (int) room.getRoomType().getRate();

                // Find a way to make sure this is filled
                //String.valueOf(reservation.getTotalCost().divide(BigDecimal.valueOf(reservation.getLengthOfStay())))
            }
            ratePerNight.setText("$"+String.valueOf(rate));
        }else{
            bookingId.setText("");
            guestName.setText("");
            bookedRoomsNo.setText("");
            total.setText("");
            ratePerNight.setText("");
            single.setSelected(false);
            doubleR.setSelected(false);
            deluxe.setSelected(false);
            pentHouse.setSelected(false);

        }

    }


}
