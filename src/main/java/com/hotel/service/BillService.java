package com.hotel.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.hotel.entity.Bill;

import java.math.BigDecimal;
import java.util.List;

public interface BillService extends IService<Bill> {

    List<Bill> listBills();

    Bill generateBill(Long checkinId);

    Bill getBillByCheckinId(Long checkinId);

    void payBill(Long billId, BigDecimal paidAmount);
}
