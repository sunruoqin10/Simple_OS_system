# 企业OA管理系统 - AI助手指南
## 一、Skill 调用规范

### 对话开始时 必须调用skill using-superpowers，不可跳过
### 构建或修改 Web 组件/页面/应用程序/网站 时 必须调用 skill frontend-design,不可跳过

## 一、Mcp 调用规范
### DB相关的操作 必须调用 MySQL MCP,不可跳过
### 涉及项目技术栈的 API 使用、配置项、版本特性、框架规则、最佳实践、错误排查 必须调用 Context7 MCP ,不可跳过
### 每次完成任务时自动记录工作内容，工作内容的记录使用 Memory MCP,不可跳过

## 📋 项目概述

**系统名称**：企业OA管理系统
**当前版本**：V1.0
**技术栈**：
- 后端：Spring Boot + MyBatis + MySQL + JWT
- 前端：Vue 3 + Element Plus + Vite
- 数据库：MySQL

**核心功能模块**（第一期）：
- ✅ 用户登录认证（JWT）
- ✅ 用户管理（CRUD）
- ✅ 部门管理（CRUD）
- ✅ 角色权限管理（RBAC）
- ✅ 考勤管理（CRUD、统计、导出）
- ✅ 会餐费管理（CRUD、统计）
- ✅ 数据字典管理（CRUD）
- ✅ 节假日管理（CRUD、日期范围批量创建）

**第二期规划**：
- ⏳ 采购管理
- ⏳ 物品管理
- ⏳ 库存管理
- ⏳ 领取管理
- ⏳ 盘点管理

---

## 🎯 核心开发规范

### 1. 必须遵守的规则

所有AI助手在项目开发和分析时**必须**遵循以下规则：

#### 1.1 文档关联（必读）
- **综合需求文档.md** 是项目的核心需求文档，所有开发和分析都需要以它为上下文
- **项目进度文档.md** 记录每个模块的开发进度，新功能开发后必须更新
- **开发问题记录.md** 记录开发过程中遇到的问题和解决方案
- 开发涉及权限管理时，必须参考综合需求文档中的权限管理模块说明

#### 1.2 权限管理规范
- 考勤模块权限：ATT_VIEW_OWN、ATT_EDIT_OWN、ATT_VIEW_ALL、ATT_EXPORT
- 权限注解：@RequirePermission、@OwnerOnly、@CurrentUser
- 权限检查器：PermissionChecker、PermissionAspect
- 详细权限定义见综合需求文档3.5.10.7节

#### 1.3 数据库设计规范
- **外键约束必须在Java代码中实现，不要在数据库建表时添加外键约束**
- 使用MyBatis ORM，支持XML映射文件和注解两种方式
- 表结构设计参考schema.sql

#### 1.4 数据字典使用规范
- 涉及代码类数据必须使用数据字典（DictItem）
- 字典项包含：考勤状态（ATT）、会餐类型等
- 避免硬编码，使用字典的code进行业务判断
- 数据字典配置见综合需求文档3.4节

#### 1.5 响应和提交规范
- **所有回答必须使用中文**
- **不要自动启动前端和后端服务，由用户手动启动**
- **不要自动提交代码，由用户手动提交**
- 提交代码前确保代码质量和功能完整性

### 2. 代码风格规范

#### 2.1 后端代码风格
```java
// 使用Lombok简化代码
@Data
public class User {
    private Long id;
    private String username;
}

// 使用@CurrentUser获取当前用户ID
@GetMapping("/attendance")
public Result<List<Attendance>> getAttendance(@CurrentUser Long userId) {
    // 直接使用userId，无需手动从SecurityContext获取
}

// 权限检查使用@RequirePermission注解
@RequirePermission("ATT_VIEW_ALL")
@GetMapping("/attendance/all")
public Result<List<Attendance>> getAllAttendance() {
    // 自动权限检查
}

// 数据归属检查使用@OwnerOnly注解
@DeleteMapping("/attendance/{id}")
@RequirePermission("ATT_EDIT_OWN")
public Result<Void> deleteAttendance(@PathVariable Long id, @OwnerOnly Attendance attendance) {
    // 自动检查当前用户是否是数据的拥有者
}
```

