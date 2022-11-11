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
public class UpdateReservationTransactionException extends Exception{

    /**
     * Creates a new instance of
     * <code>UpdateReservationTransactionException</code> without detail
     * message.
     */
    public UpdateReservationTransactionException() {
    }

    /**
     * Constructs an instance of
     * <code>UpdateReservationTransactionException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public UpdateReservationTransactionException(String msg) {
        super(msg);
    }
}
