/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.translaters;

import java.util.ArrayList;
import java.util.List;
import org.arc.sys.dto.employee.EmployeeSimpleDto;
import org.arc.sys.hibernate.entities.Employee;

/**
 *
 * @author angel
 */
public class EmployeeTranslater {
    
    public static List<EmployeeSimpleDto> translateEntityToDto(List<Employee> listE){
        List<EmployeeSimpleDto> listD = new ArrayList<>();
        listD.add(new EmployeeSimpleDto(0, "Sin vendedor"));
        EmployeeSimpleDto employeeD;
        for(Employee employee : listE){
            employeeD = new EmployeeSimpleDto();
            
            employeeD.setEmpId(employee.getEmpId());
            String names = "";
            if(employee.getPerson().getFirstname() != null)
                names = names+" "+employee.getPerson().getFirstname();
            if(employee.getPerson().getLastname() != null)
                names = names+" "+employee.getPerson().getLastname();
            if(employee.getPerson().getNames() != null)
                names = names+" "+employee.getPerson().getNames();
            
            employeeD.setNames(names);
            
            listD.add(employeeD);
        }
        return listD;
    }
}
