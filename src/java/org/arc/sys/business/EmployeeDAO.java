/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.EmployeeDto;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.employee.EmployeeSimpleDto;
import org.arc.sys.hibernate.entities.Branch;

/**
 *
 * @author angel
 */
public interface EmployeeDAO {
    
    public boolean createEmployee(EmployeeDto employee);
    
    public Branch getBranchById(int granchId);
    
    public boolean updateEmployeed(EmployeeDto employeedDto);
    
    public List<EmployeeSimpleDto> getAllVendors();
    
    public boolean ciExistEmployee(int ci);
    
    public boolean createEmployee(org.arc.sys.dto.employee.EmployeeDto employee);
    
    public List<KeyValue> getAllRols();
}
