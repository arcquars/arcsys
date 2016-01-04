/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.dto.buy.RecordProductList;

/**
 *
 * @author angel
 */
public interface RecordProductDAO {
    
    public RecordProductList getListProduct(int type, String criteria);
    
    public List<RecordProductDto> getListProductPrint(int type, String criteria);
    
    public double getLastPrice(int codPro);
    
}
