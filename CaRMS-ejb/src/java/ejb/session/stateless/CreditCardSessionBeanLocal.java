/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.CreditCard;
import java.util.List;
import javax.ejb.Local;
import util.exception.CardNumberExistException;
import util.exception.CreditCardNotFoundException;
import util.exception.DeleteCreditCardException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCreditCardException;

/**
 *
 * @author andre
 */
@Local
public interface CreditCardSessionBeanLocal {

    public Long createNewCreditCard(CreditCard newCreditCard) throws CardNumberExistException, UnknownPersistenceException, InputDataValidationException;

    public List<CreditCard> retrieveAllCreditCards();

    public CreditCard retrieveCreditCardByCreditCardId(Long creditCardId) throws CreditCardNotFoundException;

    public void updateCreditCard(CreditCard creditCard) throws CreditCardNotFoundException, UpdateCreditCardException, InputDataValidationException;

    public void deleteCreditCard(Long creditCardId) throws CreditCardNotFoundException, DeleteCreditCardException;
    
}
