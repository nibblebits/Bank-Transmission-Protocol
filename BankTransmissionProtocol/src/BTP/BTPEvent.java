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
public abstract class BTPEvent {
    private BTPClient client;
    
    public BTPEvent() {
        
    }
    
    public void setClient(BTPClient client) {
        this.client = client;
    }
    
    public BTPClient getClient() {
        return this.client;
    }
}
