/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

/**
 * <h1>The bank container</h1>
 *
 * The "BTPBank" class is a container which holds information about a particular
 * bank. This class is used to represent the bank who is using this protocol and
 * also its trusted banks that it may make transfers to.
 *
 * @version 1.0
 * @author Daniel
 */
public class BTPBank {

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

    /* 
     @return String Returns the sortcode of this bank
     */
    public String getSortcode() {
        return this.bank_sortcode;
    }

    /* 
     @return String Returns the authentication code of this bank
     */
    public String getAuthCode() {
        return this.bank_authcode;
    }

    /* 
     @return String Returns the IP address or hostname of the bank server of this bank.
     */
    public String getAddress() {
        return this.bank_address;
    }

    /* 
     @return int Returns the port of the bank server of this bank.
     */
    public int getPort() {
        return this.bank_port;
    }
}
