package com.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.entity.RoomConsumption;

import java.math.BigDecimal;
import java.util.List;

public interface RoomConsumptionService extends IService<RoomConsumption> {

    List<RoomConsumption> listByCheckinId(Long checkinId);

    BigDecimal getTotalByCheckinId(Long checkinId);
}
