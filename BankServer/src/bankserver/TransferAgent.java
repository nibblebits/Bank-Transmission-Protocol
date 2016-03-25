/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPAccount;
import BTP.BTPTransaction;
import BTP.exceptions.BTPPermissionDeniedException;
import java.sql.SQLException;
import java.util.Date;

/**
 *
 * @author Daniel
 */
public class TransferAgent {

    private Database database;

    public TransferAgent(Database database) {
        this.database = database;
    }

    public void transfer(DBAccount account_from, DBAccount account_to, double amount) throws SQLException, BTPPermissionDeniedException {
        account_from.withdraw(amount);
        account_to.deposit(amount);
        this.database.updateAccount(account_from);
        this.database.updateAccount(account_to);
        this.database.newTransaction(new BTPTransaction(account_from, account_to, amount, new Date()));
    }
}
