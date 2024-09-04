package controllers;

import database.DataAccess;
import enumarators.RoomType;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import models.Room;

public class AvailableRooms {

    private Stage st;
    private Scene sc;
    private Parent root;
    private DataAccess da;
    public AvailableRooms() {da = new DataAccess();}

    @FXML
    private Label availableRooms;
    @FXML
    private TableColumn<Room, Integer> roomNumber;

    @FXML
    private TableColumn<Room, RoomType> roomType;

    @FXML
    private TableView<Room> tableView;

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
        ObservableList<Room> rooms = da.getAvailableRooms();
        availableRooms.setText("" + rooms.size());
        roomNumber.setCellValueFactory(cellData -> cellData.getValue().roomNumberProperty().asObject());
        roomType.setCellValueFactory(cellData -> cellData.getValue().roomTypeProperty());
        tableView.setItems(rooms);
    }

}
