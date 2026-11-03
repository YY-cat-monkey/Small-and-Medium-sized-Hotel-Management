package com.hotel.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hotel.entity.RoomConsumption;
import com.hotel.mapper.RoomConsumptionMapper;
import com.hotel.service.RoomConsumptionService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * 房间消费服务实现类
 * 继承ServiceImpl并实现RoomConsumptionService接口，提供房间消费相关的业务逻辑处理
 */
@Service
public class RoomConsumptionServiceImpl extends ServiceImpl<RoomConsumptionMapper, RoomConsumption> implements RoomConsumptionService {

    /**
     * 根据入住ID获取房间消费列表
     * @param checkinId 入住记录ID
     * @return 房间消费列表
     */
    @Override
    public List<RoomConsumption> listByCheckinId(Long checkinId) {
        return baseMapper.selectByCheckinId(checkinId);
    }

    /**
     * 根据入住ID获取房间消费总金额
     * @param checkinId 入住记录ID
     * @return 房间消费总金额
     */
    public BigDecimal getTotalByCheckinId(Long checkinId) {
        return baseMapper.selectTotalByCheckinId(checkinId);
    }
}
