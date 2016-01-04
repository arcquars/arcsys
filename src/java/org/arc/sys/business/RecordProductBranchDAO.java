/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.RecordProductBranchDto;

/**
 *
 * @author angel
 */
public interface RecordProductBranchDAO {
    
    public List<RecordProductBranchDto> getProductInBranchByCriteria(int branchId, int type, String criteria);
    
    public boolean validDeleteBranch(int branchId);
}
