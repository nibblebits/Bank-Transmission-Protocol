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
public class BTPEmployee extends BTPUser{

    public BTPEmployee(int employee_id, String title, String firstname, String middlename, String surname, BTPKeyContainer extra) {
        super(employee_id, title, firstname, middlename, surname, extra);
    }
   
}
