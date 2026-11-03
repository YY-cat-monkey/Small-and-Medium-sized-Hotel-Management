package com.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.entity.Bill;
import com.hotel.entity.CheckinRecord;
import com.hotel.entity.Room;
import com.hotel.mapper.BillMapper;
import com.hotel.service.BillService;
import com.hotel.service.CheckinRecordService;
import com.hotel.service.RoomConsumptionService;
import com.hotel.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

/**
 * 账单服务实现类
 * 继承ServiceImpl<BillMapper, Bill>并实现BillService接口
 * 提供账单相关的业务逻辑实现
 */
@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {

    // 注入入住记录服务
    private final CheckinRecordService checkinRecordService;
    // 注入房间消费服务
    private final RoomConsumptionService roomConsumptionService;
    // 注入房间服务
    private final RoomService roomService;

    /**
     * 构造函数，注入所需的服务
     * @param checkinRecordService 入住记录服务
     * @param roomConsumptionService 房间消费服务
     * @param roomService 房间服务
     */
    public BillServiceImpl(CheckinRecordService checkinRecordService, RoomConsumptionService roomConsumptionService,
                           RoomService roomService) {
        this.checkinRecordService = checkinRecordService;
        this.roomConsumptionService = roomConsumptionService;
        this.roomService = roomService;
    }

    /**
     * 获取所有账单列表
     * @return 账单列表
     */
    @Override
    public List<Bill> listBills() {
        return baseMapper.selectBillList();
    }

    /**
     * 生成账单
     * @param checkinId 入住记录ID
     * @return 生成的账单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Bill generateBill(Long checkinId) {
        // 获取入住记录
        CheckinRecord record = checkinRecordService.getById(checkinId);
        if (record == null) {
            throw new RuntimeException("入住记录不存在");
        }

        // 检查是否已存在账单
        Bill exist = baseMapper.selectByCheckinId(checkinId);
        if (exist != null) {
            return exist;
        }

        // 计算房费
        // 确定结束时间：实际退房时间或当前时间
        LocalDateTime end = record.getActualCheckoutTime() != null ? record.getActualCheckoutTime() : LocalDateTime.now();
        LocalDateTime start = record.getCheckinTime();
        // 计算入住天数
        long hours = ChronoUnit.HOURS.between(start, end);
        long days = hours / 24;
        if (hours % 24 > 0 || days == 0) {
            days += 1;
        }

        // 获取房间价格
        BigDecimal roomPrice = BigDecimal.ZERO;
        if (record.getRoomId() != null) {
            Room room = roomService.getById(record.getRoomId());
            roomPrice = room != null && room.getPrice() != null ? room.getPrice() : BigDecimal.ZERO;
        }
        // 计算房费
        BigDecimal roomFee = roomPrice.multiply(BigDecimal.valueOf(days));

        // 获取消费金额
        BigDecimal consumptionAmount = roomConsumptionService.getTotalByCheckinId(checkinId);

        // 创建并设置账单信息
        Bill bill = new Bill();
        bill.setBillNo("B" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        bill.setCheckinId(checkinId);
        bill.setRoomFee(roomFee);
        bill.setConsumptionAmount(consumptionAmount);
        bill.setDiscount(BigDecimal.ZERO);
        bill.setTotalAmount(roomFee.add(consumptionAmount));
        bill.setPaidAmount(BigDecimal.ZERO);
        bill.setStatus(0);
        bill.setCreateTime(LocalDateTime.now());
        // 保存账单
        save(bill);
        // 返回生成的账单
        return baseMapper.selectByCheckinId(checkinId);
    }

    /**
     * 根据入住记录ID获取账单
     * @param checkinId 入住记录ID
     * @return 账单信息
     */
    @Override
    public Bill getBillByCheckinId(Long checkinId) {
        return baseMapper.selectByCheckinId(checkinId);
    }

    /**
     * 支付账单
     * @param billId 账单ID
     * @param paidAmount 支付金额
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void payBill(Long billId, BigDecimal paidAmount) {
        // 获取账单信息
        Bill bill = getById(billId);
        if (bill == null) {
            throw new RuntimeException("账单不存在");
        }
        // 更新支付信息和状态
        bill.setPaidAmount(paidAmount);
        bill.setStatus(1);
        updateById(bill);
    }
}
