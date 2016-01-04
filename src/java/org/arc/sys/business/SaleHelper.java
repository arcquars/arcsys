/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.arc.sys.crypt.NumberToLetterConverter;
import org.arc.sys.dto.*;
import org.arc.sys.dto.credit.CreditReportDto;
import org.arc.sys.dto.sale.ListSaleDto;
import org.arc.sys.dto.sale.SaleDto;
import org.arc.sys.dto.translaters.SalesAmountTranslate;
import org.arc.sys.hibernate.entities.*;
import org.arc.sys.util.Utils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class SaleHelper implements SaleDAO {

    @Override
    public ListProductSale getListProductSale(String ids, int branchId) {
        String[] idsArray = ids.split(",");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ListProductSale lps = new ListProductSale();
        lps.setBranchId(branchId);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        lps.setDateSale(sdf.format(d));
        List<ProductSale> listProductSale = new ArrayList<ProductSale>();
        ProductSale ps = null;
        PriceSale price = null;
        double total = 0;
        for (int i = 0; i < idsArray.length; i++) {
            String string = idsArray[i];
            try {
                tx = session.getTransaction();
                tx.begin();

                Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
                q.setParameter("proId", string);
                Product product = (Product) q.uniqueResult();

                q = session.createQuery("select Max(priceSale) from PriceSale as priceSale where priceSale.product.del=0 and priceSale.product.productId=" + string);
                Double maxPrice = 0.0;
                price = (PriceSale) q.uniqueResult();
                if (price != null) {
                    maxPrice = price.getPrice();
                }
                if (product != null) {
                    ps = new ProductSale();
                    ps.setAmount(1);
                    ps.setCategoryName(product.getCategory().getCategoryName());
                    ps.setFatoryName(product.getFactory().getName());
                    ps.setPrice(maxPrice);
                    ps.setProductCodOrigin(product.getCodOrigin());
                    ps.setProductId(product.getProductId());
                    ps.setProductName(product.getName());
                    ps.setTotal(maxPrice);
                    ps.setDetail(product.getDescription());
                    ps.setHigherCost(maxPrice.doubleValue());
                    if (branchId == 0) {
                        ps.setLimitedAmount(RecordProductHelper.getRpTotalByProductId(product.getProductId(), session));
                    } else {
                        ps.setLimitedAmount(RecordProductBranchHelper.getRpbTotalByProductIdAndBranchId(product.getProductId(), branchId, session));
                    }
                    listProductSale.add(ps);
                    total = total + maxPrice;
                }
                session.flush();
                tx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        lps.setTotal(total);
        lps.setListProductSale(listProductSale);
        return lps;
    }

    @Override
    public HashMap<String, String> saveListProductSale(ListProductSale lps) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        Sale newSale = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date d = sdf.parse(lps.getDateSale());
            Date dNow = new Date();
            if (sdf.format(dNow).compareTo(sdf.format(d)) == 0) {
                d = new Date();
            }

            newSale = new Sale(d, lps.getTotal(), false);
            newSale.setUserId(lps.getUserId());
            newSale.setBranchId(Long.valueOf(lps.getBranchId()));

            Query qClient = session.createQuery("select client from Client as client where client.clientId=:id");
            qClient.setParameter("id", lps.getClientId());
            Client client = (Client) qClient.uniqueResult();

            newSale.setClient(client);
            newSale.setVendorId(lps.getVendorId());

            CreditSale creditSale = null;
            if (lps.isCredit()) {
                tx = session.getTransaction();
                tx.begin();
                creditSale = new CreditSale();
                creditSale.setCanceled(false);
                creditSale.setTotal(lps.getTotal());
                creditSale.setDel(false);
                session.save(creditSale);
                session.flush();
                tx.commit();

                newSale.setCreditSale(creditSale);
            }
            tx = session.getTransaction();
            tx.begin();
            session.save(newSale);
            session.flush();

            Iterator<ProductSale> productSale = lps.getListProductSale().iterator();
            DetailSale detailSale = null;
            RecordProductBranch newRpb = null;
            Branch branch = BranchHelper.getBranchByIdStatic(lps.getBranchId(), session);
            tx.commit();

            while (productSale.hasNext()) {
                ProductSale productSale1 = productSale.next();

                Double aux = new Double(productSale1.getAmount());
                int totalBuy = aux.intValue();
                while (totalBuy > 0) {
                    tx = session.getTransaction();
                    tx.begin();

                    detailSale = new DetailSale();
                    detailSale.setBranch(branch);
                    detailSale.setDel(false);
                    detailSale.setPrice(productSale1.getPrice());
                    detailSale.setProduct(ProductHelper.getProductByIdStatic(productSale1.getProductId(), session));
                    detailSale.setSale(newSale);
                    detailSale.setTotal(productSale1.getTotal());

                    // get max priceBuy by ProductId
                    Query q12 = session.createQuery("select MIN(dbuy) from Detailbuy as dbuy where dbuy.exist=1 AND dbuy.product.del=0 and dbuy.product.productId= :productId");
                    q12.setParameter("productId", productSale1.getProductId());

                    Detailbuy maxbuyProduct = (Detailbuy) q12.uniqueResult();
                    if (maxbuyProduct != null) {
                        System.out.println("--------------------------");
                        System.out.println("Total:: " + totalBuy);
                        System.out.println("Detail buy id:: " + maxbuyProduct.getDetailBuyId());
                        System.out.println("Detail buy amount:: " + maxbuyProduct.getAmount());
                        System.out.println("Detail buy consumed:: " + maxbuyProduct.getConsumed());
                        System.out.println("--------------------------");
                    } else {
                        System.out.println("--------------------------");
                        System.out.println("Detail buy ELSE!!! ");
                        System.out.println("--------------------------");
                    }
                    int amountPp = maxbuyProduct.getAmount();
                    int consumedPp = maxbuyProduct.getConsumed();
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) < totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) == totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) > totalBuy) {
                        maxbuyProduct.setConsumed(maxbuyProduct.getConsumed() + totalBuy);
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        detailSale.setAmount(totalBuy);
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    totalBuy = totalBuy - (amountPp - consumedPp);
                    // end
                    detailSale.setCoste(maxbuyProduct.getPriceBuy());
                    detailSale.setGain((detailSale.getPrice() - detailSale.getCoste()) * detailSale.getAmount());
                    session.save(detailSale);
                    session.flush();

                    // Inicio get rpb max 
                    Query q = session.createQuery("select MAX(rpb.rpbId) from RecordProductBranch as rpb where rpb.stock.product.productId=:productId and rpb.stock.branch.branchId=:branchId and rpb.del=0");
                    q.setParameter("productId", productSale1.getProductId());
                    q.setParameter("branchId", lps.getBranchId());

                    Integer rpbIdMax = (Integer) q.uniqueResult();

                    Query q1 = session.createQuery("select rpb from RecordProductBranch as rpb where rpb.rpbId=:rpbIdMax and rpb.del=0");
                    q1.setParameter("rpbIdMax", rpbIdMax.toString());

                    RecordProductBranch rpbMax = (RecordProductBranch) q1.uniqueResult();
                    // end

                    rpbMax.setActive("no active");
                    session.update(rpbMax);
                    session.flush();

                    newRpb = new RecordProductBranch();
                    newRpb.setActive("active");
                    newRpb.setAmount((int) detailSale.getAmount());
                    newRpb.setDel(false);
                    newRpb.setFecha(new Date());
                    newRpb.setDetailSaleId(detailSale.getDetailSaleId());
                    newRpb.setStock(StockHelper.getStockByProductIdAndBranchId(productSale1.getProductId(), lps.getBranchId(), session));
                    newRpb.setTotal(rpbMax.getTotal() - (int) detailSale.getAmount());

                    session.save(newRpb);
                    session.flush();
                    tx.commit();
                }

            }
            inserValid = true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        HashMap<String, String> has = new HashMap<String, String>();
        has.put("salId", newSale.getSalId().toString());
        has.put("nit", newSale.getClient().getNit());
        has.put("razonSocial", newSale.getClient().getRazonSocial());
        has.put("address", newSale.getClient().getAddress());
        has.put("phone", newSale.getClient().getPhone());

        int p_ent = (int) newSale.getTotal();
        String re[] = (newSale.getTotal() + "").split("\\.");
        String decimal = "00";
        if (re.length > 0) {
            if (re[1].length() == 1) {
                decimal = re[1] + "0";
            } else {
                decimal = re[1];
            }
        }
        has.put("literal", NumberToLetterConverter.convertNumberToLetter(new Double(p_ent + "")) + " " + decimal + "/100 Bolivianos");

        has.put("zone", newSale.getClient().getZone());
        has.put("printFacturaA", newSale.getClient().getNameInvoice());

        session.close();
        return has;
