/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.DeliveryHelper;
import org.arc.sys.dto.DeliveryBranch;

/**
 *
 * @author angel
 */
@Path("delivery")
public class DeliveryRest {
    @Context
    private UriInfo context;
    private DeliveryHelper deliveryHelper;

    public DeliveryRest() {
        this.deliveryHelper = new DeliveryHelper();
    }
    
    
    @POST
    @Path("saveDelivery")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCategory(DeliveryBranch db) {
        return ""+deliveryHelper.saveDelivery(db);
    }

}
