/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import org.arc.sys.hibernate.entities.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class StockHelper implements StockDAO {

    public static boolean isCreateByProductAndBranch(int productId, int branchId) {
        boolean valid = false;
        Stock stock = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select stock from Stock as stock where stock.product.productId=:productId and stock.branch.branchId=:branchId and stock.del=0");
            q.setParameter("productId", productId);
            q.setParameter("branchId", branchId);

            stock = (Stock) q.uniqueResult();
            if (stock != null) {
                valid = true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        System.out.println("is stock --->>> "+valid );
        return valid;
    }

    public static Stock createStockStatic(Product product, Branch branch, Session session) {
        Transaction tx = null;
        
        Stock newStock = new Stock(branch, product, false);
        try {
            tx = session.getTransaction();
            tx.begin();

            session.save(newStock);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return newStock;
    }
    
    public static Stock getStockByProductIdAndBranchId(int productId, int branchId, Session session) {
        Stock stock = null;
        try {
            Query q = session.createQuery("select stock from Stock as stock where stock.product.productId=:productId and stock.branch.branchId=:branchId and stock.del=0");
            q.setParameter("productId", productId);
            q.setParameter("branchId", branchId);

            stock = (Stock) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stock;
    }
}
