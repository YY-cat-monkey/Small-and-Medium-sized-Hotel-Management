package com.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.entity.CheckinRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CheckinRecordMapper extends BaseMapper<CheckinRecord> {

    @Select("SELECT cr.*, c.name as customerName, c.phone as customerPhone, r.room_no as roomNo, rt.type_name as typeName " +
            "FROM checkin_record cr " +
            "LEFT JOIN customer c ON cr.customer_id = c.id " +
            "LEFT JOIN room r ON cr.room_id = r.id " +
            "LEFT JOIN room_type rt ON r.room_type_id = rt.id " +
            "WHERE cr.status = #{status} " +
            "ORDER BY cr.checkin_time DESC")
    List<CheckinRecord> selectByStatus(@Param("status") Integer status);

    @Select("SELECT cr.*, c.name as customerName, c.phone as customerPhone, r.room_no as roomNo, rt.type_name as typeName " +
            "FROM checkin_record cr " +
            "LEFT JOIN customer c ON cr.customer_id = c.id " +
            "LEFT JOIN room r ON cr.room_id = r.id " +
            "LEFT JOIN room_type rt ON r.room_type_id = rt.id " +
            "WHERE cr.id = #{id}")
    CheckinRecord selectByIdDetail(@Param("id") Long id);
}
