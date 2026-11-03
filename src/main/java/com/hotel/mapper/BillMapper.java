package com.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.entity.Bill;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface BillMapper extends BaseMapper<Bill> {

    @Select("SELECT b.*, cr.record_no as recordNo, c.name as customerName, r.room_no as roomNo " +
            "FROM bill b " +
            "LEFT JOIN checkin_record cr ON b.checkin_id = cr.id " +
            "LEFT JOIN customer c ON cr.customer_id = c.id " +
            "LEFT JOIN room r ON cr.room_id = r.id " +
            "ORDER BY b.create_time DESC")
    List<Bill> selectBillList();

    @Select("SELECT b.*, cr.record_no as recordNo, c.name as customerName, r.room_no as roomNo " +
            "FROM bill b " +
            "LEFT JOIN checkin_record cr ON b.checkin_id = cr.id " +
            "LEFT JOIN customer c ON cr.customer_id = c.id " +
            "LEFT JOIN room r ON cr.room_id = r.id " +
            "WHERE b.checkin_id = #{checkinId} LIMIT 1")
    Bill selectByCheckinId(@Param("checkinId") Long checkinId);
}
