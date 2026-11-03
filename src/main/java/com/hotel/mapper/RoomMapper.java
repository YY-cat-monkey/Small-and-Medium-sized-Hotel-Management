package com.hotel.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hotel.entity.Room;
import com.hotel.vo.RoomStatusVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoomMapper extends BaseMapper<Room> {

    @Select("SELECT r.*, rt.type_name as typeName FROM room r LEFT JOIN room_type rt ON r.room_type_id = rt.id ORDER BY r.floor, r.room_no")
    List<Room> selectRoomList();

    @Select("SELECT r.*, rt.type_name as typeName FROM room r LEFT JOIN room_type rt ON r.room_type_id = rt.id WHERE r.id = #{id}")
    Room selectRoomById(@Param("id") Long id);

    @Select("SELECT COUNT(*) as count, status FROM room GROUP BY status")
    List<RoomStatusVO> selectStatusStats();
}
