package models;

import javafx.beans.property.*;
import javafx.collections.ObservableList;

import java.math.BigDecimal;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;

public class Reservation {
    private final IntegerProperty reservation_id = new SimpleIntegerProperty();
    private final IntegerProperty bookID = new SimpleIntegerProperty();
    private final ObjectProperty<Date> bookDate = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> checkIn = new SimpleObjectProperty<>();
    private final ObjectProperty<Date> checkOut = new SimpleObjectProperty<>();
    private final ObjectProperty<GuestInformation> guest = new SimpleObjectProperty<>();
    // Room should be an array of Room objects
    private final ObjectProperty<ObservableList<Room>> room = new SimpleObjectProperty<>();
    private final IntegerProperty noOfRooms = new SimpleIntegerProperty();
    private final IntegerProperty noOfGuests = new SimpleIntegerProperty();
    private final IntegerProperty lengthOfStay = new SimpleIntegerProperty();
    private final ObjectProperty<BigDecimal> totalCost = new SimpleObjectProperty<>();
    private final BooleanProperty isPaid = new SimpleBooleanProperty();
    private final BooleanProperty isCancelled = new SimpleBooleanProperty();

    // Genrate unique ID for each reservation
    private int generateUniqueID() {
        long timestamp = Instant.now().toEpochMilli();
        return (int) (timestamp % 1000000); // Use the last 6 digits of the timestamp as the ID
    }


    public Reservation() {
        this.bookID.set(generateUniqueID());
        this.isPaid.set(false);
        this.bookDate.set(Date.valueOf(LocalDate.now()));
        this.isCancelled.set(false);
    }

    // Getters and setters using binding properties
    public int getReservationID() {
        return reservation_id.get();
    }

    public IntegerProperty reservationIDProperty() {
        return reservation_id;
    }

    public void setReservationID(int reservation_id) {
        this.reservation_id.set(reservation_id);
    }

    public int getBookID() {
        return bookID.get();
    }

    public IntegerProperty bookIDProperty() {
        return bookID;
    }

    public void setBookID(int bookID) {
        this.bookID.set(bookID);
    }

    public Date getBookDate() {
        return bookDate.get();
    }

    public ObjectProperty<Date> bookDateProperty() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate.set(bookDate);
    }

    public Date getCheckIn() {
        return checkIn.get();
    }

    public ObjectProperty<Date> checkInProperty() {
        return checkIn;
    }

    public void setCheckIn(Date checkIn) {
        this.checkIn.set(checkIn);
    }

    public Date getCheckOut() {
        return checkOut.get();
    }

    public ObjectProperty<Date> checkOutProperty() {
        return checkOut;
    }

    public void setCheckOut(Date checkOut) {
        this.checkOut.set(checkOut);
    }

    public GuestInformation getGuest() {
        return guest.get();
    }

    public ObjectProperty<GuestInformation> guestProperty() {
        return guest;
    }

    public void setGuest(GuestInformation guest) {
        this.guest.set(guest);
    }

    public ObservableList<Room> getRoom(){
        return room.get();
    }

    public ObjectProperty<ObservableList<Room>> roomProperty() {
        return room;
    }

    public void setRoom(ObservableList<Room> room) {
        this.room.set(room);
    }

    public int getNoOfRooms() {
        return noOfRooms.get();
    }

    public IntegerProperty noOfRoomsProperty() {
        return noOfRooms;
    }

    public void setNoOfRooms(int noOfRooms) {
        this.noOfRooms.set(noOfRooms);
    }

    public int getNoOfGuests() {
        return noOfGuests.get();
    }

    public IntegerProperty noOfGuestsProperty() {
        return noOfGuests;
    }

    public void setNoOfGuests(int noOfGuests) {
        this.noOfGuests.set(noOfGuests);
    }

    public int getLengthOfStay() {
        return lengthOfStay.get();
    }

    public IntegerProperty lengthOfStayProperty() {
        return lengthOfStay;
    }

    public void setLengthOfStay(int lengthOfStay) {
        this.lengthOfStay.set(lengthOfStay);
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

//    public void calculateTotalCost() {
//        totalCost.set(noOfRooms.get() * lengthOfStay.get());
//    }

    public boolean getIsPaid() {
        return isPaid.get();
    }

    public BooleanProperty isPaidProperty() {
        return isPaid;
    }

    public void setIsPaid(boolean isPaid) {
        this.isPaid.set(isPaid);
    }

    public boolean getIsCancelled() {
        return isCancelled.get();
    }

    public BooleanProperty isCancelledProperty() {
        return isCancelled;
    }

    public void setIsCancelled(boolean isCancelled) {
        this.isCancelled.set(isCancelled);
    }

    public String toString() {
        System.out.println("Reservation ID: " + reservation_id.get());
        System.out.println("Book ID: " + bookID.get());
        System.out.println("Book Date: " + bookDate.get());
        System.out.println("Check In: " + checkIn.get());
        System.out.println("Check Out: " + checkOut.get());
        System.out.println("Guest: " + guest.get().getFirstName());
        System.out.println("Rooms: "+ room.get().size() + " ");
//        for (Room room : room.get()){
//            System.out.print(room.getRoomNumber() + " ");
//        }
//        System.out.println();
        System.out.println("No of Rooms: " + noOfRooms.get());
        System.out.println("Length of Stay: " + lengthOfStay.get());
        System.out.println("Total Cost: " + totalCost.get());
        System.out.println("Is Paid: " + isPaid.get());
        return null;
    }

}
