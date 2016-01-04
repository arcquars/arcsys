/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.arc.sys.business;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import org.arc.sys.crypt.AESCrypt;
import org.arc.sys.dto.EmployeeDto;
import org.arc.sys.dto.KeyValue;
import org.arc.sys.dto.employee.EmployeeSimpleDto;
import org.arc.sys.dto.translaters.EmployeeTranslater;
import org.arc.sys.hibernate.entities.Branch;
import org.arc.sys.hibernate.entities.Employee;
import org.arc.sys.hibernate.entities.HibernateUtil;
import org.arc.sys.hibernate.entities.Person;
import org.arc.sys.hibernate.entities.Rol;
import org.arc.sys.hibernate.entities.User;
import org.arc.sys.hibernate.entities.UserRol;
import org.arc.sys.hibernate.entities.UserRolId;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author angel
 */
public class EmployeeHelper implements EmployeeDAO {

    @Override
    public boolean createEmployee(EmployeeDto employee) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        boolean valSave;
        try {
            Person personSave = new Person();
            personSave.setAddress(employee.getAddress());
            personSave.setCi(employee.getCi());
            personSave.setDel(employee.isDel());
            personSave.setEmail(employee.getEmail());
            personSave.setFirstname(employee.getFirstname());
            personSave.setLastname(employee.getLastname());
            personSave.setNames(employee.getNames());
            personSave.setPhoneAddress(employee.getPhoneAddress());
            personSave.setPhoneMobil(employee.getPhoneMobil());

            Branch branchSave = getBranchById(employee.getBranchId());

            Employee employeeSave = new Employee(personSave, branchSave, employee.getPosition(), employee.isDel());

            String ee = "";
            try {
                Properties p = new Properties();
                InputStream in = this.getClass()
                        .getClassLoader()
                        .getResourceAsStream("arcsys.properties");
                p.load(in);

                ee = AESCrypt.encrypt(employee.getPassword(), p.getProperty("aes"));
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }

            User user = new User();
            user.setDel(false);
            user.setLogin(employee.getCi() + "");
            user.setPassword(ee);
            user.setPerson(personSave);

            //System.out.println("EmplId:::: "+personSave.getPerId().toString());
            tx = session.getTransaction();
            tx.begin();

            session.save(employeeSave);
            session.save(user);
            valSave = true;
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            valSave = false;
            e.printStackTrace();
        } finally {
            session.close();
        }
        return valSave;
    }

    @Override
    public Branch getBranchById(int branchId) {
        Branch branch = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select branch from Branch as branch where branch.branchId=:branchId and branch.del=0");
            q.setParameter("branchId", branchId);

            branch = (Branch) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return branch;
    }

    @Override
    public boolean updateEmployeed(EmployeeDto employeedDto) {
        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = null;
        Person person = null;
        Employee employee = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            person = new Person();
            person.setAddress(employeedDto.getAddress());
            person.setCi(employeedDto.getCi());
            person.setDel(employeedDto.isDel());
            person.setEmail(employeedDto.getEmail());
            person.setFirstname(employeedDto.getFirstname());
            person.setLastname(employeedDto.getLastname());
            person.setNames(employeedDto.getNames());
            person.setPerId(employeedDto.getPerId());
            person.setPhoneAddress(employeedDto.getPhoneAddress());
            person.setPhoneMobil(employeedDto.getPhoneMobil());

            employee = new Employee();
            employee.setBranch(this.getBranchById(employeedDto.getBranchId(), session));
            employee.setDel(employeedDto.isDel());
            employee.setEmpId(new Integer(getEmpIdByPerId(person.getPerId(), session)));
            employee.setPerson(person);
            employee.setPosition(employeedDto.getPosition());
            //session.saveOrUpdate(employee);
            employee = (Employee) session.merge(employee);
            session.update(employee);

            session.flush();
            tx.commit();

        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            return false;
        } finally {
            session.close();
        }
        return true;
    }

    public Branch getBranchById(int branchId, Session session) {
        Branch branch = null;
        try {
            Query q = session.createQuery("select branch from Branch as branch where branch.branchId=:branchId and branch.del=0");
            q.setParameter("branchId", branchId);

            branch = (Branch) q.uniqueResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return branch;
    }

    public int getEmpIdByPerId(int perId, Session session) {
        Employee employee = null;
        int empId = 0;
        try {
            Query q = session.createQuery("select employee from Employee employee where employee.person.perId = " + perId);

            employee = (Employee) q.uniqueResult();
            empId = employee.getEmpId();
        } catch (Exception e) {
            e.printStackTrace();
            return empId;
        }
        return empId;
    }

    @Override
    public List<EmployeeSimpleDto> getAllVendors() {
        List<EmployeeSimpleDto> allVendor = null;
        Session session = HibernateUtil.getSessionFactory().openSession();
        try {
            Query q = session.createQuery("select employee from Employee as employee where employee.del=0");

            List<Employee> list = q.list();
            System.out.println("uuuuuuuuuuuuuuuuuuuuuuuuu:: " + list.size());
            allVendor = EmployeeTranslater.translateEntityToDto(list);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            session.close();
        }
        return allVendor;
    }

    @Override
    public boolean ciExistEmployee(int ci) {
        Employee employee = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        boolean exist = false;
        try {
            session.beginTransaction();
            Query q = session.createQuery("select employee from Employee employee where employee.person.ci = " + ci);

            employee = (Employee) q.uniqueResult();
            if (employee != null) {
                exist = true;
            }
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return exist;
    }

    @Override
    public boolean createEmployee(org.arc.sys.dto.employee.EmployeeDto employee) {
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        boolean valSave;
        try {
            Person personSave = new Person();
            personSave.setAddress(employee.getAddress());
            personSave.setCi(employee.getCi());
            personSave.setDel(false);
            personSave.setEmail(employee.getEmail());
            personSave.setFirstname(employee.getFirstname());
            personSave.setLastname(employee.getLastname());
            personSave.setNames(employee.getNames());
            if (employee.getPhone_address() != "") {
                personSave.setPhoneAddress(Integer.parseInt(employee.getPhone_address()));
            }
            if (employee.getPhone_mobil() != "") {
                personSave.setPhoneMobil(Integer.parseInt(employee.getPhone_mobil()));
            }

            Branch branchSave = getBranchById(employee.getBranch());

            tx = session.getTransaction();
            tx.begin();

            Query qRols = session.createQuery("select rol from Rol as rol where rol.del=0 and rol.rolId=" + employee.getPosition());
            Rol rol = (Rol) qRols.uniqueResult();

            Query qPerson = session.createQuery("select person from Person as person where person.ci=" + employee.getCi());
            Person person = (Person) qPerson.uniqueResult();

            if (person != null) {
                person.setAddress(personSave.getAddress());
                person.setCi(personSave.getCi());
                person.setDel(personSave.getDel());
                person.setEmail(personSave.getEmail());
                person.setFirstname(personSave.getFirstname());
                person.setLastname(personSave.getLastname());
                person.setNames(personSave.getNames());
                person.setPhoneAddress(personSave.getPhoneAddress());
                person.setPhoneMobil(personSave.getPhoneMobil());

                session.saveOrUpdate(person);
                personSave = person;
            } else {
                session.saveOrUpdate(personSave);
            }

            Employee employeeSave = new Employee(personSave, branchSave, rol.getNameRol(), false);

            session.saveOrUpdate(employeeSave);

            User user = new User(personSave, personSave.getCi() + "", employee.getPassword(), true);
            session.saveOrUpdate(user);

            UserRol userRol = new UserRol(new UserRolId(user.getUserId(), rol.getRolId()));
            session.saveOrUpdate(userRol);

            valSave = true;
            tx.commit();
        } catch (Exception e) {
            if (tx != null) {
                tx.rollback();
            }
            valSave = false;
            e.printStackTrace();
        } finally {
//            session.close();
        }
        return valSave;
    }

    @Override
    public List<KeyValue> getAllRols() {
        List<KeyValue> mapFactory = new ArrayList<KeyValue>();
        KeyValue kv = null;
        Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        Transaction tx = null;
        try {
            tx = session.getTransaction();
            tx.begin();
            Query q = session.createQuery("select rol from Rol as rol where rol.del=0 order by rol.rolId");

            List<Rol> listRol = (List<Rol>) q.list();
            Iterator<Rol> iterable = listRol.iterator();
            while (iterable.hasNext()) {
                Rol rol = iterable.next();
                kv = new KeyValue();
                kv.setKey(rol.getRolId().toString());
                kv.setValue(rol.getNameRol());
                mapFactory.add(kv);

            }

            tx.commit();
            //System.out.println("Tamaño de la lista de categorias:::::: "+category.size());
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
        }

        return mapFactory;
    }

}
