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
import org.arc.sys.business.ReserveHelper;
import org.arc.sys.dto.buy.RecordProductDto;

/**
 *
 * @author angel
 */
@Path("reserve")
public class ReserveRest {
    @Context
    private UriInfo context;
    private ReserveHelper reserveHelper;

    public ReserveRest() {
        this.reserveHelper = new ReserveHelper();
    }
    
    
    @GET
    @Path("getListProductReserve")
    @Produces(MediaType.APPLICATION_JSON)
    public List<RecordProductDto> createCategory(@QueryParam(value="branchId") int branchId) {
        return reserveHelper.getListProductReserve(branchId);
    }

}
