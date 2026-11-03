package com.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.entity.Room;
import com.hotel.mapper.RoomMapper;
import com.hotel.service.RoomService;
import com.hotel.vo.RoomStatusVO;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 房间服务实现类
 * 继承ServiceImpl并实现RoomService接口，提供房间相关的业务逻辑实现
 */
@Service
public class RoomServiceImpl extends ServiceImpl<RoomMapper, Room> implements RoomService {

    /**
     * 获取房间列表
     * @return 返回所有房间的列表
     */
    @Override
    public List<Room> listRooms() {
        return baseMapper.selectRoomList();
    }

    /**
     * 获取房间状态统计信息
     * @return 返回房间状态统计信息的视图对象列表
     */
    @Override
    public List<RoomStatusVO> getStatusStats() {
        return baseMapper.selectStatusStats();
    }

    /**
     * 根据ID获取房间详细信息
     * @param id 房间ID
     * @return 返回指定ID的房间详细信息
     */
    @Override
    public Room getRoomDetail(Long id) {
        return baseMapper.selectRoomById(id);
    }
}
