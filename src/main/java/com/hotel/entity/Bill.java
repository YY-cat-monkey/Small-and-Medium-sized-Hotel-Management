package com.hotel.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 账单实体类
 * 对应数据库中的bill表
 * 用于存储和管理酒店账单相关信息
 */
@TableName("bill")
public class Bill {

    /**
     * 账单ID
     * 主键，自增长
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 账单编号
     * 唯一标识每笔账单
     */
    private String billNo;

    /**
     * 入住记录ID
     * 关联入住记录表的主键
     */
    private Long checkinId;

    /**
     * 房间费用
     * 使用BigDecimal确保金额计算的精确性
     */
    private BigDecimal roomFee;

    /**
     * 消费金额
     * 客户在酒店内的其他消费总额
     */
    private BigDecimal consumptionAmount;

    /**
     * 折扣金额
     * 应用于账单的总折扣金额
     */
    private BigDecimal discount;

    /**
     * 账单总金额
     * 房费+消费金额-折扣后的总额
     */
    private BigDecimal totalAmount;

    /**
     * 已支付金额
     * 客户已经支付的金额
     */
    private BigDecimal paidAmount;

    /**
     * 账单状态
     * 例如：0-未支付，1-已支付，2-已取消等
     */
    private Integer status;

    /**
     * 创建时间
     * 记录账单创建的日期和时间
     */
    private LocalDateTime createTime;

    /**
     * 记录编号
     * 非数据库字段，用于业务逻辑中的记录标识
     */
    @TableField(exist = false)
    private String recordNo;

    /**
     * 客户姓名
     * 非数据库字段，通常通过关联查询获取
     */
    @TableField(exist = false)
    private String customerName;

    /**
     * 房间号
     * 非数据库字段，通常通过关联查询获取
     */
    @TableField(exist = false)
    private String roomNo;

    // 以下为getter和setter方法
    // 每个属性都有对应的getter和setter方法，用于属性的访问和修改
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public Long getCheckinId() {
        return checkinId;
    }

    public void setCheckinId(Long checkinId) {
        this.checkinId = checkinId;
    }

    public BigDecimal getRoomFee() {
        return roomFee;
    }

    public void setRoomFee(BigDecimal roomFee) {
        this.roomFee = roomFee;
    }

    public BigDecimal getConsumptionAmount() {
        return consumptionAmount;
    }

    public void setConsumptionAmount(BigDecimal consumptionAmount) {
        this.consumptionAmount = consumptionAmount;
    }

    public BigDecimal getDiscount() {
        return discount;
    }

    public void setDiscount(BigDecimal discount) {
        this.discount = discount;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public BigDecimal getPaidAmount() {
        return paidAmount;
    }

    public void setPaidAmount(BigDecimal paidAmount) {
        this.paidAmount = paidAmount;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getRecordNo() {
        return recordNo;
    }

    public void setRecordNo(String recordNo) {
        this.recordNo = recordNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }
}