# AI Skills 技能库文档

## 📋 文档说明

本文档整理了当前可用的所有AI Skills，详细说明每个skill的作用、触发条件、使用场景和使用顺序建议。

---

## 🎯 Skills总览

| 序号 | Skill名称 | 分类 | 优先级 | 核心作用 |
| --- | --- | --- | --- | --- |
| 1 | systematic-debugging | 调试 | P0 | 系统化调试和问题排查 |
| 2 | requesting-code-review | 代码质量 | P0 | 代码提交前审查 |
| 3 | ui-ux-pro-max | 前端设计 | P0 | UI/UX专业设计 |
| 4 | frontend-design | 前端设计 | P0 | 前端开发设计 |
| 5 | receiving-code-review | 代码质量 | P1 | 处理代码审查反馈 |
| 6 | test-driven-development | 测试 | P1 | 测试驱动开发 |
| 7 | webapp-testing | 测试 | P1 | Web应用测试 |
| 8 | brainstorming | 创意 | P1 | 头脑风暴和创意发散 |
| 9 | executing-plans | 项目管理 | P1 | 执行复杂项目计划 |
| 10 | verification-before-completion | 质量保证 | P1 | 完成前验证 |
| 11 | docx | 文档 | P2 | Word文档处理 |
| 12 | xlsx | 文档 | P2 | Excel表格处理 |
| 13 | pptx | 文档 | P2 | PowerPoint演示文稿 |
| 14 | pdf | 文档 | P2 | PDF文档处理 |
| 15 | doc-coauthoring | 文档 | P2 | 文档协作编写 |
| 16 | writing-plans | 项目管理 | P2 | 编写项目计划 |
| 17 | ckm:ui-styling | 前端设计 | P2 | UI样式定制 |
| 18 | canvas-design | 设计 | P3 | Canvas视觉设计 |
| 19 | ckm:design | 设计 | P3 | 综合设计 |
| 20 | redesign-existing-projects | 设计 | P3 | 重新设计现有项目 |

---

## 🔍 调试和问题排查类

### 1. systematic-debugging（系统化调试）

**作用**：
- 系统化排查错误和异常
- 定位问题根因
- 提供调试方案和修复建议
- 分析运行时问题
- 性能问题诊断

**触发条件**：
- 遇到系统错误、异常、bug时
- 功能不工作或行为异常时
- 性能问题需要定位时
- 前后端联调出现问题时
- 数据库连接异常时

**使用场景示例**：
```
场景1：后端接口报错
用户：考勤打卡接口返回500错误
→ 触发 systematic-debugging
→ 系统化排查：检查日志、参数校验、数据库连接、事务管理等

场景2：前端功能异常
用户：考勤日历显示不正常
→ 触发 systematic-debugging
→ 分析数据加载、渲染逻辑、API调用等

场景3：性能问题
用户：系统响应很慢
→ 触发 systematic-debugging
→ 分析SQL查询、缓存使用、并发问题等
```

**使用顺序**：建议第一个使用，在问题排查时优先触发

---

### 2. receiving-code-review（接收代码审查）

**作用**：
- 分析代码审查反馈
- 理解审查意见
- 实施代码改进
- 学习最佳实践
- 与审查者沟通

**触发条件**：
- 收到代码审查反馈时
- 需要改进代码质量时
- 不理解审查意见时
- 需要与团队讨论时

**使用场景示例**：
```
场景1：收到权限控制的审查意见
用户：收到了关于权限控制的代码审查意见
→ 触发 receiving-code-review
→ 分析反馈、实施改进、确保符合规范

场景2：需要解释自己的实现
用户：需要解释为什么这样实现权限控制
→ 触发 receiving-code-review
→ 提供技术说明和权衡分析
```

---

## 💻 代码质量类

### 3. requesting-code-review（请求代码审查）

**作用**：
- 代码提交前的自我审查
- 发现潜在问题
- 优化代码质量
- 检查安全性
- 确保符合规范

**触发条件**：
- 代码提交前
- 功能开发完成，准备提交时
- 重构代码后
- 修复bug后

**使用场景示例**：
```
场景1：准备提交代码
用户：准备提交考勤模块代码
→ 触发 requesting-code-review
→ 全面检查代码规范性、安全性、性能、可维护性

场景2：代码重构后
用户：重构了权限控制模块
→ 触发 requesting-code-review
→ 验证功能完整性、安全性、性能影响
```

**使用顺序**：建议提交前使用，确保代码质量

---

### 4. test-driven-development（测试驱动开发）

