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
public class SetBankAccountOverdraftLimitEvent {
    private final BTPAccount account;
    private final double limit;
    
    public SetBankAccountOverdraftLimitEvent(BTPAccount account, double limit) {
        this.account = account;
        this.limit = limit;
    }
    
    public BTPAccount getAccountToSetOverdraftLimitTo() {
        return this.account;
    }
    
    public double getAmountToSetOverdraftLimitTo() {
        return this.limit;
    }
}
