/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.List;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.User;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class UserHelper implements UserDAO{

    public UserHelper(){
    }
    
    @Override
    public User createUser(Integer userId, Integer perId, String login, String password) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public User readUser(Integer userId) {
        User readUser = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            Query q = session.createQuery("from User where user_id="+userId.toString());
            readUser = (User)q.uniqueResult();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        finally{
            session.close();
        }
        return readUser;
    }

    @Override
    public boolean userValid(String login, String password) {
        User readUser = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            Query q = session.createQuery("from User where login='"+login+"' and password='"+password+"'");
            readUser = (User)q.uniqueResult();
        }
        catch(Exception e){
            e.printStackTrace();
            return false;
        }
        finally{
            session.close();
        }
        if(readUser == null){
            return false;
        }
        return true;
    }
    
    public int getUserId(String login, String password){
        User readUser = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try{
            Query q = session.createQuery("from User where login='"+login+"' and password='"+password+"'");
            readUser = (User)q.uniqueResult();
        }
        catch(Exception e){
            e.printStackTrace();
            return -1;
        }
        finally{
            session.close();
        }
        if(readUser == null){
            return -1;
        }
        return readUser.getUserId();
    }
}
