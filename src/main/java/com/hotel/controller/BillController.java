package com.hotel.controller;

import com.hotel.entity.Bill;
import com.hotel.entity.RoomConsumption;
import com.hotel.service.BillService;
import com.hotel.service.CheckinRecordService;
import com.hotel.service.RoomConsumptionService;
import com.hotel.vo.Result;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账单控制器
 * 处理与账单相关的HTTP请求，包括账单列表查询、详情查询、生成账单、支付等功能
 */
@RestController
@RequestMapping("/api/bill")
public class BillController {

    // 注入账单服务
    private final BillService billService;
    // 注入入住记录服务
    private final CheckinRecordService checkinRecordService;
    // 注入房间消费服务
    private final RoomConsumptionService roomConsumptionService;

    /**
     * 构造函数，通过依赖注入初始化服务
     * @param billService 账单服务
     * @param checkinRecordService 入住记录服务
     * @param roomConsumptionService 房间消费服务
     */
    public BillController(BillService billService, CheckinRecordService checkinRecordService,
                          RoomConsumptionService roomConsumptionService) {
        this.billService = billService;
        this.checkinRecordService = checkinRecordService;
        this.roomConsumptionService = roomConsumptionService;
    }

    /**
     * 获取所有账单列表
     * @return 返回包含所有账单的Result对象
     */
    @GetMapping("/list")
    public Result<List<Bill>> list() {
        return Result.success(billService.listBills());
    }

    /**
     * 根据ID获取账单详情
     * @param id 账单ID
     * @return 返回包含指定账单的Result对象
     */
    @GetMapping("/detail/{id}")
    public Result<Bill> detail(@PathVariable Long id) {
        return Result.success(billService.getById(id));
    }

    /**
     * 根据入住ID获取入住记录、消费记录和账单信息
     * @param checkinId 入住ID
     * @return 返回包含入住记录、消费记录和账单的Map对象
     */
    @GetMapping("/byCheckin/{checkinId}")
    public Result<Map<String, Object>> byCheckin(@PathVariable Long checkinId) {
        Map<String, Object> result = new HashMap<>();
        result.put("record", checkinRecordService.getDetail(checkinId));
        result.put("consumptions", roomConsumptionService.listByCheckinId(checkinId));
        Bill bill = billService.getBillByCheckinId(checkinId);
        result.put("bill", bill);
        return Result.success(result);
    }

    /**
     * 生成账单并办理退房
     * 注意：先生成账单，再办理退房，避免循环依赖
     * @param checkinId 入住ID
     * @return 返回生成的账单
     */
    @PostMapping("/generate/{checkinId}")
    public Result<Bill> generate(@PathVariable Long checkinId) {
        // 先生成账单，再办理退房，避免循环依赖
        billService.generateBill(checkinId);
        checkinRecordService.checkout(checkinId);
        return Result.success(billService.getBillByCheckinId(checkinId));
    }

    /**
     * 支付账单
     * @param billId 账单ID
     * @param paidAmount 支付金额
     * @return 返回支付结果
     */
    @PostMapping("/pay")
    public Result<Boolean> pay(@RequestParam Long billId, @RequestParam BigDecimal paidAmount) {
        billService.payBill(billId, paidAmount);
        return Result.success(true);
    }

    /**
     * 保存房间消费记录
     * @param consumption 房间消费对象
     * @return 返回保存结果
     */
    @PostMapping("/consumption/save")
    public Result<Boolean> saveConsumption(@RequestBody RoomConsumption consumption) {
        return Result.success(roomConsumptionService.save(consumption));
    }

    /**
     * 删除房间消费记录
     * @param id 消费记录ID
     * @return 返回删除结果
     */
    @DeleteMapping("/consumption/delete/{id}")
    public Result<Boolean> deleteConsumption(@PathVariable Long id) {
        return Result.success(roomConsumptionService.removeById(id));
    }
}
