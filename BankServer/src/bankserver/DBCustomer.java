/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPKeyContainer;
import BTP.BTPUser;

/**
 *
 * @author Daniel
 */
public class DBCustomer extends BTPUser {

    private final String password;
    public DBCustomer(int user_id, String title, String firstname, String middlename, String surname, BTPKeyContainer extra, String password) {
        super(user_id, title, firstname, middlename, surname, extra);
        this.password = password;
    }
    
    public String getPassword() { 
        return this.password;
    }
    
    
}
