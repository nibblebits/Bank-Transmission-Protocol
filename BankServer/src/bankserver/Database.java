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
import BTP.exceptions.BTPDataException;
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
                    rs.getDouble("balance"),
                    rs.getBoolean("overdraft_enabled"),
                    rs.getBoolean("interest_rate_enabled"),
                    rs.getFloat("percentage_rate"));
        }

        rs.close();
        return bank_account;
    }

    public DBAccount[] getAllBankAccounts() throws SQLException {
        ArrayList<DBAccount> accounts = new ArrayList<DBAccount>();
        String sql = "SELECT * FROM `bank_accounts` "
                + "INNER JOIN `bank_account_types` ON `bank_account_types`.`type_id` = `bank_accounts`.`account_type` ";
        stmt = db_con.prepareStatement(sql);
        rs = stmt.executeQuery();
        while (rs.next()) {
            DBAccount bank_account = new DBAccount(
                    rs.getInt("customer_id"),
                    rs.getInt("bank_account_id"),
                    this.server.getSystem().getOurBank().getSortcode(),
                    new BTPAccountType(rs.getInt("account_type"), rs.getString("name")),
                    null,
                    rs.getDouble("balance"),
                    rs.getBoolean("overdraft_enabled"),
                    rs.getBoolean("interest_rate_enabled"),
                    rs.getFloat("percentage_rate"));
            accounts.add(bank_account);
        }

        rs.close();

        return accounts.toArray(new DBAccount[accounts.size()]);
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
                    rs.getDouble("balance"),
                    rs.getBoolean("overdraft_enabled"),
                    rs.getBoolean("interest_rate_enabled"),
                    rs.getFloat("percentage_rate"));
            accounts.add(bank_account);
        }

        rs.close();

        return accounts.toArray(new DBAccount[accounts.size()]);
    }

    public BTPTransaction[] getTransactions(BTPAccount account, Date date_from, Date date_to) throws SQLException {
        ArrayList<BTPTransaction> transactions = new ArrayList<BTPTransaction>();
        String sql = "SELECT * FROM `transactions` WHERE (`sender_account_no` = ? OR (`receiver_account_no` = ? AND `receiver_sort_code` = ?)) AND `transaction_date` >= ? AND `transaction_date` <= ?";
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
                    new BTPAccount(rs.getInt("sender_account_no"), rs.getString("sender_account_sort_code"), null, null),
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
                + "(`transaction_date`, `receiver_account_no`, `receiver_sort_code`, `sender_account_no`, `sender_account_sort_code`, `amount`) "
                + "VALUES(?, ?, ?, ?, ?, ?);";
        stmt = db_con.prepareStatement(sql);
        stmt.setLong(1, transaction.getDate().getTime());
        stmt.setInt(2, transaction.getReceiverAccount().getAccountNumber());
        stmt.setString(3, transaction.getReceiverAccount().getSortCode());
        stmt.setInt(4, transaction.getSenderAccount().getAccountNumber());
        stmt.setString(5, transaction.getSenderAccount().getSortCode());
        stmt.setDouble(6, transaction.getAmountTransferred());
        stmt.execute();
    }

    public DBEmployee getEmployee(int employee_id) throws SQLException {
        DBEmployee employee = null;
        String sql = "SELECT * FROM `employees`\n"
                + "INNER JOIN (person) ON `employees`.`person_id` = `person`.`person_id`\n"
                + "INNER JOIN (titles) ON `person`.`title` = `titles`.`title_id`\n"
                + "WHERE `employee_id` = ?\n";
        stmt = db_con.prepareStatement(sql);
        stmt.setInt(1, employee_id);
        rs = stmt.executeQuery();
        if (rs.next()) {
            employee = new DBEmployee(
                    rs.getInt("person_id"),
                    rs.getString("title"),
                    rs.getString("firstname"),
                    rs.getString("middlename"),
                    rs.getString("surname"),
                    null,
                    rs.getString("password"));
        }

        rs.close();
        return employee;
    }

    public int getPersonTitleIdByString(String title) throws SQLException {
        int title_id = -1;
        String sql = "SELECT `title_id` FROM `titles` WHERE `title` LIKE(?)";
        stmt = db_con.prepareStatement(sql);
        stmt.setString(1, title);
        rs = stmt.executeQuery();
        if (rs.next()) {
            title_id = rs.getInt("title_id");
        }

        rs.close();
        return title_id;
    }

    public int newCustomer(BTPCustomer customer, String password) throws SQLException, BTPDataException {
        int person_title = getPersonTitleIdByString(customer.getTitle());
        int person_id = -1;
        int customer_id = -1;
        if (person_title == -1) {
            throw new BTP.exceptions.BTPDataException("An invalid person title was provided");
        }
        String sql = "INSERT INTO `person` (title, firstname, middlename, surname) VALUES(?, ?, ?, ?)";
        stmt = db_con.prepareStatement(sql);
        stmt.setInt(1, person_title);
        stmt.setString(2, customer.getFirstname());
        stmt.setString(3, customer.getMiddlename());
        stmt.setString(4, customer.getSurname());
        stmt.execute();
        rs = stmt.getGeneratedKeys();
        person_id = rs.getInt(1);
        rs.close();

        // Now we have created a person for this customer lets create the customer record
        sql = "INSERT INTO `customers` (person_id, password) VALUES(?, ?)";
        stmt = db_con.prepareStatement(sql);
        stmt.setInt(1, person_id);
        stmt.setString(2, password);
        stmt.execute();
        rs = stmt.getGeneratedKeys();
        customer_id = rs.getInt(1);
        rs.close();

        return customer_id;
    }

    public BTPAccountType[] getBankAccountTypes() throws SQLException {
        ArrayList<BTPAccountType> account_types = new ArrayList<BTPAccountType>();
        String sql = "SELECT * FROM `bank_account_types`";
        stmt = db_con.prepareStatement(sql);
        rs = stmt.executeQuery();

        while (rs.next()) {
            BTPAccountType account_type = new BTPAccountType(rs.getInt("type_id"), rs.getString("name"));
            account_types.add(account_type);
        }

        rs.close();

        return account_types.toArray(new BTPAccountType[account_types.size()]);
    }

    public int newBankAccount(int customer_id, BTPAccount account) throws SQLException {
        int bank_account_id = -1;
        String sql = "INSERT INTO `bank_accounts` (`balance`, `customer_id`, `account_type`) VALUES(?, ?, ?)";
        stmt = db_con.prepareStatement(sql);
        stmt.setDouble(1, 0.00);
        stmt.setInt(2, customer_id);
        stmt.setInt(3, account.getAccountType().getId());
        stmt.execute();
        rs = stmt.getGeneratedKeys();
        bank_account_id = rs.getInt(1);
        rs.close();
        return bank_account_id;
    }
}
