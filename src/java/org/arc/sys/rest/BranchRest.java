/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.BranchHelper;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Branch;

/**
 *
 * @author angel
 */
@Path("branch")
public class BranchRest {
    
    @Context
    private UriInfo context;
    private BranchHelper branchHelper;

    public BranchRest() {
        this.branchHelper = new BranchHelper();
    }
    
    @GET
    @Path("branchById")
    @Produces(MediaType.APPLICATION_JSON)
    public Branch branchById(@QueryParam(value = "branchId") int branchId) {
        Branch result = null;
        try {
            result = branchHelper.getBranchById(branchId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("findByCriteria")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Branch> findByCriteria(@QueryParam(value = "criteria") String criteria) {
        return branchHelper.getListBranchByName(criteria);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateBranch(Branch branch) {
        return ""+branchHelper.updateBranch(branch);
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeCategory(@PathParam("id") int id) {
        return branchHelper.deleteBranch(id);
    }
    
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createBranch(Branch branch) {
        return ""+branchHelper.createBranch(branch);
    }
    
    @GET
    @Path("validName")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean validName(@QueryParam(value = "newName") String newName, @QueryParam(value = "branchId") int branchId) {
        return branchHelper.validName(newName, branchId);
        //return branchHelper.validName(newName);
    }
    
    @GET
    @Path("validNameBranch")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean validNameBranch(@QueryParam(value = "newName") String newName) {
        return branchHelper.validName(newName);
    }
    
    @GET
    @Path("getAllBranch")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> findAllBranch() {
        return branchHelper.getAllBranch();
    }
    
    @GET
    @Path("getAllBranchWithMain")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> findAllBranchWithMain() {
        return branchHelper.getAllBranchWithMain();
    }
}
