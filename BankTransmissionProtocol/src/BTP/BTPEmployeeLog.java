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
public class BTPEmployeeLog {
    private final int action;
    private final String log;
    
    public BTPEmployeeLog(int action, String log) {
        this.action = action;
        this.log = log;
    }
    
    public int getAction() {
        return this.action;
    }
    public String getLog() {
        return this.log;
    }
}
