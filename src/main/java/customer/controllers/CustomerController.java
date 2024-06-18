package customer.controllers;

import customer.models.Customer;
import customer.services.ICustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/customer")
public class CustomerController {
    @Autowired
    private ICustomerService customerService;

    @GetMapping("/list")
    public String list(ModelMap modelMap) {
        List<Customer> customers = customerService.findAll();
        modelMap.addAttribute("customers", customers);
        return "list";
    }

    @GetMapping("/create")
    public String add(ModelMap modelMap) {
        modelMap.addAttribute("customer", new Customer());
        return "create";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute Customer customer) {
        customerService.save(customer);
        return "redirect:/customer/list";
    }

    @PostMapping("/delete/{id}")
    public String delete(@PathVariable int id) {
        customerService.remove(id);
        return "redirect:/customer/list";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable int id, ModelMap modelMap) {
        modelMap.addAttribute("customer", customerService.findById(id));
        return "edit";
    }

    @PostMapping("/edit")
    public String update(@ModelAttribute Customer customer) {
        customerService.save(customer);
        return "redirect:/customer/list";
    }

    @GetMapping("/view/{id}")
    public String view(@PathVariable int id, ModelMap modelMap) {
        modelMap.addAttribute("customer", customerService.findById(id));
        return "view";
    }
}