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
public class CategoryNotFoundExeception extends Exception {

    public CategoryNotFoundExeception() {
    }

    public CategoryNotFoundExeception(String msg) {
        super(msg);
    }

}
