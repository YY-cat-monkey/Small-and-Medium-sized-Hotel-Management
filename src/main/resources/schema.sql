-- 酒店管理系统 · 建表脚本（幂等，可重复执行）
-- 用于云端首次启动自动建表；本地已有库时不会破坏数据

CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    username VARCHAR(50) NOT NULL COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '加密密码',
    real_name VARCHAR(50) DEFAULT NULL COMMENT '真实姓名',
    role TINYINT NOT NULL DEFAULT 1 COMMENT '0-管理员 1-前台',
    phone VARCHAR(20) DEFAULT NULL COMMENT '电话',
    status TINYINT NOT NULL DEFAULT 1 COMMENT '0-禁用 1-启用',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统用户表';

CREATE TABLE IF NOT EXISTS room_type (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    type_name VARCHAR(50) NOT NULL COMMENT '房型名称',
    base_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '基础房价',
    bed_type VARCHAR(30) DEFAULT NULL COMMENT '床型',
    capacity INT NOT NULL DEFAULT 2 COMMENT '可住人数',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房型表';

CREATE TABLE IF NOT EXISTS room (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    room_no VARCHAR(20) NOT NULL COMMENT '房间号',
    room_type_id BIGINT NOT NULL COMMENT '房型ID',
    floor INT NOT NULL DEFAULT 1 COMMENT '楼层',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-空闲 1-预订 2-在用 3-清洁 4-维修',
    price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实际售价',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    UNIQUE KEY uk_room_no (room_no),
    KEY idx_type_id (room_type_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间表';

CREATE TABLE IF NOT EXISTS customer (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) DEFAULT NULL COMMENT '电话',
    id_card VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

CREATE TABLE IF NOT EXISTS booking_order (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    order_no VARCHAR(32) NOT NULL COMMENT '订单号',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    room_id BIGINT DEFAULT NULL COMMENT '分配房间ID',
    room_type_id BIGINT NOT NULL COMMENT '预订房型ID',
    checkin_date DATE NOT NULL COMMENT '预计入住日期',
    checkout_date DATE NOT NULL COMMENT '预计离店日期',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-待确认 1-已确认 2-已入住 3-已取消',
    total_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '预计总价',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_order_no (order_no),
    KEY idx_customer_id (customer_id),
    KEY idx_room_id (room_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='预订订单表';

CREATE TABLE IF NOT EXISTS checkin_record (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    record_no VARCHAR(32) NOT NULL COMMENT '入住单号',
    booking_id BIGINT DEFAULT NULL COMMENT '关联预订ID',
    customer_id BIGINT NOT NULL COMMENT '客户ID',
    room_id BIGINT NOT NULL COMMENT '房间ID',
    checkin_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '入住时间',
    checkout_time DATETIME DEFAULT NULL COMMENT '预计退房时间',
    actual_checkout_time DATETIME DEFAULT NULL COMMENT '实际退房时间',
    deposit DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '押金',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-在住 1-已退房',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_record_no (record_no),
    KEY idx_customer_id (customer_id),
    KEY idx_room_id (room_id),
    KEY idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='入住记录表';

CREATE TABLE IF NOT EXISTS bill (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    bill_no VARCHAR(32) NOT NULL COMMENT '账单号',
    checkin_id BIGINT NOT NULL COMMENT '入住记录ID',
    room_fee DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '房费',
    consumption_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '消费金额',
    discount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '优惠金额',
    total_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '应收金额',
    paid_amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '实收金额',
    status TINYINT NOT NULL DEFAULT 0 COMMENT '0-未结 1-已结',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_bill_no (bill_no),
    KEY idx_checkin_id (checkin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='账单表';

CREATE TABLE IF NOT EXISTS room_consumption (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    checkin_id BIGINT NOT NULL COMMENT '入住记录ID',
    item_name VARCHAR(100) NOT NULL COMMENT '消费项目',
    amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
    consume_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消费时间',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_checkin_id (checkin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间消费明细表';
