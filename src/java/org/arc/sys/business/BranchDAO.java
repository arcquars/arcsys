/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Branch;
import org.arc.sys.hibernate.entities.Category;

/**
 *
 * @author angel
 */
public interface BranchDAO {
    public List<Branch> getListBranchByName(String criteria);
    
    public Branch getBranchById(int branchId);
    
    public boolean updateBranch(Branch branch);
    
    public boolean deleteBranch(int branchId);
    
    public boolean createBranch(Branch branch);
    
    public boolean validName(String newName, int branchId);
    
    public boolean validName(String newName);
    
    public List<KeyValue> getAllBranch();
    
    public List<KeyValue> getAllBranchWithMain();
}
