/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import java.util.List;
import java.util.Map;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.BranchHelper;
import org.arc.sys.business.ProviderHelper;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.ProviderPersonDto;

/**
 *
 * @author angel
 */
@Path("provider")
public class ProviderRest {
    
    @Context
    private UriInfo context;
    private ProviderHelper providerHelper;

    public ProviderRest() {
        this.providerHelper = new ProviderHelper();
    }

    @GET
    @Path("validName")
    @Produces(MediaType.APPLICATION_JSON)
    public String validName(@QueryParam(value = "newName") String newName) {
        return ""+providerHelper.validNameCompany(newName);
    }
    
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createProvider(ProviderPersonDto ppd) {
        return ""+providerHelper.createProvider(ppd);
    }

    @GET
    @Path("getAllProvider")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> findAllProvider() {
        return providerHelper.getProviderAll();
    }
}
