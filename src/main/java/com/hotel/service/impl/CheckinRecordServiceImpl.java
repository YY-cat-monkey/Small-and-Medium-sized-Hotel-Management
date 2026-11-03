package com.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.entity.BookingOrder;
import com.hotel.entity.CheckinRecord;
import com.hotel.entity.Customer;
import com.hotel.entity.Room;
import com.hotel.mapper.CheckinRecordMapper;
import com.hotel.service.BookingOrderService;
import com.hotel.service.CheckinRecordService;
import com.hotel.service.CustomerService;
import com.hotel.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 入住记录服务实现类
 * 继承ServiceImpl并实现CheckinRecordService接口，提供入住记录相关的业务逻辑
 */
@Service
public class CheckinRecordServiceImpl extends ServiceImpl<CheckinRecordMapper, CheckinRecord> implements CheckinRecordService {

    // 依赖注入的客户服务
    private final CustomerService customerService;
    // 依赖注入的房间服务
    private final RoomService roomService;
    // 依赖注入的预订订单服务
    private final BookingOrderService bookingOrderService;

    /**
     * 构造函数，注入所需的服务
     * @param customerService 客户服务
     * @param roomService 房间服务
     * @param bookingOrderService 预订订单服务
     */
    public CheckinRecordServiceImpl(CustomerService customerService, RoomService roomService,
                                    BookingOrderService bookingOrderService) {
        this.customerService = customerService;
        this.roomService = roomService;
        this.bookingOrderService = bookingOrderService;
    }

    /**
     * 获取入住记录列表
     * @param status 状态条件，null表示查询所有状态的记录
     * @return 入住记录列表
     */
    @Override
    public List<CheckinRecord> listCheckinRecords(Integer status) {
        if (status != null) {
            return baseMapper.selectByStatus(status);
        }
        return baseMapper.selectByStatus(0);
    }

    /**
     * 获取入住记录详情
     * @param id 入住记录ID
     * @return 入住记录详情
     */
    @Override
    public CheckinRecord getDetail(Long id) {
        return baseMapper.selectByIdDetail(id);
    }

    /**
     * 办理入住手续
     * @param record 入住记录信息
     * @return 办理后的入住记录详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckinRecord checkin(CheckinRecord record) {
        // 获取或创建客户信息
        Customer customer = customerService.getOrCreateCustomer(record.getCustomerName(), record.getCustomerPhone(), record.getIdCard());
        record.setCustomerId(customer.getId());

        // 检查房间状态
        Room room = roomService.getById(record.getRoomId());
        if (room == null || (room.getStatus() != 0 && room.getStatus() != 1)) {
            throw new RuntimeException("房间不可用，无法办理入住");
        }

        // 设置入住记录号
        record.setRecordNo("CI" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        // 设置默认值
        if (record.getCheckinTime() == null) {
            record.setCheckinTime(LocalDateTime.now());
        }
        if (record.getCheckoutTime() == null) {
            record.setCheckoutTime(record.getCheckinTime().plusDays(1));
        }
        if (record.getDeposit() == null) {
            record.setDeposit(BigDecimal.ZERO);
        }
        record.setStatus(0);
        record.setCreateTime(LocalDateTime.now());

        // 保存入住记录
        save(record);

        // 更新房间状态为已入住
        room.setStatus(2);
        roomService.updateById(room);

        // 返回入住记录详情
        return getDetail(record.getId());
    }

    /**
     * 从预订订单办理入住
     * @param bookingId 预订订单ID
     * @return 入住记录详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public CheckinRecord checkinFromBooking(Long bookingId) {
        // 获取预订订单
        BookingOrder order = bookingOrderService.getById(bookingId);
        if (order == null || order.getStatus() == 3) {
            throw new RuntimeException("预订订单不存在或已取消");
        }
        // 获取客户信息
        Customer customer = customerService.getById(order.getCustomerId());

        // 创建入住记录
        CheckinRecord record = new CheckinRecord();
        record.setBookingId(bookingId);
        record.setCustomerId(order.getCustomerId());
        record.setCustomerName(customer != null ? customer.getName() : "未知");
        record.setCustomerPhone(customer != null ? customer.getPhone() : "");
        record.setRoomId(order.getRoomId());
        record.setCheckinTime(LocalDateTime.now());
        record.setCheckoutTime(order.getCheckoutDate() != null ? order.getCheckoutDate().atTime(12, 0) : LocalDateTime.now().plusDays(1));
        record.setDeposit(BigDecimal.ZERO);
        record.setStatus(0);
        record.setCreateTime(LocalDateTime.now());

        // 调用入住方法
        return checkin(record);
    }

    /**
     * 办理退房手续
     * @param recordId 入住记录ID
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void checkout(Long recordId) {
        // 获取入住记录
        CheckinRecord record = getById(recordId);
        if (record == null || record.getStatus() == 1) {
            throw new RuntimeException("入住记录不存在或已退房");
        }

        // 更新入住记录状态
        record.setStatus(1);
        record.setActualCheckoutTime(LocalDateTime.now());
        updateById(record);

        // 更新房间状态
        Room room = roomService.getById(record.getRoomId());
        if (room != null) {
            room.setStatus(0);
            roomService.updateById(room);
        }

        // 如果有关联的预订订单，更新订单状态
        if (record.getBookingId() != null) {
            BookingOrder order = bookingOrderService.getById(record.getBookingId());
            if (order != null) {
                order.setStatus(2);
                bookingOrderService.updateById(order);
            }
        }
    }
}
