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
public class EmployeeEmailExistsException extends Exception {

    public EmployeeEmailExistsException() {
    }

    public EmployeeEmailExistsException(String msg) {
        super(msg);
    }

}
