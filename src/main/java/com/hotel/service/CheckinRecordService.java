package com.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.entity.CheckinRecord;

import java.util.List;

public interface CheckinRecordService extends IService<CheckinRecord> {

    List<CheckinRecord> listCheckinRecords(Integer status);

    CheckinRecord getDetail(Long id);

    CheckinRecord checkin(CheckinRecord record);

    CheckinRecord checkinFromBooking(Long bookingId);

    void checkout(Long recordId);
}
