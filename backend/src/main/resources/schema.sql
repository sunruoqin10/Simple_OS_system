-- =============================================
-- 企业OA系统数据库建表脚本
-- 数据库名: enterprise_oa
-- 创建时间: 2026-04-14
-- =============================================

-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS enterprise_oa DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE enterprise_oa;

-- =============================================
-- 1. 部门表 (department)
-- =============================================
CREATE TABLE IF NOT EXISTS department (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '部门ID',
    name VARCHAR(50) NOT NULL UNIQUE COMMENT '部门名称',
    description VARCHAR(200) COMMENT '部门描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='部门表';

-- =============================================
-- 2. 用户表 (sys_user)
-- =============================================
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) COMMENT '真实姓名',
    role VARCHAR(20) NOT NULL DEFAULT '普通员工' COMMENT '角色：系统管理员、部门主管、普通员工、财务人员',
    department_id BIGINT NOT NULL COMMENT '部门ID',
    email VARCHAR(100) COMMENT '邮箱',
    phone VARCHAR(20) COMMENT '电话',
    status TINYINT DEFAULT 1 COMMENT '状态：1正常，0禁用',
    error_count INT DEFAULT 0 COMMENT '密码错误次数',
    locked_until DATETIME COMMENT '锁定截止时间',
    must_change_password TINYINT DEFAULT 1 COMMENT '是否必须修改密码：1是，0否',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_department_id (department_id),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- =============================================
