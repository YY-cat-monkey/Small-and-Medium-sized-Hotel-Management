package com.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.entity.BookingOrder;

import java.util.List;

public interface BookingOrderService extends IService<BookingOrder> {

    List<BookingOrder> listOrders();

    BookingOrder getOrderDetail(Long id);

    BookingOrder createOrder(BookingOrder order);

    void assignRoom(Long orderId, Long roomId);

    void cancelOrder(Long orderId);
}
