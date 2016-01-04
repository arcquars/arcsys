/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.arc.sys.dto.translaters;

import org.arc.sys.business.PersonDAO;
import org.arc.sys.dto.EmployeeDto;
import org.arc.sys.hibernate.entities.Employee;
import org.arc.sys.hibernate.entities.Person;
import org.arc.sys.hibernate.entities.User;

/**
 *
 * @author angel
 */
public class PersonTranslater {

    public static EmployeeDto translatePersonEmployee(User user, Employee employee){
        EmployeeDto eDto = new EmployeeDto();
        
        //Translate Person 
        eDto.setAddress(user.getPerson().getAddress());
        eDto.setCi(user.getPerson().getCi());
        eDto.setDel(user.getPerson().getDel());
        eDto.setEmail(user.getPerson().getEmail());
        eDto.setFirstname(user.getPerson().getFirstname());
        eDto.setLastname(user.getPerson().getLastname());
        eDto.setNames(user.getPerson().getNames());
        eDto.setPassword(user.getPassword());
        eDto.setPerId(user.getPerson().getPerId());
        if(user.getPerson().getPhoneAddress() != null)
            eDto.setPhoneAddress(user.getPerson().getPhoneAddress());
        if(user.getPerson().getPhoneMobil() != null)
            eDto.setPhoneMobil(user.getPerson().getPhoneMobil());
        
        //Translate Employee
        eDto.setBranchId(employee.getBranch().getBranchId());
        eDto.setEmpId(employee.getEmpId());
        eDto.setPosition(employee.getPosition());
        
        return eDto;
    }
}
