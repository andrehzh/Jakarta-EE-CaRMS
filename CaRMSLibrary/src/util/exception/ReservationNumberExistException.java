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
public class ReservationNumberExistException extends Exception{

    /**
     * Creates a new instance of <code>ReservationNumberExistException</code>
     * without detail message.
     */
    public ReservationNumberExistException() {
    }

    /**
     * Constructs an instance of <code>ReservationNumberExistException</code>
     * with the specified detail message.
     *
     * @param msg the detail message.
     */
    public ReservationNumberExistException(String msg) {
        super(msg);
    }
}
