package com.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.entity.Room;
import com.hotel.vo.RoomStatusVO;

import java.util.List;

public interface RoomService extends IService<Room> {

    List<Room> listRooms();

    List<RoomStatusVO> getStatusStats();

    Room getRoomDetail(Long id);
}
