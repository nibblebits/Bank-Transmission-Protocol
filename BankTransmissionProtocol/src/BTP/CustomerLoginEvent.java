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
public class CustomerLoginEvent {
    private final int customer_id;
    private final String password;
    public CustomerLoginEvent(int customer_id, String password) {
        this.customer_id = customer_id;
        this.password = password;
    }
    
    public int getCustomerId() {
        return this.customer_id;
    }
    
    public String getPassword() {
        return this.password;
    }
}
