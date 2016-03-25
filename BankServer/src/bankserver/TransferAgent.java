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
        double account_from_old_balance = account_from.getBalance();
        double account_to_old_balance = account_to.getBalance();
        account_from.withdraw(amount);
        account_to.deposit(amount);
        this.database.updateAccount(account_from);
        this.database.updateAccount(account_to);
        this.database.newTransaction(new BTPTransaction(account_from, account_to, amount, new Date()));

        /* If the sender account is overdrawn then withdraw money from the main banks bank account 
           as the sender is now lending.*/
        DBAccount banks_account = this.database.getBankAccount(BankServer.CENTRAL_BANK_ACCOUNT_NO);
        if (account_from.isOverdrawn()) {
            double amount_for_bank_to_pay = Math.abs(account_from.getBalance());
            if (account_from_old_balance < 0) {
                amount_for_bank_to_pay -= Math.abs(account_from_old_balance);
            }
            // Ok time to remove the money from the banks bank account
            banks_account.withdraw(amount_for_bank_to_pay);
            this.database.updateAccount(banks_account);
        }

        /* Check if the receiver account was overdrawn and pay the main banks bank account back.*/
        if (account_to_old_balance < 0) {
            double amount_to_pay_bank;
            if (account_to.isInBalance()) {
                amount_to_pay_bank = Math.abs(account_to.getBalance() - amount);
            } else {
                amount_to_pay_bank = amount;
            }

            // Ok time to deposit the money they owe to the banks account.
            banks_account.deposit(amount_to_pay_bank);
            this.database.updateAccount(banks_account);
        }
    }
}