**作用**：
- 先写测试用例再实现功能
- 保证代码覆盖率
- 防止功能回归
- 提高代码质量
- TDD流程指导

**触发条件**：
- 开发新功能时
- 需要编写单元测试
- 需要编写集成测试
- 重构时保证功能完整
- 需要保证测试覆盖率

**使用场景示例**：
```
场景1：为新功能写测试
用户：需要为考勤统计功能写单元测试
→ 触发 test-driven-development
→ 先写测试用例，再实现功能，保证覆盖率

场景2：重构时保证功能
用户：需要重构节假日判断逻辑
→ 触发 test-driven-development
→ 先写测试用例，重构后确保测试通过
```

---

## 🎨 前端设计类

### 5. ui-ux-pro-max（UI/UX专业设计）

**作用**：
- 专业UI设计建议
- 用户体验优化
- 复杂布局设计
- 数据可视化设计
- 设计规范指导
- 响应式布局方案

**触发条件**：
- 需要设计新页面时
- 优化现有UI时
- 复杂布局问题
- 数据可视化需求
- 设计规范咨询

**使用场景示例**：
```
场景1：设计新页面
用户：需要设计一个数据统计页面
→ 触发 ui-ux-pro-max
→ 提供专业的布局建议、组件选择、数据展示方案

场景2：优化现有UI
用户：考勤管理页面加载慢，需要优化
→ 触发 ui-ux-pro-max
→ 分析性能瓶颈，提供优化方案

场景3：复杂表单设计
用户：需要设计一个复杂的查询表单
→ 触发 ui-ux-pro-max
→ 提供表单布局、验证、用户体验优化建议
```

**使用顺序**：前端开发首选，特别是在UI/UX相关问题时

---

### 6. frontend-design（前端开发设计）

**作用**：
- 前端页面开发
- 组件设计
- 交互逻辑实现
- 技术选型建议
- 前端问题解决

**触发条件**：
- 开发新的前端页面
- 需要前端技术咨询
- 前端兼容性问题
- 组件设计
- 路由设计

**使用场景示例**：
```
场景1：创建新页面
用户：需要添加一个新的统计页面
→ 触发 frontend-design
→ 设计页面结构、组件选择、交互逻辑

场景2：前端问题
用户：Vue组件间通信有问题
→ 触发 frontend-design
→ 分析问题、提供解决方案

场景3：技术选型
用户：需要选择图表库
→ 触发 frontend-design
→ 对比ECharts、Chart.js等，提供推荐
```

---

### 7. ckm:ui-styling（UI样式定制）

**作用**：
- Element Plus组件样式定制
- 主题颜色配置
- 暗色模式实现
- CSS动画效果
- 样式规范指导

**触发条件**：
- 需要定制主题色
- 实现暗色模式
- 添加动画效果
- 样式不生效问题
- 响应式样式问题

**使用场景示例**：
```
场景1：定制主题色
用户：把系统主题色改为公司品牌色
→ 触发 ckm:ui-styling
→ 提供完整的主题定制方案和CSS变量配置

场景2：实现暗色模式
用户：需要添加暗色模式支持
→ 触发 ckm:ui-styling
→ 提供CSS变量、主题切换方案

场景3：动画效果
用户：需要添加按钮点击动画
→ 触发 ckm:ui-styling
→ 提供CSS动画实现方案
```

---

## 🧪 测试类

### 8. webapp-testing（Web应用测试）

**作用**：
- 前端功能测试
- 用户交互测试
- 表单验证测试
- 浏览器兼容性测试
- 自动化测试脚本

**触发条件**：
- 功能测试需求
- 用户流程测试
- 表单验证测试
- 跨浏览器测试
- 自动化测试

**使用场景示例**：
```
场景1：功能测试
用户：测试考勤打卡功能
→ 触发 webapp-testing
→ 模拟用户操作，验证功能正确性

场景2：跨浏览器测试
用户：需要在不同浏览器测试
→ 触发 webapp-testing
→ 提供跨浏览器测试方案

场景3：表单验证
用户：测试会餐记录的表单验证
→ 触发 webapp-testing
→ 验证所有验证规则是否生效
```

---

## 💡 创意和项目管理类

### 9. brainstorming（头脑风暴）

**作用**：
- 功能设计讨论
- 技术方案探索
- 问题解决方案
- 需求分析
- 创意发散

**触发条件**：
- 新功能设计阶段
- 技术方案选型
- 需求不明确时
- 需要多种解决方案
- 团队讨论前准备

