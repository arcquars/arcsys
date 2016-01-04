/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.util.*;
import org.arc.sys.dto.DeliveryBranch;
import org.arc.sys.dto.DeliveryProducts;
import org.arc.sys.dto.ProductDetailDto;
import org.arc.sys.dto.ProductDto;
import org.arc.sys.dto.buy.RecordProductDto;
import org.arc.sys.dto.translaters.ProductTranslater;
import org.arc.sys.hibernate.entities.*;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class ProductHelper implements ProductDAO {

    @Override
    public boolean productCreate(ProductDto productDto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Product productSave = ProductTranslater.translateDtoToEntities(productDto);
        Category category = this.getCategoryById(productDto.getCategoryId().intValue());
        Factory factory = this.getFactoryById(productDto.getFactoryId());

        RecordProducto rp = new RecordProducto();
        rp.setActive("active");
        rp.setAmount(new Integer(0));
        rp.setDate(new Date());
        rp.setDel(false);
        rp.setProduct(productSave);
        rp.setTotal(new Float(0));

        productSave.setCategory(category);
        productSave.setFactory(factory);
        
        Transaction tx = null;
        boolean valSave;
        try {
            tx = session.getTransaction();
            tx.begin();

            session.save(productSave);
            
            PriceSale ps = new PriceSale();
            ps.setActive(true);
            ps.setPrice(Math.round(productDto.getPriceSale()*100)/100.00);
            ps.setProduct(productSave);
            
            session.save(ps);
            rp.setProduct(productSave);
            session.save(rp);
            session.flush();
            valSave = true;
            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (tx != null) {
                tx.rollback();
            }
            valSave = false;
        } finally {
            session.close();
        }
        return valSave;
    }

    @Override
    public Product getProductById(int proId) {
        Product product = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
            q.setParameter("proId", proId);

            product = (Product) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return product;
    }

    @Override
    public List<ProductDetailDto> getListPersonByName(int type, String criteria) {
        List<Product> readProductList = null;
        List<ProductDetailDto> lpdDto = null;
        StringBuffer queryString = new StringBuffer("from Product where del = 0 and ");
        try {
            switch (type) {
                case 1:
                    queryString.append("name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 2:
                    queryString.append(" cod_origin like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;
                case 3:
                    int catId = getCategoryByName(criteria);
                    queryString.append(" category_id = ");
                    queryString.append(catId);
                    break;
                default:
                    queryString.append("name like '%");
                    queryString.append(criteria);
                    queryString.append("%'");
                    break;

            }
            Session session = HibernateUtil.getSessionFactory().getCurrentSession();
            session.beginTransaction();
            Query q = session.createQuery(queryString.toString());
            //q.setParameter("criteria", criteria);
            readProductList = q.list();
            lpdDto = ProductTranslater.translateListEntityToDto(readProductList);
            session.getTransaction().commit();
            //session.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return lpdDto;
    }

    @Override
    public Category getCategoryById(int categoryId) {
        Category category = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select category from Category as category where category.categoryId like '" + categoryId + "%' and category.del=0");

            category = (Category) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return category;
    }

    @Override
    public int getCategoryByName(String nameCategory) {
        Category category = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select category from Category as category where category.categoryName like '" + nameCategory + "%' and category.del=0");

            category = (Category) q.uniqueResult();
            System.out.println("Codigo id:::::: " + category.getCategoryId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        if (category != null) {
            return category.getCategoryId();
        }
        return -1;
    }

    @Override
    public Map<String, String> getCategoryAll() {
        Map<String, String> mapCategory = new HashMap<String, String>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select category from Category as category where category.del=0");

            List<Category> category = q.list();
            System.out.println("Tamaño de la lista de categorias:::::: " + category.size());
            Iterator<Category> iCategory = category.iterator();
            Category catAux = null;
            while (iCategory.hasNext()) {
                catAux = iCategory.next();
                mapCategory.put(catAux.getCategoryId().toString(), catAux.getCategoryName());

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }

        return mapCategory;
    }

    @Override
    public boolean deleteProduct(int pro_id) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean validDelete = false;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
            q.setParameter("proId", pro_id);

            Product product = (Product) q.uniqueResult();

            if (product != null) {
                product.setDel(Boolean.TRUE);
                product = (Product) session.merge(product);
                session.saveOrUpdate(product);
                validDelete = true;
            }

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
        return validDelete;
    }

    @Override
    public void updateProduct(Product product) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            product = (Product) session.merge(product);
            session.saveOrUpdate(product);

            session.flush();
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public boolean validName(String newName) {
        boolean validName = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select product from Product as product where product.name like '" + newName + "' and product.del=0");

            Product product = (Product) q.uniqueResult();
            tx.commit();
            if (product != null) {
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
    public boolean validNameEdit(String newName, int prodId) {
        boolean validName = false;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();

            Query q = session.createQuery("select product from Product as product where product.name like '" + newName + "' and product.del=0 and product.productId <> "+prodId);
            System.out.println("oooooooooooooooooooooooo:: "+q);
            Product product = (Product) q.uniqueResult();
            tx.commit();
            if (product != null) {
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
    public Factory getFactoryById(int factoryId) {
        Factory factory = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select factory from Factory as factory where factory.factoryId like '" + factoryId + "%' and factory.del=0");

            factory = (Factory) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return factory;

    }

    @Override
    public List<RecordProductDto> getListProductBuy(String ids) {
        String[] idsArray = ids.split(",");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        List<RecordProductDto> listRpd = new ArrayList<RecordProductDto>();
        RecordProductDto rpd = null;
        
        for (int i = 0; i < idsArray.length; i++) {
            String string = idsArray[i];
            try {
                tx = session.getTransaction();
                tx.begin();

                Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
                q.setParameter("proId", string);
                Product product = (Product) q.uniqueResult();

                System.out.println("2");
                if (product != null) {
                    System.out.println("3");
                    rpd = new RecordProductDto();
                    rpd.setAmount(0);
                    rpd.setCategoryId(product.getCategory().getCategoryId().intValue());
                    rpd.setCategoryName(product.getCategory().getCategoryName());
                    rpd.setCodOrigin(product.getCodOrigin());
                    rpd.setFactoryId(product.getFactory().getFactoryId());
                    rpd.setFactoryName(product.getFactory().getName());
                    rpd.setProductName(product.getName());
                    rpd.setDetail(product.getDescription());
                    rpd.setProductId(product.getProductId().intValue());
                    rpd.setRpId(0);
                    rpd.setTotal(0);
                    rpd.setPrice(0);
                    listRpd.add(rpd);
                }
                System.out.println("4");
                session.flush();
                tx.commit();
            } catch (Exception e) {
                e.printStackTrace();
                if (tx != null) {
                    tx.rollback();
                }
            }
            System.out.println("5");
        }
        System.out.println("6");
        return listRpd;
    }
    
    public static Product getProductByIdStatic(int proId) {
        Product product = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.getTransaction();
        try {
            tx.begin();
            Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
            q.setParameter("proId", proId);
            product = (Product) q.uniqueResult();
            tx.commit();
            session.close();
            
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        }
        return product;
    }
    
    public static Product getProductByIdStatic(int proId, Session session) {
        Product product = null;
        try {
            Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
            q.setParameter("proId", proId);
            product = (Product) q.uniqueResult();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return product;
    }
    
    public DeliveryBranch getListProductDelivery(String ids) {
        System.out.println("dis::: "+ids);
        String[] idsArray = ids.split(",");
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        
        List<DeliveryProducts> listRpd = new ArrayList<DeliveryProducts>();
        DeliveryProducts dp = null;
        DeliveryBranch rpd = new DeliveryBranch();
        rpd.setBranchId(0);
        
        for (int i = 0; i < idsArray.length; i++) {
            String string = idsArray[i];
            try {
                tx = session.getTransaction();
                tx.begin();

                Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
                q.setParameter("proId", string);
                Product product = (Product) q.uniqueResult();
                
                q = session.createQuery("select MAX(rp.rpId) from RecordProducto as rp where rp.product.productId="+string);
                
                Integer rpIdMAx = (Integer) q.uniqueResult();
                
                q = session.createQuery("select rp from RecordProducto as rp where rp.rpId="+rpIdMAx);
                
                RecordProducto rp = (RecordProducto) q.uniqueResult();

                if (product != null) {
                    dp = new DeliveryProducts();
                    dp.setAmount(0);
                    dp.setAvailableStock(rp.getTotal());
                    dp.setCategoryName(product.getCategory().getCategoryName());
                    dp.setCodOrigin(product.getCodOrigin());
                    dp.setFactoryName(product.getFactory().getName());
                    dp.setProductId(product.getProductId());
                    dp.setProductName(product.getName());
                    
                    listRpd.add(dp);
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
        rpd.setDeliveryProduct(listRpd);
        return rpd;
    }

    @Override
    public ProductDto getProductDtoById(int prodId) {
        Product product = null;
        PriceSale price = null;
        ProductDto pDto = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select product from Product as product where product.productId=:proId and product.del=0");
            q.setParameter("proId", prodId);
            
            Query q1 = session.createQuery("select max(price) from PriceSale as price where price.product.productId=:proId ");
            q1.setParameter("proId", prodId);

            product = (Product) q.uniqueResult();
            price = (PriceSale) q1.uniqueResult();
            pDto = ProductTranslater.translateEntitiesToDto(product);
            if(price != null)
                pDto.setPriceSale(price.getPrice());
            else
                pDto.setPriceSale(0);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return pDto;
    }

    @Override
    public void updateProductDto(ProductDto productDto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        Category category = this.getCategoryById(productDto.getCategoryId().intValue());
        Factory factory = this.getFactoryById(productDto.getFactoryId());
        
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            Product product = new Product();
            
            product.setCategory(category);
            product.setCodOrigin(productDto.getCodOrigin());
            product.setDel(Boolean.FALSE);
            product.setDescription(productDto.getDescription());
            product.setFactory(factory);
            product.setName(productDto.getName());
            product.setProductId(productDto.getProductId());
            
            product = (Product) session.merge(product);
            session.saveOrUpdate(product);

            //PriceSale priceSale = getPriceByProdId(productDto.getProductId());
//            if(priceSale != null)
//                priceSale.setPrice(productDto.getPriceSale());
//            else{
//                priceSale = new PriceSale();
//                priceSale.setActive(true);
//                priceSale.setPrice(productDto.getPriceSale());
//                priceSale.setProduct(product);
//            }
            
            PriceSale priceSale = new PriceSale();
            priceSale.setActive(true);
            priceSale.setPrice(Math.round(productDto.getPriceSale()*100)/100.00);
            priceSale.setProduct(product);
            
            session.saveOrUpdate(priceSale);
            
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
    }

    @Override
    public PriceSale getPriceByProdId(int productId) {
        PriceSale ps = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select max(priceSale) from PriceSale as priceSale where priceSale.product.productId = " + productId);

            ps = (PriceSale) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return ps;
    }

    @Override
    public List<Double> getDatesPriceByProductId(int productId) {
        List<Double> dates = new ArrayList<Double>();
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select Max(priceSale.price), AVG(priceSale.price), Min(priceSale.price) from PriceSale as priceSale where priceSale.product.productId =:proId");
            q.setParameter("proId", productId);

            Object[] result = (Object[]) q.uniqueResult();

            try{
                /**
                dates[0] = Double.parseDouble(result[0].toString());
                dates[1] = Double.parseDouble(result[1].toString());
                dates[2] = Double.parseDouble(result[2].toString());
                dates[0] = Math.rint(dates[0]*100)/100;
                dates[1] = Math.rint(dates[1]*100)/100;
                dates[2] = Math.rint(dates[2]*100)/100;
                * /
                **/
                dates.add(Math.rint(Double.parseDouble(result[0].toString())*100)/100);
                dates.add(Math.rint(Double.parseDouble(result[1].toString())*100)/100);
                dates.add(Math.rint(Double.parseDouble(result[2].toString())*100)/100);
            }catch(NullPointerException e){
                e.printStackTrace();
                System.out.println("nunca se hiso una compra en este producto.3");
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return dates;
    }
    
    
}
