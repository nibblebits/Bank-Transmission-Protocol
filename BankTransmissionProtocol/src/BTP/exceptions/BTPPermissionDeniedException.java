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
public class BTPPermissionDeniedException extends Exception {
    public BTPPermissionDeniedException(String message) {
        super(message);
    }
}
