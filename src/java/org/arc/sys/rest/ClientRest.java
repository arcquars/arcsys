/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.rest;

import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import org.arc.sys.business.ClientHelper;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.client.ClientDto;
import org.arc.sys.hibernate.entities.Person;


/**
 *
 * @author angel
 */

@Path("client")
public class ClientRest {
    
    @Context
    private UriInfo context;
    private ClientHelper clientHelper;

    private Gson gson;
    /**
     * Creates a new instance of UserRest
     */
    public ClientRest() {
        clientHelper = new ClientHelper();
        gson = new Gson();
    }
    
    @GET
    @Path("getClient")
    @Produces(MediaType.APPLICATION_JSON)
    public String personById(@QueryParam(value = "nit") String nit) {
        ClientDto client = clientHelper.getClientDtoByCi(nit);
        String json = gson.toJson(client);
        System.out.println("Rest client get by ci::: "+json);
        if(client != null)
            return json;
        return null;
    }
    
    @GET
    @Path("getClientById")
    @Produces(MediaType.APPLICATION_JSON)
    public String getClientById(@QueryParam(value = "id") String id) {
        ClientDto client = clientHelper.getClientDtoById(id);
        String json = gson.toJson(client);
        System.out.println("Rest client get by id::: "+json);
        if(client != null)
            return json;
        return null;
    }
    
    @GET
    @Path("getAgentByNit")
    @Produces(MediaType.APPLICATION_JSON)
    public String agentByNit(@QueryParam(value = "nit") long nit) {
        Person person = clientHelper.getAgentDtoByCi(nit);
        if(person != null){
            Person person1 = new Person(person.getNames(), person.getFirstname(), person.getLastname(), person.getCi(), person.getAddress(), person.getPhoneAddress(), person.getPhoneMobil(), person.getEmail(), person.getDel());
//            Person person1 = new Person();
            return gson.toJson(person1);
        }
        return null;
    }
    
    @POST
    @Consumes({"application/json"})
    public void postclient(ClientDto client) {
//        System.out.println("posttttttt:: "+client.getAttendant());
        clientHelper.saveClient(client);
    }
    
    @POST
    @Path("upload")
    @Consumes({"application/json"})
    public void uploadclient(ClientDto client) {
        System.out.println("posttttttt:: "+client.getAttendant());
        clientHelper.updateClient(client);
    }
    
    @GET
    @Path("test")
    @Produces(MediaType.APPLICATION_JSON)
    public void getTest() {
        ClientDto client = new ClientDto();
        client.setAddress("Direccion 1");
        client.setClientId(Long.MIN_VALUE);
        client.setEmail("arc@gmail.com");
        client.setFax("1235464");
        client.setLatitud("latitud");
        client.setLongitud("longitud");
        long d = 456456465L;
        client.setNit(d+"");
//        client.setPerson(new Long(111));
        client.setPhone("xxx-phone");
        client.setRazonSocial("Test de proveedor");
        client.setUrl("www.arc.com");
        
//        Gson gson = new Gson();
//        System.out.println("wwwwwww:: "+gson.toJson(client));
    }
    
    @GET
    @Path("listClientDto")
    @Produces(MediaType.APPLICATION_JSON)
    public String listClientDtoByCriteria(@QueryParam(value = "type") int type, @QueryParam(value = "criteria") String criteria) {
        List<ClientDto> list = clientHelper.getListClientByCriteria(type, criteria);
        return gson.toJson(list);
    }

    @GET
    @Path("validateNit")
    @Produces(MediaType.TEXT_PLAIN)
    public String validateNit(@QueryParam(value = "nit") String nit) {
        return clientHelper.validateNit(nit)+"";
    }
    
    @GET
    @Path("autocomplete")
    @Produces(MediaType.TEXT_PLAIN)
    public String getAutocomplete(@QueryParam(value = "term") String term) {
        List<KeyValue> list = clientHelper.searchRazon(term);
        return gson.toJson(list);
    }
    
    @POST
    @Path("validateNitE")
    @Produces(MediaType.TEXT_PLAIN)
    public Response postvalidateNitE(@FormParam("nit") String nit, @FormParam("clientId") int clientId ) {
        System.out.println("posttttttt:: "+clientId);
        return Response.ok(clientHelper.validateNitE(nit, clientId)).build();
//        return Response.ok("valido").build();
    }
    
    @GET
    @Path("corregirNit")
    @Produces(MediaType.TEXT_PLAIN)
    public String corregirNit() {
        clientHelper.updateClientsNit();
        return "true";
    }
}

