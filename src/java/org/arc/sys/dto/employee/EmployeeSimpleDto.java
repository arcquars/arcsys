/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.employee;

/**
 *
 * @author angel
 */
public class EmployeeSimpleDto {
    
    private int empId;
    private String Names;

    public EmployeeSimpleDto() {
    }

    public EmployeeSimpleDto(int empId, String Names) {
        this.empId = empId;
        this.Names = Names;
    }

    public int getEmpId() {
        return empId;
    }

    public void setEmpId(int empId) {
        this.empId = empId;
    }

    public String getNames() {
        return Names;
    }

    public void setNames(String Names) {
        this.Names = Names;
    }
    
}
