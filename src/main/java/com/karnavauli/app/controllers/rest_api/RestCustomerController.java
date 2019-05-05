package com.karnavauli.app.controllers.rest_api;

import com.karnavauli.app.model.dto.CustomerDto;
import com.karnavauli.app.model.jwt.Info;
import com.karnavauli.app.service.CustomerService;
import com.karnavauli.app.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Set;

@RestController
public class RestCustomerController {
    private CustomerService customerService;
    private UserService userService;

    public RestCustomerController(CustomerService customerService, UserService userService) {
        this.customerService = customerService;
        this.userService = userService;
    }

    @GetMapping("/user/show-your-customers")
    public Set<CustomerDto> showUsersCustomers(Principal principal) {
        return customerService.getCustomersSoldByUser(userService.getUserDtoFromUsername(principal.getName()));
    }

    @GetMapping("/admin/show-all-customers")
    public Set<CustomerDto> showAllCustomers() {
        return customerService.getAll();
    }

    @PostMapping("/user/add-customer")
    public Info addCustomer(@RequestBody CustomerDto customerDto) {
        customerService.addCustomer(customerDto);
        return Info.builder().message("Success! customer [" + customerDto.getName() + "] added!").build();
    }

    //todo reszta
}
