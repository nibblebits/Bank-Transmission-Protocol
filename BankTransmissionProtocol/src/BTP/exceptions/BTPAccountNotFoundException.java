/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BTP.exceptions;

/**
 *
 * @author Daniel
 */
public class BTPAccountNotFoundException extends Exception {
    public BTPAccountNotFoundException(String message) {
        super(message);
    }
}
