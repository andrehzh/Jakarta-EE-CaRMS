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
public class DeleteReservationTransactionException extends Exception{

    /**
     * Creates a new instance of
     * <code>DeleteReservationTransactionException</code> without detail
     * message.
     */
    public DeleteReservationTransactionException() {
    }

    /**
     * Constructs an instance of
     * <code>DeleteReservationTransactionException</code> with the specified
     * detail message.
     *
     * @param msg the detail message.
     */
    public DeleteReservationTransactionException(String msg) {
        super(msg);
    }
}
