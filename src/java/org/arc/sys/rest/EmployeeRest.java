/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.EmployeeHelper;
import org.arc.sys.crypt.AESCrypt;
import org.arc.sys.dto.EmployeeDto;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.employee.EmployeeSimpleDto;

/**
 *
 * @author angel
 */
@Path("employee")
public class EmployeeRest {

    @Context
    private UriInfo context;
    private EmployeeHelper employeeHelper;

    public EmployeeRest() {
        this.employeeHelper = new EmployeeHelper();
    }

    @POST
    @Consumes("application/json")
    public void SaveEmployee(EmployeeDto employeeDto) {
//        System.out.println("postttttttttttttttttttttttttttt:::: "+employeeDto.getNames());
        employeeHelper.createEmployee(employeeDto);
    }

    @POST
    @Path("updateEmployee")
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEmployee(EmployeeDto employeeDto) {
        return "" + employeeHelper.updateEmployeed(employeeDto);
    }

    @GET
    @Path("getListAllVendor")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EmployeeSimpleDto> getListAllVendor() {
        return employeeHelper.getAllVendors();
    }

    @GET
    @Path("existCiEmployee")
    @Produces(MediaType.APPLICATION_JSON)
    public String existCiEmployee(@QueryParam(value = "ci") int ci) {
        return !employeeHelper.ciExistEmployee(ci) + "";
//        return "wwwwwwww";
    }

    @GET
    @Path("getAllRols")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> getAllRols() {
        return employeeHelper.getAllRols();
    }

    @GET
    @Path("testEncypt")
    @Produces(MediaType.APPLICATION_JSON)
    public String testEncypt(@QueryParam(value = "cadena") String cadena) throws Exception {
        String ee = "";
        try {
            Properties p = new Properties();
            InputStream in = this.getClass()
                    .getClassLoader()
                    .getResourceAsStream("arcsys.properties");
            p.load(in);
            
            ee = AESCrypt.encrypt(cadena, p.getProperty("aes"));
        } catch (IOException e) {
            e.printStackTrace(System.err);
        }

        return ee;
    }

    @POST
    @Path("emplSaveDto")
    @Consumes("application/json")
    public String SaveEmployeeDto(org.arc.sys.dto.employee.EmployeeDto employeeDto) {
        return employeeHelper.createEmployee(employeeDto) + "";
    }
}
