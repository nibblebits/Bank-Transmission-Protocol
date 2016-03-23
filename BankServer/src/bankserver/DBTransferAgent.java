package bankserver;

import exceptions.InsignificantFundsException;
import java.sql.SQLException;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Daniel
 */
public class DBTransferAgent {
    private Object exceptions;

    public void transfer(DBAccount from, DBAccount to, double amount) throws SQLException, InsignificantFundsException {
        if (from.getBalance() < amount) {
            throw new exceptions.InsignificantFundsException("Insignificant funds, Transfer failed");
        }
        // Deposit the money into the receiver account
        to.deposit(amount);
        // Withdraw it from the sender account
        from.withdraw(amount);
        to.save();
        from.save();

        // Create a transfer record here....
    }
}
