/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.Iterator;
import org.arc.sys.dto.DeliveryBranch;
import org.arc.sys.dto.DeliveryProducts;
import org.arc.sys.hibernate.entities.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class DeliveryHelper implements DeliveryDAO {

    @Override
    public boolean saveDelivery(DeliveryBranch db){
        
        boolean saveValid = false;
        Transaction tx = null;

        Iterator<DeliveryProducts> iterator = db.getDeliveryProduct().iterator();

        Branch branch = BranchHelper.getBranchByIdStatic(db.getBranchId());
        Product product = null;
        Stock stock = null;
        Delivery delivery = null;
        RecordProductBranch rpb = null;
        
        while (iterator.hasNext()) {
            Session session = HibernateUtil.getSessionFactory().openSession();
            DeliveryProducts deliveryProducts = iterator.next();
            product = ProductHelper.getProductByIdStatic(deliveryProducts.getProductId());
            if (StockHelper.isCreateByProductAndBranch(deliveryProducts.getProductId(), db.getBranchId())) {
                stock = StockHelper.getStockByProductIdAndBranchId(deliveryProducts.getProductId(), db.getBranchId(), session);
                System.out.println("entro if stock!!" + stock.getStockId().toString());
            } else {
                stock = StockHelper.createStockStatic(product, branch, session);
                System.out.println("entro else stock!!" + stock.getStockId().toString());
            }
            delivery = new Delivery(branch, product, deliveryProducts.getAmount(), new Date(), false);
            session.close();
            try {
                session = HibernateUtil.getSessionFactory().openSession();
                
                tx = session.getTransaction();
                tx.begin();
                
                session.save(delivery);
                session.flush();
                rpb = new RecordProductBranch();
                rpb.setActive("active");
                rpb.setDel(false);
                rpb.setDeliveryId(delivery.getDeliId());
                rpb.setFecha(new Date());
                rpb.setStock(stock);

                tx.commit();
                session.close();
                
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                
                // Start rpb max
                Query q = session.createQuery("select MAX(rpb) from RecordProductBranch as rpb where rpb.stock.product.productId=:productId and rpb.stock.branch.branchId=:branchId and rpb.del=0");
                q.setParameter("productId", product.getProductId().intValue());
                q.setParameter("branchId", branch.getBranchId().intValue());
                RecordProductBranch prevRpb = (RecordProductBranch) q.uniqueResult();

                tx.commit();
                session.close();

                // end
                if (prevRpb != null) {
                    rpb.setAmount(delivery.getAmount());
                    rpb.setTotal(delivery.getAmount() + prevRpb.getTotal());
                } else {
                    rpb.setAmount(0);
                    rpb.setTotal(delivery.getAmount());
                }

                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                
                session.save(rpb);
                // inicio RP MAX
                Query q11 = session.createQuery("select MAX(rp) from RecordProducto as rp where rp.del=0 and rp.product.productId= :productId ");
                q11.setParameter("productId", product.getProductId());
                RecordProducto rp = (RecordProducto) q11.uniqueResult();
                // end

                tx.commit();
                session.close();
                
                //System.out.println("oooooooooooooooooooooooo::: prodId: "+product.getProductId()+"; maxidrp: "+getRpMax(product.getProductId()));
                RecordProducto newRp = new RecordProducto();
                newRp.setActive("active");
                newRp.setAmount(delivery.getAmount());
                newRp.setDate(new Date());
                newRp.setDel(false);
                newRp.setDelivery(delivery);
                newRp.setProduct(product);
                newRp.setTotal(rp.getTotal() - delivery.getAmount());
                System.out.println("PPPPPPPPPPPPPPPPPPPPPPPPPP::: total: "+rp.getTotal() +" amount: "+ delivery.getAmount());

                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                
                session.saveOrUpdate(newRp);
                session.flush();
                
                session.clear();
                tx.commit();
                saveValid = true;
            } catch (Exception e) {
                saveValid = false;
                e.printStackTrace();
                tx.rollback();
                break;
            } finally { 
                session.close();
            }
        }
        //session.flush();
        return false;
    }

    public RecordProducto getRpMaxByProductId(int productId) {
        RecordProducto rp = null;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select MAX(rp.rpId) from RecordProducto as rp where rp.del=0 and rp.product.productId= :productId");
            q.setParameter("productId", productId);

            Integer rpIdMAx = (Integer) q.uniqueResult();
            session.flush();
            System.out.println("max::::::::::::::::::::::: rpIdMax :: " + rpIdMAx);

            Query q1 = session.createQuery("select rp from RecordProducto as rp where rp.rpId=" + rpIdMAx);

            rp = (RecordProducto) q1.uniqueResult();
            session.flush();

            if (rp != null) {
                System.out.println("max111111111111111::::::::::::: rpId:: " + rp.getRpId());
                System.out.println("max::::::::::::: rpTotal:: " + rp.getTotal());
                System.out.println("max::::::::::::: rpActive:: " + rp.getActive());
                System.out.println("max::::::::::::: rpAmount:: " + rp.getAmount());
            } else {
                System.out.println("max::::::::::::: NULOOOOOOOOOOOOOOOOOO");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rp;
    }
    
    private int getRpMax(int proId){
        Connection conn = null;
        Statement stmt = null;
        int id = 0;
        try{
            conn = Conn.getconn();
            stmt = conn.createStatement();

            String sql = "select MAX(rp.RP_ID) as max "
                    + "from RECORD_PRODUCTO rp "
                    + "inner join PRODUCT p on rp.PRODUCT_ID=p.PRODUCT_ID "
                    + "where rp.del=0 and rp.product_id="+proId;
            ResultSet rs = stmt.executeQuery(sql);
            //STEP 5: Extract data from result set
            while(rs.next()){
               //Retrieve by column name
               id  = rs.getInt("max");
            }
            rs.close();
        }catch(SQLException esql){
            esql.printStackTrace();
        }
        return id;
    }
}
