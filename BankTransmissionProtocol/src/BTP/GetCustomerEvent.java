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
public class GetCustomerEvent extends BTPEvent {
    private final int customer_id;
    public GetCustomerEvent(BTPClient client, int customer_id) {
        super(client);
        this.customer_id = customer_id;
    }
    
    public int getCustomerId() {
        return this.customer_id;
    }
}
