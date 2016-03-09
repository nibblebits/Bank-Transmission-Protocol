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
public class GetBankAccountsOfCustomerEvent extends BTPEvent {
    private final int customerId;
    public GetBankAccountsOfCustomerEvent(BTPClient client, int customerId) {
        super(client);
        this.customerId = customerId;
    }
    
    public int getCustomerId() {
        return this.customerId;
    }
}