-- 3. 考勤表 (attendance)
-- =============================================
CREATE TABLE IF NOT EXISTS attendance (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '考勤ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    year INT NOT NULL COMMENT '年份',
    month INT NOT NULL COMMENT '月份',
    day INT NOT NULL COMMENT '日期',
    status VARCHAR(10) NOT NULL DEFAULT '正常上班' COMMENT '考勤状态：正常上班、全天休假、半天休假、加班',
    remark VARCHAR(200) COMMENT '备注',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_user_date (user_id, year, month, day),
    INDEX idx_user_id (user_id),
    INDEX idx_year_month (year, month)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='考勤表';

-- =============================================
-- 4. 会餐费记录表 (dinner_record)
-- =============================================
CREATE TABLE IF NOT EXISTS dinner_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    record_date DATE NOT NULL COMMENT '会餐日期',
    department VARCHAR(50) COMMENT '部门',
    participant_count INT COMMENT '参与人数',
    amount DECIMAL(10,2) COMMENT '金额',
    purpose VARCHAR(200) COMMENT '用途',
    responsible_person VARCHAR(50) COMMENT '负责人',
    invoice_path VARCHAR(500) COMMENT '发票路径',
    created_by BIGINT COMMENT '创建人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_record_date (record_date),
    INDEX idx_department (department),
    INDEX idx_created_by (created_by)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='会餐费记录表';

-- =============================================
-- 5. 物品分类表 (item_category)
-- =============================================
CREATE TABLE IF NOT EXISTS item_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    name VARCHAR(50) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT 0 COMMENT '父分类ID',
    sort_order INT DEFAULT 0 COMMENT '排序',
    description VARCHAR(200) COMMENT '分类描述',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物品分类表';

-- =============================================
-- 6. 物品表 (item)
-- =============================================
CREATE TABLE IF NOT EXISTS item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '物品ID',
    category_id BIGINT COMMENT '分类ID',
    name VARCHAR(100) NOT NULL COMMENT '物品名称',
    spec VARCHAR(100) COMMENT '规格型号',
    unit VARCHAR(20) COMMENT '单位',
    min_stock INT DEFAULT 0 COMMENT '最低库存',
    price DECIMAL(10,2) COMMENT '参考价格',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='物品表';

-- =============================================
-- 7. 采购申请表 (purchase_request)
-- =============================================
CREATE TABLE IF NOT EXISTS purchase_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '申请ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    quantity INT NOT NULL COMMENT '申请数量',
    purpose VARCHAR(200) COMMENT '用途',
    expected_date DATE COMMENT '期望日期',
    status VARCHAR(20) DEFAULT '待审批' COMMENT '状态：待审批、已审批、已拒绝、已采购、已完成',
    applicant_id BIGINT NOT NULL COMMENT '申请人ID',
    approver_id BIGINT COMMENT '审批人ID',
    approve_time DATETIME COMMENT '审批时间',
    reject_reason VARCHAR(200) COMMENT '拒绝原因',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_applicant_id (applicant_id),
    INDEX idx_status (status),
    INDEX idx_item_id (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购申请表';

-- =============================================
-- 8. 采购订单表 (purchase_order)
-- =============================================
CREATE TABLE IF NOT EXISTS purchase_order (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '订单ID',
    request_id BIGINT NOT NULL COMMENT '采购申请ID',
    supplier VARCHAR(100) COMMENT '供应商',
    price DECIMAL(10,2) COMMENT '采购价格',
    order_no VARCHAR(50) COMMENT '订单号',
    expected_date DATE COMMENT '期望日期',
    actual_date DATE COMMENT '实际到货日期',
    status VARCHAR(20) DEFAULT '待采购' COMMENT '状态：待采购、采购中、已到货、已入库',
    operator_id BIGINT COMMENT '操作员ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_request_id (request_id),
    INDEX idx_status (status),
    INDEX idx_operator_id (operator_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='采购订单表';

-- =============================================
-- 9. 库存表 (inventory)
-- =============================================
CREATE TABLE IF NOT EXISTS inventory (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '库存ID',
    item_id BIGINT NOT NULL UNIQUE COMMENT '物品ID',
    quantity INT DEFAULT 0 COMMENT '库存数量',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_item_id (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存表';

-- =============================================
-- 10. 库存调整表 (inventory_adjustment)
-- =============================================
CREATE TABLE IF NOT EXISTS inventory_adjustment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '调整ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    adjustment_type VARCHAR(20) NOT NULL COMMENT '调整类型：盘亏、盘盈、报废、其他',
    quantity INT NOT NULL COMMENT '调整数量',
    reason VARCHAR(200) COMMENT '调整原因',
    operator_id BIGINT COMMENT '操作员ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_item_id (item_id),
    INDEX idx_operator_id (operator_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='库存调整表';

-- =============================================
-- 11. 领取申请表 (claim_request)
-- =============================================
CREATE TABLE IF NOT EXISTS claim_request (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '申请ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    quantity INT NOT NULL COMMENT '申请数量',
    purpose VARCHAR(200) COMMENT '用途',
    status VARCHAR(20) DEFAULT '待审批' COMMENT '状态：待审批、已通过、已拒绝、已发放',
    requester_id BIGINT NOT NULL COMMENT '申请人ID',
    approver_id BIGINT COMMENT '审批人ID',
    approve_time DATETIME COMMENT '审批时间',
    reject_reason VARCHAR(200) COMMENT '拒绝原因',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_requester_id (requester_id),
    INDEX idx_status (status),
    INDEX idx_item_id (item_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领取申请表';

-- =============================================
-- 12. 领取记录表 (claim_record)
-- =============================================
CREATE TABLE IF NOT EXISTS claim_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    request_id BIGINT NOT NULL COMMENT '领取申请ID',
    actual_quantity INT NOT NULL COMMENT '实际发放数量',
    giver_id BIGINT COMMENT '发放人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_request_id (request_id),
    INDEX idx_giver_id (giver_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='领取记录表';

-- =============================================
-- 13. 消耗记录表 (consumption_record)
-- =============================================
CREATE TABLE IF NOT EXISTS consumption_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '记录ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    quantity INT NOT NULL COMMENT '消耗数量',
    consumer_id BIGINT COMMENT '消费人ID',
    claim_record_id BIGINT COMMENT '领取记录ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_item_id (item_id),
    INDEX idx_consumer_id (consumer_id),
    INDEX idx_claim_record_id (claim_record_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='消耗记录表';

-- =============================================
-- 14. 盘点计划表 (check_plan)
-- =============================================
CREATE TABLE IF NOT EXISTS check_plan (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '计划ID',
    plan_name VARCHAR(100) COMMENT '计划名称',
    start_date DATE NOT NULL COMMENT '开始日期',
    end_date DATE NOT NULL COMMENT '结束日期',
    scope VARCHAR(50) COMMENT '盘点范围',
    status VARCHAR(20) DEFAULT '待盘点' COMMENT '状态：待盘点、盘点中、已确认、已处理',
    creator_id BIGINT COMMENT '创建人ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_status (status),
    INDEX idx_creator_id (creator_id),
    INDEX idx_start_date (start_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='盘点计划表';

-- =============================================
-- 15. 字典分类表 (dict_category)
-- =============================================
CREATE TABLE IF NOT EXISTS dict_category (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '分类ID',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '分类代码',
    name VARCHAR(100) NOT NULL COMMENT '分类名称',
    parent_id BIGINT DEFAULT NULL COMMENT '父级ID',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_parent_id (parent_id),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典分类表';

-- =============================================
-- 16. 字典项表 (dict_item)
-- =============================================
CREATE TABLE IF NOT EXISTS dict_item (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '字典项ID',
    category_id BIGINT NOT NULL COMMENT '分类ID',
    code VARCHAR(50) NOT NULL COMMENT '字典代码',
    name VARCHAR(100) NOT NULL COMMENT '字典名称',
    description VARCHAR(200) COMMENT '说明',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    extra JSON COMMENT '扩展字段',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_category_id (category_id),
    INDEX idx_code (code),
    INDEX idx_status (status),
    UNIQUE KEY uk_category_code (category_id, code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='字典项表';

-- =============================================
-- 初始化数据字典数据
-- =============================================

-- 插入考勤状态分类
INSERT INTO dict_category (code, name, parent_id, sort_order, status) VALUES
('DICT_ATT', '考勤状态', NULL, 1, 1),
('DICT_ROLE', '用户角色', NULL, 2, 1),
('DICT_ITEM_CATEGORY', '物品分类', NULL, 3, 1),
('DICT_APPROVAL', '审批状态', NULL, 4, 1),
('DICT_INV_ADJUST', '库存调整类型', NULL, 5, 1);

-- 插入考勤状态字典项
INSERT INTO dict_item (category_id, code, name, description, sort_order, status) VALUES
(1, 'ATT001', '正常上班', '正常出勤', 1, 1),
(1, 'ATT002', '迟到', '上班迟到', 2, 1),
(1, 'ATT003', '早退', '上班早退', 3, 1),
(1, 'ATT004', '全天休假', '全天请假', 4, 1),
(1, 'ATT005', '半天休假', '半天请假', 5, 1),
(1, 'ATT006', '旷工', '无故缺勤', 6, 1),
(1, 'ATT007', '出差', '外出办公', 7, 1),
(1, 'ATT008', '加班', '延长工时', 8, 1);

-- 插入用户角色字典项
INSERT INTO dict_item (category_id, code, name, description, sort_order, status) VALUES
(2, 'ROLE_ADMIN', '系统管理员', '系统全部功能管理', 1, 1),
(2, 'ROLE_DEPT', '部门主管', '部门内部管理、审批', 2, 1),
(2, 'ROLE_USER', '普通员工', '基础功能操作', 3, 1),
(2, 'ROLE_FIN', '财务人员', '财务相关功能', 4, 1);

-- 插入物品分类字典项
INSERT INTO dict_item (category_id, code, name, description, sort_order, status) VALUES
(3, 'CAT_OFFICE', '办公用品', '笔、纸、文件夹等', 1, 1),
(3, 'CAT_CONSUME', '消耗品', '打印纸、墨盒等', 2, 1),
(3, 'CAT_ASSET', '设备资产', '电脑、打印机等', 3, 1);

-- 插入审批状态字典项
INSERT INTO dict_item (category_id, code, name, description, sort_order, status) VALUES
(4, 'APP_PEND', '待审批', '等待审批', 1, 1),
(4, 'APP_PASS', '已通过', '审批通过', 2, 1),
(4, 'APP_REJ', '已拒绝', '审批拒绝', 3, 1),
(4, 'APP_CANC', '已取消', '申请人取消', 4, 1);

-- 插入库存调整类型字典项
INSERT INTO dict_item (category_id, code, name, description, sort_order, status) VALUES
(5, 'INV_ADD', '盘盈', '实际多于账面', 1, 1),
(5, 'INV_SUB', '盘亏', '实际少于账面', 2, 1),
(5, 'INV_LOSS', '报损', '损坏报废', 3, 1),
(5, 'INV_GIFT', '赠送', '赠送物品', 4, 1),
(5, 'INV_TRANS', '调拨', '部门间调拨', 5, 1);

-- =============================================
-- 15. 盘点明细表 (check_record)
-- =============================================
CREATE TABLE IF NOT EXISTS check_record (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '明细ID',
    plan_id BIGINT NOT NULL COMMENT '计划ID',
    item_id BIGINT NOT NULL COMMENT '物品ID',
    book_quantity INT COMMENT '账面数量',
    actual_quantity INT COMMENT '实际数量',
    difference INT COMMENT '差异数量',
    reason VARCHAR(200) COMMENT '差异原因',
    checker_id BIGINT COMMENT '盘点人ID',
    check_time DATETIME COMMENT '盘点时间',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_plan_id (plan_id),
    INDEX idx_item_id (item_id),
    INDEX idx_checker_id (checker_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='盘点明细表';

-- =============================================
-- 初始化数据
-- =============================================

-- 插入部门数据
INSERT INTO department (name, description) VALUES
('行政部门', '负责公司行政事务'),
('财务部', '负责公司财务管理工作'),
('人力资源部', '负责公司人力资源管理'),
('技术部', '负责公司技术研发'),
('市场部', '负责公司市场推广');

-- 插入管理员用户（密码为 admin123，明文存储）
INSERT INTO sys_user (username, password, real_name, role, department_id, must_change_password) VALUES
('admin', 'admin123', '系统管理员', '系统管理员', 1, 1),
('manager', 'admin123', '部门主管', '部门主管', 1, 1),
('user1', 'admin123', '张三', '普通员工', 1, 1),
('finance', 'admin123', '财务专员', '财务人员', 2, 1);

-- 插入物品分类数据
INSERT INTO item_category (name, description) VALUES
('办公用品', '日常办公使用物品'),
('消耗品', '一次性消耗物品'),
('设备资产', '电子设备和固定资产');

-- 插入物品数据
INSERT INTO item (category_id, name, spec, unit, min_stock, price) VALUES
(1, 'A4打印纸', '70g/500张/包', '包', 50, 25.00),
(1, '中性笔', '黑色/0.5mm', '支', 100, 2.00),
(2, '矿泉水', '550ml/瓶', '箱', 20, 36.00),
(3, '台式电脑', 'Dell OptiPlex 3080', '台', 5, 4500.00),
(3, '打印机', 'HP LaserJet Pro', '台', 3, 1800.00);

-- =============================================
-- 17. 权限表 (sys_permission)
-- =============================================
CREATE TABLE IF NOT EXISTS sys_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '权限ID',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '权限代码',
    name VARCHAR(100) NOT NULL COMMENT '权限名称',
    category VARCHAR(50) COMMENT '权限分类',
    description VARCHAR(200) COMMENT '权限描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_category (category),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='权限表';

-- =============================================
-- 18. 角色表 (sys_role)
-- =============================================
CREATE TABLE IF NOT EXISTS sys_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '角色ID',
    code VARCHAR(50) NOT NULL UNIQUE COMMENT '角色代码',
    name VARCHAR(100) NOT NULL COMMENT '角色名称',
    description VARCHAR(200) COMMENT '角色描述',
    sort_order INT DEFAULT 0 COMMENT '排序号',
    status TINYINT DEFAULT 1 COMMENT '状态：1启用，0禁用',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (code),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- =============================================
-- 19. 角色权限关联表 (sys_role_permission)
-- =============================================
CREATE TABLE IF NOT EXISTS sys_role_permission (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    permission_id BIGINT NOT NULL COMMENT '权限ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_role_permission (role_id, permission_id),
    INDEX idx_role_id (role_id),
    INDEX idx_permission_id (permission_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色权限关联表';

-- =============================================
-- 20. 用户角色关联表 (sys_user_role)
-- =============================================
CREATE TABLE IF NOT EXISTS sys_user_role (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    role_id BIGINT NOT NULL COMMENT '角色ID',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    UNIQUE KEY uk_user_role (user_id, role_id),
    INDEX idx_user_id (user_id),
    INDEX idx_role_id (role_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户角色关联表';

-- =============================================
-- 21. 操作日志表 (sys_operation_log)
-- =============================================
CREATE TABLE IF NOT EXISTS sys_operation_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '日志ID',
    user_id BIGINT COMMENT '操作用户ID',
    action VARCHAR(50) COMMENT '操作类型',
    resource VARCHAR(50) COMMENT '资源类型',
    resource_id BIGINT COMMENT '资源ID',
    detail TEXT COMMENT '操作详情',
    ip_address VARCHAR(50) COMMENT 'IP地址',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
    INDEX idx_user_id (user_id),
    INDEX idx_action (action),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='操作日志表';

-- =============================================
-- 初始化权限数据
-- =============================================

-- 插入权限数据
INSERT INTO sys_permission (code, name, category, description, sort_order, status) VALUES
-- 考勤权限
('ATT_VIEW_OWN', '查看自己的考勤', '考勤', '普通员工查看个人考勤记录', 1, 1),
('ATT_EDIT_OWN', '修改自己的考勤', '考勤', '普通员工修改个人考勤记录', 2, 1),
('ATT_VIEW_ALL', '查看所有考勤', '考勤', '管理员查看所有员工考勤', 3, 1),
('ATT_EXPORT', '导出考勤报表', '考勤', '管理员导出考勤数据', 4, 1),
-- 会餐权限
('DNM_VIEW', '查看会餐记录', '会餐', '查看会餐费用记录', 11, 1),
('DNM_ADD', '添加会餐记录', '会餐', '新增会餐费用记录', 12, 1),
('DNM_EDIT', '编辑会餐记录', '会餐', '修改会餐费用记录', 13, 1),
('DNM_DELETE', '删除会餐记录', '会餐', '删除会餐费用记录', 14, 1),
-- 采购权限
('PUR_SUBMIT', '提交采购申请', '采购', '员工提交采购申请', 21, 1),
('PUR_APPROVE', '审批采购申请', '采购', '部门主管审批采购申请', 22, 1),
('PUR_EXECUTE', '执行采购', '采购', '管理员执行采购', 23, 1),
('PUR_VIEW', '查看采购记录', '采购', '查看采购历史记录', 24, 1),
-- 物品权限
('ITEM_MANAGE', '物品管理', '物品', '物品的增删改查', 31, 1),
-- 库存权限
('INV_VIEW', '查看库存', '库存', '查看库存数据', 41, 1),
('INV_ADJUST', '库存调整', '库存', '调整库存数量', 42, 1),
-- 领取权限
('CLAIM_SUBMIT', '提交领取申请', '领取', '员工提交物品领取申请', 51, 1),
('CLAIM_APPROVE', '审批领取申请', '领取', '部门主管审批领取申请', 52, 1),
('CLAIM_ISSUE', '发放物品', '领取', '管理员发放物品', 53, 1),
-- 盘点权限
('CHECK_CREATE', '创建盘点计划', '盘点', '创建月度盘点计划', 61, 1),
('CHECK_EXECUTE', '执行盘点', '盘点', '执行库存盘点', 62, 1),
('CHECK_AUDIT', '核对盘点结果', '盘点', '财务人员核对盘点结果', 63, 1),
-- 系统-用户权限
('SYS_USER_VIEW', '查看用户', '系统-用户', '查看用户列表', 71, 1),
('SYS_USER_EDIT', '用户管理', '系统-用户', '增删改用户', 72, 1),
-- 系统-部门权限
('SYS_DEPT_VIEW', '查看部门', '系统-部门', '查看部门列表', 81, 1),
('SYS_DEPT_EDIT', '部门管理', '系统-部门', '增删改部门', 82, 1),
-- 系统-角色权限
('SYS_ROLE_VIEW', '查看角色', '系统-角色', '查看角色列表', 91, 1),
('SYS_ROLE_EDIT', '角色管理', '系统-角色', '增删改角色，分配权限', 92, 1),
-- 系统-字典权限
('SYS_DICT_VIEW', '查看数据字典', '系统-字典', '查看字典分类和字典项', 101, 1),
('SYS_DICT_EDIT', '数据字典管理', '系统-字典', '增删改字典分类和字典项', 102, 1);

-- 插入角色数据
INSERT INTO sys_role (code, name, description, sort_order, status) VALUES
('ROLE_ADMIN', '系统管理员', '系统全部功能管理', 1, 1),
('ROLE_DEPT', '部门主管', '部门内部管理、审批', 2, 1),
('ROLE_USER', '普通员工', '基础功能操作', 3, 1),
('ROLE_FIN', '财务人员', '财务相关功能', 4, 1);

-- 插入角色权限关联数据（系统管理员拥有所有权限）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 1, id FROM sys_permission WHERE status = 1;

-- 为部门主管分配权限（审批相关）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 2, id FROM sys_permission WHERE code IN ('PUR_APPROVE', 'CLAIM_APPROVE', 'INV_VIEW');

-- 为普通员工分配权限（基础功能）
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 3, id FROM sys_permission WHERE code IN ('ATT_VIEW_OWN', 'ATT_EDIT_OWN', 'DNM_VIEW', 'DNM_ADD', 'DNM_EDIT', 'PUR_SUBMIT', 'INV_VIEW', 'CLAIM_SUBMIT');

-- 为财务人员分配权限
INSERT INTO sys_role_permission (role_id, permission_id)
SELECT 4, id FROM sys_permission WHERE code IN ('CHECK_AUDIT');

-- 初始化用户角色关联（将现有用户的角色关联起来）
INSERT INTO sys_user_role (user_id, role_id)
SELECT id, 
    CASE role 
        WHEN '系统管理员' THEN 1
        WHEN '部门主管' THEN 2
        WHEN '普通员工' THEN 3
        WHEN '财务人员' THEN 4
        ELSE 3
    END
FROM sys_user WHERE status = 1;
