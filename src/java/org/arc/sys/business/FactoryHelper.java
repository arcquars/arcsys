/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Factory;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class FactoryHelper implements FactoryDAO{

    @Override
    public List<KeyValue> getAllFactories() {
        List<KeyValue> mapFactory = new ArrayList<KeyValue>();
        KeyValue kv = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select factory from Factory as factory where factory.del=0 order by factory.name");

            KeyValue kvEmpty = new KeyValue("0", "Seleccione Industria");
            mapFactory.add(kvEmpty);
            List<Factory> listFactory = (List<Factory>)q.list();
            Iterator<Factory> iterable = listFactory.iterator();
            while (iterable.hasNext()) {
                Factory factory = iterable.next();
                kv = new KeyValue();
                kv.setKey(factory.getFactoryId().toString());
                kv.setValue(factory.getName());
                mapFactory.add(kv);
                
            }
            //System.out.println("Tamaño de la lista de categorias:::::: "+category.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return mapFactory;
    }

    @Override
    public boolean createFactory(Factory newFactory) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        if (!validName(newFactory.getName())) {
            try {
                tx = session.getTransaction();
                tx.begin();

                session.save(newFactory);
                session.flush();
                tx.commit();
                inserValid = true;
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
        return inserValid;
    }
    
    @Override
    public boolean validName(String newName) {
        boolean validName = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select factory from Factory as factory where factory.name like '" + newName + "' and factory.del=0");

            Factory factory = (Factory) q.uniqueResult();
            tx.commit();
            if (factory != null) {
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
}
