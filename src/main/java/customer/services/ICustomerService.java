package customer.services;

import customer.models.Customer;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ICustomerService {
    List<Customer> findAll();

    void save(Customer customer);

    Customer findById(int id);

    void remove(int id);
}
