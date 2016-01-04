/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.dto.translaters;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.arc.sys.dto.client.ClientDto;
import org.arc.sys.hibernate.entities.Client;

/**
 *
 * @author angel
 */
public class ClientTranslate {

    public static Client translateDtoToEntity(ClientDto clientDto) {
        Client client = new Client();
        client.setAddress(clientDto.getAddress());
        client.setClientId(clientDto.getClientId());
        client.setEmail(clientDto.getEmail());
        client.setFax(clientDto.getFax());
        client.setLatitud(clientDto.getLatitud());
        client.setLongitud(clientDto.getLongitud());
        client.setNit(clientDto.getNit());
        client.setPhone(clientDto.getPhone());
        client.setRazonSocial(clientDto.getRazonSocial());
        client.setUrl(clientDto.getUrl());
        
        client.setAttendant(clientDto.getAttendant());
        client.setNameInvoice(clientDto.getNameInvoice());
        client.setZone(clientDto.getZone());

        return client;
    }

    public static ClientDto translateEntityToDto(Client client) {
        ClientDto clientDto = new ClientDto();
        if(client != null){
            clientDto.setAddress(client.getAddress());
            clientDto.setClientId(client.getClientId());
            clientDto.setEmail(client.getEmail());
            clientDto.setFax(client.getFax());
            clientDto.setLatitud(client.getLatitud());
            clientDto.setLongitud(client.getLongitud());
            clientDto.setNit(client.getNit());
            clientDto.setPhone(client.getPhone());
            clientDto.setRazonSocial(client.getRazonSocial());
            clientDto.setUrl(client.getUrl());
            clientDto.setAttendant(client.getAttendant());
            clientDto.setZone(client.getZone());
            clientDto.setNameInvoice(client.getNameInvoice());
        }
        return clientDto;
    }

    public static List<ClientDto> translateListEntityToDto(List<Client> listE) {
        List<ClientDto> listD = new ArrayList<>();

        ClientDto clientDto = null;
        for (Client client : listE) {
            clientDto = new ClientDto();
            clientDto.setAddress(client.getAddress());
            clientDto.setClientId(client.getClientId());
            clientDto.setEmail(client.getEmail());
            clientDto.setFax(client.getFax());

//            clientDto.setFirtsname(client.getPerson().getFirstname());
            clientDto.setLatitud(client.getLatitud());
            clientDto.setLongitud(client.getLongitud());
            clientDto.setNit(client.getNit());
//            clientDto.setPerson(new Long(client.getPerson().getPerId() + ""));
            clientDto.setPhone(client.getPhone());
            clientDto.setRazonSocial(client.getRazonSocial());
            clientDto.setUrl(client.getUrl());
            if(client.getAttendant() != null)
                clientDto.setAttendant(client.getAttendant());
            else
                clientDto.setAttendant("");
            clientDto.setZone(client.getZone());
            clientDto.setNameInvoice(client.getNameInvoice());
            listD.add(clientDto);
        }
        return listD;
    }
}
