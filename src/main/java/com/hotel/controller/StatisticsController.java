package com.hotel.controller;

import com.hotel.entity.CheckinRecord;
import com.hotel.service.BillService;
import com.hotel.service.CheckinRecordService;
import com.hotel.service.RoomService;
import com.hotel.vo.Result;
import com.hotel.vo.RoomStatusVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 统计数据控制器
 * 提供酒店各类统计数据的API接口
 */
@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    // 房间服务注入
    private final RoomService roomService;
    // 入住记录服务注入
    private final CheckinRecordService checkinRecordService;
    // 账单服务注入
    private final BillService billService;

    /**
     * 构造函数，注入所需服务
     * @param roomService 房间服务
     * @param checkinRecordService 入住记录服务
     * @param billService 账单服务
     */
    public StatisticsController(RoomService roomService, CheckinRecordService checkinRecordService, BillService billService) {
        this.roomService = roomService;
        this.checkinRecordService = checkinRecordService;
        this.billService = billService;
    }

    /**
     * 获取仪表盘统计数据
     * @return 包含房间状态、今日入住数、在住人数和总营收的统计结果
     */
    @GetMapping("/dashboard")
    public Result<Map<String, Object>> dashboard() {
        // 创建结果集
        Map<String, Object> result = new HashMap<>();

        // 房间状态统计
        List<RoomStatusVO> stats = roomService.getStatusStats();
        result.put("roomStatus", stats);

        // 今日入住数
        LocalDateTime start = LocalDateTime.of(LocalDate.now(), LocalTime.MIN);
        LocalDateTime end = LocalDateTime.of(LocalDate.now(), LocalTime.MAX);
        List<CheckinRecord> todayList = checkinRecordService.lambdaQuery()
                .ge(CheckinRecord::getCheckinTime, start)
                .le(CheckinRecord::getCheckinTime, end)
                .list();
        result.put("todayCheckin", todayList.size());

        // 在住人数
        List<CheckinRecord> inHouseList = checkinRecordService.listCheckinRecords(0);
        result.put("inHouseCount", inHouseList.size());

        // 总营收
        BigDecimal totalRevenue = BigDecimal.ZERO;
        for (com.hotel.entity.Bill bill : billService.listBills()) {
            if (bill.getStatus() != null && bill.getStatus() == 1 && bill.getPaidAmount() != null) {
                totalRevenue = totalRevenue.add(bill.getPaidAmount());
            }
        }
        result.put("totalRevenue", totalRevenue);

        return Result.success(result);
    }
}
