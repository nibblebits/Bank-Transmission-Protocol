/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;

/**
 *
 * @author Daniel
 */
public class BTPAdministratorClient extends BTPEmployeeClient {

    public BTPAdministratorClient(Socket socket) throws IOException {
        super(socket);
    }
    
    @Override
    public boolean login(int admin_id, String password) {
        return true;
    }
    
    public BTPEmployee getEmployee(int employee_id) {
        return null;
    }
    
    public BTPEmployeeLog[] getEmployeeLogs(int employee_id) {
        return null;
    }
    
    public BTPEmployeeLog[] getEmployeeLogs(int employee_id, int start, int total) {
        return null;
    }
    
    public void lockEmployeeAccount(int employee_id) {
        
    }
    
    public void updateEmployeeAccount(BTPEmployee employee) {
        
    }
    
    public void createEmployeeAccount(BTPEmployee employee) {
        
    }
    
}
