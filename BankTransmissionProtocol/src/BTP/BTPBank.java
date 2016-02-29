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
class BTPBank {
    private String bank_sortcode;
    private String bank_authcode;
    private String bank_address;
    private int bank_port;
    public BTPBank(String bank_sortcode, String bank_authcode, String bank_address) {
        
    }
    
    public String getSortcode() {
        return this.bank_sortcode;
    }
    
    public String getAuthCode() {
        return this.bank_authcode;
    }
    
    public String getAddress() {
        return this.bank_address;
    }
    
    public int getPort() {
        return this.bank_port;
    }
}
