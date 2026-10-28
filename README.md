<div align="center">

# 🖼️ Sekai Picture · 二次元壁纸图库系统

> ⭐ Sekai 系列实战项目 07 / 14 · 单项目独立仓库

![Java](https://img.shields.io/badge/Java-red?style=flat-square&logo=openjdk&logoColor=white) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-6DB33F?style=flat-square&logo=springboot&logoColor=white) ![MyBatis](https://img.shields.io/badge/MyBatis-blue?style=flat-square) ![Thymeleaf](https://img.shields.io/badge/Thymeleaf-blue?style=flat-square) ![MySQL](https://img.shields.io/badge/MySQL-blue?style=flat-square)

[![Stars](https://img.shields.io/github/stars/sekai-lyr/memory-7?style=flat-square&logo=github)](https://github.com/sekai-lyr/memory-7/stargazers) [![合集](https://img.shields.io/badge/📚_全系列合集-sekai--product-8250DF?style=flat-square)](https://github.com/sekai-lyr/sekai-product)

</div>

---

Sekai Picture 是一个二次元壁纸图库系统，目录结构参考 `memory-three`，业务表统一使用 `sekai_picture_` 前缀，目标数据库为 `sekai_friend`。

## 功能亮点

- 用户注册、登录、退出
- 壁纸分类管理和分类筛选
- 标题、角色、作品、标签关键词搜索
- 分页查询、色调筛选、比例筛选、下载量排序
- 上传自制壁纸，文件保存到 `uploads/wallpapers`
- 收藏 / 取消收藏
- 一键下载和下载次数统计
- 热门壁纸推荐、随机抽图
- 批量收藏、批量取消收藏、批量移动分类
- 卡片内快速编辑标题、角色、来源、标签、色调和分类
- 会员中心演示：套餐、下载额度、商业化路径
- 运营看板：资产评分、收入模型、原创占比、收藏转化、热门榜

## 变现方向

这个项目更适合包装成“图库系统源码 / 私有图床 / 壁纸资源站后台 / 接单展示项目”，不建议直接售卖未授权动漫图片。

推荐玩法：

- 卖源码：提供数据库脚本、部署教程、演示账号和二开服务
- 接定制：改成企业素材库、个人相册、COS 图库、摄影作品站
- 做会员演示：扩展 VIP 下载额度、高清包、批量打包下载
- 做作品集：展示文件上传、图片管理、分页查询、筛选、批量操作

## 数据表

初始化脚本在 `build-data/init.sql`。

```powershell
mysql -uroot -p123456 --default-character-set=utf8mb4 -D sekai_friend < build-data/init.sql
```

## 启动

默认端口：`8087`

```powershell
mvn spring-boot:run
```

访问：

```text
http://localhost:8087/login
```

## 商用提醒

如果准备公开上线或收费，请优先使用自制、授权、CC0、可商用素材，或让用户自行上传并确认版权。项目本身可以商用交付，但图片素材是否能商用要单独确认。

---

## 🗺️ Sekai 项目全景导航

本项目是 **Sekai 系列作品集** 的一员，全部 14 个实战项目：

| 项目 | 简介 |
|------|------|
| [`memory-1`](https://github.com/sekai-lyr/memory-1) | 📊 二次元数据中心 |
| [`memory-2`](https://github.com/sekai-lyr/memory-2) | 🛒 二次元电商商城 |
| [`memory-3`](https://github.com/sekai-lyr/memory-3) | 📔 二次元回忆录 |
| [`memory-4`](https://github.com/sekai-lyr/memory-4) | 🎋 角色抽签与今日运势 |
| [`memory-5`](https://github.com/sekai-lyr/memory-5) | 🎵 歌词同步音乐播放器 |
| [`memory-6`](https://github.com/sekai-lyr/memory-6) | 🎮 原创角色合成小游戏 |
| [`memory-7`](https://github.com/sekai-lyr/memory-7) | 🖼️ 二次元壁纸图库系统 |
| [`memory-8`](https://github.com/sekai-lyr/memory-8) | 📖 英语单词学习系统 |
| [`memory-9`](https://github.com/sekai-lyr/memory-9) | 📝 表单收集与 AI 对话系统 |
| [`memory-10`](https://github.com/sekai-lyr/memory-10) | 📺 动漫视频导航站 |
| [`memory-11`](https://github.com/sekai-lyr/memory-11) | ⚔️ 游戏王规则卡牌对战 |
| [`memory-12`](https://github.com/sekai-lyr/memory-12) | 🎓 游戏化学习平台 |
| [`memory-13`](https://github.com/sekai-lyr/memory-13) | 🎭 Ren'Py 视觉小说 |
| [`memory-14`](https://github.com/sekai-lyr/memory-14) | 🤖 识番推荐与 RAG 助手平台 |

📦 **一站式浏览全部项目**：[sekai-lyr/sekai-product](https://github.com/sekai-lyr/sekai-product)（monorepo 合集）

<div align="center">

**如果这个项目对你有帮助或启发，欢迎点一个 ⭐ Star 支持一下！**

*Made with ❤️ by [sekai-lyr](https://github.com/sekai-lyr)*

</div>
