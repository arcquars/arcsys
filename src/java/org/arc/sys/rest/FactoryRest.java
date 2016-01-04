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
import org.arc.sys.business.CategoryHelper;
import org.arc.sys.business.FactoryHelper;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Category;
import org.arc.sys.hibernate.entities.Factory;

/**
 *
 * @author angel
 */
@Path("factory")
public class FactoryRest {
    @Context
    private UriInfo context;
    private FactoryHelper factoryHelper;

    public FactoryRest() {
        this.factoryHelper = new FactoryHelper();
    }
    
    
    @GET
    @Path("getAllFactory")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> findAllFactory() {
        return factoryHelper.getAllFactories();
    }
    
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createFactory(Factory factory) {
        return ""+factoryHelper.createFactory(factory);
    }
    
    @GET
    @Path("validName")
    @Produces(MediaType.APPLICATION_JSON)
    public String validName(@QueryParam(value = "newName") String newName) {
        return ""+factoryHelper.validName(newName);
    }
}
