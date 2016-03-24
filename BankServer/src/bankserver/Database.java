/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPAccount;
import BTP.BTPAccountType;
import BTP.BTPCustomer;
import BTP.BTPKey;
import BTP.BTPKeyContainer;
import BTP.BTPTransaction;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
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
    private BankServer server = null;

    public Database(BankServer server) {
        this.server = server;
    }

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

            customer = new DBCustomer(this, customer_id, title, firstname, middlename, surname, extra_data, password);
        }
        return customer;
    }

    public DBAccount getBankAccount(int account_no) throws SQLException {
        DBAccount bank_account = null;
        String sql = "SELECT * FROM `bank_accounts` "
                + "INNER JOIN `bank_account_types` ON `bank_account_types`.`type_id` = `bank_accounts`.`account_type` "
                + "WHERE `bank_account_id` = ? ";
        stmt = db_con.prepareStatement(sql);
        stmt.setInt(1, account_no);
        rs = stmt.executeQuery();
        if (rs.next()) {
            bank_account = new DBAccount(
                    rs.getInt("customer_id"),
                    rs.getInt("bank_account_id"),
                    this.server.getSystem().getOurBank().getSortcode(),
                    new BTPAccountType(rs.getInt("account_type"), rs.getString("name")),
                    null,
                    rs.getDouble("balance"));
        }

        rs.close();
        return bank_account;
    }

    public DBAccount[] getBankAccounts(int customer_id) throws SQLException {
        ArrayList<DBAccount> accounts = new ArrayList<DBAccount>();
        String sql = "SELECT * FROM `bank_accounts` "
          + "INNER JOIN `bank_account_types` ON `bank_account_types`.`type_id` = `bank_accounts`.`account_type` "
          + "WHERE `customer_id` = ? ";
        stmt = db_con.prepareStatement(sql);
        stmt.setInt(1, customer_id);
        rs = stmt.executeQuery();
        while (rs.next()) {
            DBAccount bank_account = new DBAccount(
                    rs.getInt("customer_id"),
                    rs.getInt("bank_account_id"),
                    this.server.getSystem().getOurBank().getSortcode(),
                    new BTPAccountType(rs.getInt("account_type"), rs.getString("name")),
                    null,
                    rs.getDouble("balance"));
            accounts.add(bank_account);
        }

        rs.close();

        return accounts.toArray(new DBAccount[accounts.size()]);
    }

    public BTPTransaction[] getTransactions(BTPAccount account, Date date_from, Date date_to) throws SQLException {
        ArrayList<BTPTransaction> transactions = new ArrayList<BTPTransaction>();
        String sql = "SELECT * FROM `transactions` WHERE (`sender_account` = ? OR (`receiver_account_no` = ? AND `receiver_sort_code` = ?)) AND `transaction_date` >= ? AND `transaction_date` <= ?";
        stmt = db_con.prepareStatement(sql);
        stmt.setInt(1, account.getAccountNumber());
        stmt.setInt(2, account.getAccountNumber());
        stmt.setString(3, account.getSortCode());
        stmt.setLong(4, date_from.getTime());
        stmt.setLong(5, date_to.getTime());
        rs = stmt.executeQuery();

        while (rs.next()) {
            Date date = new Date();
            date.setTime(rs.getLong("transaction_date"));
            BTPTransaction transaction = new BTPTransaction(
                    new BTPAccount(rs.getInt("sender_account"), this.server.getSystem().getOurBank().getSortcode(), null, null),
                    new BTPAccount(rs.getInt("receiver_account_no"), rs.getString("receiver_sort_code"), null, null),
                    rs.getDouble("amount"),
                    date
            );
            transactions.add(transaction);
        }

        rs.close();

        return transactions.toArray(new BTPTransaction[transactions.size()]);
    }

    public void updateAccount(DBAccount account) throws SQLException {
        String sql = "UPDATE `bank_accounts` SET `balance` = ? WHERE `bank_account_id` = ?";
        stmt = db_con.prepareStatement(sql);
        stmt.setDouble(1, account.getBalance());
        stmt.setInt(2, account.getAccountNumber());
        stmt.executeUpdate();
    }

    public void newTransaction(BTPTransaction transaction) throws SQLException {
        String sql = "INSERT INTO `transactions` "
                + "(`transaction_date`, `receiver_account_no`, `receiver_sort_code`, `sender_account`, `amount`) "
                + "VALUES(?, ?, ?, ?, ?);";
        stmt = db_con.prepareStatement(sql);
        stmt.setLong(1, transaction.getDate().getTime());
        stmt.setInt(2, transaction.getReceiverAccount().getAccountNumber());
        stmt.setString(3, transaction.getReceiverAccount().getSortCode());
        stmt.setInt(4, transaction.getSenderAccount().getAccountNumber());
        stmt.setDouble(5, transaction.getAmountTransferred());
        stmt.execute();
    }
}
