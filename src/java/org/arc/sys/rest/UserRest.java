/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Path;
import javax.ws.rs.GET;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import org.arc.sys.business.UserHelper;
import org.arc.sys.hibernate.entities.User;

/**
 * REST Web Service
 *
 * @author angel
 */
@Path("user")
public class UserRest {

    @Context
    private UriInfo context;
    private UserHelper userHelper;

    /**
     * Creates a new instance of UserRest
     */
    public UserRest() {
        userHelper = new UserHelper();
    }

    @GET
    @Path("valid")
    @Produces({MediaType.APPLICATION_JSON})
    public String validUser(@QueryParam(value = "login") String login, @QueryParam(value = "password") String password) {
        boolean validuser = userHelper.userValid(login, password);
        return "" +  validuser;
    }

    @GET
    @Path("getUserId")
    @Produces({MediaType.APPLICATION_JSON})
    public String userId(@QueryParam(value = "login") String login, @QueryParam(value = "password") String password) {
        int userId = userHelper.getUserId(login, password);
        return "" +  userId;
    }
    
    @GET
    @Path("userbyId")
    @Produces(MediaType.APPLICATION_JSON)
    public User userById(@QueryParam(value = "userId") int userId) {
        return userHelper.readUser(new Integer(userId));
    }
}
