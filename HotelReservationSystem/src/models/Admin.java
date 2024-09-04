package models;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Admin {
    private final IntegerProperty adminID = new SimpleIntegerProperty();
    private final StringProperty adminPassword = new SimpleStringProperty();

    public Admin() {}

    // Getters and setters using binding properties
    public int getAdminID() {
        return adminID.get();
    }

    public IntegerProperty adminIDProperty() {
        return adminID;
    }

    public void setAdminID(int adminID) {
        this.adminID.set(adminID);
    }

    public String getAdminPassword() {
        return adminPassword.get();
    }

    public StringProperty adminPasswordProperty() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword.set(adminPassword);
    }
}
