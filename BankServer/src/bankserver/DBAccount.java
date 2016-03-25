/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPAccount;
import BTP.BTPAccountType;
import BTP.BTPKeyContainer;
import BTP.exceptions.BTPPermissionDeniedException;

/**
 *
 * @author Daniel
 */
class DBAccount extends BTPAccount {

    private double balance;
    private boolean overdraft_enabled;
    private boolean interest_rate_enabled;
    private float percentage_change;

    public DBAccount(int customer_id, int account_no, String sort_code, BTPAccountType account_type, BTPKeyContainer extra,
            double balance, boolean overdraft_enabled, boolean interest_rate_enabled, float percentage_change) {
        super(customer_id, account_no, sort_code, account_type, extra);
        this.balance = balance;
        this.overdraft_enabled = overdraft_enabled;
        this.interest_rate_enabled = interest_rate_enabled;
        this.percentage_change = percentage_change;
    }

    public double getBalance() {
        return this.balance;
    }

    public void withdraw(double amount) throws BTPPermissionDeniedException {
        if (this.balance - amount > 0 || this.isOverdraftEnabled()) {
            this.balance -= amount;
        } else {
            throw new BTP.exceptions.BTPPermissionDeniedException("This account is not an overdraft "
                    + "account and may not go overdrawn");
        }
    }

    public void deposit(double amount) {
        this.balance += amount;
    }

    public boolean isOverdrawn() {
        return this.balance < 0;
    }

    public boolean isInBalance() {
        return this.balance >= 0;
    }

    public boolean isOverdraftEnabled() {
        return this.overdraft_enabled;
    }

    public boolean isInterestRateEnabled() {
        return this.interest_rate_enabled;
    }

    public float getBalancePercentageChange() {
        return this.percentage_change;
    }
}
