/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.RefundHelper;
import org.arc.sys.business.SaleHelper;
import org.arc.sys.dto.ListProductRefund;
import org.arc.sys.dto.ListProductSale;

/**
 *
 * @author angel
 */
@Path("refund")
public class RefundRest {
    
    @Context
    private UriInfo context;
    private RefundHelper refundHelper;

    public RefundRest() {
        this.refundHelper = new RefundHelper();
    }
    
    @GET
    @Path("getListByIds")
    @Produces(MediaType.APPLICATION_JSON)
    public ListProductRefund getListProductRefund(@QueryParam(value = "ids") String ids, @QueryParam(value = "branchId") int branchId){
        System.out.println("entro a ids list refund!!");
        return refundHelper.getListProductRefund(ids, branchId);
    }

    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createRefund(ListProductRefund lpr) {
        return ""+refundHelper.saveListProductRefund(lpr);
    }
}
