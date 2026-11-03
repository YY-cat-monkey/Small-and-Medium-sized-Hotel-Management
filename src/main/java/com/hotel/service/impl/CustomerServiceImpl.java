package com.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.entity.Customer;
import com.hotel.mapper.CustomerMapper;
import com.hotel.service.CustomerService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * 客户服务实现类
 * 继承ServiceImpl并实现CustomerService接口，提供客户相关的业务逻辑实现
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements CustomerService {

    /**
     * 获取或创建客户方法
     * 根据手机号查询客户，如果存在则更新客户信息，如果不存在则创建新客户
     *
     * @param name    客户姓名
     * @param phone   客户手机号
     * @param idCard  客户身份证号
     * @return 返回处理后的客户对象
     */
    @Override
    public Customer getOrCreateCustomer(String name, String phone, String idCard) {
        // 根据手机号查询客户
        Customer customer = baseMapper.selectByPhone(phone);
        if (customer != null) {
            // 如果客户存在，更新客户信息
            customer.setName(name);
            customer.setIdCard(idCard);
            updateById(customer);
            return customer;
        }
        // 如果客户不存在，创建新客户
        customer = new Customer();
        customer.setName(name);
        customer.setPhone(phone);
        customer.setIdCard(idCard);
        customer.setCreateTime(LocalDateTime.now()); // 设置创建时间为当前时间
        save(customer); // 保存新客户信息
        return customer;
    }
}