**使用场景示例**：
```
场景1：新功能设计
用户：讨论采购管理模块的功能设计
→ 触发 brainstorming
→ 多角度分析需求，提供多种实现方案

场景2：技术选型
用户：不确定用Redis还是本地缓存
→ 触发 brainstorming
→ 分析两种方案的优缺点、适用场景

场景3：需求分析
用户：需求文档不够详细
→ 触发 brainstorming
→ 深入分析需求，补充细节
```

**使用顺序**：建议设计阶段使用，在具体开发前触发

---

### 10. executing-plans（执行计划）

**作用**：
- 复杂任务分解
- Sprint计划执行
- 项目里程碑跟踪
- 任务优先级排序
- 进度监控

**触发条件**：
- 复杂任务需要分解
- Sprint计划执行
- 多模块并行开发
- 项目进度跟踪
- 里程碑管理

**使用场景示例**：
```
场景1：复杂任务分解
用户：需要完成节假日管理模块的开发
→ 触发 executing-plans
→ 将任务分解为：需求分析、数据库设计、后端开发、前端开发等步骤

场景2：Sprint计划执行
用户：这周需要完成考勤统计功能
→ 触发 executing-plans
→ 分解为每天的任务，设置检查点

场景3：多模块并行
用户：同时开发考勤和会餐两个模块
→ 触发 executing-plans
→ 合理分配资源，设定里程碑
```

---

### 11. verification-before-completion（完成前验证）

**作用**：
- 功能完整性验证
- 代码质量验证
- 文档完整性验证
- 测试覆盖验证
- 部署准备验证

**触发条件**：
- 功能开发完成
- 准备交付前
- 发布前检查
- Sprint结束前
- 里程碑达成后

**使用场景示例**：
```
场景1：功能完成后验证
用户：节假日管理模块开发完成
→ 触发 verification-before-completion
→ 验证所有功能、检查文档、更新进度

场景2：发布前检查
用户：准备发布新版本
→ 触发 verification-before-completion
→ 完整性检查、测试通过、文档更新

场景3：Sprint结束验证
用户：这周Sprint结束了
→ 触发 verification-before-completion
→ 验证所有任务、检查代码质量、更新文档
```

---

### 12. writing-plans（编写计划）

**作用**：
- 项目计划编写
- Sprint计划
- 开发计划
- 测试计划
- 文档结构规划

**触发条件**：
- 项目启动时
- Sprint开始前
- 需要编写计划文档
- 任务分配
- 时间估算

**使用场景示例**：
```
场景1：项目启动计划
用户：第二期项目要开始了
→ 触发 writing-plans
→ 编写完整的项目计划、里程碑、资源分配

场景2：Sprint计划
用户：下周的Sprint计划
→ 触发 writing-plans
→ 编写Sprint计划、任务分配、时间估算
```

---

## 📄 文档处理类

### 13. docx（Word文档）

**作用**：
- 生成Word文档
- 技术设计文档
- 用户手册
- 操作指南
- 会议纪要

**触发条件**：
- 需要生成Word文档
- 编写技术文档
- 生成用户手册
- 编写操作指南
- 导出项目资料

**使用场景示例**：
```
场景1：生成技术文档
用户：生成一份系统架构设计文档
→ 触发 docx
→ 创建专业的Word文档，包含图表、目录等

场景2：用户手册
用户：需要一份考勤系统使用手册
→ 触发 docx
→ 创建完整的用户操作手册
```

---

### 14. xlsx（Excel表格）

**作用**：
- 数据统计报表
- 考勤汇总表
- 财务对账表
- 数据批量导入模板
- 数据分析

**触发条件**：
- 需要导出Excel
- 生成统计报表
- 创建数据模板
- 数据分析需求
- 批量导入功能

**使用场景示例**：
```
场景1：考勤统计报表
用户：导出一份月度考勤统计表
→ 触发 xlsx
→ 创建包含员工姓名、部门、正常上班天数等列的Excel表

场景2：数据导入模板
用户：需要创建节假日数据导入模板
→ 触发 xlsx
→ 创建包含必填字段、格式说明的Excel模板
```

---

### 15. pptx（PowerPoint演示）

**作用**：
- 项目汇报演示
- 功能演示PPT
- 培训课件
- 周会月会汇报

**触发条件**：
- 项目汇报
- 功能演示
- 培训需求
- 周会/月会汇报
- 客户演示

**使用场景示例**：
```
场景1：项目汇报
用户：制作项目进度汇报PPT
→ 触发 pptx
→ 创建包含项目概述、已完成功能、进行中功能等幻灯片

场景2：功能演示
用户：演示新开发的节假日管理功能
→ 触发 pptx
→ 创建功能演示PPT
```

---

