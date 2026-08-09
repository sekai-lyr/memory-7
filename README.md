# 🖼️ Sekai Picture Wallpaper · 二次元壁纸图库

> **上传 · 分类 · 筛选 · 收藏 · 批量操作 · 运营看板**
> A complete anime wallpaper gallery system

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![MyBatis](https://img.shields.io/badge/MyBatis-3-lightgrey)](https://mybatis.org/)
[![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3-005F0F)](https://www.thymeleaf.org/)
[![MySQL](https://img.shields.io/badge/MySQL-8-4479A1)](https://www.mysql.com/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

完整的二次元壁纸图库系统：上传、分类、筛选、收藏、批量操作与运营看板一应俱全，可包装为图库系统源码 / 私有图床 / 壁纸资源站后台。

A full-featured wallpaper gallery: upload, categorize, filter, favorite, batch operations & an ops dashboard.

## ✨ 功能亮点 Features

- 用户注册、登录、退出 User auth
- 壁纸分类管理和分类筛选 Category management
- 标题、角色、作品、标签关键词搜索 Keyword search
- 分页查询、色调筛选、比例筛选、下载量排序 Filter & sort
- 上传自制壁纸（保存到 `uploads/wallpapers`）Upload
- 收藏/取消收藏 Favorites
- 一键下载和下载次数统计 Downloads tracking
- 热门壁纸推荐、随机抽取 Hot & random picks
- **批量收藏、批量取消收藏、批量移动分类** Batch operations
- 卡片内快速编辑标题、角色、来源、标签、色调和分类 Quick edit
- 会员中心演示：套餐、下载额度、商业化路径 Membership demo
- **运营看板**：资产评分、收入模型、原创占比、收藏转化、热门榜 Ops dashboard

## 💰 变现方向 Monetization

更适合包装成"图库系统源码 / 私有图床 / 壁纸资源站后台 / 接单展示项目"，不建议直接售卖未授权动漫图片。

- 卖源码：数据库脚本、部署教程、演示账号、二开服务
- 接定制：企业素材库、个人相册、COS 图库、摄影作品站
- 做会员演示：扩展 VIP 下载额度、高清包、批量打包下载
- 做作品集：展示文件上传、图片管理、分页查询、筛选、批量操作

## 🗄️ 数据库 Database

初始化脚本在 `build-data/init.sql`：

```powershell
mysql -uroot -p123456 --default-character-set=utf8mb4 -D sekai_friend < build-data/init.sql
```

## ▶️ 启动 Run

默认端口 `8087`：

```powershell
mvn spring-boot:run
```

访问 `http://localhost:8087/login`。

## ⚠️ 商用提醒 Legal Note

公开上线或收费前，请优先使用自制、授权、CC0、可商用素材，或让用户自行上传并确认版权。项目本身可以商用交付，但图片素材是否可商用需单独确认。

## 📄 License

[MIT](LICENSE) © 2026 [sekai-lyr](https://github.com/sekai-lyr)

---

**⭐ If this project helped you, star it! 如果这个项目对你有帮助，欢迎 Star！**
