/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import java.util.Map;
import org.arc.sys.dto.EmployeeDetailtDto;
import org.arc.sys.dto.EmployeeDto;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Person;

/**
 *
 * @author angel
 */
public interface PersonDAO {
    
    public List<Person> getListPersonByName(Criteria type, String criteria);
    
    public EmployeeDto getEmployeeByPerId(int perId);
    
    public Person getPersonByUsernamePass(String username, String password);
    
    public void updatePerson(Person person);
    
    public Person updateP(Person person);
    
    public void deletePerson(int perId);
    
    public boolean createPerson(Person person);
    
    public boolean ciValid(int newCi);
    
    public String firstNameByCi(int ci);
    
    public Person getPersonByCi(int ci);
    
    public List<EmployeeDetailtDto> getListPersonDetallByName(int type, String criteria);
    
    public Map<String, String> getAllBranch();
    
    public List<KeyValue> getAllBranchList();
    
    public Map<String, String> getAllEmployeePosition();
    
    public List<KeyValue> getBranchFree(int branchId);
    
    public Person getPerIdByCi(int ci);
    
}
