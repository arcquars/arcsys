/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.ProviderPersonDto;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.Person;
import org.arc.sys.hibernate.entities.Provider;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class ProviderHelper implements ProviderDAO {

    @Override
    public boolean validNameCompany(String newName) {
        boolean validName = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select provider from Provider as provider where provider.businessName like '" + newName + "' and provider.del=0");

            Provider provider = (Provider) q.uniqueResult();
            tx.commit();
            if (provider != null) {
                validName = true;
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return validName;
    }

    @Override
    public boolean createProvider(ProviderPersonDto ppd) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        if(ppd.getCi() == null)
            ppd.setCi(-1);
        
        Person newPerson = new Person(
                ppd.getAgentNames(), ppd.getFirstname(), 
                ppd.getLastname(), ppd.getCi(), ppd.getAddress(), ppd.getPhoneHome(), 
                ppd.getPhoneMobil(), ppd.getEmail(), ppd.isDel());
        newPerson.setPerId(ppd.getPerId());
        Provider newProvider = new Provider(ppd.getCompanyName(), null, false);
        
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            
            session.save(newPerson);
            newProvider.setAgent(newPerson.getPerId());
            session.save(newProvider);
            //session.flush();
            //tx.commit();
            inserValid = true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return inserValid;
    }

    @Override
    public List<KeyValue> getProviderAll() {
        List<KeyValue> listKv = new ArrayList<KeyValue>();
        KeyValue kv = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select provider from Provider as provider where provider.del=0 order by provider.businessName");

            List<Provider> provider = q.list();
            Iterator<Provider> iProvider = provider.iterator();
            Provider providerAux = null;
            listKv.add(new KeyValue("-1", "Seleccione un proveedor"));
            while (iProvider.hasNext()) {                
                providerAux = iProvider.next();
               kv = new KeyValue(providerAux.getProviderId().toString(), providerAux.getBusinessName());
               listKv.add(kv);
                
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return listKv;
    }
    
    public static Provider getProviderById(int id){
        Provider provider = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select provider from Provider as provider where provider.providerId=:id and provider.del=0");
            q.setParameter("id", id);

            provider = (Provider) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return provider;
    }
}
