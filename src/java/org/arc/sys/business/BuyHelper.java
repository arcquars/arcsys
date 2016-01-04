/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.buy.BuyProducts;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.hibernate.entities.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class BuyHelper implements BuyDAO {

    @Override
    public boolean createBuy(BuyProducts buyProducts) {
        boolean valirCreateBuy = false;
        Buy buyCreate = new Buy(ProviderHelper.getProviderById(buyProducts.getProviderId()), new Date(), buyProducts.getTotalBuy(), false);
        buyCreate.setUserId(buyProducts.getUserId());
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {

            tx.begin();
            session.save(buyCreate);
            session.flush();
            tx.commit();
            session.close();
            List<RecordProductDto> list = buyProducts.getLisRpd();
            Iterator<RecordProductDto> iterator = list.iterator();
            Detailbuy detailBuy = null;
            while (iterator.hasNext()) {
                RecordProductDto recordProductDto = iterator.next();
                Product product = ProductHelper.getProductByIdStatic(recordProductDto.getProductId());

                detailBuy = new Detailbuy();
                detailBuy.setAmount(recordProductDto.getAmount());
                detailBuy.setBuy(buyCreate);
                detailBuy.setDel(false);
                detailBuy.setPriceBuy(recordProductDto.getPrice());
                detailBuy.setProduct(product);
                detailBuy.setTotalBuy(recordProductDto.getTotal());
                detailBuy.setExist(true);

                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                session.save(detailBuy);
                session.flush();
                tx.commit();
                session.close();
                // inicio RP MAX
//                Query q11 = session.createQuery("select MAX(rp.rpId) from RecordProducto as rp where rp.del=0 and rp.product.productId= :productId");
//                q11.setParameter("productId", recordProductDto.getProductId());
//
//                Integer rpIdMAx = (Integer) q11.uniqueResult();
//                session.flush();
                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                Query q12 = session.createQuery("select MAX(rp) from RecordProducto as rp where rp.del=0 and rp.product.productId= :productId");
                q12.setParameter("productId", recordProductDto.getProductId());

                RecordProducto rpUltimo = (RecordProducto) q12.uniqueResult();
                session.flush();
                tx.commit();
                session.close();

                //
//                rpUltimo.setActive("no active");
//                System.out.println("rpULTIMO::::::::::::::::::::");
//                session.update(rpUltimo);
//                session.flush();
                System.out.println("rpULTIMO:::::::::::::::::::: TOTAL ultimo ro::: " + rpUltimo.getTotal() + " rpId:: " + rpUltimo.getRpId());
                RecordProducto rp = new RecordProducto();

                rp.setActive("active");
                rp.setAmount(recordProductDto.getAmount());
                rp.setBuy(buyCreate);
                rp.setDate(new Date());
                rp.setDel(false);
                rp.setDelivery(null);
                rp.setProduct(product);
                rp.setTotal(recordProductDto.getAmount() + rpUltimo.getTotal());

                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                session.save(rp);
                session.flush();
                tx.commit();
                session.close();
                System.out.println("DetailBuy:: " + detailBuy.getDetailBuyId().toString());
            }
            if (buyProducts.getCredit().equalsIgnoreCase("T")) {
                Credit credit = new Credit(buyCreate, buyCreate.getTotal(), false, false);
                credit.setUserId(buyProducts.getUserId());

                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                session.save(credit);
                session.flush();
                tx.commit();
                session.close();
                System.out.println("creditId:: " + credit.getCreditId().toString());
                Payment payment = new Payment(credit, buyProducts.getPayment(), new Date(), false);

                session = HibernateUtil.getSessionFactory().openSession();
                tx = session.getTransaction();
                tx.begin();
                session.save(payment);
                session.flush();
                tx.commit();
                session.close();
                System.out.println("PaymentId:: " + payment.getPaymentId().toString());
            }
            valirCreateBuy = true;
        } catch (Exception e) {
            //tx.rollback();
            e.printStackTrace();
        }
        return valirCreateBuy;
    }

    @Override
    public BuyProducts getListBuyProductByCreditId(int creditId) {
        BuyProducts buyProduct = new BuyProducts();

        Session session = HibernateUtil.getSessionFactory().openSession();
        String query = "select buy from Credit as credit inner join credit.buy as buy where credit.del=0 and credit.creditId=" + creditId;
        Query q = session.createQuery(query);

        Buy buy = (Buy) q.uniqueResult();
        buyProduct.setProviderId(buy.getProvider().getProviderId());
        buyProduct.setProviderName(buy.getProvider().getBusinessName());
        buyProduct.setTotalBuy(buy.getTotal());
        buyProduct.setUserId(buy.getUserId());
        buyProduct.setDateBuy(buy.getDateBuy().toString());
        String query1 = "select detailBuy, product from Detailbuy as detailBuy inner join detailBuy.product as product where detailBuy.buy.buyid =:buyId";
        Query q1 = session.createQuery(query1);
        q1.setParameter("buyId", buy.getBuyid());
        
        List listResult = q1.list();

        Iterator iterator = listResult.iterator();
        Detailbuy detailBuy = null;
        Product product = null;
        RecordProductDto rpDto = null;
        List<RecordProductDto> listDto = new ArrayList<RecordProductDto>();
        while (iterator.hasNext()) {
            Object[] listString = (Object[]) iterator.next();
            detailBuy = (Detailbuy) listString[0];
            product = (Product) listString[1];
            rpDto = new RecordProductDto();
            rpDto.setAmount(detailBuy.getAmount());
            rpDto.setCategoryId(product.getCategory().getCategoryId());
            rpDto.setCategoryName(product.getCategory().getCategoryName());
            rpDto.setCodOrigin(product.getCodOrigin());
            rpDto.setFactoryId(product.getFactory().getFactoryId());
            rpDto.setFactoryName(product.getFactory().getName());
            rpDto.setPrice(detailBuy.getPriceBuy());
            rpDto.setProductId(product.getProductId());
            rpDto.setProductName(product.getName());
            rpDto.setTotal(detailBuy.getTotalBuy());
            listDto.add(rpDto);
        }
        
        buyProduct.setLisRpd(listDto);

        return buyProduct;
    }

}
