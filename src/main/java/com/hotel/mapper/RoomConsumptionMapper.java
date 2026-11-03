package com.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.entity.RoomConsumption;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.math.BigDecimal;
import java.util.List;

@Mapper
public interface RoomConsumptionMapper extends BaseMapper<RoomConsumption> {

    @Select("SELECT * FROM room_consumption WHERE checkin_id = #{checkinId} ORDER BY consume_time DESC")
    List<RoomConsumption> selectByCheckinId(@Param("checkinId") Long checkinId);

    @Select("SELECT COALESCE(SUM(amount), 0) FROM room_consumption WHERE checkin_id = #{checkinId}")
    BigDecimal selectTotalByCheckinId(@Param("checkinId") Long checkinId);
}
