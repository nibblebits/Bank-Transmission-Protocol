/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPCustomer;
import BTP.BTPKey;
import BTP.BTPKeyContainer;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class Database {

    private Connection db_con = null;
    private PreparedStatement stmt = null;
    private ResultSet rs = null;

    public void setup() {
        try {
            Class.forName("org.sqlite.JDBC");
            db_con = DriverManager.getConnection("jdbc:sqlite:database.db");
        } catch (Exception ex) {
            Logger.getLogger(BankServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public synchronized DBCustomer getCustomer(int customer_id) throws SQLException {
        DBCustomer customer = null;
        String sql = "SELECT\n"
                + "`customers`.`customer_id`,\n"
                + "`customers`.`password`,\n"
                + "`titles`.`title`,\n"
                + "`person`.`person_id`,\n"
                + "`person`.`firstname`,\n"
                + "`person`.`middlename`,\n"
                + "`person`.`surname`\n"
                + " FROM `customers`\n"
                + "\n"
                + "INNER JOIN (person) ON `customers`.`person_id` = `person`.`person_id`\n"
                + "INNER JOIN (titles) ON `person`.`title` = `titles`.`title_id`\n"
                + "WHERE `customers`.`customer_id` = ?\n";

        stmt = db_con.prepareStatement(sql);
        stmt.setInt(1, customer_id);
        rs = stmt.executeQuery();

        if (rs.next()) {
            int person_id = rs.getInt("person_id");
            String title = rs.getString("title");
            String firstname = rs.getString("firstname");
            String middlename = rs.getString("middlename");
            String surname = rs.getString("surname");
            String password = rs.getString("password");

            // Close the result set
            rs.close();
            
            BTPKeyContainer extra_data = new BTPKeyContainer();
            // Now to get the extra data
            sql = "SELECT `key`,`value` FROM `person_extra` WHERE `person_id` = ?";
            stmt = db_con.prepareStatement(sql);
            stmt.setInt(1, person_id);
            
            rs = stmt.executeQuery();
            while (rs.next()) {
                BTPKey key = new BTPKey(rs.getString("key"), rs.getString("value"));
                extra_data.addKey(key);
            }

            // Close the result set
            rs.close();

            customer = new DBCustomer(customer_id, title, firstname, middlename, surname, extra_data, password);
        }
        return customer;
    }
}
