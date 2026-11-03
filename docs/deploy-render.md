# 云端部署指南（Render 免费版 + Aiven 免费 MySQL）

零成本方案。完成后得到一个 `*.onrender.com` 公网地址，任何电脑浏览器打开即用。

> 代价：免费版 15 分钟无访问会休眠，首次唤醒约 30–60 秒。日常演示/答辩完全够用。

---

## 前置条件（已完成）

- 代码改造、Dockerfile、render.yaml 已在 GitHub 仓库 `railway-deploy` 分支
- 仓库地址：<https://github.com/YY-cat-monkey/Small-and-Medium-sized-Hotel-Management>

---

## 第一步：创建 Aiven 免费 MySQL（约 3 分钟）

1. 打开 <https://aiven.io/free-tier>，点 **Get started for free**，用 GitHub 或邮箱注册（**不要信用卡**）。
2. 登录后进控制台，点 **Create a new service** → 选 **MySQL**。
3. 计划选 **Free-1-5gb**（永久免费），云厂商/区域任选（建议 Google Cloud / us-east1）。
4. 点 **Create**，等约 1–2 分钟状态变绿。
5. 进入该 MySQL 服务页面，点顶部 **Connect**（或 Overview 里的连接信息），找到并复制以下 4 项：
   - **Host**（形如 `mysql-xxxxx-xxxx.aivencloud.com`）
   - **Port**（一般是 `12345` 之类，不是 3306）
   - **User**（一般是 `avnadmin`）
   - **Password**（点眼睛图标显示）
   - **Database name**（一般是 `defaultdb`）
6. 把这 5 项贴给 AI 助手，会帮你拼好 JDBC 地址。

---

## 第二步：在 Render 部署（约 5 分钟）

1. 打开 <https://render.com>，右上 **Sign up**，**用 GitHub 账号登录**（这样能直接选到你的仓库）。
2. 授权 Render 访问你的仓库（授权 `YY-cat-monkey/Small-and-Medium-sized-Hotel-Management`）。
3. 进 Dashboard → 右上 **New** → **Blueprint**。
4. 选择你的仓库 → 分支选 **`railway-deploy`**。
5. Render 会自动读取 `render.yaml`，识别出一个 Web Service（plan: free, region: singapore），点 **Apply**。
6. 进入刚创建的 `hotel-management-system` 服务 → 左侧 **Environment** 标签，添加以下 3 个环境变量（值由第一步后 AI 助手给出）：
   - `DB_URL`：AI 助手拼接的 JDBC 地址
   - `DB_USERNAME`：Aiven 的 User（如 `avnadmin`）
   - `DB_PASSWORD`：Aiven 的 Password
7. 保存后 Render 自动重新部署。等构建完成（首次约 5–8 分钟），顶部出现 `https://hotel-management-system.onrender.com` 即成功。
8. 打开网址 → 登录页 → 用 `admin` / `123456` 登录验证。

> Render 自动注入 `PORT` 环境变量，应用启动时会自动建表并写入默认账号（admin/front + 4 种房型 + 9 间房），无需手动执行 SQL。

---

## 常见问题

| 现象 | 原因 / 处理 |
|------|------|
| 部署日志报 `Port 60811 already in use` | 不应出现，应用已读 `PORT`；若出现检查是否漏设 |
| 启动报数据库连接失败 | Aiven 强制 SSL，`DB_URL` 必须带 `sslMode=REQUIRED`（AI 助手会处理好） |
| 首次打开慢 / 转圈 | 免费版冷启动，等 30–60 秒 |
| 想换永久不休眠 | Render 升级 Starter $7/月，或转 Railway Hobby $5/月 |
