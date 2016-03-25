/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

import java.io.IOException;
import java.net.Socket;

/**
 * <h1>The Administration Client of the BTP(Bank Transmission Protocol)</h1> 
 * This class is used for administration with the BTP(Bank Transmission Protocol)
 * server. It may become deprecated or removed in the future should the employee
 * client merge with the administration client.
 *
 * @version 1.0
 * @author Daniel
 */
public class BTPAdministratorClient extends BTPEmployeeClient {

    public BTPAdministratorClient(BTPSystem system, Socket socket) throws IOException {
        super(system, socket);
    }

    /* 
     Authenticates with the server as an administrator
     @param admin_id The administator id of the administrator account you wish to login with
     @param password The password to login with
     @return boolean Returns weather or not the login was successful.
     */
    @Override
    public boolean login(int admin_id, String password) {
        return true;
    }

    /* 
     Retrieves an employee from the server if the request was successful.
     @param employee_id The employee id of the employee you wish to retrieve.
     @return BTPEmployee Returns the employee that was requested.
     @see BTPEmployee
     */
    public BTPEmployee getEmployee(int employee_id) {
        return null;
    }

    /* 
     Retrieves all of the logs of an employee if the request was successful.
     @param employee_id The employee id of the employee whose employee logs you wish to receive.
     @return BTPEmployeeLog[] Returns an array of employee logs
     @see BTPEmployeeLog
     */
    public BTPEmployeeLog[] getEmployeeLogs(int employee_id) {
        return null;
    }

    /* 
     Retrieves the logs of an employee if the request was successful.
     Only logs whose index in the list on the server is between the value of the start parameter and the 
     value of the total parameter.
    
     @param employee_id The employee id of the employee whose employee logs you wish to receive.
     @param start The start index of where in the employee logs list you wish to retrieve from
     @param total The total number of employee logs from the list you wish to receive.
     @return BTPEmployeeLog[] Returns an array of employee logs
     @see BTPEmployeeLog
     */
    public BTPEmployeeLog[] getEmployeeLogs(int employee_id, int start, int total) {
        return null;
    }

    /* 
        Locks an employee account indefinitely if the request was successful.
        @param employee_id The employee id of the employee account you wish to lock.
    */
    public void lockEmployeeAccount(int employee_id) {

    }

    /* 
       <h1>Updates the employee and user details of an employee.</h1>
       The contents of the employee passed in this argument are sent to the server including the
       employee id. The server may then update the employee based on these contents sent to it.
       @param employee The employee details that are to be updated.
    */
    public void updateEmployeeAccount(BTPEmployee employee) {

    }

    /* The "employee" object's contents is sent to the server
     * the server may then create the employee which was specified in the "employee" object.
     * Note: depending on the implementation the server may ignore the id attribute and generate its own.
     * @param employee The employee which is to be created.
    */
    public void createEmployeeAccount(BTPEmployee employee) {

    }

}
