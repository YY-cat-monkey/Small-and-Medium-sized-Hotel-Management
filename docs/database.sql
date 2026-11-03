-- 酒店管理系统数据库脚本
-- 数据库名：hotel_db
-- 字符集：utf8mb4

CREATE DATABASE IF NOT EXISTS hotel_db DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hotel_db;

-- 1. 系统用户表
DROP TABLE IF EXISTS sys_user;
CREATE TABLE sys_user (
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

-- 2. 房型表
DROP TABLE IF EXISTS room_type;
CREATE TABLE room_type (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    type_name VARCHAR(50) NOT NULL COMMENT '房型名称',
    base_price DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '基础房价',
    bed_type VARCHAR(30) DEFAULT NULL COMMENT '床型',
    capacity INT NOT NULL DEFAULT 2 COMMENT '可住人数',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房型表';

-- 3. 房间表
DROP TABLE IF EXISTS room;
CREATE TABLE room (
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

-- 4. 客户表
DROP TABLE IF EXISTS customer;
CREATE TABLE customer (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    phone VARCHAR(20) DEFAULT NULL COMMENT '电话',
    id_card VARCHAR(18) DEFAULT NULL COMMENT '身份证号',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id),
    KEY idx_phone (phone)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户表';

-- 5. 预订订单表
DROP TABLE IF EXISTS booking_order;
CREATE TABLE booking_order (
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

-- 6. 入住记录表
DROP TABLE IF EXISTS checkin_record;
CREATE TABLE checkin_record (
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

-- 7. 账单表
DROP TABLE IF EXISTS bill;
CREATE TABLE bill (
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

-- 8. 房间消费明细表
DROP TABLE IF EXISTS room_consumption;
CREATE TABLE room_consumption (
    id BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键',
    checkin_id BIGINT NOT NULL COMMENT '入住记录ID',
    item_name VARCHAR(100) NOT NULL COMMENT '消费项目',
    amount DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '金额',
    consume_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '消费时间',
    remark VARCHAR(200) DEFAULT NULL COMMENT '备注',
    PRIMARY KEY (id),
    KEY idx_checkin_id (checkin_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='房间消费明细表';

-- 初始数据

-- 系统用户：admin/123456，front/123456（密码均为 MD5 加密后的值）
INSERT INTO sys_user (username, password, real_name, role, phone, status) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 0, '13800138000', 1),
('front', 'e10adc3949ba59abbe56e057f20f883e', '前台小李', 1, '13800138001', 1);

-- 房型数据
INSERT INTO room_type (type_name, base_price, bed_type, capacity, remark) VALUES
('标准间', 188.00, '双床', 2, '经济实惠'),
('大床房', 228.00, '大床', 2, '适合情侣'),
('豪华套房', 488.00, '大床', 2, '带客厅'),
('家庭房', 318.00, '一大两小', 4, '亲子出行');

-- 房间数据（1-3层，每层若干房间）
INSERT INTO room (room_no, room_type_id, floor, status, price, remark) VALUES
('101', 1, 1, 0, 188.00, '靠近前台'),
('102', 1, 1, 0, 188.00, NULL),
('103', 2, 1, 0, 228.00, NULL),
('201', 2, 2, 0, 228.00, '朝南'),
('202', 2, 2, 0, 228.00, NULL),
('203', 3, 2, 0, 488.00, '带阳台'),
('301', 3, 3, 0, 488.00, NULL),
('302', 4, 3, 0, 318.00, '亲子主题'),
('303', 4, 3, 0, 318.00, NULL);

-- 客户示例数据
INSERT INTO customer (name, phone, id_card) VALUES
('张三', '13900139000', '110101199001011234'),
('李四', '13900139001', '110101199002011235');
