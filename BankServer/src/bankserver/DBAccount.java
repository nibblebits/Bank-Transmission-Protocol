/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPAccount;
import BTP.BTPKeyContainer;

/**
 *
 * @author Daniel
 */
class DBAccount extends BTPAccount {

    private double balance;
    public DBAccount(int customer_id, int account_no, String sort_code, BTPKeyContainer extra, double balance) {
        super(customer_id, account_no, sort_code, extra);
        this.balance = balance;
    }
    
    public double getBalance() {
        return this.balance;
    }
}
