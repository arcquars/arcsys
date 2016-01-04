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
import org.arc.sys.business.CreditHelper;
import org.arc.sys.dto.CreditAmountDto;
import org.arc.sys.dto.CreditDto;
import org.arc.sys.dto.ListCreditDetailDto;
import org.arc.sys.dto.credit.ListPaymentDto;

/**
 *
 * @author angel
 */
@Path("credit")
public class CreditRest {
    @Context
    private UriInfo context;
    private CreditHelper creditHelper;

    public CreditRest() {
        this.creditHelper = new CreditHelper();
    }
   
    @GET
    @Path("findByProviderId")
    @Produces(MediaType.APPLICATION_JSON)
    public List<CreditDto> findByCriteria(@QueryParam(value = "providerId") int providerId) {
        return creditHelper.getListByProviderId(providerId);
    }
    
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createPayment(CreditAmountDto cad) {
        return ""+creditHelper.savePaymentByCreditIdAndAmount(cad);
    }
    
    @GET
    @Path("getListByCriteria")
    @Produces(MediaType.APPLICATION_JSON)
    public ListCreditDetailDto getListByCriteria(@QueryParam(value = "providerId") int providerId, @QueryParam(value = "buyCancel") int buyCancel, @QueryParam(value = "dateStart") String dateStart, @QueryParam(value = "dateEnd") String dateEnd) {
        return creditHelper.getListByCriteria(providerId, buyCancel, dateStart, dateEnd);
    }
    
    @GET
    @Path("getListPaymentDto")
    @Produces(MediaType.APPLICATION_JSON)
    public ListPaymentDto getListPaymentDto(@QueryParam(value = "creditId") int creditId) {
        return creditHelper.getListPayment(creditId);
    }
}
