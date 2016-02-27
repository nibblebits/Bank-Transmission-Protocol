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
public class CreateNewBankAccountEvent extends BTPEvent {
    private final int customer_id;
    private final BTPAccount bank_account;
    
    public CreateNewBankAccountEvent(int customer_id, BTPAccount bank_account) {
        this.customer_id = customer_id;
        this.bank_account = bank_account;
    }
    
    public int getCustomerId() {
        return this.customer_id;
    }
    
    public BTPAccount getBankAccount() {
        return this.bank_account;
    }
    
}
