/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.BuyHelper;
import org.arc.sys.dto.buy.BuyProducts;

/**
 *
 * @author angel
 */
@Path("buy")
public class BuyRest {
    
    @Context
    private UriInfo context;
    private BuyHelper buyHelper;

    public BuyRest() {
        this.buyHelper = new BuyHelper();
    }
    
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createBranch(BuyProducts buyProducts) {
        return ""+buyHelper.createBuy(buyProducts);
    }
    
    @GET
    @Path("getListByCreditId")
    @Produces(MediaType.APPLICATION_JSON)
    public BuyProducts getListByCreditId(@QueryParam(value = "creditId") int creditId) {
        return buyHelper.getListBuyProductByCreditId(creditId);
    }

}
