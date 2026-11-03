package com.hotel.controller;

import com.hotel.entity.BookingOrder;
import com.hotel.entity.Customer;
import com.hotel.service.BookingOrderService;
import com.hotel.service.CustomerService;
import com.hotel.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 预订订单控制器
 * 处理与预订订单相关的HTTP请求，包括列表查询、详情查看、保存、分配和取消等功能
 */
@RestController
@RequestMapping("/api/booking")
public class BookingOrderController {

    // 注入预订订单服务
    private final BookingOrderService bookingOrderService;
    // 注入客户服务
    private final CustomerService customerService;

    /**
     * 构造函数，通过依赖注入的方式初始化服务
     * @param bookingOrderService 预订订单服务
     * @param customerService 客户服务
     */
    public BookingOrderController(BookingOrderService bookingOrderService, CustomerService customerService) {
        this.bookingOrderService = bookingOrderService;
        this.customerService = customerService;
    }

    /**
     * 获取预订订单列表
     * @return 返回所有预订订单列表
     */
    @GetMapping("/list")
    public Result<List<BookingOrder>> list() {
        return Result.success(bookingOrderService.listOrders());
    }

    /**
     * 获取预订订单详情
     * @param id 订单ID
     * @return 返回指定ID的订单详情
     */
    @GetMapping("/detail/{id}")
    public Result<BookingOrder> detail(@PathVariable Long id) {
        return Result.success(bookingOrderService.getOrderDetail(id));
    }

    /**
     * 保存预订订单
     * 如果客户ID为空，则先创建或获取客户信息
     * @param order 预订订单信息
     * @return 返回保存后的订单信息
     */
    @PostMapping("/save")
    public Result<BookingOrder> save(@RequestBody BookingOrder order) {
        if (order.getCustomerId() == null) {
            Customer customer = customerService.getOrCreateCustomer(order.getCustomerName(), order.getCustomerPhone(), null);
            order.setCustomerId(customer.getId());
        }
        return Result.success(bookingOrderService.createOrder(order));
    }

    /**
     * 分配房间给订单
     * @param orderId 订单ID
     * @param roomId 房间ID
     * @return 返回操作是否成功
     */
    @PostMapping("/assign")
    public Result<Boolean> assign(@RequestParam Long orderId, @RequestParam Long roomId) {
        bookingOrderService.assignRoom(orderId, roomId);
        return Result.success(true);
    }

    /**
     * 取消预订订单
     * @param id 订单ID
     * @return 返回操作是否成功
     */
    @PostMapping("/cancel/{id}")
    public Result<Boolean> cancel(@PathVariable Long id) {
        bookingOrderService.cancelOrder(id);
        return Result.success(true);
    }
}
