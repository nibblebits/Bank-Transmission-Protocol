/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPAccount;
import BTP.BTPKeyContainer;
import BTP.BTPUser;
import BTP.exceptions.BTPPermissionDeniedException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Daniel
 */
public class DBCustomer extends BTPUser {

    private final String password;
    private final Database db;

    public DBCustomer(Database db, int user_id, String title, String firstname, String middlename, String surname, BTPKeyContainer extra, String password) {
        super(user_id, title, firstname, middlename, surname, extra);
        this.password = password;
        this.db = db;
    }

    public String getPassword() {
        return this.password;
    }

    public DBAccount getBankAccount(int account_no) throws SQLException, BTPPermissionDeniedException {
        DBAccount account = this.db.getBankAccount(account_no);
        if (account != null) {
            if (account.getCustomerId() != this.getId()) {
                // This is not the bank account of this customer!
                throw new BTP.exceptions.BTPPermissionDeniedException("You are not the account holder of this bank account!");
            }
        }
        return account;
    }

}