### 16. pdf（PDF文档）

**作用**：
- PDF生成
- PDF合并
- 内容提取
- 表单填写
- 文档转换

**触发条件**：
- 需要生成PDF
- 文档格式转换
- PDF内容提取
- PDF表单处理
- 多PDF合并

**使用场景示例**：
```
场景1：生成PDF
用户：把考勤统计导出为PDF
→ 触发 pdf
→ 生成包含考勤汇总数据的PDF文件

场景2：文档转换
用户：把Word文档转为PDF
→ 触发 pdf
→ 转换为PDF格式
```

---

### 17. doc-coauthoring（文档协作）

**作用**：
- 需求文档编写
- 技术设计文档
- API接口文档
- 项目文档协作
- 文档结构优化

**触发条件**：
- 编写需求文档
- 技术文档编写
- API文档编写
- 多方协作文档
- 文档优化

**使用场景示例**：
```
场景1：需求文档
用户：编写节假日管理模块的需求文档
→ 触发 doc-coauthoring
→ 系统化编写结构化文档，确保完整性

场景2：API文档
用户：编写考勤模块的API文档
→ 触发 doc-coauthoring
→ 创建完整的API接口文档
```

---

## 🎨 设计类

### 18. canvas-design（Canvas视觉设计）

**作用**：
- 海报设计
- 流程图
- 架构图
- 图标设计
- 数据图表

**触发条件**：
- 需要视觉设计
- 流程图设计
- 架构图设计
- 图标设计
- 数据可视化

**使用场景示例**：
```
场景1：架构图
用户：需要一张系统架构图
→ 触发 canvas-design
→ 生成专业的架构图或流程图

场景2：流程图
用户：需要请假审批流程图
→ 触发 canvas-design
→ 创建清晰的流程图
```

---

### 19. ckm:design（综合设计）

**作用**：
- 品牌设计
- 视觉识别
- 设计系统
- Logo设计
- 综合设计咨询

**触发条件**：
- 品牌设计需求
- 视觉识别系统
- 设计系统建立
- Logo设计
- 设计咨询

**使用场景示例**：
```
场景1：品牌设计
用户：需要设计公司OA系统的品牌
→ 触发 ckm:design
→ 提供完整的品牌设计方案

场景2：设计系统
用户：需要建立设计系统
→ 触发 ckm:design
→ 提供设计系统架构和规范
```

---

### 20. redesign-existing-projects（重新设计现有项目）

**作用**：
- 项目升级
- UI重构
- 架构优化
- 设计现代化
- 性能优化

**触发条件**：
- 项目重构
- UI升级
- 架构优化
- 设计过时
- 性能问题

**使用场景示例**：
```
场景1：UI重构
用户：需要重构现有的考勤管理界面
→ 触发 redesign-existing-projects
→ 提供完整的重构方案和实施计划

场景2：架构升级
用户：需要升级前端架构
→ 触发 redesign-existing-projects
→ 提供架构升级方案和迁移计划
```

---

## 📊 使用优先级和顺序

### 按优先级排序

| 优先级 | Skill | 场景 |
| --- | --- | --- |
| P0 | systematic-debugging | 问题排查，优先使用 |
| P0 | requesting-code-review | 代码提交前 |
| P0 | ui-ux-pro-max | UI/UX设计 |
| P0 | frontend-design | 前端开发 |
| P1 | receiving-code-review | 处理审查反馈 |
| P1 | test-driven-development | 测试开发 |
| P1 | webapp-testing | 功能测试 |
| P1 | brainstorming | 创意设计 |
| P1 | executing-plans | 计划执行 |
| P1 | verification-before-completion | 完成前验证 |
| P2 | docx/xlsx/pptx/pdf | 文档生成 |
| P2 | doc-coauthoring | 文档协作 |
| P2 | writing-plans | 计划编写 |
| P2 | ckm:ui-styling | 样式定制 |
| P3 | canvas-design | 视觉设计 |
| P3 | ckm:design | 综合设计 |
| P3 | redesign-existing-projects | 项目重构 |

### 按开发阶段使用顺序

#### 阶段1：需求和设计
1. brainstorming - 需求分析
2. writing-plans - 编写计划
3. frontend-design - 技术设计
4. ui-ux-pro-max - UI设计

#### 阶段2：开发
5. frontend-design - 前端开发
6. test-driven-development - 测试开发
7. systematic-debugging - 问题调试（如有）

#### 阶段3：测试和优化
8. webapp-testing - 功能测试
9. requesting-code-review - 代码审查
10. ckm:ui-styling - 样式优化（如需要）

