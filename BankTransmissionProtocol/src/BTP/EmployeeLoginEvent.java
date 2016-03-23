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
public class EmployeeLoginEvent extends BTPEvent {

    private int employee_id;
    private String password;
    public EmployeeLoginEvent(BTPClient client, int employee_id, String password) {
        super(client);
        this.employee_id = employee_id;
        this.password = password;
    }
    
}
