package controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.GuestInformation;
import models.Reservation;

public class SelectGuests {

    @FXML
    private TableColumn<GuestInformation, String> address;

    @FXML
    private TableColumn<GuestInformation, String> email;

    @FXML
    private TableColumn<GuestInformation, String> firstName;

    @FXML
    private TableColumn<GuestInformation, String> lastName;

    @FXML
    private TableColumn<GuestInformation, String> phone;

    @FXML
    private TableView<GuestInformation> guestTable;
    private ObservableList<GuestInformation> guests;
    public GuestInformation selectedGuest;
    private Stage st;


    public void setGuest(ObservableList<GuestInformation> guest) {
        this.guests = guest;

        firstName.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastName.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
        address.setCellValueFactory(cellData -> cellData.getValue().addressProperty());
        phone.setCellValueFactory(cellData -> cellData.getValue().phoneProperty());
        email.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        guestTable.setItems(guests);

    }
    public void setDialogStage(Stage dialogStage) {
        this.st = dialogStage;
    }

    @FXML
    void exit(ActionEvent event) {
        st.close();
    }

    @FXML
    void save(ActionEvent event) {
        selectedGuest = guestTable.getSelectionModel().getSelectedItem();
        st.close();
    }

}
