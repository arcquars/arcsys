/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.MediaType;
import org.arc.sys.business.PersonHelper;
import org.arc.sys.dto.EmployeeDetailtDto;
import org.arc.sys.dto.EmployeeDto;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Person;

/**
 * REST Web Service
 *
 * @author angel
 */
@Path("person")
public class PersonRest {

    @Context
    private UriInfo context;
    private PersonHelper personHelper;

    /**
     * Creates a new instance of UserRest
     */
    public PersonRest() {
        personHelper = new PersonHelper();
    }

    @GET
    @Path("personbyId")
    @Produces(MediaType.APPLICATION_JSON)
    public EmployeeDto personById(@QueryParam(value = "perId") int perId) {
        EmployeeDto result = null;
        try {
            result = personHelper.getEmployeeByPerId(perId);
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        return result;
    }

    @GET
    @Path("findByCriteria")
    @Produces(MediaType.APPLICATION_JSON)
    public List<EmployeeDetailtDto> findByCriteria(@QueryParam(value = "type") int type, @QueryParam(value = "criteria") String criteria) {
        return personHelper.getListPersonDetallByName(type, criteria);
    }

    @POST
    @Consumes({"application/json", "application/x-www-form-urlencoded"})
    public void postPerson(Person person) {
        personHelper.updatePerson(person);
    }

    @POST
    @Path("updateP")
    @Consumes({"application/json", "application/x-www-form-urlencoded"})
    public Person postP(Person person) {
        Person p = personHelper.updateP(person);
        System.out.println("2PersonHelper.updatePerson:::::::::::::::::::::: " + p.getPerId());
        return p;
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public void removePerson(@PathParam("id") int id) {
        System.out.println("Entro a Delete de persona !!!!!");
        personHelper.deletePerson(id);
    }

    @POST
    @Path("save")
    @Consumes({"application/json", "application/x-www-form-urlencoded"})
    public void postSavePerson(Person person) {
        personHelper.createPerson(person);
    }
    

    @GET
    @Path("validCi")
    @Produces(MediaType.APPLICATION_JSON)
    public String validCi(@QueryParam(value = "newCi") int newCi) {
        Boolean result;
        try {
            result = personHelper.ciValid(newCi);
        } catch (Exception e) {
            result = false;
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result.toString();
    }
    
    @GET
    @Path("getFirstNameByCi")
    @Produces(MediaType.APPLICATION_JSON)
    public String getFirstNameByCi(@QueryParam(value = "ci") int ci) {
        String result;
        try {
            result = personHelper.firstNameByCi(ci);
        } catch (Exception e) {
            result = "";
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result;
    }
    
    @GET
    @Path("getPersonByCi")
    @Produces(MediaType.APPLICATION_JSON)
    public Person getPersonByCi(@QueryParam(value = "ci") int ci) {
        Person result = null;
        try {
            result = personHelper.getPersonByCi(ci);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result;
    }
    
    @GET
    @Path("branch")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> getAllBranch() {
        return personHelper.getAllBranchList();
    }
    
    @GET
    @Path("branchAllList")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> getAllBranchList() {
        return personHelper.getAllBranchList();
    }

    @GET
    @Path("employeePosition")
    @Produces(MediaType.APPLICATION_JSON)
    public Map<String, String> getAllEmployeePosition() {
        return personHelper.getAllEmployeePosition();
    }

    @GET
    @Path("personbyUsername")
    @Produces(MediaType.APPLICATION_JSON)
    public Person personById(@QueryParam(value = "username") String username, @QueryParam(value = "pass") String pass) {
        Person result = null;
        try {
            result = personHelper.getPersonByUsernamePass(username, pass);
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result;
    }

    @GET
    @Path("getBranchFree")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> getBranchFree(@QueryParam(value = "branchId") int branchId) {
        List<KeyValue> result = new ArrayList<KeyValue>();
        try {
            System.out.println("branchid::::::::::::"+branchId);
            result = personHelper.getBranchFree(branchId);
        } catch (Exception e) {
            result = null;
            e.printStackTrace();
        }
        //System.out.println("count Result allUser:: "+resultList.size());
        return result;
    }
    
}