#### 2.2 前端代码风格
```vue
<!-- 使用Element Plus组件 -->
<template>
  <el-table :data="tableData" v-loading="loading">
    <el-table-column prop="name" label="名称" />
  </el-table>
</template>

<script setup>
// 使用组合式API
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'

// 权限控制
const hasPermission = (code) => {
  const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')
  return permissions.includes(code)
}
</script>
```

#### 2.3 命名规范
| 类型 | 规范 | 示例 |
| --- | --- | --- |
| 数据库表 | 小写下划线 | attendance, sys_user |
| Java类 | 大驼峰 | AttendanceController |
| Java方法 | 小驼峰 | getAttendanceList |
| Java变量 | 小驼峰 | userId, userName |
| 前端变量 | 小驼峰 | userList, isLoading |
| API路径 | 小写连字符/小驼峰 | /api/attendance/list |

---

## 🏗️ 项目架构

### 1. 后端架构

```
backend/
├── src/main/java/com/oa/generalos/
│   ├── annotation/          # 自定义注解
│   │   ├── CurrentUser.java          # 注入当前用户ID
│   │   ├── OwnerOnly.java            # 数据归属校验
│   │   └── RequirePermission.java    # 权限检查
│   ├── aspect/             # AOP切面
│   │   ├── PermissionAspect.java      # 权限检查切面
│   │   └── PermissionChecker.java    # 权限检查器
│   ├── config/             # 配置类
│   │   ├── SecurityConfig.java        # Spring Security配置
│   │   └── WebMvcConfig.java         # Web MVC配置
│   ├── controller/         # 控制器层
│   │   ├── AttendanceController.java # 考勤管理
│   │   ├── AuthController.java       # 认证
│   │   ├── DepartmentController.java # 部门管理
│   │   ├── DictController.java       # 数据字典
│   │   ├── DinnerRecordController.java # 会餐费管理
│   │   ├── HolidayController.java    # 节假日管理
│   │   ├── RoleController.java       # 角色管理
│   │   └── UserController.java      # 用户管理
│   ├── entity/             # 实体类
│   ├── dto/                # 数据传输对象
│   ├── vo/                 # 视图对象
│   ├── service/            # 业务逻辑层
│   │   └── impl/          # 业务实现
│   ├── mapper/             # 数据访问层
│   ├── resolver/           # 参数解析器
│   │   └── CurrentUserArgumentResolver.java # @CurrentUser解析
│   ├── security/           # 安全相关
│   │   ├── JwtAuthenticationFilter.java     # JWT认证过滤器
│   │   ├── PermissionInterceptor.java       # 权限拦截器
│   │   └── SecurityContext.java             # 安全上下文
│   ├── exception/          # 异常处理
│   └── utils/              # 工具类
│       └── JwtUtils.java            # JWT工具
```

### 2. 前端架构

```
frontend/src/
├── api/                   # API接口封装
│   ├── attendance.js     # 考勤API
│   ├── auth.js           # 认证API
│   ├── dict.js           # 数据字典API
│   ├── holiday.js        # 节假日API
│   └── ...
├── router/
│   └── index.js          # 路由配置
├── stores/
│   └── user.js           # 用户状态管理
├── views/
│   ├── attendance/
│   │   └── AttendanceView.vue    # 考勤管理页面
│   ├── system/
│   │   ├── DepartmentView.vue    # 部门管理
│   │   ├── DictView.vue          # 数据字典
│   │   ├── HolidayView.vue       # 节假日管理
│   │   ├── RoleView.vue          # 角色管理
│   │   └── UserView.vue          # 用户管理
│   ├── dinner/
│   │   └── DinnerRecordView.vue  # 会餐费管理
│   ├── layout/
│   │   └── MainLayout.vue        # 主布局
│   └── login/
│       └── LoginView.vue         # 登录页面
├── utils/
│   └── request.js        # Axios封装
└── main.js               # 入口文件
```

