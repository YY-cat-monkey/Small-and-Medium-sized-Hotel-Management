package com.hotel.controller;

import com.hotel.entity.CheckinRecord;
import com.hotel.entity.Customer;
import com.hotel.service.CheckinRecordService;
import com.hotel.service.CustomerService;
import com.hotel.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 签到记录控制器
 * 处理签到相关的HTTP请求，包括查询签到记录、获取详情、保存签到记录等操作
 */
@RestController
@RequestMapping("/api/checkin")
public class CheckinRecordController {

    // 注入签到记录服务
    private final CheckinRecordService checkinRecordService;
    // 注入客户服务
    private final CustomerService customerService;

    /**
     * 构造函数
     * @param checkinRecordService 签到记录服务
     * @param customerService 客户服务
     */
    public CheckinRecordController(CheckinRecordService checkinRecordService, CustomerService customerService) {
        this.checkinRecordService = checkinRecordService;
        this.customerService = customerService;
    }

    /**
     * 获取签到记录列表
     * @param status 签到状态，可选参数
     * @return 返回签到记录列表的响应结果
     */
    @GetMapping("/list")
    public Result<List<CheckinRecord>> list(@RequestParam(required = false) Integer status) {
        return Result.success(checkinRecordService.listCheckinRecords(status));
    }

    /**
     * 获取签到记录详情
     * @param id 签到记录ID
     * @return 返回签到记录详情的响应结果
     */
    @GetMapping("/detail/{id}")
    public Result<CheckinRecord> detail(@PathVariable Long id) {
        return Result.success(checkinRecordService.getDetail(id));
    }

    /**
     * 保存签到记录
     * @param record 签到记录对象
     * @return 返回保存后的签到记录响应结果
     */
    @PostMapping("/save")
    public Result<CheckinRecord> save(@RequestBody CheckinRecord record) {
        // 如果客户ID为空，则创建或获取客户信息
        if (record.getCustomerId() == null) {
            Customer customer = customerService.getOrCreateCustomer(record.getCustomerName(), record.getCustomerPhone(), record.getIdCard());
            record.setCustomerId(customer.getId());
        }
        return Result.success(checkinRecordService.checkin(record));
    }

    /**
     * 从预订信息创建签到记录
     * @param bookingId 预订ID
     * @return 返回签到记录的响应结果
     */
    @PostMapping("/fromBooking")
    public Result<CheckinRecord> fromBooking(@RequestParam Long bookingId) {
        return Result.success(checkinRecordService.checkinFromBooking(bookingId));
    }
}
