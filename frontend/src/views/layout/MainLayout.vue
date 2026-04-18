<template>
  <el-container class="main-container">
    <el-aside :width="isCollapse ? '64px' : '220px'" class="aside">
      <div class="logo">
        <el-icon :size="24" color="#409EFF"><Monitor /></el-icon>
        <span v-if="!isCollapse" class="logo-text">企业OA</span>
      </div>

      <el-menu
        :default-active="activeMenu"
        :collapse="isCollapse"
        :collapse-transition="false"
        background-color="#1e1e1e"
        text-color="#bfbfbf"
        active-text-color="#409EFF"
        router
        class="menu"
      >
        <el-menu-item index="/dashboard">
          <el-icon><HomeFilled /></el-icon>
          <template #title>首页</template>
        </el-menu-item>

        <el-sub-menu index="attendance">
          <template #title>
            <el-icon><Calendar /></el-icon>
            <span>考勤管理</span>
          </template>
          <el-menu-item index="/attendance">考勤日历</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="dinner">
          <template #title>
            <el-icon><Food /></el-icon>
            <span>会餐费管理</span>
          </template>
          <el-menu-item index="/dinner">会餐记录</el-menu-item>
        </el-sub-menu>

        <el-sub-menu index="system" v-if="isAdmin">
          <template #title>
            <el-icon><Setting /></el-icon>
            <span>系统管理</span>
          </template>
          <el-menu-item index="/users">用户管理</el-menu-item>
          <el-menu-item index="/departments">部门管理</el-menu-item>
        </el-sub-menu>
      </el-menu>
    </el-aside>

    <el-container>
      <el-header class="header">
        <div class="header-left">
          <el-icon class="collapse-icon" @click="toggleCollapse">
            <Fold v-if="!isCollapse" />
            <Expand v-else />
          </el-icon>
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
            <el-breadcrumb-item v-if="route.meta.title">
              {{ route.meta.title }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="header-right">
          <el-dropdown @command="handleCommand" trigger="click">
            <div class="user-info">
              <el-avatar :size="32" bg-color="#409EFF">
                {{ userStore.userInfo?.realName?.charAt(0) || userStore.userInfo?.username?.charAt(0) || 'U' }}
              </el-avatar>
              <span class="username">{{ userStore.userInfo?.realName || userStore.userInfo?.username }}</span>
              <el-icon class="el-icon--right"><ArrowDown /></el-icon>
            </div>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon>
                  个人中心
                </el-dropdown-item>
                <el-dropdown-item command="password">
                  <el-icon><Key /></el-icon>
                  修改密码
                </el-dropdown-item>
                <el-dropdown-item divided command="logout">
                  <el-icon><SwitchButton /></el-icon>
                  退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'
import { ElMessageBox, ElMessage } from 'element-plus'
import {
  HomeFilled, Calendar, Food, Setting, User,
  Fold, Expand, ArrowDown, Monitor, Key, SwitchButton
} from '@element-plus/icons-vue'
import { changePassword } from '@/api/auth'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const isCollapse = ref(false)
const activeMenu = computed(() => route.path)
const isAdmin = computed(() => {
  return userStore.userInfo?.role === '系统管理员' || userStore.roles?.includes('admin')
})

function toggleCollapse() {
  isCollapse.value = !isCollapse.value
}

async function handleCommand(command) {
  switch (command) {
    case 'logout':
      try {
        await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        })
        await userStore.logout()
        ElMessage.success('已退出登录')
        router.push('/login')
      } catch {}
      break
    case 'profile':
      ElMessage.info('个人中心功能开发中')
      break
    case 'password':
      handleChangePassword()
      break
  }
}

async function handleChangePassword() {
  try {
    // 第一步：输入原密码
    const oldPasswordResult = await ElMessageBox.prompt(
      '请输入原密码',
      '修改密码 - 第1步',
      {
        confirmButtonText: '下一步',
        cancelButtonText: '取消',
        inputType: 'password',
        inputPattern: /.{1,}/,
        inputErrorMessage: '原密码不能为空'
      }
    )

    const oldPassword = oldPasswordResult.value

    // 第二步：输入新密码
    const newPasswordResult = await ElMessageBox.prompt(
      '请输入新密码（至少6位）',
      '修改密码 - 第2步',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        inputType: 'password',
        inputPattern: /.{6,}/,
        inputErrorMessage: '新密码长度不能少于6位'
      }
    )

    const newPassword = newPasswordResult.value

    // 第三步：确认新密码
    const confirmResult = await ElMessageBox.prompt(
      '请再次输入新密码',
      '修改密码 - 第3步',
      {
        confirmButtonText: '确认修改',
        cancelButtonText: '取消',
        inputType: 'password',
        inputPattern: /.{6,}/,
        inputErrorMessage: '新密码长度不能少于6位'
      }
    )

    const confirmPassword = confirmResult.value

    // 验证两次新密码是否一致
    if (newPassword !== confirmPassword) {
      ElMessage.error('两次输入的新密码不一致')
      return
    }

    // 调用API修改密码
    const res = await changePassword({
      oldPassword: oldPassword,
      newPassword: newPassword
    })

    if (res.code === 200) {
      ElMessage.success('密码修改成功')
    } else {
      ElMessage.error(res.message || '密码修改失败')
    }
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error('密码修改失败，请重试')
    }
  }
}

onMounted(() => {
  if (!userStore.userInfo) {
    userStore.fetchUserInfo()
  }
})
</script>

<style scoped>
.main-container {
  height: 100vh;
}

.aside {
  background: #1e1e1e;
  transition: width 0.3s;
  overflow: hidden;
  border-right: 1px solid #2d2d2d;
}

.logo {
  height: 60px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  border-bottom: 1px solid #2d2d2d;
}

.logo-text {
  color: #fff;
  font-size: 18px;
  font-weight: 600;
  letter-spacing: 1px;
}

.menu {
  border-right: none;
  background: transparent;
}

.menu:not(.el-menu--collapse) {
  width: 220px;
}

.menu :deep(.el-menu-item),
.menu :deep(.el-sub-menu__title) {
  height: 50px;
  line-height: 50px;
  margin: 4px 8px;
  border-radius: 8px;
}

.menu :deep(.el-menu-item:hover),
.menu :deep(.el-sub-menu__title:hover) {
  background: rgba(64, 158, 255, 0.1);
}

.menu :deep(.el-menu-item.is-active) {
  background: rgba(64, 158, 255, 0.15);
}

.header {
  background: #fff;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.collapse-icon {
  font-size: 20px;
  cursor: pointer;
  color: #606266;
  padding: 8px;
  border-radius: 4px;
  transition: all 0.2s;
}

.collapse-icon:hover {
  background: #f5f7fa;
  color: #409EFF;
}

.header-right {
  display: flex;
  align-items: center;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 6px 12px;
  border-radius: 8px;
  cursor: pointer;
  transition: background 0.2s;
}

.user-info:hover {
  background: #f5f7fa;
}

.username {
  font-size: 14px;
  color: #303133;
}

.main {
  background: #f5f7fa;
  padding: 20px;
}

:deep(.el-dropdown-menu__item) {
  display: flex;
  align-items: center;
  gap: 8px;
}
</style>
