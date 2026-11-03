package com.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.entity.BookingOrder;
import com.hotel.entity.Customer;
import com.hotel.entity.Room;
import com.hotel.mapper.BookingOrderMapper;
import com.hotel.service.BookingOrderService;
import com.hotel.service.CustomerService;
import com.hotel.service.RoomService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * 预订订单服务实现类
 * 继承ServiceImpl并实现BookingOrderService接口
 * 提供订单相关的业务逻辑实现
 */
@Service
public class BookingOrderServiceImpl extends ServiceImpl<BookingOrderMapper, BookingOrder> implements BookingOrderService {

    // 客户服务依赖
    private final CustomerService customerService;
    // 房间服务依赖
    private final RoomService roomService;

    /**
     * 构造函数，通过依赖注入方式初始化服务
     * @param customerService 客户服务
     * @param roomService 房间服务
     */
    public BookingOrderServiceImpl(CustomerService customerService, RoomService roomService) {
        this.customerService = customerService;
        this.roomService = roomService;
    }

    /**
     * 获取所有订单列表
     * @return 订单列表
     */
    @Override
    public List<BookingOrder> listOrders() {
        return baseMapper.selectOrderList();
    }

    /**
     * 根据ID获取订单详情
     * @param id 订单ID
     * @return 订单详情
     */
    @Override
    public BookingOrder getOrderDetail(Long id) {
        return baseMapper.selectOrderById(id);
    }

    /**
     * 创建订单
     * @param order 订单信息
     * @return 创建后的订单详情
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public BookingOrder createOrder(BookingOrder order) {
        // 获取或创建客户信息
        Customer customer = customerService.getOrCreateCustomer(order.getCustomerName(), order.getCustomerPhone(), null);
        order.setCustomerId(customer.getId());

        // 生成订单号（格式：BK+时间戳）
        if (order.getOrderNo() == null) {
            order.setOrderNo("BK" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")));
        }

        // 设置订单状态为待支付（1）
        if (order.getStatus() == null) {
            order.setStatus(1);
        }

        // 自动分配房间逻辑
        if (order.getRoomId() == null && order.getRoomTypeId() != null) {
            // 查找可用房间
            Room room = roomService.lambdaQuery()
                    .eq(Room::getRoomTypeId, order.getRoomTypeId())
                    .eq(Room::getStatus, 0)
                    .orderByAsc(Room::getRoomNo)
                    .last("LIMIT 1")
                    .one();
            if (room != null) {
                // 设置房间信息并更新房间状态为已预订
                order.setRoomId(room.getId());
                order.setPrice(room.getPrice());
                room.setStatus(1);
                roomService.updateById(room);
            }
        } else if (order.getRoomId() != null) {
            // 处理已指定房间的情况
            Room room = roomService.getById(order.getRoomId());
            if (room != null && room.getStatus() == 0) {
                // 更新房间状态为已预订
                room.setStatus(1);
                roomService.updateById(room);
            }
        }

        // 计算总价
        if (order.getTotalPrice() == null && order.getCheckinDate() != null && order.getCheckoutDate() != null) {
            long days = order.getCheckoutDate().toEpochDay() - order.getCheckinDate().toEpochDay();
            if (days <= 0) {
                days = 1;
            }
            BigDecimal price = order.getPrice();
            if (price == null) {
                Room room = roomService.getById(order.getRoomId());
                price = room != null ? room.getPrice() : BigDecimal.ZERO;
            }
            order.setTotalPrice(price.multiply(BigDecimal.valueOf(days)));
        }

        order.setCreateTime(LocalDateTime.now());
        save(order);
        return getOrderDetail(order.getId());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoom(Long orderId, Long roomId) {
        BookingOrder order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        Room room = roomService.getById(roomId);
        if (room == null || room.getStatus() != 0) {
            throw new RuntimeException("房间不可用");
        }
        room.setStatus(1);
        roomService.updateById(room);
        order.setRoomId(roomId);
        order.setRoomTypeId(room.getRoomTypeId());
        updateById(order);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void cancelOrder(Long orderId) {
        BookingOrder order = getById(orderId);
        if (order == null) {
            throw new RuntimeException("订单不存在");
        }
        if (order.getRoomId() != null) {
            Room room = roomService.getById(order.getRoomId());
            if (room != null && room.getStatus() == 1) {
                room.setStatus(0);
                roomService.updateById(room);
            }
        }
        order.setStatus(3);
        updateById(order);
    }
}
