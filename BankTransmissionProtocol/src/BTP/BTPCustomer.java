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
public class BTPCustomer extends BTPUser {

    public BTPCustomer(int customer_id, String title, String firstname, String middlename, String surname, BTPKeyContainer extra) {
        super(customer_id, title, firstname, middlename, surname, extra);
    }
    
}
