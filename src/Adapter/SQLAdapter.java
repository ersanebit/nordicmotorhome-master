package Adapter;

import Model.*;
import com.mysql.jdbc.*;
import javafx.scene.control.Alert;

import java.sql.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Created by lamtr on 03-May-17.
 */
public class SQLAdapter {
    private static String url = "jdbc:mysql://localhost:3306/nordicmotorhomes";
    private static String username = "root";
    private static String password = "";
    private static ResultSet rs;
    private static ArrayList<Motorhome> motorhomes;
    private static ArrayList<Reservation> reservations;
    private static ArrayList<Rental> rentals;
    private static ArrayList<Maintenance> maintenances;
    private static ArrayList<Customer> customers;
    private static ArrayList<Motorhome> availableMotorhomeList;
    private static ArrayList<Location> locations;
    private static ArrayList<Extra> extras;

    public ArrayList<Location> getLocationList(){
        return this.locations;
    }

    public ArrayList<Extra> getExtrasList(){
        return this.extras;
    }

    public static ArrayList<Customer> getCustomers() {
        return customers;
    }

    public static ArrayList<Motorhome> getMotorhomes() {
        return motorhomes;
    }

    //This function is not stable
    public ArrayList<Motorhome> getAvailableMotorhomeList(LocalDate startDate, LocalDate endDate){
        availableMotorhomeList = new ArrayList<>();
        //motorhomes = getMotorhomes();
        for (Motorhome motorhome : motorhomes) {
            if (!motorhome.isBroken() && motorhome.isCleaned()) {
                if (motorhome.getOccupiedPeriods().size() == 0)
                    availableMotorhomeList.add(motorhome);
                else
                    for (OccupiedPeriod occupiedPeriod : motorhome.getOccupiedPeriods()) {
                        if ((startDate.isBefore(occupiedPeriod.getStartDate()) && endDate.isBefore(occupiedPeriod.getStartDate())) ||
                                (startDate.isAfter(occupiedPeriod.getEndDate()) && endDate.isAfter(occupiedPeriod.getEndDate()))) {
                            if (!availableMotorhomeList.contains(motorhome)) {
                                availableMotorhomeList.add(motorhome);
                            }
                        } else {
                            if (availableMotorhomeList.contains(motorhome)) {
                                availableMotorhomeList.remove(motorhome);
                            }
                        }
                    }
            }
        }
        return availableMotorhomeList;
    }

