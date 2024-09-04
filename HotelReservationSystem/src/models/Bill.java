package models;

import javafx.beans.property.*;

import java.math.BigDecimal;
import java.time.Instant;

public class Bill {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty billID = new SimpleIntegerProperty();
    private final ObjectProperty<Reservation> reservation = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> totalCost = new SimpleObjectProperty<>();
    private final ObjectProperty<BigDecimal> discount = new SimpleObjectProperty<>();
    private final IntegerProperty adminID = new SimpleIntegerProperty();

    private int generateUniqueID() {
        long timestamp = Instant.now().toEpochMilli();
        return (int) (timestamp % 1000000); // Use the last 6 digits of the timestamp as the ID
    }


    public Bill() {
        this.billID.set(generateUniqueID());
    }

    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public int getBillID() {
        return billID.get();
    }

    public IntegerProperty billIDProperty() {
        return billID;
    }

    public void setBillID(int billID) {
        this.billID.set(billID);
    }

    public Reservation getReservation() {
        return reservation.get();
    }

    public ObjectProperty<Reservation> reservationProperty() {
        return reservation;
    }

    public void setReservation(Reservation reservation) {
        this.reservation.set(reservation);
    }

    public BigDecimal getTotalCost() {
        return totalCost.get();
    }

    public ObjectProperty<BigDecimal> totalCostProperty() {
        return totalCost;
    }

    public void setTotalCost(BigDecimal totalCost) {
        this.totalCost.set(totalCost);
    }

    public BigDecimal getDiscount() {
        return discount.get();
    }

    public ObjectProperty<BigDecimal> discountProperty() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount.set(discount);
    }

    public int getAdminID() {
        return adminID.get();
    }

    public IntegerProperty adminIDProperty() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID.set(adminID);
    }

    public BigDecimal calculateTotalCost(BigDecimal totalCost, BigDecimal discount) {
        return totalCost.subtract(discount);
    }

    public String toString() {
        return "Bill ID: " + billID.get() + "\n" +
                "Reservation ID: " + reservation.get().getBookID() + "\n" +
                "Guest Name: " + reservation.get().getGuest().getFirstName() + " " + reservation.get().getGuest().getLastName() + "\n" +
                "Total Cost: " + totalCost.get() + "\n" +
                "Discount: " + discount.get() + "\n" +
                "Admin ID: " + adminID.get();
    }
}
