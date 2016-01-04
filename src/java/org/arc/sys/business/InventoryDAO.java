/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.ProductInventoryDto;

/**
 *
 * @author angel
 */
public interface InventoryDAO {
    
    public List<ProductInventoryDto> getListByCriteria(int type, String criteria);
    
}
