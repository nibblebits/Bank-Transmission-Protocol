/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.util.Date;

/**
 *
 * @author Daniel
 */
public class BTPTransaction {
    private BTPAccount account_from;
    private BTPAccount account_to;
    private double amount;
    private Date date;
    
    public BTPTransaction(BTPAccount account_from, BTPAccount account_to, double amount, Date date) {
        this.account_from = account_from;
        this.account_to = account_to;
        this.amount = amount;
        this.date = date;
    }
    
    public BTPAccount getSenderAccount() {
        return this.account_from;
    }
    
    public BTPAccount getReceiverAccount() {
        return this.account_to;
    }
    
    public double getAmountTransferred() {
        return this.amount;
    }
    
    public Date getDate() {
        return this.date;
    }
}
