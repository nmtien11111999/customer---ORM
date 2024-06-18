package customer.services;

import customer.models.Customer;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Service
public class CustomerService implements ICustomerService {
    private static SessionFactory sessionFactory;
    private static EntityManager entityManager;

    static {
        try {
            sessionFactory = new Configuration()
                    .configure("hibernate.conf.xml")
                    .buildSessionFactory();
            entityManager = sessionFactory.createEntityManager();
        } catch (HibernateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Customer> findAll() {
        String queryStr = "select c from Customer as c";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        return query.getResultList();
    }

    @Override
    public void save(Customer customer) {
        // khai báo 1 biến để quản lý giao dịch
        Transaction transaction = null;
        // đối tượng customer sẽ được lưu hoă cập nhật
        Customer origin;
        // kiê tra xem đối tượng customer có tồn tại hay không
        if (customer.getId() == 0) {
            // nếu chưa tồn tại thì tạo 1 customer mới
            origin = new Customer();
        } else {
            // nếu đã tô tại thì gán cho origin là đối tượng đó
            origin = findById(customer.getId());
        }
        // làm việc với csdl
        try (Session session = sessionFactory.openSession()) {
            // bắt đầu 1 giao dịch mới
            transaction = session.beginTransaction();
            // cập nhật thông tin đối tượng
            origin.setName(customer.getName());
            origin.setEmail(customer.getEmail());
            origin.setAddress(customer.getAddress());
            // lưu hoặc cập nhật thông tin đối tượng
            session.saveOrUpdate(origin);
            // xác nhận giao dịch
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public Customer findById(int id) {
        String queryStr = "SELECT c FROM Customer c WHERE c.id = :id";
        TypedQuery<Customer> query = entityManager.createQuery(queryStr, Customer.class);
        query.setParameter("id", id);
        return query.getSingleResult();
    }

    @Override
    public void remove(int id) {
        Customer customer = findById(id);
        if (customer != null) {
            Transaction transaction;
            try (Session session = sessionFactory.openSession()) {
                transaction = session.beginTransaction();
                session.remove(customer);
                transaction.commit();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}