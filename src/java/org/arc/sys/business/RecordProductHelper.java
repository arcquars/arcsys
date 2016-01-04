/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.dto.buy.RecordProductList;
import org.arc.sys.hibernate.entities.Detailbuy;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.RecordProducto;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author angel
 */
public class RecordProductHelper implements RecordProductDAO {

    @Override
    public RecordProductList getListProduct(int type, String criteria) {
        RecordProductList rpList = new RecordProductList();
        List<RecordProductDto> readProductList = new ArrayList<RecordProductDto>();
        StringBuffer queryString = new StringBuffer("select rp.rpId, product.productId, product.name, product.codOrigin,product.category.categoryId,product.category.categoryName,product.factory.factoryId,product.factory.name,rp.amount,rp.total, product.description "
                + "from RecordProducto as rp inner join rp.product as product where product.del=0 and "
                + "rp.rpId in (select max(rp1.rpId) from RecordProducto as rp1 where rp1.product.productId=product.productId) "
                + "and ");
        try {
            switch (type) {
                case 1:
                    queryString.append("product.name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 2:
                    queryString.append(" product.codOrigin like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 3:
                    queryString.append(" product.category.categoryId = ");
                    queryString.append(criteria);
                    break;
                default:
                    queryString.append("product.name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;

            }
            
            queryString.append(" order by product.name");
            
            System.out.println("query:::: " + queryString.toString());
            
            Session session = HibernateUtil.getSessionFactory().openSession();
            session.flush();
            session.clear();
            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);

            List listResult = q.list();

            Iterator it = listResult.iterator();
            RecordProductDto recordDto = null;
            double granTotal = 0;
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                recordDto = new RecordProductDto();
                recordDto.setRpId(Integer.parseInt(listString[0].toString()));
                recordDto.setProductId(Integer.parseInt(listString[1].toString()));
                recordDto.setProductName(listString[2].toString());
                recordDto.setCodOrigin(listString[3].toString());
                recordDto.setCategoryId(Integer.parseInt(listString[4].toString()));
                recordDto.setCategoryName(listString[5].toString());
                recordDto.setFactoryId(Integer.parseInt(listString[6].toString()));
                recordDto.setFactoryName(listString[7].toString());
                recordDto.setAmount(Integer.parseInt(listString[8].toString()));
                recordDto.setTotal(Double.parseDouble(listString[9].toString()));
                recordDto.setDetail(listString[10].toString());
                
                recordDto.setCoste(getLastPrice(recordDto.getProductId()));
                
                granTotal += (recordDto.getTotal()*recordDto.getCoste());
                
                readProductList.add(recordDto);
            }
            rpList.setList(readProductList);
            rpList.setGranTotal(granTotal);
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rpList;
    }
    
    @Override
    public List<RecordProductDto> getListProductPrint(int type, String criteria) {
        System.out.println("criteria__::::::::::::::::::::" + criteria);
        List<RecordProductDto> readProductList = new ArrayList<RecordProductDto>();
        StringBuffer queryString = new StringBuffer("select rp.rpId, product.productId, product.name, product.codOrigin,product.category.categoryId,product.category.categoryName,product.factory.factoryId,product.factory.name,rp.amount,rp.total "
                + "from RecordProducto as rp inner join rp.product as product where product.del=0 and rp.active like 'active' and rp.total != 0 and ");
        try {
            switch (type) {
                case 1:
                    queryString.append(" product.name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 2:
                    queryString.append(" product.codOrigin like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 3:
                    queryString.append(" product.category.categoryId = ");
                    queryString.append(criteria);
                    break;
                default:
                    queryString.append(" product.name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;

            }
            Session session = HibernateUtil.getSessionFactory().openSession();

            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);

            List listResult = q.list();

            Iterator it = listResult.iterator();
            RecordProductDto recordDto = null;
            while (it.hasNext()) {
                Object[] listString = (Object[]) it.next();
                recordDto = new RecordProductDto();
                recordDto.setRpId(Integer.parseInt(listString[0].toString()));
                recordDto.setProductId(Integer.parseInt(listString[1].toString()));
                recordDto.setProductName(listString[2].toString());
                recordDto.setCodOrigin(listString[3].toString());
                recordDto.setCategoryId(Integer.parseInt(listString[4].toString()));
                recordDto.setCategoryName(listString[5].toString());
                recordDto.setFactoryId(Integer.parseInt(listString[6].toString()));
                recordDto.setFactoryName(listString[7].toString());
                recordDto.setAmount(Integer.parseInt(listString[8].toString()));
                recordDto.setTotal(Double.parseDouble(listString[9].toString()));

                recordDto.setCoste(getLastPrice(recordDto.getProductId()));
                System.out.println("wwwww:: "+recordDto.getCoste());
                readProductList.add(recordDto);
            }
            session.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return readProductList;
    }

    public static double getRpTotalByProductId(int productId, Session session) {
        RecordProducto rp = null;
        try {
            Query q = session.createQuery("select Max(rp) from RecordProducto as rp where rp.del=0 and rp.product.del=0 and rp.product.productId=:productId ");
            q.setParameter("productId", productId);

            rp = (RecordProducto) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rp.getTotal();
    }

    @Override
    public double getLastPrice(int codPro) {
        Detailbuy db = null;
        double price = 0;
        try {
            Session session = HibernateUtil.getSessionFactory().openSession();
            Query q = session.createQuery("select Max(db) from Detailbuy as db where db.product.productId=:productId ");
            q.setParameter("productId", codPro);

            db = (Detailbuy) q.uniqueResult();
            if(db != null)
                price = db.getPriceBuy();
            
            session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return price;
    }
    
}