//        return inserValid;
    }

    @Override
    public ListSaleDetailGain getListSDG(int branchId, String dateStart, String dateEnd) {
        ListSaleDetailGain lsdg = new ListSaleDetailGain();
        List<SaleDetailGain> lSaleDetailGain = new ArrayList<SaleDetailGain>();
        double bigGain = 0;
        double bigTotal = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String query = "select "
                    + "dsale.product.name, "
                    + "dsale.product.category.categoryName, "
                    + "dsale.product.factory.name, "
                    + "dsale.coste, "
                    + "dsale.price, "
                    + "dsale.amount, "
                    + "dsale.total, dsale.sale "
                    + "from "
                    + "DetailSale dsale inner join dsale.sale as sale "
                    + "left join sale.reserve as reserve "
                    + "where "
                    + "dsale.product.del=0 and "
                    + "(sale.reserve is null or reserve.stateReserve = 0) and "
                    + "dsale.sale.dateSale BETWEEN :dateStart  and :dateEnd and ";

            if (branchId == 0) {
                query = query + "dsale.branch is null";
            } else {
                query = query + "dsale.branch.branchId=" + branchId;
            }
            Query q = session.createQuery(query);
            q.setParameter("dateStart", dateStart + " 00:00:00");
            q.setParameter("dateEnd", dateEnd + " 23:59:59");
            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            SaleDetailGain sdg = null;
            Sale sale = null;
            DecimalFormat df = new DecimalFormat("#.##");
            double aux = 0.0;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                sdg = new SaleDetailGain();
                sdg.setProductName(object[0].toString());
                sdg.setProductCategory(object[1].toString());
                sdg.setProductFactory(object[2].toString());
                sdg.setCosteBuy(Double.parseDouble(object[3].toString()));
                sdg.setPriceSale(Double.parseDouble(object[4].toString()));
                sdg.setAmountSale(Double.parseDouble(object[5].toString()));
                sdg.setTotal(Double.parseDouble(object[6].toString()));
                sale = (Sale) object[7];

                sdg.setDateSale(sale.getDateSale().toString());
                aux = (sdg.getPriceSale() - sdg.getCosteBuy()) * sdg.getAmountSale();
                sdg.setGain(Math.round(aux * 100.0) / 100.0);
                bigGain = bigGain + sdg.getGain();
                bigTotal = bigTotal + sdg.getTotal();
                lSaleDetailGain.add(sdg);
            }

            lsdg.setList(lSaleDetailGain);
            lsdg.setTotalGain(bigGain);
            lsdg.setTotalSale(bigTotal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lsdg;
    }

    @Override
    public ListSaleDetailGain getListSDGReserve(int branchId, String dateStart, String dateEnd) {
        ListSaleDetailGain lsdg = new ListSaleDetailGain();
        List<SaleDetailGain> lSaleDetailGain = new ArrayList<SaleDetailGain>();
        double bigGain = 0;
        double bigTotal = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String query = "select "
                    + "dsale.product.name, "
                    + "dsale.product.category.categoryName, "
                    + "dsale.product.factory.name, "
                    + "dsale.coste, "
                    + "dsale.price, "
                    + "dsale.amount, "
                    + "dsale.total, dsale.sale, "
                    + "reserve.onAccount, "
                    + "from "
                    + "DetailSale dsale inner join dsale.sale as sale "
                    + "left join sale.reserve as reserve "
                    + "where "
                    + "dsale.product.del=0 and "
                    + "reserve.stateReserve = 1 and "
                    + "dsale.sale.dateSale>=:dateStart and "
                    + "dsale.sale.dateSale<=:dateEnd  and ";

            if (branchId == 0) {
                query = query + "dsale.branch is null";
            } else {
                query = query + "dsale.branch.branchId=" + branchId;
            }
            Query q = session.createQuery(query);
            q.setParameter("dateStart", dateStart);
            q.setParameter("dateEnd", dateEnd);

            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            SaleDetailGain sdg = null;
            Sale sale = null;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                sdg = new SaleDetailGain();
                sdg.setProductName("Reserve: " + object[0].toString());
                sdg.setProductCategory(object[1].toString());
                sdg.setProductFactory(object[2].toString());
                sdg.setCosteBuy(Double.parseDouble(object[3].toString()));
                sdg.setPriceSale(Double.parseDouble(object[4].toString()));
                sdg.setAmountSale(Double.parseDouble(object[5].toString()));
                sdg.setTotal(Double.parseDouble(object[6].toString()));
                sale = (Sale) object[7];
                sdg.setDateSale(sale.getDateSale().toString());
                sdg.setGain((sdg.getPriceSale() - sdg.getCosteBuy()) * sdg.getAmountSale());
                bigGain = bigGain + sdg.getGain();
                bigTotal = bigTotal + sdg.getTotal();
                lSaleDetailGain.add(sdg);
            }
            lsdg.setList(lSaleDetailGain);
            lsdg.setTotalGain(bigGain);
            lsdg.setTotalSale(bigTotal);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lsdg;
    }

    @Override
    public HashMap<String, String> saveListProductSaleForInventoryGral(ListProductSale lps) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        Sale newSale = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date d = sdf.parse(lps.getDateSale());

            Date dNow = new Date();
            if (sdf.format(dNow).compareTo(sdf.format(d)) == 0) {
                d = new Date();
            }

            System.out.println("SALE grabar::::::: " + lps.getVendorId());

            newSale = new Sale(d, lps.getTotal(), false);
            newSale.setUserId(lps.getUserId());
            newSale.setBranchId(Long.valueOf(lps.getBranchId()));

            Query qClient = session.createQuery("select client from Client as client where client.clientId=:id");
            qClient.setParameter("id", lps.getClientId());
            Client client = (Client) qClient.uniqueResult();
            newSale.setClient(client);
            newSale.setVendorId(lps.getVendorId());

            CreditSale creditSale = null;
            if (lps.isCredit()) {
                tx = session.getTransaction();
                tx.begin();
                creditSale = new CreditSale();
                creditSale.setCanceled(false);
                creditSale.setTotal(lps.getTotal());
                creditSale.setDel(false);
                session.save(creditSale);
                session.flush();
                tx.commit();

                newSale.setCreditSale(creditSale);
            }
            tx = session.getTransaction();
            tx.begin();
            session.save(newSale);
            session.flush();
            tx.commit();

            Iterator<ProductSale> productSale = lps.getListProductSale().iterator();
            DetailSale detailSale = null;
            RecordProducto newRpb = null;
            while (productSale.hasNext()) {
                ProductSale productSale1 = productSale.next();

                Double aux = new Double(productSale1.getAmount());
                int totalBuy = aux.intValue();

                while (totalBuy > 0) {
                    tx = session.getTransaction();
                    tx.begin();

                    detailSale = new DetailSale();
                    detailSale.setAmount(productSale1.getAmount());
                    detailSale.setDel(false);
                    detailSale.setPrice(productSale1.getPrice());
                    detailSale.setProduct(ProductHelper.getProductByIdStatic(productSale1.getProductId(), session));
                    detailSale.setSale(newSale);
                    detailSale.setTotal(productSale1.getTotal());

                    // get max priceBuy by ProductId
                    Query q12 = session.createQuery("select MIN(dbuy) from Detailbuy as dbuy where dbuy.exist=1 AND dbuy.product.del=0 and dbuy.product.productId= :productId");
                    q12.setParameter("productId", productSale1.getProductId());

                    Detailbuy maxbuyProduct = (Detailbuy) q12.uniqueResult();
                    if (maxbuyProduct != null) {
                        System.out.println("--------------------------");
                        System.out.println("Total:: " + totalBuy);
                        System.out.println("Detail buy id:: " + maxbuyProduct.getDetailBuyId());
                        System.out.println("Detail buy amount:: " + maxbuyProduct.getAmount());
                        System.out.println("Detail buy consumed:: " + maxbuyProduct.getConsumed());
                        System.out.println("--------------------------");
                    } else {
                        System.out.println("--------------------------");
                        System.out.println("Detail buy ELSE!!! ");
                        System.out.println("--------------------------");
                    }

                    int amountPp = maxbuyProduct.getAmount();
                    int consumedPp = maxbuyProduct.getConsumed();
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) < totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) == totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) > totalBuy) {
                        maxbuyProduct.setConsumed(maxbuyProduct.getConsumed() + totalBuy);
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        detailSale.setAmount(totalBuy);
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    totalBuy = totalBuy - (amountPp - consumedPp);
                    // end
                    detailSale.setCoste(maxbuyProduct.getPriceBuy());
                    detailSale.setGain((detailSale.getPrice() - detailSale.getCoste()) * detailSale.getAmount());
                    session.save(detailSale);
                    session.flush();

                    // Inicio get rpb max 
                    Query q = session.createQuery("select MAX(rpb.rpId) from RecordProducto as rpb where rpb.product.productId=:productId and rpb.del=0");
                    q.setParameter("productId", productSale1.getProductId());

                    Integer rpbIdMax = (Integer) q.uniqueResult();

                    System.out.println("sale:::::::::::: rpbIdMax:: " + rpbIdMax);

                    Query q1 = session.createQuery("select rpb from RecordProducto as rpb where rpb.rpId=:rpbIdMax and rpb.del=0");
                    q1.setParameter("rpbIdMax", rpbIdMax.toString());

                    RecordProducto rpbMax = (RecordProducto) q1.uniqueResult();

                    System.out.println("sale:::::::::::: rpIdAmount:: " + rpbMax.getAmount());

                    Query q2 = session.createQuery("select p from Product as p where p.productId=:rpbIdMax and p.del=0");
                    q2.setParameter("rpbIdMax", productSale1.getProductId());

                    Product p = (Product) q2.uniqueResult();
                    // end

                    rpbMax.setActive("no active");
                    session.update(rpbMax);
                    session.flush();

                    newRpb = new RecordProducto();

                    newRpb.setActive("active");
                    newRpb.setAmount((int) detailSale.getAmount());
                    newRpb.setDel(false);
                    newRpb.setDate(new Date());
                    newRpb.setSale(detailSale);
                    newRpb.setProduct(p);
                    newRpb.setTotal(rpbMax.getTotal() - detailSale.getAmount());

                    session.save(newRpb);
                    session.flush();
                    tx.commit();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }
        HashMap<String, String> has = new HashMap<String, String>();
        has.put("salId", newSale.getSalId().toString());
        has.put("nit", newSale.getClient().getNit());
        has.put("razonSocial", newSale.getClient().getRazonSocial());
        has.put("address", newSale.getClient().getAddress());
        has.put("phone", newSale.getClient().getPhone());
        int p_ent = (int) newSale.getTotal();
        String re[] = (newSale.getTotal() + "").split("\\.");
        String decimal = "00";
        if (re.length > 0) {
            if (re[1].length() == 1) {
                decimal = re[1] + "0";
            } else {
                decimal = re[1];
            }
        }
        has.put("literal", NumberToLetterConverter.convertNumberToLetter(new Double(p_ent + "")) + " " + decimal + "/100 Bolivianos");

        has.put("zone", newSale.getClient().getZone());
        has.put("printFacturaA", newSale.getClient().getNameInvoice());
