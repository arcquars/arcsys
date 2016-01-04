/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest.auth;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author angel
 */
@Path("/auth/product")
public class Product {
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String sayHello(){
        
        return "xxxx";
    }
}
