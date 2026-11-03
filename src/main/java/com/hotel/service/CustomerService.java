package com.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.entity.Customer;

public interface CustomerService extends IService<Customer> {

    Customer getOrCreateCustomer(String name, String phone, String idCard);
}
