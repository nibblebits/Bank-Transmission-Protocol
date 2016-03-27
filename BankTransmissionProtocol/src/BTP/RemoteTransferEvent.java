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

    private boolean is_outgoing;
    public RemoteTransferEvent(BTPClient client,BTPAccount account_from, BTPAccount account_to, double amount, boolean is_outgoing) {
        super(client, account_from, account_to, amount);
        this.is_outgoing = is_outgoing;
    }
    
    public boolean isOutgoing() {
        return this.is_outgoing;
    }
    
    public boolean isIncoming() {
        return !this.is_outgoing;
    }
}
