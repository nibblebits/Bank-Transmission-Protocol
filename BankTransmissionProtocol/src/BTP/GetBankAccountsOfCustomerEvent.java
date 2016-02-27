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
public class GetBankAccountsOfCustomerEvent {
    private final int customerId;
    public GetBankAccountsOfCustomerEvent(int customerId) {
        this.customerId = customerId;
    }
    
    public int getCustomerId() {
        return this.customerId;
    }
}
