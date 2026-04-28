# 企业OA管理系统 - AI助手指南

## 一、Skill 调用规范

- 对话开始时必须调用 `skill using-superpowers`，不可跳过
- 构建或修改 Web 组件/页面/应用程序/网站 时必须调用 `skill frontend-design`，不可跳过

## 二、MCP 调用规范

- DB相关操作必须调用 `MySQL MCP`，不可跳过
- 涉及项目技术栈的 API 使用、配置项、版本特性、框架规则、最佳实践、错误排查必须调用 `Context7 MCP`，不可跳过
- 每次完成任务时自动记录工作内容，使用 `Memory MCP`

## 三、项目概述

**技术栈**：Spring Boot + MyBatis + MySQL + JWT | Vue 3 + Element Plus + Vite

**第一期已完成**：
- 用户登录认证（JWT）
- 用户管理、部门管理、角色权限管理（RBAC）
- 考勤管理（CRUD、统计、导出）
- 会餐费管理（CRUD、统计）
- 数据字典管理、节假日管理

**第二期规划**：采购管理、物品管理、库存管理、领取管理、盘点管理

## 四、核心规则

### 4.1 文档关联（必读）
- **综合需求文档.md**：核心需求文档，所有开发以它为上下文

### 4.2 权限管理
- 核心注解：`@CurrentUser`、`@RequirePermission`、`@OwnerOnly`
- 考勤权限：ATT_VIEW_OWN、ATT_EDIT_OWN、ATT_VIEW_ALL、ATT_EXPORT
- 详细定义见综合需求文档3.5.10.7节

### 4.3 数据库规范
- **外键约束在Java代码中实现，不在数据库建表时添加**
- 使用MyBatis ORM，参考schema.sql

### 4.4 数据字典
- 涉及代码类数据必须使用DictItem
- 避免硬编码，使用字典code进行业务判断

### 4.5 响应规范
- 所有回答用中文
- **不要自动启动服务，用户手动启动**
- **不要自动提交代码，用户手动提交**

## 五、权限代码

| 模块 | 权限代码 | 适用角色 |
| --- | --- | --- |
| 考勤 | ATT_VIEW_OWN/EDIT_OWN/DELETE_OWN | 所有用户 |
| 考勤 | ATT_VIEW_ALL、ATT_EXPORT | 管理员 |
| 会餐 | DNM_VIEW/ADD/EDIT | 所有用户 |
| 会餐 | DNM_DELETE | 管理员 |
| 系统 | SYS_USER_VIEW/EDIT | 管理员 |
| 系统 | SYS_DEPT_VIEW/EDIT | 管理员 |
| 系统 | SYS_ROLE_VIEW/EDIT | 管理员 |
| 系统 | SYS_DICT_VIEW/EDIT | 管理员 |

## 六、API规范

### 响应格式
```java
Result<T> { code: 200, message: "", data: T }
```

### 路径规范
- GET `/api/{module}/list` - 查询列表
- GET `/api/{module}/{id}` - 查询单个
- POST `/api/{module}/create` - 创建
- PUT `/api/{module}/update/{id}` - 更新
- DELETE `/api/{module}/delete/{id}` - 删除
- POST `/api/{module}/batch` - 批量操作

## 七、项目架构

```
backend/src/main/java/com/oa/generalos/
├── annotation/     # @CurrentUser, @OwnerOnly, @RequirePermission
├── aspect/         # PermissionAspect, PermissionChecker
├── controller/     # AttendanceController, AuthController, UserController等
├── service/impl/   # 业务实现
├── mapper/         # MyBatis Mapper
├── security/       # JwtAuthenticationFilter, PermissionInterceptor
└── utils/          # JwtUtils

frontend/src/
├── api/           # API封装
├── views/         # Vue组件
├── router/        # 路由配置
└── stores/        # 状态管理
```

## 八、命名规范

| 类型 | 规范 | 示例 |
| --- | --- | --- |
| 数据库表 | 小写下划线 | attendance, sys_user |
| Java类 | 大驼峰 | AttendanceController |
| Java方法/变量 | 小驼峰 | getAttendanceList, userId |
| 前端变量 | 小驼峰 | userList, isLoading |
| API路径 | 小写连字符 | /api/attendance-list |

## 九、数据库表（外键在Java中实现）

核心表：sys_user, sys_department, sys_role, sys_permission, sys_user_role, sys_role_permission, attendance, dinner_record, dict_category, dict_item, holiday

详细表结构参考 `backend/src/main/resources/schema.sql`

## 十、常用命令

```bash
# 后端启动
cd backend && mvn spring-boot:run

# 前端启动
cd frontend && npm run dev

# Git操作
git add . && git commit -m "描述" && git push
```

## 十一、重要提醒

1. **开发前必读**：综合需求文档.md
4. **权限开发必参考**：综合需求文档.md 权限管理模块
5. **不要自动启动服务**
6. **不要自动提交代码**