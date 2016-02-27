/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

/**
 *
 * @author Daniel
 */
public class SetSavingsAccountInterestRateEvent extends BTPEvent {
    private final BTPAccount account;
    private final double amount;
    
    public SetSavingsAccountInterestRateEvent(BTPAccount account, double amount) {
        this.account = account;
        this.amount = amount;
    }
    
    public BTPAccount getAccountToSetInterestRateFor() {
        return this.account;
    }
    
    public double getAmountToSetInterestRateTo() {
        return this.amount;
    }
}
