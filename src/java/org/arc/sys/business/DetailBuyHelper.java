/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.List;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author angel
 */
public class DetailBuyHelper implements DetailBuyDAO {

    @Override
    public List<Double> getDatesBuyByProductId(int productId) {
        List<Double> dates = new ArrayList<Double>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select Max(detailbuy.priceBuy), AVG(detailbuy.priceBuy), Min(detailbuy.priceBuy) from Detailbuy as detailbuy where detailbuy.product.productId =:proId");
            q.setParameter("proId", productId);

            Object[] result = (Object[]) q.uniqueResult();

            try{
                dates.add(Double.parseDouble(result[0].toString()));
                dates.add(Double.parseDouble(result[1].toString()));
                dates.add(Double.parseDouble(result[2].toString()));
                dates.add(Math.rint(dates.get(0)*100)/100);
                dates.add(Math.rint(dates.get(1)*100)/100);
                dates.add(Math.rint(dates.get(2)*100)/100);
            }catch(NullPointerException e){
                System.out.println("nunca se hiso una compra en este producto1.");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return dates;
    }
}
