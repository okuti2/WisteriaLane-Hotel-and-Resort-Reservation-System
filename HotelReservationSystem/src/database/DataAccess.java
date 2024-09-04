package database;

import java.math.BigDecimal;
import java.sql.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import models.*;
import enumarators.*;

public class DataAccess {
    private static  String DB_URL= "jdbc:mysql://localhost:3306/hotelreservationsystem?useSSL=false";
    private static final String DB_USERNAME="root";
    private static final String DB_PASSWORD="Fol@kunm1";


    // Insert query for Guest
    private static final String GUEST_INSERT_QRY = "INSERT INTO Guest (first_name, last_name, title, email, address, phone_number) VALUES (?, ?, ?, ?, ?, ?)";
    // Insert query for Bill
    private static final String BILL_INSERT_QRY = "INSERT INTO Bill (bill_id, book_no, discount_applied, total_cost, admin_no) VALUES (?, ?, ?, ?, ?)";
    // Insert query for Reservation
    private static final String RESERVATION_INSERT_QRY = "INSERT INTO Reservation (reservation_id, reservation_date, check_in, check_out, guest_id, no_of_guests, paid, cancelled) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    // Insert query for Room_Reservation
    private static final String ROOM_RESERVATION_INSERT_QRY = "INSERT INTO Room_Reservation (room_id, reserve_id) VALUES (?, ?)";
    // Select query for Admin
    private static final String ADMIN_SELECT_QRY = "SELECT * FROM Admin WHERE admin_username = ? AND admin_password = ?";
    private static final String ADMIN_SELECT_ID_QRY = "SELECT admin_id FROM Admin WHERE admin_no = ?";
    // Update query for Reservation
    private static final String RESERVATION_UPDATE_QRY = "UPDATE Reservation SET cancelled = ? WHERE reservation_id = ?";
    // Update query for Room
    private static final String ROOM_UPDATE_QRY = "UPDATE Room SET available = ? WHERE room_number = ?";
    private static final String ROOM_FROM_RESERVATION_ID = "SELECT * FROM Room WHERE room_id IN (SELECT room_id FROM Room_Reservation WHERE reserve_id = ?)";

