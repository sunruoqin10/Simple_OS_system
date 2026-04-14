<template>
  <div class="dashboard">
    <div class="page-header">
      <h1 class="page-title">工作台</h1>
      <p class="page-subtitle">欢迎使用企业OA系统，当前日期：{{ currentTime }}</p>
    </div>

    <el-row :gutter="20" class="stat-row">
      <el-col :span="6">
        <div class="stat-card stat-card-blue">
          <div class="stat-icon">
            <el-icon :size="32"><Calendar /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">本月考勤天数</div>
            <div class="stat-value">--</div>
          </div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="stat-card stat-card-green">
          <div class="stat-icon">
            <el-icon :size="32"><Coin /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">会餐费合计</div>
            <div class="stat-value">¥ --</div>
          </div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="stat-card stat-card-orange">
          <div class="stat-icon">
            <el-icon :size="32"><User /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">系统用户</div>
            <div class="stat-value">--</div>
          </div>
        </div>
      </el-col>

      <el-col :span="6">
        <div class="stat-card stat-card-red">
          <div class="stat-icon">
            <el-icon :size="32"><Warning /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-label">库存预警</div>
            <div class="stat-value">--</div>
          </div>
        </div>
      </el-col>
    </el-row>

    <el-row :gutter="20">
      <el-col :span="16">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">快捷操作</span>
            </div>
          </template>
          <div class="quick-actions">
            <div class="action-item">
              <div class="action-icon action-icon-blue">
                <el-icon :size="24"><Calendar /></el-icon>
              </div>
              <span class="action-text">考勤打卡</span>
            </div>
            <div class="action-item">
              <div class="action-icon action-icon-green">
                <el-icon :size="24"><Document /></el-icon>
              </div>
              <span class="action-text">会餐记录</span>
            </div>
            <div class="action-item">
              <div class="action-icon action-icon-orange">
                <el-icon :size="24"><ShoppingCart /></el-icon>
              </div>
              <span class="action-text">采购申请</span>
            </div>
            <div class="action-item">
              <div class="action-icon action-icon-purple">
                <el-icon :size="24"><Box /></el-icon>
              </div>
              <span class="action-text">物品领取</span>
            </div>
          </div>
        </el-card>
      </el-col>

      <el-col :span="8">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span class="card-title">用户信息</span>
            </div>
          </template>
          <div class="user-info">
            <el-avatar :size="64" bg-color="#409EFF" class="user-avatar">
              {{ userStore.userInfo?.realName?.charAt(0) || userStore.userInfo?.username?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="user-details">
              <div class="user-name">{{ userStore.userInfo?.realName || userStore.userInfo?.username }}</div>
              <div class="user-role">
                <el-tag size="small" type="primary">{{ userStore.userInfo?.role || '--' }}</el-tag>
              </div>
              <div class="user-meta">
                <span>部门：{{ userStore.userInfo?.department?.name || '--' }}</span>
              </div>
            </div>
          </div>
          <el-divider />
          <div class="system-stats">
            <div class="stat-row">
              <span class="stat-label">系统版本</span>
              <span class="stat-value">v1.0.0</span>
            </div>
            <div class="stat-row">
              <span class="stat-label">在线状态</span>
              <span class="stat-value">
                <el-tag size="small" type="success">在线</el-tag>
              </span>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'
import { useUserStore } from '@/stores/user'
import {
  Calendar, Coin, User, Warning,
  ShoppingCart, Box, Document
} from '@element-plus/icons-vue'

const userStore = useUserStore()
const currentTime = ref('')

let timer = null

function updateTime() {
  const now = new Date()
  currentTime.value = now.toLocaleString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
    weekday: 'long'
  })
}

onMounted(() => {
  updateTime()
  timer = setInterval(updateTime, 60000)
})

onUnmounted(() => {
  if (timer) clearInterval(timer)
})
</script>

<style scoped>
.dashboard {
  padding: 0;
}

.page-header {
  margin-bottom: 24px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
  margin: 0 0 8px;
}

.page-subtitle {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

.stat-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 24px;
  border-radius: 12px;
  background: #fff;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s, box-shadow 0.2s;
}

.stat-card:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);
}

.stat-icon {
  width: 60px;
  height: 60px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 20px;
  color: #fff;
}

.stat-card-blue .stat-icon { background: linear-gradient(135deg, #409EFF, #66b1ff); }
.stat-card-green .stat-icon { background: linear-gradient(135deg, #67C23A, #85ce61); }
.stat-card-orange .stat-icon { background: linear-gradient(135deg, #E6A23C, #ebb563); }
.stat-card-red .stat-icon { background: linear-gradient(135deg, #F56C6C, #f78989); }

.stat-info {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 4px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.content-card {
  border-radius: 12px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.content-card :deep(.el-card__header) {
  padding: 16px 20px;
  border-bottom: 1px solid #f0f0f0;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.quick-actions {
  display: flex;
  justify-content: space-around;
  padding: 20px 0;
}

.action-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
  cursor: pointer;
  padding: 16px 24px;
  border-radius: 12px;
  transition: background 0.2s;
}

.action-item:hover {
  background: #f5f7fa;
}

.action-icon {
  width: 56px;
  height: 56px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: #fff;
}

.action-icon-blue { background: linear-gradient(135deg, #409EFF, #66b1ff); }
.action-icon-green { background: linear-gradient(135deg, #67C23A, #85ce61); }
.action-icon-orange { background: linear-gradient(135deg, #E6A23C, #ebb563); }
.action-icon-purple { background: linear-gradient(135deg, #9c27b0, #bb66f0); }

.action-text {
  font-size: 14px;
  color: #606266;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px 0;
}

.user-avatar {
  font-size: 24px;
  font-weight: 500;
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
  margin-bottom: 8px;
}

.user-role {
  margin-bottom: 8px;
}

.user-meta {
  font-size: 13px;
  color: #909399;
}

.system-stats {
  padding: 8px 0;
}

.system-stats .stat-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  margin: 0;
}

.system-stats .stat-label {
  font-size: 14px;
  color: #909399;
}

.system-stats .stat-value {
  font-size: 14px;
  color: #606266;
}
</style>
