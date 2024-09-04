package models;

import enumarators.RoomType;
import javafx.beans.property.*;

public class Room {
    private final IntegerProperty roomID = new SimpleIntegerProperty();
    private final IntegerProperty roomNumber = new SimpleIntegerProperty();
    private final ObjectProperty<RoomType> roomType = new SimpleObjectProperty<>();
    private final DoubleProperty rate = new SimpleDoubleProperty();

    public Room(){}

    public Room(int roomID, int roomNumber, RoomType roomType) {
        this.roomID.set(roomID);
        this.roomNumber.set(roomNumber);
        this.roomType.set(roomType);
        this.rate.set(roomType.getRate());
    }

    // Getters and setters using binding properties
    public int getRoomID() {
        return roomID.get();
    }

    public IntegerProperty roomIDProperty() {
        return roomID;
    }

    public void setRoomID(int roomID) {
        this.roomID.set(roomID);
    }

    public int getRoomNumber() {
        return roomNumber.get();
    }

    public IntegerProperty roomNumberProperty() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber.set(roomNumber);
    }

    public RoomType getRoomType() {
        return roomType.get();
    }

    public ObjectProperty<RoomType> roomTypeProperty() {
        return roomType;
    }

    public void setRoomType(RoomType roomType) {

        this.roomType.set(roomType);
        this.rate.set(roomType.getRate());
    }

    public double getRate() {
        return rate.get();
    }

    public DoubleProperty rateProperty() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate.set(rate);
    }

}
