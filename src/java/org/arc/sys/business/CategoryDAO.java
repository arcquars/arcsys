/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import java.util.Map;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Category;

/**
 *
 * @author angel
 */
public interface CategoryDAO {
    
    public List<Category> getListCategoryByName(String criteria);
    
    public Category getCategoryById(int catId);
    
    public boolean updateCategory(Category category);
    
    public boolean deleteCategory(int catId);
    
    public boolean createCategory(Category category);
    
    public boolean validName(String newName);
    
    public List<KeyValue> getCategoryAll();
    
}