    public ArrayList<Customer> getAllCustomers() throws SQLException {
        ResultSet rs = null;
        Connection con = getConn();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM customers WHERE id > 0;");
            customers = new ArrayList<>();
            while (rs.next()) {
                customers.add(new Customer(rs.getInt(1), rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6)));
            }
            return customers;
        }finally {
            try{ rs.close(); }catch (Exception e){};
            try{ con.close(); }catch (Exception e){};
            try{ stmt.close(); }catch (Exception e){};
        }
    }

    public ArrayList<Location> getLocations() throws SQLException {
        ResultSet rs = null;
        Connection con = getConn();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM locations WHERE private = FALSE ;");
            locations = new ArrayList<>();
            while (rs.next()) {
                locations.add(new Location(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getBoolean(4)));
            }
            return locations;
        }finally {
            try{ rs.close(); }catch (Exception e){};
            try{ con.close(); }catch (Exception e){};
            try{ stmt.close(); }catch (Exception e){};
        }
    }

    public ArrayList<Extra> getExtras() throws SQLException {
        ResultSet rs = null;
        Connection con = getConn();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT * FROM extras;");
            extras = new ArrayList<>();
            while (rs.next()) {
                extras.add(new Extra(rs.getInt(1), rs.getString(2), rs.getInt(3), rs.getDouble(4)));
            }
            return extras;
        }finally {
            try{ rs.close(); }catch (Exception e){};
            try{ con.close(); }catch (Exception e){};
            try{ stmt.close(); }catch (Exception e){};
        }
    }

    public ArrayList<Rental> getRentals() throws Exception{
        ResultSet rs = null;
        Connection con = getConn();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT rentals.* ,customers.*, pickup.*, dropoff.* FROM rentals JOIN customers, locations pickup, locations dropoff\n" +
                    "WHERE rentals.customer_id = customers.id AND pickup.id = rentals.pickup AND dropoff.id = rentals.dropoff;");
            rentals = new ArrayList<>();
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt(2), rs.getString(9),
                        rs.getString(10), rs.getString(11), rs.getString(12), rs.getString(13));

                Rental rental = new Rental(rs.getInt(1), customer, rs.getString(3), rs.getString(4), rs.getDouble(7));

                Location pickUp = new Location(rs.getInt(14), rs.getString(15), rs.getInt(16), rs.getBoolean(17));
                Location dropOff = new Location(rs.getInt(18), rs.getString(19), rs.getInt(20), rs.getBoolean(21));

                rental.setPickUp(pickUp);
                rental.setDropOff(dropOff);

                rentals.add(rental);
            }

            rs = stmt.executeQuery("SELECT rentals_motorhomes.rental_id,motorhomes.* FROM rentals_motorhomes " +
                    "JOIN motorhomes WHERE rentals_motorhomes.motorhome_id = motorhomes.id;");
            while (rs.next()) {
                Motorhome motorhome = new Motorhome(rs.getInt(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getDouble(6), rs.getInt(7), rs.getBoolean(8),
                        rs.getBoolean(9), rs.getBoolean(10), rs.getBoolean(11));
                for (Rental rental : rentals) {
                    if (rental.getId() == rs.getInt(1))
                        rental.addMotorhome(motorhome);
                }
            }
            rs = stmt.executeQuery("SELECT rentals_extras.rental_id,extras.*,rentals_extras.quantity FROM " +
                    "rentals_extras JOIN extras WHERE rentals_extras.extra_id = extras.id;");
            while (rs.next()) {
                Extra extra = new Extra(rs.getInt(2), rs.getString(3), rs.getInt(6), rs.getDouble(5));
                for (Rental rental : rentals) {
                    if (rental.getId() == rs.getInt(1))
                        rental.addExtra(extra);
                }
            }
            return rentals;
        }finally {
            try{ rs.close(); }catch (Exception e){};
            try{ con.close(); }catch (Exception e){};
            try{ stmt.close(); }catch (Exception e){};
        }
    }

    public ArrayList<Reservation> getReservations() throws Exception{
        ResultSet rs = null;
        Connection con = getConn();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            reservations = new ArrayList<>();
            rs = stmt.executeQuery("SELECT reservations.*,customers.*, pickup.*, dropoff.* FROM reservations JOIN customers, locations pickup, locations dropoff\n" +
                    "WHERE reservations.customer_id = customers.id AND pickup.id = reservations.pickup AND dropoff.id = reservations.dropoff;");
            while (rs.next()) {
                Customer customer = new Customer(rs.getInt(2), rs.getString(10),
                        rs.getString(11), rs.getString(12), rs.getString(13), rs.getString(14));
                Reservation reservation = new Reservation(rs.getInt(1), customer, rs.getString(3), rs.getString(4), rs.getString(5), rs.getDouble(8));
                Location pickUp = new Location(rs.getInt(15), rs.getString(16), rs.getInt(17), rs.getBoolean(18));
                Location dropOff = new Location(rs.getInt(19), rs.getString(20), rs.getInt(21), rs.getBoolean(22));
                reservation.setPickUp(pickUp);
                reservation.setDropOff(dropOff);
                reservations.add(reservation);
            }
            rs = stmt.executeQuery("SELECT reservations_motorhomes.reservation_id,motorhomes.* FROM reservations_motorhomes JOIN" +
                    " motorhomes WHERE reservations_motorhomes.motorhome_id = motorhomes.id;");
            while (rs.next()) {
                Motorhome motorhome = new Motorhome(rs.getInt(2), rs.getString(3), rs.getString(4),
                        rs.getInt(5), rs.getDouble(6), rs.getInt(7), rs.getBoolean(8),
                        rs.getBoolean(9), rs.getBoolean(10), rs.getBoolean(11));
                for (Reservation reservation : reservations) {
                    if (reservation.getId() == rs.getInt(1))
                        reservation.addMotorhome(motorhome);
                }
            }
            rs = stmt.executeQuery("SELECT reservations_extras.reservation_id,extras.*,reservations_extras.quantity FROM " +
                    "reservations_extras JOIN extras WHERE reservations_extras.extra_id = extras.id;");
            while (rs.next()) {
                Extra extra = new Extra(rs.getInt(2), rs.getString(3), rs.getInt(6), rs.getDouble(5));
                for (Reservation reservation : reservations) {
                    if (reservation.getId() == rs.getInt(1))
                        reservation.addExtra(extra);
                }
            }
            return reservations;
        }finally {
            try{ rs.close(); }catch (Exception e){};
            try{ con.close(); }catch (Exception e){};
            try{ stmt.close(); }catch (Exception e){};
        }
    }

    public ArrayList<Motorhome> getMotorhome() throws Exception{
        ResultSet rs = null;
        Connection con = getConn();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            motorhomes = new ArrayList<>();
            rs = stmt.executeQuery("SELECT * FROM  motorhomes");
            while (rs.next()) {
                Motorhome motorhome = new Motorhome(rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getInt(4), rs.getDouble(5), rs.getInt(6),
                        rs.getBoolean(7), rs.getBoolean(8), rs.getBoolean(9), rs.getBoolean(10)
                );
                motorhomes.add(motorhome);
            }

            rs = stmt.executeQuery("SELECT rentals_motorhomes.motorhome_id,rentals.* FROM rentals_motorhomes JOIN rentals WHERE rentals_motorhomes.rental_id = rentals.id;");

            while (rs.next()) {
                Rental rental = new Rental(rs.getInt(2), rs.getString(4), rs.getString(5));
                for (Motorhome motorhome : motorhomes)
                    if (motorhome.getId() == rs.getInt(1))
                        motorhome.addRentals(rental);
            }

            rs = stmt.executeQuery("SELECT reservations_motorhomes.motorhome_id,reservations.* FROM reservations_motorhomes JOIN reservations WHERE reservations_motorhomes.reservation_id = reservations.id;");

            while (rs.next()) {
                Reservation reservation = new Reservation(rs.getInt(2), rs.getString(4), rs.getString(5), rs.getString(6));
                for (Motorhome motorhome : motorhomes)
                    if (motorhome.getId() == rs.getInt(1))
                        motorhome.addReservations(reservation);
            }

            return motorhomes;
        }finally {
            try{ rs.close(); }catch (Exception e){};
            try{ con.close(); }catch (Exception e){};
            try{ stmt.close(); }catch (Exception e){};
        }
    }

    public ArrayList<Maintenance> getMaintenances() throws Exception{
        ResultSet rs = null;
        Connection con = getConn();
        Statement stmt = null;
        try{
            stmt = con.createStatement();
            maintenances = new ArrayList<>();
            rs = stmt.executeQuery("SELECT maintenances.id, repair_cost,message, motorhomes.id, motorhomes.brand, motorhomes.model,motorhomes.cleaned,motorhomes.broken\n" +
                    "FROM maintenances JOIN motorhomes ON maintenances.car_id = motorhomes.id;");
            while (rs.next()) {
                Maintenance maintenance = new Maintenance(rs.getInt(1), rs.getInt(4),
                        rs.getString(5), rs.getString(6), rs.getBoolean(7), rs.getBoolean(8),
                        rs.getString(3), rs.getDouble(2)
                );
                maintenances.add(maintenance);
            }
            return maintenances;
        }finally {
            try{ rs.close(); }catch (Exception e){};
            try{ con.close(); }catch (Exception e){};
            try{ stmt.close(); }catch (Exception e){};
        }
    }

    public Connection getConn(){
        Connection con = null;
        try{
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection(url,username,password);
            return con;
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public ResultSet logIn(String user, String pass){
        String sql = "SELECT type FROM `users` WHERE users.username = ? AND users.password = MD5(?);";

        PreparedStatement ps = null;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setString(1, user);
            ps.setString(2, pass);
            ps.executeQuery();
            return ps.getResultSet();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void addRental(Rental rental) throws SQLException{
        Connection con = getConn();
        PreparedStatement stmt = null;
        ResultSet cprDB = null;
        ResultSet pickupDB = null;
        ResultSet dropoffDB = null;
        try {
            String query;
            //Add new row to rentals. Notice customer_id, pickup & dropoff could have value 0.
            query = "INSERT INTO rentals (id, customer_id, start_date, end_date, pickup, dropoff, price) " +
                    "VALUE (NULL,?,?,?,?,?,?) ; ";
            stmt = con.prepareStatement(query);
            stmt.setInt(1,rental.getCustomer().getId());
            stmt.setString(2, rental.getStartDate());
            stmt.setString(3, rental.getEndDate());
            stmt.setInt(4,rental.getPickUp().getId());
            stmt.setInt(5,rental.getDropOff().getId());
            stmt.setDouble(6, rental.getPrice());
            stmt.executeUpdate();

            query = "SET @rentalId := LAST_INSERT_ID();";
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();

            //Insert motorhome lists for a rental
            for (int i = 0; i < rental.getMotorhomes().size(); i++){
                query = "INSERT INTO rentals_motorhomes (id, rental_id, motorhome_id) VALUES ( NULL ,@rentalId , ?);";
                stmt = con.prepareStatement(query);
                stmt.setInt(1, rental.getMotorhomes().get(i).getId());
                stmt.executeUpdate();
            }
            //Insert the extras of a rental
            for (int i = 0; i < rental.getExtras().size(); i++){
                query = "INSERT INTO rentals_extras (id, rental_id, extra_id, quantity) VALUES (NULL ,@rentalId,?,?);";
                stmt = con.prepareStatement(query);
                stmt.setInt(1, rental.getExtras().get(i).getId());
                stmt.setInt(2, rental.getExtras().get(i).getQuantity());
                stmt.executeUpdate();
            }
            //Update motorhome status
            for (int i = 0; i < rental.getMotorhomes().size(); i++) {
                query = "UPDATE motorhomes SET rented = 1 WHERE motorhomes.id = " + rental.getMotorhomes().get(i).getId() + ";";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
            //Update extra quantity
            for (int i = 0; i < rental.getExtras().size(); i++){
                for (int j = 0; j < extras.size(); j++)
                    if (extras.get(j).getId() == rental.getExtras().get(i).getId()){
                        query = "UPDATE extras SET quantity = " + extras.get(j).getQuantity() + " WHERE extras.id = " + rental.getExtras().get(i).getId() + ";";
                        stmt = con.prepareStatement(query);
                        stmt.executeUpdate();
                }
            }
            //Add customer if not exist
            query = "SELECT cpr FROM customers WHERE cpr = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1,rental.getCustomer().getCpr());
            cprDB = stmt.executeQuery();
            if (!cprDB.isBeforeFirst()){
                query = "INSERT INTO customers (id, name, cpr, address,email,phone) VALUES (NULL ,?,?,?,?,?);";
                stmt = con.prepareStatement(query);
                stmt.setString(1, rental.getCustomer().getName());
                stmt.setString(2, rental.getCustomer().getCpr());
                stmt.setString(3, rental.getCustomer().getAddress());
                stmt.setString(4, rental.getCustomer().getEmail());
                stmt.setString(5, rental.getCustomer().getPhoneNumber());
                stmt.executeUpdate();
                //Update customer_id column in rentals. At this time customer_id should be 0. After the UPDATE id should be set to newly added customer's id.
                query = "UPDATE rentals SET customer_id = LAST_INSERT_ID() WHERE rentals.id = @rentalId;";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
            //Add Pick up point if not exist
            query = "SELECT address FROM locations WHERE address = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1,rental.getPickUp().getAddress());
            pickupDB = stmt.executeQuery();
            if (!pickupDB.isBeforeFirst()){
                query = "INSERT INTO locations (id, address, km, private) VALUES (NULL , ?, ? , ?);\n";
                stmt = con.prepareStatement(query);
                stmt.setString(1,rental.getPickUp().getAddress());
                stmt.setInt(2,rental.getPickUp().getKm());
                stmt.setBoolean(3, rental.getPickUp().isPrivateAddress());
                stmt.executeUpdate();
                //Update pickup column in rentals. At this time pickup should be 0. After the UPDATE id should be set to newly added location's id.
                query = "UPDATE rentals SET pickup = LAST_INSERT_ID() WHERE rentals.id = @rentalId;";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
            //Add Drop off point if not exist
            query = "SELECT address FROM locations WHERE address = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1,rental.getDropOff().getAddress());
            dropoffDB = stmt.executeQuery();
            if (!dropoffDB.isBeforeFirst()){
                query = "INSERT INTO locations (id, address, km, private) VALUES (NULL , ?, ? , ?);\n";
                stmt = con.prepareStatement(query);
                stmt.setString(1,rental.getDropOff().getAddress());
                stmt.setInt(2,rental.getDropOff().getKm());
                stmt.setBoolean(3, rental.getDropOff().isPrivateAddress());
                stmt.executeUpdate();
                //Update dropoff column in rentals. At this time dropoff should be 0. After the UPDATE id should be set to newly added location's id.
                query = "UPDATE rentals SET dropoff = LAST_INSERT_ID() WHERE rentals.id = @rentalId;";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
            try { pickupDB.close(); } catch (Exception e) { /* ignored */ }
            try { dropoffDB.close(); } catch (Exception e) { /* ignored */ }
            try { cprDB.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void deleteRental(Rental rental) throws SQLException {
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {
            String query;
            query = "DELETE FROM rentals WHERE rentals.id =" + rental.getId() + ";";
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();

            //Update motorhome status
            for (int i = 0; i < rental.getMotorhomes().size(); i++) {
                Motorhome motorhome = rental.getMotorhomes().get(i);

                query = "UPDATE motorhomes SET km_driven = "+motorhome.getNewKmDriven()+
                        " ,rented = 0, cleaned = 0, broken = "+motorhome.isBroken()+" WHERE motorhomes.id = " + motorhome.getId() + ";";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();

                query = "INSERT INTO `maintenances` (`id`, `car_id`, `repair_cost`, `message`) " +
                        "VALUES (NULL, '" + motorhome.getId() + "', '" + motorhome.getMaintenancePrice() + "', '" + motorhome.getNote() + "');";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
            //Update extra quantity
            for (int i = 0; i < rental.getExtras().size(); i++) {
                for (int j = 0; j < extras.size(); j++) {
                    Extra extraInStock = extras.get(j);
                    Extra extraRental =  rental.getExtras().get(i);
                    if (extraInStock.getId() == extraRental.getId()){
                        query = "UPDATE extras SET quantity = " + (extraInStock.getQuantity() + extraRental.getQuantity()) + " WHERE extras.id = " + extraRental.getId() + ";";
                        stmt = con.prepareStatement(query);
                        stmt.executeUpdate();
                    }
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void addReservation(Reservation reservation) throws SQLException{
        Connection con = getConn();
        PreparedStatement stmt = null;
        ResultSet result = null;
        ResultSet dropoffDB = null;
        try {
            String query;
            //Add new row to rentals. Notice customer_id, pickup & dropoff could have value 0.
            query = "INSERT INTO reservations (id, customer_id,reservation_date, start_date, end_date, pickup, dropoff, price) " +
                    "VALUE (NULL,?,?,?,?,?,?,?) ; ";
            stmt = con.prepareStatement(query);
            stmt.setInt(1,reservation.getCustomer().getId());
            stmt.setString(2, reservation.getReservationDate());
            stmt.setString(3, reservation.getStartDate());
            stmt.setString(4, reservation.getEndDate());
            stmt.setInt(5,reservation.getPickUp().getId());
            stmt.setInt(6,reservation.getDropOff().getId());
            stmt.setDouble(7, reservation.getPrice());
            stmt.executeUpdate();

            query = "SET @reservationId := LAST_INSERT_ID();";
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();

            //Insert motorhome lists for a reservation
            for (int i = 0; i < reservation.getMotorhomes().size(); i++){
                query = "INSERT INTO reservations_motorhomes (id, reservation_id, motorhome_id) VALUES ( NULL ,@reservationId , ?);";
                stmt = con.prepareStatement(query);
                stmt.setInt(1, reservation.getMotorhomes().get(i).getId());
                stmt.executeUpdate();
            }
            //Insert the extras of a reservation
            for (int i = 0; i < reservation.getExtras().size(); i++){
                query = "INSERT INTO reservations_extras (id, reservation_id, extra_id, quantity) VALUES (NULL ,@reservationId,?,?);";
                stmt = con.prepareStatement(query);
                stmt.setInt(1, reservation.getExtras().get(i).getId());
                stmt.setInt(2, reservation.getExtras().get(i).getQuantity());
                stmt.executeUpdate();
            }
            //Update motorhome status
            for (int i = 0; i < reservation.getMotorhomes().size(); i++) {
                query = "UPDATE motorhomes SET reserved = 1 WHERE motorhomes.id = " + reservation.getMotorhomes().get(i).getId() + ";";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
            //Add customer if not exist
            query = "SELECT cpr FROM customers WHERE cpr = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1,reservation.getCustomer().getCpr());
            result = stmt.executeQuery();
            if (!result.isBeforeFirst()){
                query = "INSERT INTO customers (id, name, cpr, address,email,phone) VALUES (NULL ,?,?,?,?,?);";
                stmt = con.prepareStatement(query);
                stmt.setString(1, reservation.getCustomer().getName());
                stmt.setString(2, reservation.getCustomer().getCpr());
                stmt.setString(3, reservation.getCustomer().getAddress());
                stmt.setString(4, reservation.getCustomer().getEmail());
                stmt.setString(5, reservation.getCustomer().getPhoneNumber());
                stmt.executeUpdate();
                //Update customer_id column in rentals. At this time customer_id should be 0. After the UPDATE id should be set to newly added customer's id.
                query = "UPDATE reservations SET customer_id = LAST_INSERT_ID() WHERE reservations.id = @reservationId;";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
            //Add Pick up point if not exist
            query = "SELECT address FROM locations WHERE address = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1,reservation.getPickUp().getAddress());
            ResultSet pickupDB = stmt.executeQuery();
            if (!pickupDB.isBeforeFirst()){
                query = "INSERT INTO locations (id, address, km, private) VALUES (NULL , ?, ? , ?);\n";
                stmt = con.prepareStatement(query);
                stmt.setString(1,reservation.getPickUp().getAddress());
                stmt.setInt(2,reservation.getPickUp().getKm());
                stmt.setBoolean(3, reservation.getPickUp().isPrivateAddress());
                stmt.executeUpdate();
                //Update pickup column in rentals. At this time pickup should be 0. After the UPDATE id should be set to newly added location's id.
                query = "UPDATE reservations SET pickup = LAST_INSERT_ID() WHERE reservations.id = @reservationId;";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
            //Add Drop off point if not exist
            query = "SELECT address FROM locations WHERE address = ?";
            stmt = con.prepareStatement(query);
            stmt.setString(1,reservation.getDropOff().getAddress());
            dropoffDB = stmt.executeQuery();
            if (!dropoffDB.isBeforeFirst()){
                query = "INSERT INTO locations (id, address, km, private) VALUES (NULL , ?, ? , ?);\n";
                stmt = con.prepareStatement(query);
                stmt.setString(1,reservation.getDropOff().getAddress());
                stmt.setInt(2,reservation.getDropOff().getKm());
                stmt.setBoolean(3, reservation.getDropOff().isPrivateAddress());
                stmt.executeUpdate();
                //Update dropoff column in rentals. At this time dropoff should be 0. After the UPDATE id should be set to newly added location's id.
                query = "UPDATE reservations SET dropoff = LAST_INSERT_ID() WHERE reservations.id = @reservationId;";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
            try { dropoffDB.close(); } catch (Exception e) { /* ignored */ }
            try { result.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void deleteReservation(Reservation reservation) throws SQLException {
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {
            String query;
            query = "DELETE FROM reservations WHERE reservations.id =" + reservation.getId() + ";";
            stmt = con.prepareStatement(query);
            stmt.executeUpdate();

            //Update motorhome status
            for (int i = 0; i < reservation.getMotorhomes().size(); i++) {
                Motorhome motorhome = reservation.getMotorhomes().get(i);

                query = "UPDATE motorhomes SET reserved = 0, rented = 1 WHERE motorhomes.id = " + motorhome.getId() + ";";
                stmt = con.prepareStatement(query);
                stmt.executeUpdate();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void addMotorhome (Motorhome motorhome) throws SQLException{
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query = "INSERT INTO motorhomes (id , brand, model, capacity, price, km_driven, cleaned, broken, reserved, rented ) VALUES (NULL ,?,?,?,?,?,?,?,?,?);";
            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, motorhome.getBrand());
            stmt.setString(2, motorhome.getModel());
            stmt.setInt(3, motorhome.getCapacity());
            stmt.setDouble(4, motorhome.getPrice());
            stmt.setInt(5, motorhome.getKmDriven());
            stmt.setBoolean(6, motorhome.isCleaned());
            stmt.setBoolean(7, motorhome.isBroken());
            stmt.setBoolean(8, motorhome.isReserved());
            stmt.setBoolean(9, motorhome.isRented());
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
                int last_inserted_id = rs.getInt(1);
                System.out.println("last id "+last_inserted_id);
                motorhome.setId(last_inserted_id);
                motorhomes.add(motorhome);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void updateMotorhome(Motorhome motorhome){


        for (int i = 0; i < motorhomes.size(); i++){
            if (motorhome.getId() == motorhomes.get(i).getId()){
                motorhomes.get(i).setBrand(motorhome.getBrand());
                motorhomes.get(i).setModel(motorhome.getModel());
                motorhomes.get(i).setCapacity(motorhome.getCapacity());
                motorhomes.get(i).setPrice(motorhome.getBasePrice());
                motorhomes.get(i).setKmDriven(motorhome.getKmDriven());
            }
        }

        for (int i = 0; i < rentals.size(); i++){
            for (int j = 0; j < rentals.get(i).getMotorhomes().size(); j++){
                if (motorhome.getId() == rentals.get(i).getMotorhomes().get(j).getId()){
                    rentals.get(i).getMotorhomes().get(j).setBrand(motorhome.getBrand());
                    rentals.get(i).getMotorhomes().get(j).setModel(motorhome.getModel());
                    rentals.get(i).getMotorhomes().get(j).setCapacity(motorhome.getCapacity());
                    rentals.get(i).getMotorhomes().get(j).setPrice(motorhome.getBasePrice());
                    rentals.get(i).getMotorhomes().get(j).setKmDriven(motorhome.getKmDriven());
                }
            }
        }

        for (int i = 0; i < reservations.size(); i++){
            for (int j = 0; j < reservations.get(i).getMotorhomes().size(); j++){
                if (motorhome.getId() == reservations.get(i).getMotorhomes().get(j).getId()){
                    reservations.get(i).getMotorhomes().get(j).setBrand(motorhome.getBrand());
                    reservations.get(i).getMotorhomes().get(j).setModel(motorhome.getModel());
                    reservations.get(i).getMotorhomes().get(j).setCapacity(motorhome.getCapacity());
                    reservations.get(i).getMotorhomes().get(j).setPrice(motorhome.getBasePrice());
                    reservations.get(i).getMotorhomes().get(j).setKmDriven(motorhome.getKmDriven());
                }
            }
        }


        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query  = "UPDATE motorhomes SET brand = ? , model = ?, capacity = ?, price = ?, km_driven = ? WHERE id = ? ;";
            stmt = con.prepareStatement(query);


            stmt.setString(1, motorhome.getBrand());
            stmt.setString(2, motorhome.getModel());
            stmt.setInt(3, motorhome.getCapacity());
            stmt.setDouble(4, motorhome.getBasePrice());
            stmt.setInt(5, motorhome.getKmDriven());

            stmt.setInt(6, motorhome.getId());

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void deleteMotorhome(int motorhomeId){

        String sql = "DELETE FROM `motorhomes` WHERE `motorhomes`.`id` = ?;";

        PreparedStatement ps = null;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, motorhomeId);

            ps.executeUpdate();
        } catch(SQLIntegrityConstraintViolationException e) {
            // Hieu: Error message for integrity constraint violation
            alert("Error", "Cannot delete a motorhome while it is used in rentals/reservation or in maintenance.");
            return;
        }catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
        }
        for (int i = 0; i < motorhomes.size(); i++){
            if (motorhomes.get(i).getId() == motorhomeId){
                motorhomes.remove(motorhomes.get(i));
            }
        }

        for (int i = 0; i < maintenances.size(); i++){
            if (maintenances.get(i).getId() == motorhomeId){
                maintenances.remove(maintenances.get(i));
            }
        }
    }

    public void updateCustomer(Customer customer){

        for (int i = 0; i < customers.size(); i++){
            if (customer.getId() == customers.get(i).getId()){
                customers.get(i).setName(customer.getName());
                customers.get(i).setCpr(customer.getCpr());
                customers.get(i).setAddress(customer.getAddress());
                customers.get(i).setEmail(customer.getEmail());
                customers.get(i).setPhoneNumber(customer.getPhoneNumber());
            }
        }

        for (int i = 0; i < rentals.size(); i++){
            if (customer.getId() == rentals.get(i).getCustomer().getId()){
                rentals.get(i).getCustomer().setName(customer.getName());
                rentals.get(i).getCustomer().setCpr(customer.getCpr());
                rentals.get(i).getCustomer().setAddress(customer.getAddress());
                rentals.get(i).getCustomer().setEmail(customer.getEmail());
                rentals.get(i).getCustomer().setPhoneNumber(customer.getPhoneNumber());
            }
        }

        for (int i = 0; i < reservations.size(); i++){
            if (customer.getId() == rentals.get(i).getCustomer().getId()){
                reservations.get(i).getCustomer().setName(customer.getName());
                reservations.get(i).getCustomer().setCpr(customer.getCpr());
                reservations.get(i).getCustomer().setAddress(customer.getAddress());
                reservations.get(i).getCustomer().setEmail(customer.getEmail());
                reservations.get(i).getCustomer().setPhoneNumber(customer.getPhoneNumber());
            }
        }
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query  = "UPDATE customers SET name = ? , cpr = ?, address = ?, email = ?, phone = ? WHERE id = ? ;";
            stmt = con.prepareStatement(query);
            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getCpr());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getPhoneNumber());
            stmt.setInt(6, customer.getId());
            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void addCustomer (Customer customer) throws SQLException{
        Connection con = getConn();
        PreparedStatement stmt = null;
        ResultSet rs = null;
        try {

            String query = "INSERT INTO customers (id , name, cpr, address, email, phone ) VALUES (NULL ,?,?,?,?,?);";
            stmt = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            stmt.setString(1, customer.getName());
            stmt.setString(2, customer.getCpr());
            stmt.setString(3, customer.getAddress());
            stmt.setString(4, customer.getEmail());
            stmt.setString(5, customer.getPhoneNumber());
            stmt.executeUpdate();

            rs = stmt.getGeneratedKeys();
            if(rs.next())
            {
                int last_inserted_id = rs.getInt(1);
                System.out.println("last id "+last_inserted_id);
                customer.setId(last_inserted_id);
                customers.add(customer);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
            try { rs.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void deleteCustomer(int customerId){

        String sql = "DELETE FROM `customers` WHERE `customers`.`id` = ?;";

        PreparedStatement ps = null;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, customerId);

            ps.executeUpdate();
        } catch(SQLIntegrityConstraintViolationException e) {
            // Hieu: Error message for integrity constraint violation
            alert("Error", "Cannot delete a customer while they are renting/reserving.");
            return;
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
        }
        for (int i = 0; i < customers.size(); i++){
            if (customers.get(i).getId() == customerId){
                customers.remove(customers.get(i));
            }
        }
    }

    public void addLocations(Location location) throws SQLException{
        locations.add(location);
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query = "INSERT INTO locations (id, address, km, private ) VALUES (NULL ,?,?,?);";
            stmt = con.prepareStatement(query);


            stmt.setString(1, location.getAddress());
            stmt.setInt(2, location.getKm());
            stmt.setBoolean(3, location.isPrivateAddress());

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Error!");
            alert.setHeaderText(("You have typed sometthing wrong! \nUse text for \"Address\" and digits for \"km\"!"));
            alert.showAndWait();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void editLocation(Location location) throws SQLException {
        locations.add(location);
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query  = "UPDATE locations SET address = ? , km = ? WHERE id = "+ location.getId() + " ;";
            stmt = con.prepareStatement(query);


            stmt.setString(1, location.getAddress());
            stmt.setInt(2, location.getKm());

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }

    }

    public void deleteLocation(Location location) throws SQLException {
        //locations.add(location);
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query  = "DELETE FROM locations WHERE locations.id = ? ;";
            stmt = con.prepareStatement(query);

            stmt.setInt(1, location.getId());

            stmt.executeUpdate();

        } catch(SQLIntegrityConstraintViolationException e) {
            // Hieu: Error message for integrity constraint violation
            alert("Error", "Cannot delete a location while it is used in rentals/reservation.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }

    }

    public void addExtras(Extra extra) throws SQLException{
        extras.add(extra);
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query = "INSERT INTO extras (id, name, quantity, price) VALUES (NULL ,?,?,?);";
            stmt = con.prepareStatement(query);


            stmt.setString(1, extra.getName());
            stmt.setInt(2, extra.getQuantity());
            stmt.setDouble(3, extra.getPrice());

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();

        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void editExtra(Extra extra) throws SQLException {
        extras.add(extra);
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {
            String query  = "UPDATE extras SET name = ? , quantity = ?, price = ? WHERE id = ? ;";
            stmt = con.prepareStatement(query);


            stmt.setString(1, extra.getName());
            stmt.setInt(2, extra.getQuantity());
            stmt.setDouble(3, extra.getPrice());
            stmt.setInt(4,extra.getId());

            stmt.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void deleteExtra(Extra extra) throws SQLException {
        Connection con = getConn();
        PreparedStatement stmt = null;
        try {

            String query  = "DELETE FROM extras WHERE extras.id = ? ;";
            stmt = con.prepareStatement(query);

            stmt.setInt(1, extra.getId());

            stmt.executeUpdate();

        } catch(SQLIntegrityConstraintViolationException e) {
            // Hieu: Error message for integrity constraint violation
            alert("Error", "Cannot delete an extra while it is used in rentals/reservation.");
        } catch (Exception ex) {
            ex.printStackTrace();
        }finally {
            try { stmt.close(); } catch (Exception e) { /* ignored */ }
            try { con.close(); } catch (Exception e) { /* ignored */ }
        }

    }

    public void updateMaintenance(double cost, String text,int carId){
        String sql = "UPDATE `maintenances` SET `repair_cost` = ?, `message` = ? WHERE `maintenances`.`car_id` = ?;";

        PreparedStatement ps = null;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setDouble(1, cost);
            ps.setString(2, text);
            ps.setInt(3, carId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void insertMaintenance(Double cost, String text,int carId){
        String sql = "INSERT INTO `maintenances` (`id`, `car_id`, `repair_cost`, `message`) VALUES (NULL, ?, ?, ?);";

        PreparedStatement ps = null;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, carId);
            ps.setDouble(2, cost);
            ps.setString(3, text);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void updateMotorhomeMaintenance(int cleaned, int broken,int carId){
        String sql = "UPDATE `motorhomes` SET `cleaned` = ?, `broken` = ? WHERE `motorhomes`.`id` = ?;";

        PreparedStatement ps = null;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, cleaned);
            ps.setInt(2, broken);
            ps.setInt(3, carId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void deleteMaintenance(int carId){
        String sql = "DELETE FROM `maintenances` WHERE `maintenances`.`car_id` = ?;";

        PreparedStatement ps = null;
        try {
            ps = getConn().prepareStatement(sql);
            ps.setInt(1, carId);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try { ps.close(); } catch (Exception e) { /* ignored */ }
        }
    }

    public void alert(String title, String text){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText((text));
        alert.showAndWait();
    }
}
