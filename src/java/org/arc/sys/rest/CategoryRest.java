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
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Category;

/**
 *
 * @author angel
 */
@Path("category")
public class CategoryRest {
    @Context
    private UriInfo context;
    private CategoryHelper categoryHelper;

    public CategoryRest() {
        this.categoryHelper = new CategoryHelper();
    }
    
    @GET
    @Path("categoryById")
    @Produces(MediaType.APPLICATION_JSON)
    public Category categoryById(@QueryParam(value = "catId") int catId) {
        Category result = null;
        try {
            result = categoryHelper.getCategoryById(catId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    @GET
    @Path("findByCriteria")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Category> findByCriteria(@QueryParam(value = "criteria") String criteria) {
        return categoryHelper.getListCategoryByName(criteria);
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public String updateCategory(Category category) {
        return ""+categoryHelper.updateCategory(category);
    }
    
    @DELETE
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public boolean removeCategory(@PathParam("id") int id) {
        return categoryHelper.deleteCategory(id);
    }
    
    @POST
    @Path("save")
    @Consumes(MediaType.APPLICATION_JSON)
    public String createCategory(Category category) {
        return ""+categoryHelper.createCategory(category);
    }

    @GET
    @Path("validName")
    @Produces(MediaType.APPLICATION_JSON)
    public String validName(@QueryParam(value = "newName") String newName) {
        return ""+categoryHelper.validName(newName);
    }
    
    @GET
    @Path("getAllCategory")
    @Produces(MediaType.APPLICATION_JSON)
    public List<KeyValue> findAllCategory() {
        return categoryHelper.getCategoryAll();
    }
}
