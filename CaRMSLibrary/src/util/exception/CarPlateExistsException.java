/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util.exception;

/**
 *
 * @author tian
 */
public class CarPlateExistsException extends Exception {

    public CarPlateExistsException() {
    }

    public CarPlateExistsException(String msg) {
        super(msg);
    }

}
