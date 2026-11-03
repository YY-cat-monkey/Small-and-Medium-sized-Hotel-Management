# 酒店管理系统 - 架构设计

## 一、技术选型

| 层级 | 技术 | 说明 |
|------|------|------|
| 后端框架 | Spring Boot 3.2.x | 主流、配置简单、生态完善 |
| ORM | MyBatis-Plus 3.5.x | 简化 CRUD、内置分页、代码生成 |
| 数据库 | MySQL 8.x | 通过 Navicat 管理 |
| 视图层 | Thymeleaf 3.1.x | Spring 官方推荐的服务端模板 |
| 前端样式 | Bootstrap 5 + Bootstrap Icons | 响应式、组件丰富 |
| 图表 | ECharts 5.x | 房间状态统计、营收可视化 |
| 构建工具 | Maven 3.9.x | IDEA 默认支持 |
| JDK | JDK 17 | Spring Boot 3 最低要求 |

## 二、功能模块

```
酒店管理系统
├── 系统管理
│   ├── 用户管理（管理员/前台账号）
│   ├── 登录/登出
│   └── 修改密码
├── 客房管理
│   ├── 房型管理（标准间、大床房、套房等）
│   ├── 房间信息管理（房号、楼层、价格）
│   ├── 房间状态看板（图形化：空闲/预订/在用/清洁/维修）
│   └── 房间状态实时查询与统计
├── 预订管理
│   ├── 电话/网上预订登记
│   ├── 预订订单查询
│   └── 自动/手动分配房间
├── 入住管理
│   ├── 办理入住（有预订/无预订）
│   ├── 入住人员信息管理
│   └── 更换房间
├── 退房管理
│   ├── 退房结算
│   ├── 房费计算 + 额外消费
│   └── 账单打印/预览
└── 统计报表
    ├── 房间入住率
    ├── 营业收入统计
    └── 今日房态概览
```

## 三、数据库表设计

### 1. sys_user（系统用户）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| username | VARCHAR(50) | 用户名 |
| password | VARCHAR(100) | 加密密码 |
| real_name | VARCHAR(50) | 真实姓名 |
| role | TINYINT | 0-管理员 1-前台 |
| phone | VARCHAR(20) | 电话 |
| status | TINYINT | 0-禁用 1-启用 |
| create_time | DATETIME | 创建时间 |

### 2. room_type（房型）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| type_name | VARCHAR(50) | 房型名称 |
| base_price | DECIMAL(10,2) | 基础房价 |
| bed_type | VARCHAR(30) | 床型 |
| capacity | INT | 可住人数 |
| remark | VARCHAR(200) | 备注 |

### 3. room（房间）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| room_no | VARCHAR(20) | 房间号 |
| room_type_id | BIGINT FK | 房型ID |
| floor | INT | 楼层 |
| status | TINYINT | 0-空闲 1-预订 2-在用 3-清洁 4-维修 |
| price | DECIMAL(10,2) | 实际售价 |
| remark | VARCHAR(200) | 备注 |

### 4. customer（客户）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| name | VARCHAR(50) | 姓名 |
| phone | VARCHAR(20) | 电话 |
| id_card | VARCHAR(18) | 身份证号 |
| create_time | DATETIME | 创建时间 |

### 5. booking_order（预订订单）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_no | VARCHAR(32) | 订单号 |
| customer_id | BIGINT FK | 客户ID |
| room_id | BIGINT FK | 分配房间ID |
| room_type_id | BIGINT FK | 预订房型ID |
| checkin_date | DATE | 预计入住日期 |
| checkout_date | DATE | 预计离店日期 |
| status | TINYINT | 0-待确认 1-已确认 2-已入住 3-已取消 |
| total_price | DECIMAL(10,2) | 预计总价 |
| remark | VARCHAR(200) | 备注 |
| create_time | DATETIME | 创建时间 |

### 6. checkin_record（入住记录）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| record_no | VARCHAR(32) | 入住单号 |
| booking_id | BIGINT FK | 关联预订ID（可为空） |
| customer_id | BIGINT FK | 客户ID |
| room_id | BIGINT FK | 房间ID |
| checkin_time | DATETIME | 入住时间 |
| checkout_time | DATETIME | 预计退房时间 |
| actual_checkout_time | DATETIME | 实际退房时间 |
| deposit | DECIMAL(10,2) | 押金 |
| status | TINYINT | 0-在住 1-已退房 |
| remark | VARCHAR(200) | 备注 |
| create_time | DATETIME | 创建时间 |

### 7. bill（账单）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| bill_no | VARCHAR(32) | 账单号 |
| checkin_id | BIGINT FK | 入住记录ID |
| room_fee | DECIMAL(10,2) | 房费 |
| consumption_amount | DECIMAL(10,2) | 消费金额 |
| discount | DECIMAL(10,2) | 优惠金额 |
| total_amount | DECIMAL(10,2) | 应收金额 |
| paid_amount | DECIMAL(10,2) | 实收金额 |
| status | TINYINT | 0-未结 1-已结 |
| create_time | DATETIME | 创建时间 |

### 8. room_consumption（房间消费）
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| checkin_id | BIGINT FK | 入住记录ID |
| item_name | VARCHAR(100) | 消费项目 |
| amount | DECIMAL(10,2) | 金额 |
| consume_time | DATETIME | 消费时间 |
| remark | VARCHAR(200) | 备注 |

## 四、核心状态流转

```
房间状态：
空闲 → 预订（生成预订订单）
空闲 → 在用（直接入住）
预订 → 在用（办理入住）
预订 → 空闲（取消预订）
在用 → 空闲（办理退房 + 结账）
空闲 → 清洁/维修（管理操作）
清洁/维修 → 空闲（恢复可用）
```

## 五、项目目录结构

```
hotel-management-system/
├── src/
│   ├── main/
│   │   ├── java/com/hotel/
│   │   │   ├── config/          # 配置类
│   │   │   ├── controller/      # 控制器
│   │   │   ├── entity/          # 实体类
│   │   │   ├── mapper/          # 数据访问
│   │   │   ├── service/         # 业务逻辑
│   │   │   └── HotelManagementSystemApplication.java
│   │   └── resources/
│   │       ├── static/          # CSS/JS/图片
│   │       ├── templates/       # Thymeleaf 页面
│   │       ├── application.yml
│   │       └── mapper/          # XML映射文件
│   └── test/
├── docs/
│   ├── architecture.md
│   └── database.sql
├── pom.xml
└── README.md
```
