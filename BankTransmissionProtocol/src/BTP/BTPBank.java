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
    private final String bank_sortcode;
    private final String bank_authcode;
    private final String bank_address;
    private final int bank_port;
    
    public BTPBank(String bank_sortcode, String bank_address, int bank_port) {
        this.bank_sortcode = bank_sortcode;
        this.bank_address = bank_address;
        this.bank_port = bank_port;
        this.bank_authcode = "";
    }
    
    public BTPBank(String bank_sortcode, String bank_authcode, String bank_address, int bank_port) {
        this.bank_sortcode = bank_sortcode;
        this.bank_authcode = bank_authcode;
        this.bank_address = bank_address;
        this.bank_port = bank_port;
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