---

## 🔐 权限管理系统

### 1. 权限体系（RBAC）

**核心组件**：
- @CurrentUser：自动注入当前用户ID
- @RequirePermission：接口权限检查
- @OwnerOnly：数据归属校验
- PermissionInterceptor：请求权限拦截
- PermissionAspect：AOP权限检查
- SecurityContext：安全上下文

**权限流程**：
1. 用户登录 → JwtAuthenticationFilter验证JWT → PermissionInterceptor加载权限
2. 请求进入 → PermissionAspect拦截 → 检查@RequirePermission注解
3. 数据操作 → @OwnerOnly自动校验数据归属

### 2. 权限代码（完整列表）

| 模块 | 权限代码 | 权限名称 | 适用角色 |
| --- | --- | --- | --- |
| 考勤 | ATT_VIEW_OWN | 查看自己的考勤 | 所有用户 |
| 考勤 | ATT_EDIT_OWN | 修改自己的考勤 | 所有用户 |
| 考勤 | ATT_DELETE_OWN | 删除自己的考勤 | 所有用户 |
| 考勤 | ATT_VIEW_ALL | 查看所有考勤 | 管理员 |
| 考勤 | ATT_EXPORT | 导出考勤报表 | 管理员 |
| 会餐 | DNM_VIEW | 查看会餐记录 | 所有用户 |
| 会餐 | DNM_ADD | 添加会餐记录 | 所有用户 |
| 会餐 | DNM_EDIT | 编辑会餐记录 | 所有用户 |
| 会餐 | DNM_DELETE | 删除会餐记录 | 管理员 |
| 系统 | SYS_USER_VIEW | 查看用户 | 管理员 |
| 系统 | SYS_USER_EDIT | 用户管理 | 管理员 |
| 系统 | SYS_DEPT_VIEW | 查看部门 | 管理员 |
| 系统 | SYS_DEPT_EDIT | 部门管理 | 管理员 |
| 系统 | SYS_ROLE_VIEW | 查看角色 | 管理员 |
| 系统 | SYS_ROLE_EDIT | 角色管理 | 管理员 |
| 系统 | SYS_DICT_VIEW | 查看数据字典 | 管理员 |
| 系统 | SYS_DICT_EDIT | 数据字典管理 | 管理员 |

---

## 📊 数据库设计

### 1. 核心表结构

**sys_user（用户表）**
```sql
CREATE TABLE sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(50) UNIQUE NOT NULL,
    password VARCHAR(255) NOT NULL,
    real_name VARCHAR(50),
    department_id BIGINT,
    status TINYINT DEFAULT 1,
    first_login TINYINT DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME
);
```

**sys_department（部门表）**
```sql
CREATE TABLE sys_department (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    sort_order INT DEFAULT 0,
    created_at DATETIME,
    updated_at DATETIME
);
```

**sys_role（角色表）**
```sql
CREATE TABLE sys_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);
```

**sys_permission（权限表）**
```sql
CREATE TABLE sys_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    created_at DATETIME,
    updated_at DATETIME
);
```

**sys_user_role（用户角色关联表）**
```sql
CREATE TABLE sys_user_role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    created_at DATETIME
);
```

**sys_role_permission（角色权限关联表）**
```sql
CREATE TABLE sys_role_permission (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role_id BIGINT NOT NULL,
    permission_id BIGINT NOT NULL,
    created_at DATETIME
);
```

**attendance（考勤表）**
```sql
CREATE TABLE attendance (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    year INT NOT NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    status VARCHAR(20) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY uk_user_date (user_id, year, month, day)
);
```