    // Method to insert guest into the Guest table
    public int insertGuest(GuestInformation guest) {
        int generatedId = -1;

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(GUEST_INSERT_QRY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, guest.getFirstName());
            stmt.setString(2, guest.getLastName());
            stmt.setString(3, guest.getTitle());
            stmt.setString(4, guest.getEmail());
            stmt.setString(5, guest.getAddress());
            stmt.setString(6, guest.getPhone());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1); // Assuming guest_id is the auto-generated column
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    // Method to insert bill into the Bill table
    public void insertBill(Bill bill) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(BILL_INSERT_QRY);
             PreparedStatement admin = conn.prepareStatement(ADMIN_SELECT_ID_QRY)) {
            admin.setInt(1, (int) bill.getAdminID());
            ResultSet rs = admin.executeQuery();
            rs.next();
            stmt.setLong(1, bill.getBillID());
            stmt.setLong(2, bill.getReservation().getReservationID());
            stmt.setBigDecimal(3, bill.getDiscount());
            stmt.setBigDecimal(4, bill.getTotalCost());
            stmt.setLong(5, rs.getInt("admin_id"));
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to insert reservation into the Reservation table
    public int insertReservation(Reservation reservation) {
        int generatedId = -1;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(RESERVATION_INSERT_QRY, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, reservation.getBookID());
            stmt.setDate(2, reservation.getBookDate());
            stmt.setDate(3, reservation.getCheckIn());
            stmt.setDate(4, reservation.getCheckOut());
            stmt.setInt(5, reservation.getGuest().getGuestID());
            stmt.setInt(6, reservation.getNoOfGuests());
            stmt.setBoolean(7, reservation.getIsPaid());
            stmt.setBoolean(8, reservation.getIsCancelled());
            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedId = generatedKeys.getInt(1); // Assuming reservation_id is the auto-generated column
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return generatedId;
    }

    public void setRoomsForReservation(Reservation reservation) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(ROOM_RESERVATION_INSERT_QRY)){
            for (Room room : reservation.getRoom()) {
                stmt.setInt(1, room.getRoomID());
                stmt.setInt(2, reservation.getReservationID());
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to select from the Reservation table
    public Reservation selectReservation(long reservationID) {
        Reservation reservation = new Reservation();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Reservation WHERE reservation_id = ? AND paid = false");
             PreparedStatement roomStmt = conn.prepareStatement(ROOM_FROM_RESERVATION_ID);
                PreparedStatement guestStmt = conn.prepareStatement("SELECT * FROM Guest WHERE guest_id IN (SELECT guest_id FROM Reservation WHERE id = ?)")
        ) {
            stmt.setLong(1, reservationID);
            try (ResultSet rs = stmt.executeQuery()) {
                if(rs.next()) {
                    guestStmt.setInt(1, rs.getInt("id"));
                    ResultSet gs = guestStmt.executeQuery();
                    GuestInformation guest = new GuestInformation();
                    while (gs.next()) {
                        guest.setGuestID(gs.getInt("guest_id"));
                        guest.setFirstName(gs.getString("first_name"));
                        guest.setLastName(gs.getString("last_name"));
                        guest.setTitle(gs.getString("title"));
                        guest.setEmail(gs.getString("email"));
                        guest.setAddress(gs.getString("address"));
                        guest.setPhone(gs.getString("phone_number"));
                    }
                    reservation = createReservation(rs, guest, roomStmt);
                }else{
                    reservation = null;
                }

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservation;
    }

    public ObservableList<Reservation> getReservations(String name, String phone, boolean paid) { // A reservation can only have one Guest but multiple rooms
        ObservableList<Reservation> reservations = FXCollections.observableArrayList();
        PreparedStatement stmt = null;
        PreparedStatement guestStmt = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement roomStmt = conn.prepareStatement(ROOM_FROM_RESERVATION_ID)) {

            // If name and phone are provided, fetch reservations related to the guest
            if (name!= null && phone != null) {
                if(name.isEmpty() && phone.isEmpty()){
                    guestStmt = conn.prepareStatement("SELECT * FROM Guest");
                }else{
                    guestStmt = conn.prepareStatement("SELECT * FROM Guest WHERE last_name = ? OR phone_number = ?");
                    guestStmt.setString(1, name);
                    guestStmt.setString(2, phone);
                };
                stmt = conn.prepareStatement("SELECT * FROM Reservation WHERE guest_id = ?");
                ResultSet guestRs = guestStmt.executeQuery();

                while (guestRs.next()) {
                    GuestInformation guest = new GuestInformation();
                    guest.setFirstName(guestRs.getString("first_name"));
                    guest.setLastName(guestRs.getString("last_name"));
                    guest.setTitle(guestRs.getString("title"));
                    guest.setEmail(guestRs.getString("email"));
                    guest.setAddress(guestRs.getString("address"));
                    guest.setPhone(guestRs.getString("phone_number"));

                    // Fetch reservations for the guest
                    int guestId = guestRs.getInt("guest_id");
                    stmt.setInt(1,guestId);
                    try (ResultSet rs = stmt.executeQuery()) {
                        while (rs.next()) {
                            Reservation reservation = createReservation(rs, guest, roomStmt);
                            reservations.add(reservation);
                            guest.getReservation().add(reservation);
                        }
                    }
                }
            } else {
                // If no name or phone number provided, fetch all reservations
                stmt = conn.prepareStatement("SELECT * FROM Reservation");
                guestStmt = conn.prepareStatement("SELECT * FROM Guest WHERE guest_id IN (SELECT guest_id FROM Reservation WHERE id = ?)");
                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                        guestStmt.setInt(1, rs.getInt("id"));
                        ResultSet gs = guestStmt.executeQuery();
                        GuestInformation guest = new GuestInformation();
                        while (gs.next()) {
                            guest.setGuestID(gs.getInt("guest_id"));
                            guest.setFirstName(gs.getString("first_name"));
                            guest.setLastName(gs.getString("last_name"));
                            guest.setTitle(gs.getString("title"));
                            guest.setEmail(gs.getString("email"));
                            guest.setAddress(gs.getString("address"));
                            guest.setPhone(gs.getString("phone_number"));
                        }
                        Reservation reservation = createReservation(rs, guest, roomStmt);
                        if (!paid && !reservation.getIsPaid() && !reservation.getIsCancelled()) {
                            reservations.add(reservation);
                        }


                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return reservations;
    }

    private Reservation createReservation(ResultSet rs, GuestInformation guest, PreparedStatement roomStmt) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationID(rs.getInt("id"));
        reservation.setBookID(rs.getInt("reservation_id"));
        reservation.setBookDate(rs.getDate("reservation_date"));
        reservation.setCheckIn(rs.getDate("check_in"));
        reservation.setCheckOut(rs.getDate("check_out"));
        reservation.setNoOfGuests(rs.getInt("no_of_guests"));
        reservation.setLengthOfStay((int) ((rs.getDate("check_out").getTime() - rs.getDate("check_in").getTime()) / (1000 * 60 * 60 * 24)));
        reservation.setIsPaid(rs.getBoolean("paid"));
        reservation.setIsCancelled(rs.getBoolean("cancelled"));
        reservation.setGuest(guest);

        // Fetch and set rooms for the reservation
        roomStmt.setInt(1, rs.getInt("id"));
        try (ResultSet roomRs = roomStmt.executeQuery()) {
            int totalrate = 0;
            ObservableList<Room> rooms = FXCollections.observableArrayList();
            while (roomRs.next()) {
                Room room = new Room();
                room.setRoomID(roomRs.getInt("room_id"));
                room.setRoomNumber(roomRs.getInt("room_number"));
                room.setRoomType(RoomType.valueOf(roomRs.getString("room_type").toUpperCase()));
                rooms.add(room);
                totalrate += (int) room.getRoomType().getRate();
            }
            reservation.setTotalCost(BigDecimal.valueOf((long) totalrate *reservation.getLengthOfStay()));
            reservation.setRoom(rooms);
            reservation.setNoOfRooms(rooms.size());
        }
        return reservation;
    }

    public void checkoutReservation(Reservation reservation){
        try(Connection conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement("UPDATE Reservation SET paid = true WHERE id = ?")){
            stmt.setInt(1, reservation.getReservationID());
            stmt.executeUpdate();
            for(Room room : reservation.getRoom()){
                updateRoom(true, room.getRoomNumber());
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    // Method to return Guests from the Guest table when given the phone number

    public ObservableList<GuestInformation> getGuests(String phone) {
        ObservableList<GuestInformation> guests = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Guest WHERE phone_number = ?");
             PreparedStatement reservationStmt = conn.prepareStatement("SELECT * FROM Reservation WHERE guest_id = ?")) {
            stmt.setString(1, phone);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    PreparedStatement roomStmt = conn.prepareStatement(ROOM_FROM_RESERVATION_ID);
                    GuestInformation guest = new GuestInformation();
                    guest.setGuestID(rs.getInt("guest_id"));
                    guest.setFirstName(rs.getString("first_name"));
                    guest.setLastName(rs.getString("last_name"));
                    guest.setTitle(rs.getString("title"));
                    guest.setEmail(rs.getString("email"));
                    guest.setAddress(rs.getString("address"));
                    guest.setPhone(rs.getString("phone_number"));
                    guests.add(guest);
                    reservationStmt.setInt(1, guest.getGuestID());
                    try (ResultSet Reserve = reservationStmt.executeQuery()) {
                        while (Reserve.next()) {
                            Reservation reservation = createReservation(Reserve, guest, roomStmt);
                            guest.getReservation().add(reservation);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return guests;
    }

    // This function receives a room type and number of rooms to be booked, returns a list of available rooms and updates each room in the database to unavailable
    public ObservableList<Room> bookRoom(RoomType roomType, int noOfRooms) {
        if(noOfRooms <= 0) {
            return FXCollections.observableArrayList();
        }
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        String roomTypeString = roomType.toString().toLowerCase();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Room WHERE room_type = ? AND available = true LIMIT ?")) {
            stmt.setString(1,roomTypeString);
            stmt.setInt(2, noOfRooms);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(new Room(rs.getInt("room_id"), rs.getInt("room_number"),roomType));
                    updateRoom(false, rs.getInt("room_number"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // Return number of Rooms that are available based on the room type
    public int getAvailableRooms(RoomType roomType) {
        int availableRooms = 0;
        String roomTypeString = roomType.toString().toLowerCase();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT COUNT(*) FROM Room WHERE room_type = ? AND available = true")) {
            stmt.setString(1, roomTypeString);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    availableRooms = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return availableRooms;
    }

    public ObservableList<Room> getAvailableRooms(){
        ObservableList<Room> rooms = FXCollections.observableArrayList();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Room WHERE available = true")) {
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rooms.add(new Room(rs.getInt("room_id"), rs.getInt("room_number"), RoomType.valueOf(rs.getString("room_type").toUpperCase())));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rooms;
    }

    public void deleteReservation(Reservation reservation) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(RESERVATION_UPDATE_QRY)){
                for(Room room : reservation.getRoom()){
                    updateRoom(true, room.getRoomNumber());
                }
                stmt.setBoolean(1, true);
                stmt.setLong(2, reservation.getBookID());
                stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to update room in the Room table
    public void updateRoom(boolean available,int roomNumber) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(ROOM_UPDATE_QRY)) {
            stmt.setBoolean(1, available);
            stmt.setLong(2, roomNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int validation(String username, String pass) throws SQLException {
        int adminNo = -1;
        try(Connection conn = DriverManager.getConnection(DB_URL,DB_USERNAME,DB_PASSWORD);
            PreparedStatement ps = conn.prepareStatement(ADMIN_SELECT_QRY)){

            ps.setString(1, username);
            ps.setString(2, pass);

            ResultSet rs = ps.executeQuery();
            while(rs.next())
                adminNo = rs.getInt("admin_no");

        }
        return adminNo;
    }

    //ROOM_FROM_RESERVATION_ID = "SELECT * FROM Room WHERE room_id IN (SELECT room_id FROM Room_Reservation WHERE reserve_id = ?)"
    public ObservableList<Bill> getBills(String name, String phone) { // A bill can only have one reservation and one guest
        ObservableList<Bill> bills = FXCollections.observableArrayList();
        PreparedStatement billStmt = null;
        PreparedStatement Reservestmt = null;
        PreparedStatement guestStmt = null;
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement roomStmt = conn.prepareStatement(ROOM_FROM_RESERVATION_ID)) {

            // If name and phone are provided, fetch reservations related to the guest
            if (!name.isEmpty() || !phone.isEmpty()) {
                guestStmt = conn.prepareStatement("SELECT * FROM Guest WHERE last_name = ? OR phone_number = ?");
                Reservestmt = conn.prepareStatement("SELECT * FROM Reservation WHERE guest_id = ?");
                billStmt = conn.prepareStatement("SELECT * FROM Bill WHERE book_no = ?");
                guestStmt.setString(1, name);
                guestStmt.setString(2, phone);
                ResultSet guestRs = guestStmt.executeQuery();

                while (guestRs.next()) {
                    GuestInformation guest = new GuestInformation();
                    guest.setFirstName(guestRs.getString("first_name"));
                    guest.setLastName(guestRs.getString("last_name"));
                    guest.setTitle(guestRs.getString("title"));
                    guest.setEmail(guestRs.getString("email"));
                    guest.setAddress(guestRs.getString("address"));
                    guest.setPhone(guestRs.getString("phone_number"));

                    // Fetch reservations for the guest
                    int guestId = guestRs.getInt("guest_id");
                    Reservestmt.setInt(1, guestId);
                    try (ResultSet rs = Reservestmt.executeQuery()) {
                        while (rs.next()) {
                            Reservation reservation = createReservation(rs, guest, roomStmt);
                            guest.getReservation().add(reservation);
                            billStmt.setInt(1, reservation.getReservationID());
                            try (ResultSet billRs = billStmt.executeQuery()) {
                                if(billRs.next()) {
                                    Bill bill = new Bill();
                                    bill.setId(billRs.getInt("id"));
                                    bill.setBillID(billRs.getInt("bill_id"));
                                    bill.setDiscount(billRs.getBigDecimal("discount_applied"));
                                    bill.setTotalCost(billRs.getBigDecimal("total_cost"));
                                    bill.setAdminID(billRs.getInt("admin_no"));
                                    bill.setReservation(reservation);
                                    bills.add(bill);
                                }

                            }catch (SQLException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }
            } else {
                // If no name or phone number provided, fetch all reservations
                Reservestmt = conn.prepareStatement("SELECT * FROM Reservation");
                guestStmt = conn.prepareStatement("SELECT * FROM Guest WHERE guest_id IN (SELECT guest_id FROM Reservation WHERE id = ?)");
                billStmt = conn.prepareStatement("SELECT * FROM Bill WHERE book_no = ?");
                try (ResultSet rs = Reservestmt.executeQuery()) {
                    while (rs.next()) {
                        guestStmt.setInt(1, rs.getInt("id"));
                        ResultSet gs = guestStmt.executeQuery();
                        GuestInformation guest = new GuestInformation();
                        while (gs.next()) {
                            guest.setGuestID(gs.getInt("guest_id"));
                            guest.setFirstName(gs.getString("first_name"));
                            guest.setLastName(gs.getString("last_name"));
                            guest.setTitle(gs.getString("title"));
                            guest.setEmail(gs.getString("email"));
                            guest.setAddress(gs.getString("address"));
                            guest.setPhone(gs.getString("phone_number"));
                        }
                        Reservation reservation = createReservation(rs, guest, roomStmt);
                        guest.getReservation().add(reservation);
                        billStmt.setInt(1, reservation.getReservationID());
                        try (ResultSet billRs = billStmt.executeQuery()) {
                            if(billRs.next()) {
                                Bill bill = new Bill();
                                bill.setId(billRs.getInt("id"));
                                bill.setBillID(billRs.getInt("bill_id"));
                                bill.setDiscount(billRs.getBigDecimal("discount_applied"));
                                bill.setTotalCost(billRs.getBigDecimal("total_cost"));
                                bill.setAdminID(billRs.getInt("admin_no"));
                                bill.setReservation(reservation);
                                bills.add(bill);
                            }

                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bills;
    }
}

