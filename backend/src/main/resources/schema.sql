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

-- 插入管理员用户（密码为 admin123，BCrypt加密）
INSERT INTO sys_user (username, password, real_name, role, department_id, must_change_password) VALUES
('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '系统管理员', '系统管理员', 1, 1),
('manager', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '部门主管', '部门主管', 1, 1),
('user1', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '张三', '普通员工', 1, 1),
('finance', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iAt6Z5EH', '财务专员', '财务人员', 2, 1);

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
