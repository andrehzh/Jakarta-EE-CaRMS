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
public class UpdateReservationException extends Exception{

    /**
     * Creates a new instance of <code>UpdateReservationException</code> without
     * detail message.
     */
    public UpdateReservationException() {
    }

    /**
     * Constructs an instance of <code>UpdateReservationException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public UpdateReservationException(String msg) {
        super(msg);
    }
}