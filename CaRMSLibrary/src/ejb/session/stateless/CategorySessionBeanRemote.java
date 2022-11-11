/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ejb.session.stateless;

import entity.Category;
import java.util.List;
import javax.ejb.Remote;
import util.exception.CategoryNameExistsException;
import util.exception.CategoryNotFoundException;
import util.exception.DeleteCategoryException;
import util.exception.InputDataValidationException;
import util.exception.UnknownPersistenceException;
import util.exception.UpdateCategoryException;

/**
 *
 * @author tian
 */
@Remote
public interface CategorySessionBeanRemote {

    public Long createNewCategory(Category category) throws UnknownPersistenceException, InputDataValidationException, CategoryNameExistsException;

    public Category retrieveCategoryById(Long id) throws CategoryNotFoundException;

    public List<Category> retrieveAllCategories();

    public void updateCategory(Category category) throws CategoryNotFoundException, InputDataValidationException, UpdateCategoryException;

    public void deleteCategory(Long categoryId) throws CategoryNotFoundException, DeleteCategoryException;

}