#### 阶段4：完成和交付
11. verification-before-completion - 完成验证
12. receiving-code-review - 处理审查（如有）
13. docx/xlsx/pptx - 文档生成
14. executing-plans - 进度跟踪

---

## 🎯 按模块推荐Skills

### 考勤管理模块
| 场景 | 推荐Skill | 说明 |
| --- | --- | --- |
| UI优化 | ui-ux-pro-max | 优化考勤日历布局 |
| 权限控制 | systematic-debugging | 排查权限问题 |
| 报表导出 | xlsx/pdf | 导出统计报表 |
| 批量操作 | frontend-design | 日期范围选择 |
| 测试 | test-driven-development | 编写测试用例 |

### 会餐费管理模块
| 场景 | 推荐Skill | 说明 |
| --- | --- | --- |
| 表单设计 | frontend-design | 设计会餐记录表单 |
| 统计功能 | ui-ux-pro-max | 设计统计页面 |
| 数据导出 | xlsx | 导出费用统计 |
| 表单验证 | webapp-testing | 测试验证规则 |

### 节假日管理模块
| 场景 | 推荐Skill | 说明 |
| --- | --- | --- |
| 批量创建 | frontend-design | 日期范围功能 |
| UI优化 | ui-ux-pro-max | 节假日列表展示 |
| 数据初始化 | mcp_MySQL_execute_sql | 数据库操作 |

### 权限管理模块
| 场景 | 推荐Skill | 说明 |
| --- | --- | --- |
| 安全检查 | requesting-code-review | 审查权限代码 |
| 问题调试 | systematic-debugging | 排查权限问题 |
| 设计讨论 | brainstorming | 权限方案讨论 |

---

## 💡 使用技巧

### 1. Skill组合使用
```
示例：开发新功能
1. brainstorming → 确定方案
2. writing-plans → 编写计划
3. frontend-design → 开发页面
4. test-driven-development → 编写测试
5. requesting-code-review → 检查代码
6. verification-before-completion → 验证完成
```

### 2. 优先使用专业Skill
- UI问题 → ui-ux-pro-max
- 前端开发 → frontend-design
- 调试问题 → systematic-debugging
- 代码质量 → requesting-code-review

### 3. 按场景选择
- 问题排查 → 从 systematic-debugging 开始
- 新功能开发 → 从 brainstorming 开始
- 代码提交 → 使用 requesting-code-review
- 功能测试 → 使用 webapp-testing

---

## ⚠️ 注意事项

1. 按需触发：不要过度使用，选择最适合当前场景的Skill
2. 按顺序使用：复杂任务建议按顺序使用多个Skills
3. 优先级顺序：P0优先使用，P1、P2、P3依次降低
4. 组合使用：多个Skills可以组合使用，解决复杂问题
5. 质量保证：使用 requesting-code-review 确保代码质量
6. 文档同步：使用 doc-coauthoring 保持文档同步更新
7. 测试验证：使用 test-driven-development 和 webapp-testing 保证功能正确性

---

## 📝 附录：Skills快速参考卡

### P0 - 必备Skills（遇到相关问题立即触发）

| 场景 | Skill | 触发关键词 |
| --- | --- | --- |
| 系统错误 | systematic-debugging | 报错、异常、不工作 |
| 代码审查 | requesting-code-review | 提交代码、准备完成 |
| UI设计 | ui-ux-pro-max | 设计、优化界面、布局 |
| 前端开发 | frontend-design | 开发页面、前端问题、组件 |

### P1 - 重要Skills（推荐使用）

| 场景 | Skill | 触发关键词 |
| --- | --- | --- |
| 测试开发 | test-driven-development | 写测试、TDD、测试用例 |
| 功能测试 | webapp-testing | 测试功能、测试一下 |
| 头脑风暴 | brainstorming | 讨论、设计、方案 |
| 执行计划 | executing-plans | 计划、分解任务、Sprint |
| 验证 | verification-before-completion | 验证、完成、检查 |

### P2 - 常用Skills（按需使用）

| 场景 | Skill | 触发关键词 |
| --- | --- | --- |
| Word文档 | docx | Word、文档、手册 |
| Excel表格 | xlsx | Excel、表格、报表 |
| PPT演示 | pptx | PPT、演示、汇报 |
| PDF文档 | pdf | PDF、转换 |
| 文档协作 | doc-coauthoring | 写文档、需求文档 |
| 样式定制 | ckm:ui-styling | 样式、主题、颜色 |

---

**文档创建时间**：2026-04-19
**最后更新**：2026-04-19
**维护者**：Claude AI助手
**版本**：V1.0
