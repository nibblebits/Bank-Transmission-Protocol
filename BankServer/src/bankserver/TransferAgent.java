/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPAccount;
import BTP.BTPSystem;
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
    private BTPSystem system;
    public TransferAgent(BankServer server) {
        this.database = server.getDatabase();
        this.system = server.getSystem();
    }

    private void handleOverdraftReceiver(DBAccount account, double old_balance, double amount) throws SQLException {
        DBAccount banks_account = this.database.getBankAccount(this.system.getOurBank().getBankAccount().getAccountNumber());
        /* Check if the receiver account was overdrawn and pay the main banks bank account back.*/
        if (old_balance < 0) {
            double amount_to_pay_bank;
            if (account.isInBalance()) {
                amount_to_pay_bank = Math.abs(account.getBalance() - amount);
            } else {
                amount_to_pay_bank = amount;
            }

            // Ok time to deposit the money they owe to the banks account.
            banks_account.deposit(amount_to_pay_bank);
            this.database.updateAccount(banks_account);
        }
    }

    private void handleOverdraftSender(DBAccount account, double old_balance) throws BTPPermissionDeniedException, SQLException {
        /* If the sender account is overdrawn then withdraw money from the main banks bank account 
         as the sender is now lending.*/
        DBAccount banks_account = this.database.getBankAccount(this.system.getOurBank().getBankAccount().getAccountNumber());
        if (account.isOverdrawn()) {
            double amount_for_bank_to_pay = Math.abs(account.getBalance());
            if (old_balance < 0) {
                amount_for_bank_to_pay -= Math.abs(old_balance);
            }
            // Ok time to remove the money from the banks bank account
            banks_account.withdraw(amount_for_bank_to_pay);
            this.database.updateAccount(banks_account);
        }
    }

    public void transfer(BTPAccount account_from, DBAccount account_to, double amount) throws SQLException {
        double account_to_old_balance = account_to.getBalance();
        account_to.deposit(amount);
        this.database.updateAccount(account_to);
        this.database.newTransaction(new BTPTransaction(account_from, account_to, amount, new Date()));
        this.handleOverdraftReceiver(account_to, amount, amount);
    }

    public void transfer(DBAccount account_from, BTPAccount account_to, double amount) throws BTPPermissionDeniedException, SQLException {
        double account_from_old_balance = account_from.getBalance();
        account_from.withdraw(amount);
        this.database.updateAccount(account_from);
        this.database.newTransaction(new BTPTransaction(account_from, account_to, amount, new Date()));
        this.handleOverdraftSender(account_from, amount);
    }

    public void transfer(DBAccount account_from, DBAccount account_to, double amount) throws SQLException, BTPPermissionDeniedException {
        double account_from_old_balance = account_from.getBalance();
        double account_to_old_balance = account_to.getBalance();
        account_from.withdraw(amount);
        account_to.deposit(amount);
        this.database.updateAccount(account_from);
        this.database.updateAccount(account_to);
        this.database.newTransaction(new BTPTransaction(account_from, account_to, amount, new Date()));
        this.handleOverdraftReceiver(account_to, account_to_old_balance, amount);
        this.handleOverdraftSender(account_from, amount);
    }
}
