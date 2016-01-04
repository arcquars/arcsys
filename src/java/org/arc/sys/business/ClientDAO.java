/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.client.ClientDto;
import org.arc.sys.hibernate.entities.Client;
import org.arc.sys.hibernate.entities.Person;

/**
 *
 * @author angel
 */
public interface ClientDAO {
    
    public Client getClientByCi(int ci);
    
    public void saveClient(Client c);
    
    public ClientDto getClientDtoByCi(String nit);
    
    public ClientDto getClientDtoById(String id);
    
    public void updateClient(ClientDto client);
    
    public void saveClient(ClientDto client);
    
    public Person getAgentDtoByCi(long nit);

    public List<ClientDto> getListClientByCriteria(int type, String criteria);
    
    public boolean validateNit(String nit);
    
    public boolean validateNitE(String nit, int clientId);
    
    public List<KeyValue> searchRazon(String term);
    
    public Client getClientById(int id);
    
    public void updateClientsNit();
}
