package com.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.entity.BookingOrder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BookingOrderMapper extends BaseMapper<BookingOrder> {

    @Select("SELECT bo.*, c.name as customerName, c.phone as customerPhone, r.room_no as roomNo, rt.type_name as typeName " +
            "FROM booking_order bo " +
            "LEFT JOIN customer c ON bo.customer_id = c.id " +
            "LEFT JOIN room r ON bo.room_id = r.id " +
            "LEFT JOIN room_type rt ON bo.room_type_id = rt.id " +
            "WHERE bo.status != 3 " +
            "ORDER BY bo.create_time DESC")
    List<BookingOrder> selectOrderList();

    @Select("SELECT bo.*, c.name as customerName, c.phone as customerPhone, r.room_no as roomNo, rt.type_name as typeName " +
            "FROM booking_order bo " +
            "LEFT JOIN customer c ON bo.customer_id = c.id " +
            "LEFT JOIN room r ON bo.room_id = r.id " +
            "LEFT JOIN room_type rt ON bo.room_type_id = rt.id " +
            "WHERE bo.id = #{id}")
    BookingOrder selectOrderById(@Param("id") Long id);
}
