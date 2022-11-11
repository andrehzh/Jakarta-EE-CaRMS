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
public class CardNumberExistException extends Exception{

    /**
     * Creates a new instance of <code>CardNumberExistException</code> without
     * detail message.
     */
    public CardNumberExistException() {
    }

    /**
     * Constructs an instance of <code>CardNumberExistException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public CardNumberExistException(String msg) {
        super(msg);
    }
}
