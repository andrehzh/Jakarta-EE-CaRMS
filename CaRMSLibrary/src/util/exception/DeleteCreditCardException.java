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
public class DeleteCreditCardException extends Exception{

    /**
     * Creates a new instance of <code>DeleteCreditCardException</code> without
     * detail message.
     */
    public DeleteCreditCardException() {
    }

    /**
     * Constructs an instance of <code>DeleteCreditCardException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public DeleteCreditCardException(String msg) {
        super(msg);
    }
}