//        return newSale.getSalId()+"";
        session.close();
        return has;
    }

    @Override
    public ProductReserve getListProductReserve(String ids, int branchId) {
        String[] idsArray = ids.split(",");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        ProductReserve lps = new ProductReserve();
        lps.setBranchId(branchId);
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        lps.setDateSale(sdf.format(d));
        List<ProductSale> listProductSale = new ArrayList<ProductSale>();
        ProductSale ps = null;
        double total = 0;

        tx = session.getTransaction();
        tx.begin();

        Query qBranch = session.createQuery("select branch from Branch as branch where branch.branchId=:branchId");
        qBranch.setParameter("branchId", branchId);

        Branch branch = (Branch) qBranch.uniqueResult();
        if (branch != null) {
            lps.setBranchName(branch.getName());
        } else {
            lps.setBranchName("Principal");
        }
        tx.commit();

        for (int i = 0; i < idsArray.length; i++) {
            String string = idsArray[i];
            try {
                tx = session.getTransaction();
                tx.begin();

                Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
                q.setParameter("proId", string);
                Product product = (Product) q.uniqueResult();

                q = session.createQuery("select Max(priceSale) from PriceSale as priceSale where priceSale.product.del=0 and priceSale.product.productId=" + string);
                PriceSale price = (PriceSale) q.uniqueResult();
                Double maxPrice = price.getPrice();

                if (product != null) {
                    ps = new ProductSale();
                    ps.setAmount(1);
                    ps.setCategoryName(product.getCategory().getCategoryName());
                    ps.setFatoryName(product.getFactory().getName());
                    ps.setPrice(maxPrice);
                    ps.setProductCodOrigin(product.getCodOrigin());
                    ps.setProductId(product.getProductId());
                    ps.setProductName(product.getName());
                    ps.setDetail(product.getDescription());
                    ps.setTotal(maxPrice);
                    ps.setHigherCost(maxPrice.doubleValue());
                    if (branchId == 0) {
                        ps.setLimitedAmount(RecordProductHelper.getRpTotalByProductId(product.getProductId(), session));
                    } else {
                        ps.setLimitedAmount(RecordProductBranchHelper.getRpbTotalByProductIdAndBranchId(product.getProductId(), branchId, session));
                    }
                    listProductSale.add(ps);
                    total = total + maxPrice;
                }
                session.flush();
                tx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                if (tx != null) {
                    tx.rollback();
                }
            }
        }
        lps.setTotal(total);
        lps.setOnAccount(Double.parseDouble("0.0"));
        lps.setListProductSale(listProductSale);
        return lps;
    }

    @Override
    public boolean saveListProductReserveForInventoryGral(ProductReserve pr) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        Sale newSale = null;
        Reserve reserve = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date dReserve = sdf.parse(pr.getDateClose());

            Date d = sdf.parse(pr.getDateSale());
            Date dNow = new Date();
            if (sdf.format(dNow).compareTo(sdf.format(d)) == 0) {
                d = new Date();
            }

            reserve = new Reserve();
            reserve.setDateReserve(d);
            reserve.setDateClose(dReserve);
            reserve.setDel(false);
            reserve.setOnAccount(pr.getOnAccount());
            reserve.setStateReserve(Short.parseShort("1"));
            reserve.setUserid(pr.getUserId());

            tx.begin();
            session.save(reserve);
            session.flush();

            newSale = new Sale(d, pr.getTotal(), false);
            newSale.setUserId(pr.getUserId());
            newSale.setReserve(reserve);
            newSale.setBranchId(Long.valueOf(pr.getBranchId()));
            Query qClient = session.createQuery("select client from Client as client where client.clientId=:id");
            qClient.setParameter("id", pr.getClientId());
            Client client = (Client) qClient.uniqueResult();
            newSale.setClient(client);

            session.save(newSale);
            session.flush();
            tx.commit();

            Iterator<ProductSale> productSale = pr.getListProductSale().iterator();
            DetailSale detailSale = null;
            RecordProducto newRpb = null;
            while (productSale.hasNext()) {
                ProductSale productSale1 = productSale.next();

                Double aux = new Double(productSale1.getAmount());
                int totalBuy = aux.intValue();

                while (totalBuy > 0) {
                    tx = session.getTransaction();
                    tx.begin();

                    detailSale = new DetailSale();
                    detailSale.setAmount(productSale1.getAmount());
                    detailSale.setDel(false);
                    detailSale.setPrice(productSale1.getPrice());
                    detailSale.setProduct(ProductHelper.getProductByIdStatic(productSale1.getProductId(), session));
                    detailSale.setSale(newSale);
                    detailSale.setTotal(productSale1.getTotal());

                    Query q12 = session.createQuery("select MIN(dbuy) from Detailbuy as dbuy where dbuy.exist=1 AND dbuy.product.del=0 and dbuy.product.productId= :productId");
                    q12.setParameter("productId", productSale1.getProductId());

                    Detailbuy maxbuyProduct = (Detailbuy) q12.uniqueResult();
                    if (maxbuyProduct != null) {
                        System.out.println("--------------------------");
                        System.out.println("Total:: " + totalBuy);
                        System.out.println("Detail buy id:: " + maxbuyProduct.getDetailBuyId());
                        System.out.println("Detail buy amount:: " + maxbuyProduct.getAmount());
                        System.out.println("Detail buy consumed:: " + maxbuyProduct.getConsumed());
                        System.out.println("--------------------------");
                    } else {
                        System.out.println("--------------------------");
                        System.out.println("Detail buy ELSE!!! ");
                        System.out.println("--------------------------");
                    }

                    int amountPp = maxbuyProduct.getAmount();
                    int consumedPp = maxbuyProduct.getConsumed();
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) < totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) == totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) > totalBuy) {
                        maxbuyProduct.setConsumed(maxbuyProduct.getConsumed() + totalBuy);
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        detailSale.setAmount(totalBuy);
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    totalBuy = totalBuy - (amountPp - consumedPp);

                    // end
                    detailSale.setCoste(maxbuyProduct.getPriceBuy());
                    detailSale.setGain((detailSale.getPrice() - detailSale.getCoste()) * detailSale.getAmount());
                    session.save(detailSale);
                    session.flush();

                    // Inicio get rpb max 
                    Query q = session.createQuery("select MAX(rpb.rpId) from RecordProducto as rpb where rpb.product.productId=:productId and rpb.del=0");
                    q.setParameter("productId", productSale1.getProductId());

                    Integer rpbIdMax = (Integer) q.uniqueResult();

                    Query q1 = session.createQuery("select rpb from RecordProducto as rpb where rpb.rpId=:rpbIdMax and rpb.del=0");
                    q1.setParameter("rpbIdMax", rpbIdMax.toString());

                    RecordProducto rpbMax = (RecordProducto) q1.uniqueResult();

                    System.out.println("sale:::::::::::: rpIdAmount:: " + rpbMax.getAmount());

                    Query q2 = session.createQuery("select p from Product as p where p.productId=:rpbIdMax and p.del=0");
                    q2.setParameter("rpbIdMax", productSale1.getProductId());

                    Product p = (Product) q2.uniqueResult();
                    // end

                    rpbMax.setActive("no active");
                    session.update(rpbMax);
                    session.flush();

                    newRpb = new RecordProducto();

                    newRpb.setActive("active");
                    newRpb.setAmount((int) detailSale.getAmount());
                    newRpb.setDel(false);
                    newRpb.setDate(new Date());
                    newRpb.setSale(detailSale);
                    newRpb.setProduct(p);
                    newRpb.setTotal(rpbMax.getTotal() - detailSale.getAmount());

                    session.save(newRpb);
                    session.flush();
                    tx.commit();
                }

            }

            inserValid = true;
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        }
        return inserValid;
    }

    @Override
    public boolean saveListProductReserve(ProductReserve pr) {
        boolean inserValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();

        Sale newSale = null;
        Reserve reserve = null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date dClose = sdf.parse(pr.getDateClose());

            Date d = sdf.parse(pr.getDateSale());

            Date dNow = new Date();
            if (sdf.format(dNow).compareTo(sdf.format(d)) == 0) {
                d = new Date();
            }

            reserve = new Reserve();
            reserve.setDateReserve(d);
            reserve.setDateClose(dClose);
            reserve.setDel(false);
            reserve.setOnAccount(pr.getOnAccount());
            reserve.setStateReserve(Short.parseShort("1"));
            reserve.setUserid(pr.getUserId());

            tx.begin();
            session.save(reserve);
            session.flush();

            newSale = new Sale(d, pr.getTotal(), false);
            newSale.setUserId(pr.getUserId());
            newSale.setReserve(reserve);
            newSale.setBranchId(Long.valueOf(pr.getBranchId()));
            Query qClient = session.createQuery("select client from Client as client where client.clientId=:id");
            qClient.setParameter("id", pr.getClientId());
            Client client = (Client) qClient.uniqueResult();

            newSale.setClient(client);
            session.save(newSale);
            session.flush();

            Iterator<ProductSale> productSale = pr.getListProductSale().iterator();
            DetailSale detailSale = null;
            RecordProductBranch newRpb = null;
            Branch branch = BranchHelper.getBranchByIdStatic(pr.getBranchId(), session);
            tx.commit();
            while (productSale.hasNext()) {
                ProductSale productSale1 = productSale.next();
                Double aux = new Double(productSale1.getAmount());
                int totalBuy = aux.intValue();
                while (totalBuy > 0) {
                    tx = session.getTransaction();
                    tx.begin();

                    detailSale = new DetailSale();
                    detailSale.setAmount(productSale1.getAmount());
                    detailSale.setBranch(branch);
                    detailSale.setDel(false);
                    detailSale.setPrice(productSale1.getPrice());
                    detailSale.setProduct(ProductHelper.getProductByIdStatic(productSale1.getProductId(), session));
                    detailSale.setSale(newSale);
                    detailSale.setTotal(productSale1.getTotal());

                    // get max priceBuy by ProductId
                    Query q12 = session.createQuery("select MIN(dbuy) from Detailbuy as dbuy where dbuy.exist=1 AND dbuy.product.del=0 and dbuy.product.productId= :productId");
                    q12.setParameter("productId", productSale1.getProductId());

                    Detailbuy maxbuyProduct = (Detailbuy) q12.uniqueResult();
                    if (maxbuyProduct != null) {
                        System.out.println("--------------------------");
                        System.out.println("Total:: " + totalBuy);
                        System.out.println("Detail buy id:: " + maxbuyProduct.getDetailBuyId());
                        System.out.println("Detail buy amount:: " + maxbuyProduct.getAmount());
                        System.out.println("Detail buy consumed:: " + maxbuyProduct.getConsumed());
                        System.out.println("--------------------------");
                    } else {
                        System.out.println("--------------------------");
                        System.out.println("Detail buy ELSE!!! ");
                        System.out.println("--------------------------");
                    }
                    int amountPp = maxbuyProduct.getAmount();
                    int consumedPp = maxbuyProduct.getConsumed();
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) < totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) == totalBuy) {
                        detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        maxbuyProduct.setExist(false);
                        maxbuyProduct.setConsumed(maxbuyProduct.getAmount());
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    if ((maxbuyProduct.getAmount() - maxbuyProduct.getConsumed()) > totalBuy) {
                        maxbuyProduct.setConsumed(maxbuyProduct.getConsumed() + totalBuy);
                        //detailSale.setAmount(maxbuyProduct.getAmount() - maxbuyProduct.getConsumed());
                        detailSale.setAmount(totalBuy);
                        session.save(maxbuyProduct);
                        session.flush();
                    }
                    totalBuy = totalBuy - (amountPp - consumedPp);
                    // end
                    detailSale.setCoste(maxbuyProduct.getPriceBuy());
                    detailSale.setGain((detailSale.getPrice() - detailSale.getCoste()) * detailSale.getAmount());
                    session.save(detailSale);
                    session.flush();

                    // Inicio get rpb max 
                    Query q = session.createQuery("select MAX(rpb.rpbId) from RecordProductBranch as rpb where rpb.stock.product.productId=:productId and rpb.stock.branch.branchId=:branchId and rpb.del=0");
                    q.setParameter("productId", productSale1.getProductId());
                    q.setParameter("branchId", pr.getBranchId());

                    Integer rpbIdMax = (Integer) q.uniqueResult();

                    Query q1 = session.createQuery("select rpb from RecordProductBranch as rpb where rpb.rpbId=:rpbIdMax and rpb.del=0");
                    q1.setParameter("rpbIdMax", rpbIdMax.toString());

                    RecordProductBranch rpbMax = (RecordProductBranch) q1.uniqueResult();
                    // end

                    rpbMax.setActive("no active");
                    session.update(rpbMax);
                    session.flush();

                    newRpb = new RecordProductBranch();
                    newRpb.setActive("active");
                    newRpb.setAmount((int) detailSale.getAmount());
                    newRpb.setDel(false);
                    newRpb.setFecha(new Date());
                    newRpb.setDetailSaleId(detailSale.getDetailSaleId());
                    newRpb.setStock(StockHelper.getStockByProductIdAndBranchId(productSale1.getProductId(), pr.getBranchId(), session));
                    newRpb.setTotal(rpbMax.getTotal() - detailSale.getAmount());

                    session.save(newRpb);
                    session.flush();
                    tx.commit();
                }
            }

            inserValid = true;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return inserValid;
    }

    @Override
    public ArrayList<ReserveDto> getListReserve(int branchId, String dateReserve, boolean all) {
        ArrayList<ReserveDto> listReserve = new ArrayList<ReserveDto>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            String dateReserveIni = dateReserve + " 00:00:00";
            String dateReserveEnd = dateReserve + " 23:59:59";
//            String dateReserveIni = "2014-06-05 00:00:00";
//            String dateReserveEnd = "2014-06-06 23:59:59";
            String query = "select "
                    + "distinct reserve.reserveId, "
                    + "reserve.dateReserve, "
                    + "reserve.onAccount, "
                    + "sale.total, "
                    + "client.razonSocial, client.attendant, client.nit, "
                    + "branch.name "
                    + "from "
                    + "DetailSale dsale inner join dsale.sale as sale "
                    + "left join dsale.branch as branch "
                    + "inner join sale.reserve as reserve "
                    + "left join sale.client as client "
                    + "where "
                    + "dsale.product.del=0 and "
                    + "reserve.stateReserve = 1 and ";
            if (!all) {

                query += "reserve.dateReserve>='" + dateReserveIni + "' and "
                        + "reserve.dateReserve<='" + dateReserveEnd + "'  and ";
                if (branchId == 0) {
                    query = query + "branch is null";
                } else {
                    query = query + "branch.branchId=" + branchId;
                }
            } else {
                query += " 1=1 ";
            }

            query += " order by client.razonSocial asc";
            System.out.println(query);
            Query q = session.createQuery(query);

            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            ReserveDto reserveDto = null;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                reserveDto = new ReserveDto();
                reserveDto.setReserveId(Integer.parseInt(object[0].toString()));
                reserveDto.setOnAccount(Utils.roundTwoDecinal(Double.parseDouble(object[2].toString())));
                reserveDto.setTotal(Utils.roundTwoDecinal(Double.parseDouble(object[3].toString())));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
                reserveDto.setDateReserve(object[1].toString());
                reserveDto.setDebit(Utils.roundTwoDecinal(reserveDto.getTotal() - reserveDto.getOnAccount()));
                if (object[7] != null) {
                    reserveDto.setBranchName(object[7].toString());
                } else {
                    reserveDto.setBranchName("Principal");
                }
                String firsname = "";
                if (object[4] != null) {
                    firsname = object[4].toString();
                }
                reserveDto.setPerson(firsname);
                listReserve.add(reserveDto);
            }
            session.flush();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }

        return listReserve;
    }

    @Override
    public ReserveDetail getListReserveDetail(int idReserve) {
        ReserveDetail rd = new ReserveDetail();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            String query = "select "
                    + "reserve.reserveId, "
                    + "reserve.dateReserve, "
                    + "reserve.onAccount, "
                    + "sale.total, "
                    + "client.razonSocial, client.nameInvoice, client.nit, "
                    + "dsale.amount, dsale.price, "
                    + "dsale.total, product.name,"
                    + "product.codOrigin, "
                    + "dsale.detailSaleId, "
                    + "branch.name "
                    + "from "
                    + "DetailSale dsale inner join dsale.sale as sale "
                    + "left join dsale.branch as branch "
                    + "inner join dsale.product as product "
                    + "inner join sale.reserve as reserve "
                    + "left join sale.client as client "
                    + "where "
                    + "dsale.product.del=0 and "
                    + "dsale.sale.reserve.reserveId =:idReserve";

            Query q = session.createQuery(query);
            q.setParameter("idReserve", idReserve);

            List listResult = q.list();

            Iterator iterator = listResult.iterator();

            ArrayList<SaleDetail> listReserve = new ArrayList<SaleDetail>();

            SaleDetail sd = null;
            String names = "";
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                sd = new SaleDetail();

                sd.setAmount(Double.parseDouble(object[7].toString()));
                sd.setCode(object[11].toString());
                sd.setId(Integer.parseInt(object[12].toString()));
                sd.setNameItem(object[10].toString());
                sd.setPrice(Double.parseDouble(object[8].toString()));
                sd.setTotal(Double.parseDouble(object[9].toString()));
                names = "";
                listReserve.add(sd);
                if (object[4] != null) {
                    names = object[4].toString();
                }

                rd.setNameClient(names);
                if (object[6] != null) {
                    rd.setNameCi(object[6].toString());
                } else {
                    rd.setNameCi("0");
                }
                rd.setDateReserve(object[1].toString());
                rd.setOnAmount(Double.parseDouble(object[2].toString()));
                rd.setTotal(Double.parseDouble(object[3].toString()));
                if (object[13] != null) {
                    rd.setBranchName(object[13].toString());
                } else {
                    rd.setBranchName("Principal");
                }
                rd.setDebit(rd.getTotal() - rd.getOnAmount());
            }
            rd.setListSaleDetail(listReserve);
            session.flush();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        }

        return rd;
    }

    @Override
    public boolean reserveClose(int reserveId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            String query = "select "
                    + "reserve "
                    + "from Reserve as reserve "
                    + "where reserve.reserveId=:reserveId ";
            Query q = session.createQuery(query);
            q.setParameter("reserveId", reserveId);

            Reserve reserve = (Reserve) q.uniqueResult();
            if (reserve != null) {
                System.out.println("Reserve save:::::::::::::::::: " + reserve.getDateClose());
                short s = 0;
                reserve.setDateClose(new Date());
                reserve.setStateReserve(s);
                session.saveOrUpdate(reserve);
            }
            //person = (Person) session.merge(person);
            //session.saveOrUpdate(person);

            session.flush();
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
        return true;
    }

    @Override
    public ListSalesAccount getListAccount(int branchId, String dateStart, String dateEnd) {
        ListSalesAccount lsdg = new ListSalesAccount();
        System.out.println("yyyyyyyyyyyyyyyyy:: "+dateStart);
        String newstring = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        dateEnd = newstring;
        this.getAccountSales(lsdg, branchId, dateStart, dateEnd);
        this.getAccountReserve(lsdg, branchId, dateStart, dateEnd);
        this.getAccountReserveClose(lsdg, branchId, dateStart, dateEnd);
        this.getSalesCreditPayment(lsdg, branchId, dateStart, dateEnd);

        return lsdg;
    }

    private void getAccountSales(ListSalesAccount lsdg, int branchId, String dateStart, String dateEnd) {
        List<SaleAccount> lSaleAccount = new ArrayList<SaleAccount>();
        double bigTotal = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String query = "select "
                    + "dsale.product.name, "
                    + "dsale.product.category.categoryName, "
                    + "dsale.product.factory.name, "
                    + "dsale.coste, "
                    + "dsale.price, "
                    + "dsale.amount, "
                    + "dsale.total, dsale.sale "
                    + "from "
                    + "DetailSale dsale inner join dsale.sale as sale "
                    + "left join sale.reserve as reserve "
                    + "where "
                    + "dsale.product.del=0 and "
                    + "sale.reserve is null and "
                    + "dsale.sale.dateSale BETWEEN :dateStart  and :dateEnd and ";

            if (branchId == 0) {
                query = query + "dsale.branch is null";
            } else {
                query = query + "dsale.branch.branchId=" + branchId;
            }
            Query q = session.createQuery(query);
            q.setParameter("dateStart", dateStart);
            q.setParameter("dateEnd", dateEnd);

            System.out.println("fecha ini:: " + dateStart);
            System.out.println("fecha end:: " + dateEnd);
            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            SaleAccount sdg = null;
            Sale sale = null;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                sdg = new SaleAccount();
                sdg.setProductName(object[0].toString());
                sdg.setProductCategory(object[1].toString());
                sdg.setProductFactory(object[2].toString());
                sdg.setCosteBuy(Double.parseDouble(object[3].toString()));
                sdg.setPriceSale(Double.parseDouble(object[4].toString()));
                sdg.setAmountSale(Double.parseDouble(object[5].toString()));
                sdg.setTotal(Double.parseDouble(object[6].toString()));
                sale = (Sale) object[7];

                sdg.setDateSale(sale.getDateSale().toString());
                double dd = (double)Math.round(((sdg.getPriceSale() - sdg.getCosteBuy()) * sdg.getAmountSale()) * 100) / 100;
                sdg.setGain(dd);
                bigTotal = bigTotal + sdg.getTotal();
                lSaleAccount.add(sdg);
            }

            lsdg.setListSales(lSaleAccount);

            lsdg.setTotalSale(bigTotal);
            lsdg.setTotalSale(bigTotal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void getSalesCreditPayment(ListSalesAccount lsdg, int branchId, String dateStart, String dateEnd) {
        List<CreditReportDto> lCreditReport = new ArrayList<CreditReportDto>();
        double bigTotal = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String query = "from "
                    + "PaymentSale as ps "
                    + "where "
                    + "ps.createDate BETWEEN :dateStart  and :dateEnd ";

            Query q = session.createQuery(query);
            q.setParameter("dateStart", dateStart);
            q.setParameter("dateEnd", dateEnd);

            List<PaymentSale> listResult = (List<PaymentSale>) q.list();

            Iterator iterator = listResult.iterator();
            PaymentSale ps = null;
            CreditReportDto crDto = null;
            String queryCreditSale = "";
            while (iterator.hasNext()) {
                ps = (PaymentSale) iterator.next();

                queryCreditSale = "select "
                        + "sale.client.razonSocial, "
                        + "sale.creditSale, "
                        + "sale.dateSale, "
                        + "sale.total, "
                        + "ps "
                        + "from "
                        + "Sale as sale "
                        + "inner join sale.creditSale ps "
                        + "where sale.creditSale.creditId=:creditId AND ";

                if (branchId == 0) {
                    queryCreditSale += "(sale.branchId is null OR sale.branchId=0) ";
                } else {
                    queryCreditSale += "sale.branchId=" + branchId;
                }

                Query qCs = session.createQuery(queryCreditSale);
                qCs.setParameter("creditId", ps.getCreditSale().getCreditId());

                List listCreditD = qCs.list();

                if(listCreditD.size() > 0){
                    bigTotal += ps.getAmount();
                    crDto = new CreditReportDto();

                    crDto.setDate(ps.getCreateDate().toString());
                    crDto.setTotalPayment(ps.getAmount());
                
                    Iterator iCreditD = listCreditD.iterator();

                    while (iCreditD.hasNext()) {
                        Object[] object = (Object[]) iCreditD.next();
//                        System.out.println("QQQQQQQQQ:: "+branchId+" || "+object[0].toString()+" || "+dateEnd);
                        CreditSale cs = (CreditSale) object[4];
                        Set<PaymentSale> payss = cs.getPaymentSales();
                        double totalPayments = 0;
                        for (PaymentSale pss : payss) {
                            totalPayments += pss.getAmount();
                        }

                        crDto.setRazonSocial(object[0].toString());
                        crDto.setTotal(Double.valueOf(object[3].toString()));
                        crDto.setShould(crDto.getTotal() - totalPayments);
                    }

                    lCreditReport.add(crDto);
                }
            }

            lsdg.setListCreditPayment(lCreditReport);

            lsdg.setTotalCreditPayment(bigTotal);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void getAccountReserve(ListSalesAccount lsdg, int branchId, String dateStart, String dateEnd) {
        List<ReserveDto> lSaleAccount = new ArrayList<ReserveDto>();
        double bigTotal = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String query = "select "
                    + "distinct reserve.reserveId, "
                    + "reserve.dateReserve, "
                    + "reserve.onAccount, "
                    + "sale.total, "
                    + "person.razonSocial, person.address, person.nit, "
                    + "branch.name "
                    + "from "
                    + "DetailSale dsale inner join dsale.sale as sale "
                    + "left join dsale.branch as branch "
                    + "inner join sale.reserve as reserve "
                    + "left join sale.client as person "
                    + "where "
                    + "reserve.stateReserve = 1 and "
                    + "dsale.sale.dateSale BETWEEN :dateStart  and :dateEnd and ";

            if (branchId == 0) {
                query = query + "dsale.branch is null";
            } else {
                query = query + "dsale.branch.branchId=" + branchId;
            }
            Query q = session.createQuery(query);
            //q.setParameter("dateStart", dateStart);
            q.setParameter("dateStart", dateStart);
            q.setParameter("dateEnd", dateEnd);

            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            ReserveDto reserveDto = null;
            double totalAccount = 0;
            double newKB = 0;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                reserveDto = new ReserveDto();
                reserveDto.setReserveId(Integer.parseInt(object[0].toString()));
                reserveDto.setOnAccount(Double.parseDouble(object[2].toString()));
                reserveDto.setTotal(Double.parseDouble(object[3].toString()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
                reserveDto.setDateReserve(object[1].toString());
                newKB = Math.round((reserveDto.getTotal() - reserveDto.getOnAccount()) * 100.0) / 100.0;
                reserveDto.setDebit(newKB);
                if (object[7] != null) {
                    reserveDto.setBranchName(object[7].toString());
                } else {
                    reserveDto.setBranchName("Principal");
                }
                String firsname = "";
                if (object[4] != null) {
                    firsname = object[4].toString();
                }
                if (object[5] != null) {
                    firsname += " " + object[5].toString();
                }
                reserveDto.setPerson(firsname);
                lSaleAccount.add(reserveDto);
                totalAccount += Double.parseDouble(object[2].toString());
            }

            lsdg.setListReserve(lSaleAccount);

            lsdg.setTotalReserve(totalAccount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    private void getAccountReserveClose(ListSalesAccount lsdg, int branchId, String dateStart, String dateEnd) {
        List<ReserveDto> lSaleAccount = new ArrayList<ReserveDto>();
        double bigTotal = 0;

        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            String query = "select "
                    + "distinct reserve.reserveId, "
                    + "reserve.dateReserve, "
                    + "reserve.onAccount, "
                    + "sale.total, "
                    + "person.razonSocial, person.address, person.nit, "
                    + "branch.name "
                    + "from "
                    + "DetailSale dsale inner join dsale.sale as sale "
                    + "left join dsale.branch as branch "
                    + "inner join sale.reserve as reserve "
                    + "left join sale.client as person "
                    + "where "
                    + "dsale.product.del=0 and "
                    + "reserve.stateReserve = 0 and "
                    + "reserve.dateClose BETWEEN :dateStart  and :dateEnd and ";

            if (branchId == 0) {
                query = query + "dsale.branch is null";
            } else {
                query = query + "dsale.branch.branchId=" + branchId;
            }
            Query q = session.createQuery(query);
            q.setParameter("dateStart", dateStart);
            q.setParameter("dateEnd", dateEnd);

            System.out.println("fecha ini:: " + dateStart);
            System.out.println("fecha end:: " + dateEnd);
            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            ReserveDto reserveDto = null;
            double totalAccount = 0;
            double newKB = 0;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                reserveDto = new ReserveDto();
                reserveDto.setReserveId(Integer.parseInt(object[0].toString()));
                reserveDto.setOnAccount(Double.parseDouble(object[2].toString()));
                reserveDto.setTotal(Double.parseDouble(object[3].toString()));
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss.S");
                reserveDto.setDateReserve(object[1].toString());
                newKB = Math.round((reserveDto.getTotal() - reserveDto.getOnAccount()) * 100.0) / 100.0;
                reserveDto.setDebit(newKB);
                if (object[7] != null) {
                    reserveDto.setBranchName(object[7].toString());
                } else {
                    reserveDto.setBranchName("Principal");
                }
                String firsname = "";
                if (object[4] != null) {
                    firsname = object[4].toString();
                }
                if (object[5] != null) {
                    firsname += " " + object[5].toString();
                }
                reserveDto.setPerson(firsname);
                lSaleAccount.add(reserveDto);
                totalAccount += reserveDto.getDebit();
            }

            lsdg.setListReserveClose(lSaleAccount);

            lsdg.setTotalReserveClose(totalAccount);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
    }

    @Override
    public SalesAmountDto getLastSaleAmount(int branchId, int userId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        SalesAmount salesAmount = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            String query = "select "
                    + "MAX(salesAmount) "
                    + "from SalesAmount as salesAmount "
                    + "where salesAmount.branchId=:branchId ";
            Query q = session.createQuery(query);
            q.setParameter("branchId", branchId);

            salesAmount = (SalesAmount) q.uniqueResult();
            if (salesAmount == null) {
                salesAmount = new SalesAmount();
                salesAmount.setActive(true);
                salesAmount.setAmount(0.0);
                salesAmount.setBranchId(branchId);

                SimpleDateFormat dateValue = new SimpleDateFormat("MM-dd-yyyy");
                Date d = new Date();
                salesAmount.setDateEnd(d);
                salesAmount.setDateStart(null);
                salesAmount.setUserId(userId);
            }
            session.flush();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return SalesAmountTranslate.salesAmountTosalesAmountDto(salesAmount);
    }

    @Override
    public String[] getLastSaleAmountDates(int branchId) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        SalesAmount salesAmount = null;
        String[] dates = new String[2];
        try {
            tx = session.getTransaction();
            tx.begin();
            String query = "select "
                    + "MAX(salesAmount) "
                    + "from SalesAmount as salesAmount "
                    + "where salesAmount.branchId=:branchId ";
            Query q = session.createQuery(query);
            q.setParameter("branchId", branchId);

            salesAmount = (SalesAmount) q.uniqueResult();
            SimpleDateFormat dateValue = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date datess = new Date();
            dates[1] = dateValue.format(datess);
            if (salesAmount == null) {
                dates[0] = "sin fecha";
            } else {
                dates[0] = dateValue.format(salesAmount.getDateEnd());
            }
            session.flush();
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) {
                tx.rollback();
            }
            e.printStackTrace();
        } finally {
            session.close();
        }
        return dates;
    }

    @Override
    public boolean reserveSalesAccount(SalesAmountDto saDto) {
        boolean updateValid = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {

            tx.begin();
            SalesAmount sa = SalesAmountTranslate.salesAmountDtoToSalesAmount(saDto);
            session.saveOrUpdate(sa);

            session.flush();
            tx.commit();
            updateValid = true;
        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in reserveSalesAccount: " + e.getMessage());
        } finally {
            session.close();
        }

        return updateValid;
    }

    @Override
    public ListSaleDto listSale(String dateStart, String dateEnd, int branchId) {
        List<SaleDto> listSales = new ArrayList<SaleDto>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();

            String query = "select "
                    + "sale.salId, sale.dateSale, sale.client.razonSocial, sale.reserve.reserveId, sale.creditSale.creditId, sale.total, sale.creditSale.canceled "
                    + "from Sale as sale "
                    + "where sale.dateSale BETWEEN :dateStart  and :dateEnd AND "
                    + "sale.branchId = " + branchId;
            query += " order by sale.salId";
            Query q = session.createQuery(query);
            q.setParameter("dateStart", dateStart + " 00:00:00");
            q.setParameter("dateEnd", dateEnd + " 23:59:59");

            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            SaleDto sale = null;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                sale = new SaleDto();

                sale.setCreateDate(object[1].toString());
                if (object[4] != null) {
                    sale.setCredit(Long.parseLong(object[4].toString()));
                }
                sale.setRazonSocial(object[2].toString());
                if (object[3] != null) {
                    sale.setReserve(Long.parseLong(object[3].toString()));
                }
                sale.setSaleId(Long.parseLong(object[0].toString()));
                sale.setTotal(Double.parseDouble(object[5].toString()));
                sale.setCreditCanceled(Boolean.valueOf(object[6].toString()));
                listSales.add(sale);
            }

        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in ListSale: ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        ListSaleDto list = new ListSaleDto();
        list.setData(listSales);
        return list;
    }

    @Override
    public ListProductSaleDto getDetailSale(long salId) {
        ListProductSaleDto lps = new ListProductSaleDto();
        Long vendorId = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            String query = "select "
                    + "sale.salId, sale.dateSale, sale.client.razonSocial, sale.client.nit, sale.reserve.reserveId, sale.creditSale.creditId, sale.total, sale.vendorId, sale.branchId "
                    + "from Sale as sale "
                    + "where sale.salId=:salId ";
            Query q = session.createQuery(query);
            q.setParameter("salId", salId);

            Object[] obj = (Object[]) q.uniqueResult();

            lps.setClientNit(obj[3].toString());
            lps.setClientRazonSocial(obj[2].toString());
            if (obj[5] != null) {
                lps.setCredit(Long.valueOf(obj[5].toString()));
            }
            lps.setDateSale(obj[1].toString());
            if (obj[4] != null) {
                lps.setReserve(Long.valueOf(obj[4].toString()));
            }
            lps.setTotal(Double.valueOf(obj[6].toString()));

            if (obj[7] != null) {
                vendorId = new Long(obj[7].toString());
                String queryVendor = "from Employee employee "
                        + "where employee.empId=:empId "
                        + "";
                Query qEmp = session.createQuery(queryVendor);
                qEmp.setParameter("empId", vendorId);

                Employee employee = (Employee) qEmp.uniqueResult();

                String names = "";
                if (employee != null) {
                    names += employee.getPerson().getFirstname() + " ";
                    names += employee.getPerson().getLastname() + " ";
                    names += employee.getPerson().getNames();
                }

                lps.setVendorName(names);
            }

            if (obj[8] != null) {
                int branchID = Integer.valueOf(obj[8].toString());
                if (branchID == 0) {
                    lps.setBranchName("Principal");
                } else {
                    String queryBranch = ""
                            + "from Branch branch "
                            + "where branch.branchId=:branchId";
                    Query qBranch = session.createQuery(queryBranch);
                    qBranch.setParameter("branchId", branchID);

                    Branch branch = (Branch) qBranch.uniqueResult();

                    lps.setBranchName(branch.getName());
                }
            }

            String queryds = "select "
                    + "dsale.product.name, "
                    + "dsale.product.category.categoryName, "
                    + "dsale.product.factory.name, "
                    + "dsale.coste, "
                    + "dsale.price, "
                    + "dsale.amount, "
                    + "dsale.product.codOrigin, "
                    + "dsale.product.description, "
                    + "dsale.product.productId, "
                    + "dsale.total "
                    + "from "
                    + "DetailSale dsale "
                    + "where "
                    + "dsale.sale.salId=:salId ";
            Query qDs = session.createQuery(queryds);
            qDs.setParameter("salId", salId);

            List listResult = qDs.list();

            Iterator iterator = listResult.iterator();
            ProductSale ps = null;
            List<ProductSale> listPs = new ArrayList<>();
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                ps = new ProductSale();
                ps.setAmount(Double.valueOf(object[5].toString()));
                ps.setCategoryName(object[1].toString());
                ps.setFatoryName(object[2].toString());
                ps.setPrice(Double.valueOf(object[4].toString()));
                ps.setProductCodOrigin(object[6].toString());
                ps.setDetail(object[7].toString());
                ps.setProductId(Integer.valueOf(object[8].toString()));
                ps.setProductName(object[0].toString());
                ps.setTotal(Double.valueOf(object[9].toString()));

                listPs.add(ps);
            }

            lps.setListProductSale(listPs);

        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in ListSale: ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        return lps;
    }

    @Override
    public ListSaleDto listSaleIsCredit(int branchId, int type, String search) {
        List<SaleDto> listSales = new ArrayList<SaleDto>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();

            String query = "select "
                    + "sale.salId, sale.dateSale, sale.client.razonSocial, sale.reserve.reserveId, sale.creditSale.creditId, sale.total "
                    + "from Sale as sale "
                    + "where sale.creditSale.canceled = 0 AND "
                    + "sale.branchId = " + branchId + " AND ";
            if (type == 0) {
                query += "sale.client.razonSocial like '%" + search + "%' ";
            } else {
                query += "sale.client.nit like '%" + search + "%' ";
            }
            query += " order by sale.salId";
            Query q = session.createQuery(query);

            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            SaleDto sale = null;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                sale = new SaleDto();

                sale.setCreateDate(object[1].toString());
                if (object[4] != null) {
                    sale.setCredit(Long.parseLong(object[4].toString()));
                }
                sale.setRazonSocial(object[2].toString());
                if (object[3] != null) {
                    sale.setReserve(Long.parseLong(object[3].toString()));
                }
                sale.setSaleId(Long.parseLong(object[0].toString()));
                sale.setTotal(Double.parseDouble(object[5].toString()));

                listSales.add(sale);
            }

        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in ListSale: ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        ListSaleDto list = new ListSaleDto();
        list.setData(listSales);
        return list;
    }
    
    @Override
    public ListSaleDto listSaleReport(int branchId, int type, String search) {
        List<SaleDto> listSales = new ArrayList<SaleDto>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();

            String query = "select "
                    + "sale.salId, sale.dateSale, sale.client.razonSocial, sale.reserve.reserveId, sale.creditSale.creditId, sale.total "
                    + "from Sale as sale "
                    //+ "where (sale.creditSale.canceled = 1 OR sale.creditSale is null) AND "
                    + "where "
                    + "sale.branchId = " + branchId + " AND ";
            if (type == 0) {
                query += "sale.client.razonSocial like '%" + search + "%' ";
            } else {
                query += "sale.client.nit like '%" + search + "%' ";
            }
            query += " order by sale.salId";
            
            Query q = session.createQuery(query);

            List listResult = q.list();

            Iterator iterator = listResult.iterator();
            SaleDto sale = null;
            while (iterator.hasNext()) {
                Object[] object = (Object[]) iterator.next();
                sale = new SaleDto();

                sale.setCreateDate(object[1].toString());
                if (object[4] != null) {
                    sale.setCredit(Long.parseLong(object[4].toString()));
                }
                sale.setRazonSocial(object[2].toString());
                if (object[3] != null) {
                    sale.setReserve(Long.parseLong(object[3].toString()));
                }
                sale.setSaleId(Long.parseLong(object[0].toString()));
                sale.setTotal(Double.parseDouble(object[5].toString()));

                listSales.add(sale);
            }

        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in ListSale: ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        ListSaleDto list = new ListSaleDto();
        list.setData(listSales);
        return list;
    }

    @Override
    public String getLastSaleAmountDateByBranchId(long branchId) {
        String date = "";
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try {
            tx.begin();
            String query = "select max(saleA) from SalesAmount as saleA "
                    + "where saleA.branchId = "+ branchId;
            Query q = session.createQuery(query);

            SalesAmount sa = (SalesAmount)q.uniqueResult();

            if(sa != null){
                date = dateFormat.format(sa.getDateEnd());
            }

        } catch (HibernateException e) {
            tx.rollback();
            System.out.println("Fail in last date: ");
            e.printStackTrace();
        } finally {
            session.close();
        }
        return date;
    }

    @Override
    public boolean saveSaleAmount(long userId, long branchId, String dateStart, String dateEnd, double amount) {
        boolean validSave = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        
        SalesAmount sa = new SalesAmount();
        sa.setActive(true);
        sa.setAmount(amount);
        sa.setBranchId(Integer.valueOf(branchId+""));
        try {
            sa.setDateEnd(dateFormat.parse(dateEnd));
            sa.setDateStart(dateFormat.parse(dateStart));
        } catch (ParseException ex) {
            Logger.getLogger(SaleHelper.class.getName()).log(Level.SEVERE, null, ex);
            
        }
        sa.setUserId(Integer.parseInt(userId+""));
        try {
            tx.begin();
            session.save(sa);
            session.flush();
            tx.commit();
            
            validSave = true;
            
            session.close();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        
        return validSave;
    }
}