**dinner_record（会餐记录表）**
```sql
CREATE TABLE dinner_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    department_id BIGINT,
    record_date DATE NOT NULL,
    participant_count INT,
    amount DECIMAL(10,2),
    purpose VARCHAR(255),
    responsible_person VARCHAR(50),
    invoice_path VARCHAR(255),
    created_at DATETIME,
    updated_at DATETIME
);
```

**dict_category（字典分类表）**
```sql
CREATE TABLE dict_category (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    code VARCHAR(50) UNIQUE NOT NULL,
    name VARCHAR(100) NOT NULL,
    parent_id BIGINT,
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    created_at DATETIME,
    updated_at DATETIME
);
```

**dict_item（字典项表）**
```sql
CREATE TABLE dict_item (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    sort_order INT DEFAULT 0,
    status TINYINT DEFAULT 1,
    extra JSON,
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY uk_category_code (category_id, code)
);
```

**holiday（节假日配置表）**
```sql
CREATE TABLE holiday (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    year INT NOT NULL,
    month INT NOT NULL,
    day INT NOT NULL,
    name VARCHAR(50) NOT NULL,
    type VARCHAR(20) NOT NULL DEFAULT '法定节假日',
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY uk_year_month_day (year, month, day),
    INDEX idx_year (year),
    INDEX idx_type (type)
);
```

### 2. 外键关系（Java中实现）

**重要**：所有外键约束在Java代码中通过Service层逻辑实现，不在数据库表创建时添加外键约束。

| 关系 | 说明 | 实现方式 |
| --- | --- | --- |
| User → Department | 用户属于部门 | UserService校验 |
| User → Role | 用户拥有角色 | UserRoleMapper关联 |
| Role → Permission | 角色拥有权限 | RolePermissionMapper关联 |
| Attendance → User | 考勤属于用户 | AttendanceService校验 |
| DinnerRecord → User | 会餐记录属于用户 | DinnerRecordService校验 |
| DictItem → DictCategory | 字典项属于分类 | DictItemService校验 |

---

## 🛠️ 开发指南

### 1. 新增功能模块流程

#### 1.1 需求分析阶段
1. 阅读综合需求文档.md，确认需求
2. 在项目进度文档.md中创建模块开发计划
3. 在开发问题记录.md中记录开发要点

#### 1.2 数据库设计阶段
1. 在schema.sql中添加表结构
2. 不添加外键约束，在Java中实现
3. 使用MCP工具创建表和初始数据

#### 1.3 后端开发阶段
1. 创建Entity实体类
2. 创建Mapper接口和XML映射
3. 创建Service接口和实现
4. 创建Controller控制器
5. 添加权限注解（如果需要）
6. 添加API接口文档注释

#### 1.4 前端开发阶段
1. 在api/目录创建API封装
2. 在views/目录创建页面组件
3. 在router/index.js添加路由
4. 在MainLayout.vue添加菜单（如果有权限控制）
5. 实现权限控制逻辑

#### 1.5 测试验证阶段
1. 用户手动启动后端和前端
2. 测试功能完整性
3. 测试权限控制
4. 测试边界条件

#### 1.6 文档更新阶段
1. 更新综合需求文档.md（如果需要）
2. 更新项目进度文档.md
3. 更新开发问题记录.md（如果有问题）
4. 提交代码（用户手动提交）

### 2. API接口规范

#### 2.1 响应格式
```java
public class Result<T> {
    private int code;      // 状态码：200成功，其他失败
    private String message; // 消息
    private T data;        // 数据
}
```

#### 2.2 HTTP方法对应
| 操作 | HTTP方法 | 路径规范 |
| --- | --- | --- |
| 查询列表 | GET | /api/{module}/list |
| 查询单个 | GET | /api/{module}/{id} |
| 创建 | POST | /api/{module}/create |
| 更新 | PUT | /api/{module}/update/{id} |
| 删除 | DELETE | /api/{module}/delete/{id} |
| 批量操作 | POST | /api/{module}/batch |

#### 2.3 接口权限
- 所有业务接口需要JWT认证
- 敏感接口需要权限注解
- 使用@RequirePermission("CODE")检查权限
- 使用@OwnerOnly自动检查数据归属

