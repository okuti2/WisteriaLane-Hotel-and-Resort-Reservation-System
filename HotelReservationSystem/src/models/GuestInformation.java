package models;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class GuestInformation {
    private final IntegerProperty guestID = new SimpleIntegerProperty();
    private final StringProperty title = new SimpleStringProperty();
    private final StringProperty firstName = new SimpleStringProperty();
    private final StringProperty lastName = new SimpleStringProperty();
    private final StringProperty address = new SimpleStringProperty();
    private final StringProperty phone = new SimpleStringProperty();
    private final StringProperty email = new SimpleStringProperty();

    private final ObjectProperty<ObservableList<Reservation>> reservation = new SimpleObjectProperty<>();

    public GuestInformation() {
        ObservableList<Reservation> reservations = FXCollections.observableArrayList();
        this.setReservation(reservations);
    }

    // Getters and setters using binding properties
    public int getGuestID() {
        return guestID.get();
    }

    public IntegerProperty guestIDProperty() {
        return guestID;
    }

    public void setGuestID(int guestID) {
        this.guestID.set(guestID);
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getFirstName() {
        return firstName.get();
    }

    public StringProperty firstNameProperty() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName.set(firstName);
    }

    public String getLastName() {
        return lastName.get();
    }

    public StringProperty lastNameProperty() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName.set(lastName);
    }

    public String getAddress() {
        return address.get();
    }

    public StringProperty addressProperty() {
        return address;
    }

    public void setAddress(String address) {
        this.address.set(address);
    }

    public String getPhone() {
        return phone.get();
    }

    public StringProperty phoneProperty() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone.set(phone);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public ObservableList<Reservation> getReservation() {
        return reservation.get();
    }

    public ObjectProperty<ObservableList<Reservation>> reservationProperty() {
        return reservation;
    }

    public void setReservation(ObservableList<Reservation> reservation) {
        this.reservation.set(reservation);
    }



}
