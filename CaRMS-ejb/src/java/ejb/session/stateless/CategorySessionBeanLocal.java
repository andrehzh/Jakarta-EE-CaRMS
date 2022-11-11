/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Local;
import util.exception.CategoryNameExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;

/**
 *
 * @author tian
 */
@Local
public interface CategorySessionBeanLocal {

    public Long createNewCategory(Category category) throws UnknownPersistenceException, InputDataValidationException, CategoryNameExistsException;

    public Category retrieveCategoryById(Long id) throws CategoryNotFoundException;

    public List<Category> retrieveAllCategories();

}
