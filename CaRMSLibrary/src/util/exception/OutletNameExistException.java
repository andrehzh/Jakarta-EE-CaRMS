/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author andre
 */
public class OutletNameExistException extends Exception{

    /**
     * Creates a new instance of <code>OutletNameExistException</code> without
     * detail message.
     */
    public OutletNameExistException() {
    }

    /**
     * Constructs an instance of <code>OutletNameExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public OutletNameExistException(String msg) {
        super(msg);
    }
}
