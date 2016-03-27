/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package bankserver;

import BTP.BTPKeyContainer;
import BTP.BTPEmployee;

/**
 *
 * @author Daniel
 */
public class DBEmployee extends BTPEmployee {

    private String password;
    public DBEmployee(int employee_id, String title, String firstname, String middlename, String surname, BTPKeyContainer extra, String password) {
        super(employee_id, title, firstname, middlename, surname, extra);
        this.password = password;
    }
   
    public String getPassword() {
        return this.password;
    }
}
