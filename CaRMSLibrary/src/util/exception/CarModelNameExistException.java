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
public class CarModelNameExistException extends Exception{

    /**
     * Creates a new instance of <code>CarModelNameExistException</code> without
     * detail message.
     */
    public CarModelNameExistException() {
    }

    /**
     * Constructs an instance of <code>CarModelNameExistException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public CarModelNameExistException(String msg) {
        super(msg);
    }
}
