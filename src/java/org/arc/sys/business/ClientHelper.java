/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.client.ClientDto;
import org.arc.sys.dto.translaters.ClientTranslate;
import org.arc.sys.hibernate.entities.Client;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.Person;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class ClientHelper implements ClientDAO {

    @Override
    public Client getClientByCi(int ci) {
        Client client = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select client from Client as client where client.person.ci=:ci");
            q.setParameter("ci", ci);
            client = (Client) q.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return client;
    }

    @Override
    public void updateClient(ClientDto clientDto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select client from Client as client where client.clientId=:clientId");
            q.setParameter("clientId", clientDto.getClientId());
            Client client = (Client) q.uniqueResult();
            client.setNit(clientDto.getNit());
            client.setAddress(clientDto.getAddress());
            client.setAttendant(clientDto.getAttendant());
            client.setEmail(clientDto.getEmail());
            client.setFax(clientDto.getFax());
            client.setLatitud(clientDto.getLatitud());
            client.setLongitud(clientDto.getLongitud());
            client.setNameInvoice(clientDto.getNameInvoice());
            client.setPhone(clientDto.getPhone());
            client.setRazonSocial(clientDto.getRazonSocial());
            client.setUrl(clientDto.getUrl());
            client.setZone(clientDto.getZone());
//            client.setPerson(person);
            session.update(client);

            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public ClientDto getClientDtoByCi(String nit) {
        Client client = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select client from Client as client where client.nit=:nit");
            q.setParameter("nit", nit);
            client = (Client) q.uniqueResult();
            if (client != null) {
                return ClientTranslate.translateEntityToDto(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            session.close();
        }
        return null;
    }

    @Override
    public List<ClientDto> getListClientByCriteria(int type, String criteria) {
        List<Client> readClientList = null;
        List<ClientDto> listClientDto = null;
        StringBuffer queryString = new StringBuffer("Select client from Client client where ");

        try {
            switch (type) {
                case 1:
                    queryString.append(" client.razonSocial like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 2:
                    
                    queryString.append(" client.nit like '%");
                    queryString.append(criteria);
                    queryString.append("%' ");
                    break;
                default:
                    queryString.append(" client.razonSocial like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;

            }
            Session session = HibernateUtil.getSessionFactory().openSession();
//            session.beginTransaction();
            Query q = session.createQuery(queryString.toString());
            readClientList = q.list();

            listClientDto = ClientTranslate.translateListEntityToDto(readClientList);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("eeeee:: "+readClientList.size());
        return listClientDto;
    }

    @Override
    public Person getAgentDtoByCi(long nit) {
        Client client = null;
        Person p = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery("select client from Client as client where client.nit=:nit");
            q.setParameter("nit", nit);
            client = (Client) q.uniqueResult();
            if (client != null) {
                p = client.getPerson();
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
//            session.close();
        }
        return p;
    }

    @Override
    public void saveClient(Client c) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            session.saveOrUpdate(c);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    @Override
    public boolean validateNit(String nit) {
        Client client = null;
        boolean valid = true;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery("select client from Client as client where client.nit=:nit");
            q.setParameter("nit", nit);
            client = (Client) q.uniqueResult();
            if (client != null) {
                valid = false;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
//            session.close();
        }

        return valid;
    }

    @Override
    public void saveClient(ClientDto clientDto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            session.save(ClientTranslate.translateDtoToEntity(clientDto));

            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public List<KeyValue> searchRazon(String term) {
        List<Client> readClientList = null;
        List<KeyValue> listKeyValue = null;
        StringBuffer queryString = new StringBuffer("Select client from Client client where ");
        Transaction tx = null;
        try {
            queryString.append(" client.razonSocial like '%");
            queryString.append(term);
            queryString.append("%'");
            queryString.append(" OR client.nit like '%");
            queryString.append(term);
            queryString.append("%'");
            queryString.append(" ORDER BY client.razonSocial");
            Session session = HibernateUtil.getSessionFactory().openSession();
            tx = session.getTransaction();
            tx.begin();
            Query q = session.createQuery(queryString.toString());
            readClientList = q.list();

            Iterator<Client> iterator = readClientList.iterator();
            listKeyValue = new ArrayList<KeyValue>();
            Client clientAux = null;
            KeyValue kv = null;
            while(iterator.hasNext()){
                clientAux = iterator.next();
                kv = new KeyValue(clientAux.getClientId()+"", clientAux.getRazonSocial()+" - "+clientAux.getAddress()+" - "+clientAux.getNit());
                listKeyValue.add(kv);
            }
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        System.out.println("eeeee:: "+readClientList.size());
        return listKeyValue;
    }

    @Override
    public Client getClientById(int id) {
        Client client = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select client from Client as client where client.clientId=:id");
            q.setParameter("id", id);
            client = (Client) q.uniqueResult();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return client;
    }

    @Override
    public boolean validateNitE(String nit, int clientId) {
        Client client = null;
        boolean valid = true;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            session.beginTransaction();
            Query q = session.createQuery("select client from Client as client where client.nit=:nit and client.clientId not in (select clientE from Client as clientE where clientE.clientId=:clientId)");
            q.setParameter("nit", nit);
            q.setParameter("clientId", clientId);
            client = (Client) q.uniqueResult();
            if (client != null) {
                valid = false;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            session.getTransaction().rollback();
            e.printStackTrace();
        } finally {
//            session.close();
        }

        return valid;
    }

    @Override
    public void updateClientsNit() {
        Client client = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            Query q = session.createQuery("select client from Client as client where client.nit<=0");
            List<Client> clients = q.list();
            Iterator<Client> iClient = clients.iterator();
            System.out.println("Numero de clientes con nit 0: "+clients.size());
            Client cliAux = null;
            int indice = 444100;
            while (iClient.hasNext()) {
                cliAux = iClient.next();
                cliAux.setNit((++indice)+"");
                session.update(cliAux);
            }
            session.flush();
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
    }

    @Override
    public ClientDto getClientDtoById(String id) {
        Client client = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select client from Client as client where client.clientId=:id");
            q.setParameter("id", id);
            client = (Client) q.uniqueResult();
            if (client != null) {
                return ClientTranslate.translateEntityToDto(client);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
//            session.close();
        }
        return null;
    }

}
