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
public class SetDailyOverdrawnChargeEvent extends BTPEvent {
    private final BTPAccount account;
    private final double amount;
    
    public SetDailyOverdrawnChargeEvent(BTPClient client, BTPAccount account, double amount) {
        super(client);
        this.account = account;
        this.amount = amount;
    }
    
    public BTPAccount getAccount() {
        return this.account;
    }
    
    public double getAmountToCharge() {
        return this.amount;
    }
}
