/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.hibernate.entities.Category;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class CategoryHelper implements CategoryDAO {

    @Override
    public List<Category> getListCategoryByName(String criteria) {
        List<Category> readCategoryList = null;
        StringBuffer queryString = new StringBuffer("from Category where del = 0 and ");
        try {
            queryString.append("category_name like '%");
            queryString.append(criteria);
            queryString.append("%'");

            Session session = HibernateUtil.getSessionFactory().openSession();

            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);

            readCategoryList = q.list();

            session.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return readCategoryList;
    }

    @Override
    public Category getCategoryById(int catId) {
        Category category = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select category from Category as category where category.categoryId=:catId and category.del=0");
            q.setParameter("catId", catId);

            category = (Category) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return category;
    }

    @Override
    public boolean updateCategory(Category category) {
        boolean updateValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        if (!validName(category.getCategoryName())) {
            try {
                tx = session.getTransaction();
                tx.begin();
                category = (Category) session.merge(category);
                session.saveOrUpdate(category);

                session.flush();
                tx.commit();
                updateValid = true;
            } catch (Exception e) {
                tx.rollback();
                e.printStackTrace();
            } finally {
                session.close();
            }
        }
        return updateValid;
    }

    @Override
    public boolean deleteCategory(int catId) {
        boolean deleteValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Category category = getCategoryById(catId);

            if (category != null) {
                category.setDel(Boolean.TRUE);
                session.saveOrUpdate(category);
            }

            session.flush();
            tx.commit();
            deleteValid = true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return deleteValid;
    }

    @Override
    public boolean createCategory(Category category) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        if (!validName(category.getCategoryName())) {
            try {
                tx = session.getTransaction();
                tx.begin();

                session.save(category);
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

            Query q = session.createQuery("select category from Category as category where category.categoryName like '" + newName + "' and category.del=0");

            Category category = (Category) q.uniqueResult();
            tx.commit();
            if (category != null) {
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
    public List<KeyValue> getCategoryAll() {
        List<KeyValue> mapCategory = new ArrayList<KeyValue>();
        KeyValue kv = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select category from Category as category where category.del=0 order by category.categoryName");

            KeyValue kvEmpty = new KeyValue("0", "Seleccione categoria");
            mapCategory.add(kvEmpty);
            List<Category> listCategory = (List<Category>)q.list();
            Iterator<Category> iterable = listCategory.iterator();
            while (iterable.hasNext()) {
                Category category = iterable.next();
                kv = new KeyValue();
                kv.setKey(category.getCategoryId().toString());
                kv.setValue(category.getCategoryName());
                mapCategory.add(kv);
                
            }
            //System.out.println("Tamaño de la lista de categorias:::::: "+category.size());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        
        return mapCategory;
    }
}
