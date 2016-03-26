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
public class CreateCustomerEvent extends BTPEvent {
    private final BTPCustomer customer;
    private final String password;
    public CreateCustomerEvent(BTPClient client, BTPCustomer customer, String password) {
        super(client);
        this.customer = customer;
        this.password = password;
    }
    
    public BTPCustomer getCustomerToCreate() {
        return this.customer;
    }
    
    public String getPassword() {
        return this.password;
    }
}
