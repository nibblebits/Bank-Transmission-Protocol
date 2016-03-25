/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP;

/**
 * <h1>Holds a bank account type</h1>
 * This class holds a type of bank account, only the id of the type and the name
 * are stored.
 *
 * @version 1.0
 * @author Daniel
 */
public class BTPAccountType {

    private final int id;
    private final String name;

    public BTPAccountType(int id, String name) {
        this.id = id;
        this.name = name;
    }

    /* 
     This method returns the id of the bank account type.
     @return int Returns the id of this BTPAccountType
     */
    public int getId() {
        return this.id;
    }

    /* 
     This method returns the name of the bank account type
     @return String Returns the name of the BTPAccountType
     */
    public String getName() {
        return this.name;
    }
}
