# 酒店管理系统

计算机科学与技术毕业设计项目——基于 Spring Boot + MyBatis-Plus + Thymeleaf 的酒店管理系统。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.7.18 | 后端主框架 |
| MyBatis-Plus | 3.5.2 | ORM 持久层 |
| MySQL | 8.x | 数据库 |
| Thymeleaf | 3.x | 服务端模板引擎 |
| Bootstrap 5 | - | 前端 UI |
| ECharts 5 | - | 数据可视化 |
| IDEA | - | 开发工具 |
| Navicat | - | 数据库管理工具 |

## 功能模块

1. **客房管理**：图形化房态看板，实时显示空闲/预订/在用/清洁/维修状态。
2. **房型管理**：维护房型、基础房价、床型、可住人数。
3. **预订管理**：电话/网上预订登记、自动分配房间、取消预订。
4. **入住管理**：办理入住（支持从预订单直接入住）、入住人员信息管理。
5. **退房结算**：根据入住时长、房间单价、额外消费自动生成账单并结算。
6. **客户管理**：入住人员信息查询与维护。
7. **系统用户**：管理员/前台账号管理。
8. **统计看板**：房间状态分布、今日入住、在住人数、总营收。

## 快速启动

### 1. 环境要求

- JDK 1.8 及以上
- MySQL 8.x
- IDEA（推荐）
- Maven 3.6+（IDEA 一般自带）

### 2. 初始化数据库

打开 Navicat，创建数据库 `hotel_db`（字符集 utf8mb4），执行 `docs/database.sql` 中的脚本。

默认账号密码：
- 管理员：`admin` / `123456`
- 前台：`front` / `123456`

### 3. 配置数据库连接

编辑 `src/main/resources/application.yml`：

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/hotel_db?useUnicode=true&characterEncoding=utf-8&useSSL=false&serverTimezone=Asia/Shanghai&allowPublicKeyRetrieval=true
    username: root        # 改为你的 MySQL 用户名
    password: root        # 改为你的 MySQL 密码
```

### 4. 使用 IDEA 运行

1. 打开 IDEA，选择 `Open`，选中 `D:\hotel-management-system` 目录。
2. 等待 Maven 依赖下载完成。
3. 找到 `src/main/java/com/hotel/HotelManagementSystemApplication.java`，右键 → `Run`。
4. 启动成功后，浏览器访问 `http://localhost:8080`。

### 5. 使用 Maven 命令行运行

```bash
# 编译打包
mvn clean package

# 运行
java -jar target/hotel-management-system-1.0.0.jar
```

## 项目结构

```
hotel-management-system/
├── docs/
│   ├── architecture.md   # 架构设计文档
│   └── database.sql      # 数据库脚本
├── src/
│   ├── main/
│   │   ├── java/com/hotel/
│   │   │   ├── config/        # 配置类
│   │   │   ├── controller/    # 控制器
│   │   │   ├── entity/        # 实体类
│   │   │   ├── mapper/        # 数据访问
│   │   │   ├── service/       # 业务逻辑
│   │   │   └── HotelManagementSystemApplication.java
│   │   └── resources/
│   │       ├── static/        # CSS/JS
│   │       ├── templates/     # Thymeleaf 页面
│   │       └── application.yml
│   └── test/
├── pom.xml
└── README.md
```

## 核心业务说明

### 房间状态流转

- 空闲 → 预订：创建预订订单并分配房间。
- 空闲 → 在用：直接办理入住。
- 预订 → 在用：根据预订订单办理入住。
- 在用 → 空闲：办理退房并结算。
- 空闲 ↔ 清洁/维修：管理员手动维护。

### 退房结算规则

- 房费 = 房间单价 × 住宿天数（不足一天按一天计算）。
- 总费用 = 房费 + 额外消费 - 优惠。
- 结算后房间自动恢复为空闲状态。

## 答辩要点

1. **技术选型理由**：Spring Boot 简化配置、MyBatis-Plus 提高开发效率、Thymeleaf 与 Spring 集成好。
2. **核心业务**：清晰展示客房状态流转、预订→入住→退房→结算完整流程。
3. **数据库设计**：8 张核心表，关系明确，满足酒店业务需求。
4. **亮点功能**：图形化房态看板、自动分配房间、自动结算账单。

## 联系方式

如有问题，可在毕业设计答辩前继续完善功能或文档。
