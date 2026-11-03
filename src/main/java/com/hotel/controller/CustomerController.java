package com.hotel.controller;

import com.hotel.entity.Customer;
import com.hotel.service.CustomerService;
import com.hotel.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 客户控制器类
 * 处理客户相关的HTTP请求，提供客户数据的CRUD操作接口
 */
@RestController
@RequestMapping("/api/customer")
public class CustomerController {

    /**
     * 客户服务接口，用于处理业务逻辑
     */
    private final CustomerService customerService;

    /**
     * 构造函数，通过依赖注入方式注入CustomerService
     * @param customerService 客户服务接口实例
     */
    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    /**
     * 获取客户列表接口
     * @return 返回所有客户数据的Result封装对象
     */
    @GetMapping("/list")
    public Result<List<Customer>> list() {
        return Result.success(customerService.list());
    }

    /**
     * 根据ID获取客户详情接口
     * @param id 客户ID
     * @return 返回指定ID的客户数据的Result封装对象
     */
    @GetMapping("/detail/{id}")
    public Result<Customer> detail(@PathVariable Long id) {
        return Result.success(customerService.getById(id));
    }

    /**
     * 保存或更新客户信息接口
     * @param customer 客户数据对象
     * @return 返回操作是否成功的Result封装对象
     */
    @PostMapping("/save")
    public Result<Boolean> save(@RequestBody Customer customer) {
        return Result.success(customerService.saveOrUpdate(customer));
    }

    /**
     * 根据ID删除客户接口
     * @param id 客户ID
     * @return 返回操作是否成功的Result封装对象
     */
    @DeleteMapping("/delete/{id}")
    public Result<Boolean> delete(@PathVariable Long id) {
        return Result.success(customerService.removeById(id));
    }
}
