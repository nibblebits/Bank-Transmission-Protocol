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
public class RemoteTransferEvent extends TransferEvent {

    public RemoteTransferEvent(BTPClient client,BTPAccount account_from, BTPAccount account_to, double amount) {
        super(client, account_from, account_to, amount);
    }
    
}
