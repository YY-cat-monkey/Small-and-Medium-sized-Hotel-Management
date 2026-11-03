-- 酒店管理系统 · 种子数据（幂等：已存在则跳过，重复启动不会产生重复数据）

-- 系统用户：admin / 123456，front / 123456（MD5）
INSERT IGNORE INTO sys_user (username, password, real_name, role, phone, status) VALUES
('admin', 'e10adc3949ba59abbe56e057f20f883e', '系统管理员', 0, '13800138000', 1),
('front', 'e10adc3949ba59abbe56e057f20f883e', '前台小李', 1, '13800138001', 1);

-- 房型（固定 ID，保证房间表引用稳定）
INSERT IGNORE INTO room_type (id, type_name, base_price, bed_type, capacity, remark) VALUES
(1, '标准间', 188.00, '双床', 2, '经济实惠'),
(2, '大床房', 228.00, '大床', 2, '适合情侣'),
(3, '豪华套房', 488.00, '大床', 2, '带客厅'),
(4, '家庭房', 318.00, '一大两小', 4, '亲子出行');

-- 房间（1-3 层）
INSERT IGNORE INTO room (room_no, room_type_id, floor, status, price, remark) VALUES
('101', 1, 1, 0, 188.00, '靠近前台'),
('102', 1, 1, 0, 188.00, NULL),
('103', 2, 1, 0, 228.00, NULL),
('201', 2, 2, 0, 228.00, '朝南'),
('202', 2, 2, 0, 228.00, NULL),
('203', 3, 2, 0, 488.00, '带阳台'),
('301', 3, 3, 0, 488.00, NULL),
('302', 4, 3, 0, 318.00, '亲子主题'),
('303', 4, 3, 0, 318.00, NULL);

-- 客户示例
INSERT INTO customer (name, phone, id_card)
SELECT '张三', '13900139000', '110101199001011234' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE phone = '13900139000');

INSERT INTO customer (name, phone, id_card)
SELECT '李四', '13900139001', '110101199002011235' FROM DUAL
WHERE NOT EXISTS (SELECT 1 FROM customer WHERE phone = '13900139001');
