/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.DetailBuyHelper;

/**
 *
 * @author angel
 */
@Path("detailbuy")
public class DetailBuyRest {
    
    @Context
    private UriInfo context;
    private DetailBuyHelper detailBuyHelper;

    public DetailBuyRest() {
        this.detailBuyHelper = new DetailBuyHelper();
    }
    
    @GET
    @Path("getdatesbuybyproductid")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDatesBuyByProductId(@QueryParam(value = "proId") int productId) {
        return detailBuyHelper.getDatesBuyByProductId(productId).toString();
        /*
        List<Double> list = new ArrayList<Double>();
        list.add(new Double(2));
        list.add(new Double(3));
        list.add(new Double(4));
        return list.toString();
                */
    }
    

}
