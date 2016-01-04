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
import org.arc.sys.business.RecordProductBranchHelper;
import org.arc.sys.dto.RecordProductBranchDto;

/**
 *
 * @author angel
 */
@Path("recordProductBranch")
public class RecordProductBranchRest {
    
    @Context
    private UriInfo context;
    private RecordProductBranchHelper rpbHelper;

    public RecordProductBranchRest() {
        this.rpbHelper = new RecordProductBranchHelper();
    }
    
    
    @GET
    @Path("findByCriteriaAndBranch")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RecordProductBranchDto> findByCriteria(@QueryParam(value="branchId") int branchId, @QueryParam(value="type") int type, @QueryParam(value = "criteria") String criteria) {
        return rpbHelper.getProductInBranchByCriteria(branchId, type, criteria);
    }
    
    @GET
    @Path("validDeleteBranch")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean findByCriteria(@QueryParam(value="branchId") int branchId) {
        return rpbHelper.validDeleteBranch(branchId);
    }
}