### 3. 前端权限控制

#### 3.1 权限判断函数
```javascript
const hasPermission = (code) => {
  const permissions = JSON.parse(localStorage.getItem('permissions') || '[]')
  return permissions.includes(code)
}
```

#### 3.2 菜单权限控制
```vue
<el-menu-item 
  index="/attendance" 
  v-if="hasPermission('ATT_VIEW_OWN') || hasPermission('ATT_VIEW_ALL')"
>
  考勤管理
</el-menu-item>
```

#### 3.3 按钮权限控制
```vue
<el-button 
  type="danger" 
  @click="handleDelete" 
  v-if="hasPermission('ATT_VIEW_ALL')"
>
  删除
</el-button>
```

---

## 📝 常用命令

### 1. 后端命令
```bash
# 启动后端（用户手动执行）
cd backend
mvn spring-boot:run

# 或使用IDE启动
# 打开GeneralosApplication.java，点击运行
```

### 2. 前端命令
```bash
# 安装依赖（首次）
cd frontend
npm install

# 启动前端（用户手动执行）
npm run dev

# 构建生产版本
npm run build
```

### 3. 数据库命令
```sql
-- 使用MCP工具执行SQL
-- 参考: mcp_MySQL_execute_sql 工具
```

### 4. Git命令
```bash
# 查看状态
git status

# 添加文件
git add .

# 提交代码（用户手动执行）
git commit -m "feat: 描述"

# 推送到远程（用户手动执行）
git push
```

---

## 🔧 工具使用规范

### 1. MCP工具
- **mcp_MySQL_execute_sql**：执行SQL语句，创建表、插入数据
- **mcp_Memory_***：知识图谱管理（可选使用）

### 2. 文件操作
- **Read**：读取文件内容
- **Write**：写入文件（会覆盖原文件）
- **Edit**：编辑文件（推荐，更精确）
- **SearchReplace**：搜索替换（推荐）

### 3. 代码搜索
- **Grep**：搜索代码内容
- **SearchCodebase**：语义搜索代码（推荐）
- **Glob**：按文件名搜索

### 4. 命令执行
- **RunCommand**：执行终端命令
- **CheckCommandStatus**：检查命令状态

---

## 📚 关键文档索引

| 文档 | 路径 | 说明 |
| --- | --- | --- |
| 综合需求文档 | 综合需求文档.md | 核心需求文档，必读 |
| 项目进度文档 | 项目进度文档.md | 模块开发进度跟踪 |
| 开发问题记录 | 开发问题记录.md | 开发问题和解决方案 |
| 项目规则 | .trae/rules/projectneed.md | AI助手必遵守的规则 |
| 数据库表结构 | backend/src/main/resources/schema.sql | 数据库设计 |
| API接口文档 | 综合需求文档.md 各模块接口设计章节 | 接口规范 |

---

## ⚠️ 重要提醒

1. **开发前必读**：综合需求文档.md
2. **开发后必更新**：项目进度文档.md
3. **遇到问题必记录**：开发问题记录.md
4. **权限开发必参考**：综合需求文档.md 权限管理模块
5. **代码规范必遵循**：命名规范、权限注解、数据字典
6. **不要自动启动服务**：由用户手动启动
7. **不要自动提交代码**：由用户手动提交
8. **所有回答用中文**：保持一致的沟通语言

---

## 🎓 学习资源

### 核心技能
- Spring Boot：后端框架
- MyBatis：持久层框架
- Vue 3：前端框架
- Element Plus：UI组件库
- RBAC：权限管理模型
- JWT：身份认证

### 项目特色
- 自定义权限注解（@CurrentUser, @OwnerOnly, @RequirePermission）
- AOP权限检查
- 数据归属校验
- 动态节假日配置
- 日期范围批量操作
- 数据字典驱动业务

---

**最后更新**：2026-04-19
**维护者**：Claude AI助手
**版本**：V1.0
