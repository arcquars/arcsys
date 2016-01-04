/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.buy.RecordProductDto;

/**
 *
 * @author angel
 */
public interface ReserveDAO {
    
    public List<RecordProductDto> getListProductReserve(int branchId);
    
}
