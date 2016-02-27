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
public class BalanceEnquiryEvent extends BTPEvent {
    private final BTPAccount account;
    
    public BalanceEnquiryEvent(BTPAccount account) {
        this.account = account;
    }
    
    public BTPAccount getAccount() {
        return this.account;
    }
}
