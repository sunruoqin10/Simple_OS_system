<template>
  <div class="user-view">
    <div class="page-header">
      <h1 class="page-title">用户管理</h1>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增用户
      </el-button>
    </div>

    <el-card class="table-card">
      <el-table :data="users" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="username" label="用户名" width="150" />
        <el-table-column prop="realName" label="姓名" width="120" />
        <el-table-column prop="role" label="角色" width="120" />
        <el-table-column label="部门" width="120">
          <template #default="{ row }">
            {{ getDepartmentName(row.departmentId) }}
          </template>
        </el-table-column>
        <el-table-column prop="email" label="邮箱" width="180" />
        <el-table-column prop="phone" label="电话" width="130" />
        <el-table-column prop="status" label="状态" width="80">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleAssignRoles(row)">
              <el-icon><Key /></el-icon>
              分配角色
            </el-button>
            <el-button type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button type="danger" size="small" @click="handleDelete(row)">
              <el-icon><Delete /></el-icon>
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="600px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="用户名" prop="username" v-if="!form.id">
          <el-input v-model="form.username" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="密码" prop="password" v-if="!form.id">
          <el-input v-model="form.password" type="password" placeholder="请输入密码" />
        </el-form-item>
        <el-form-item label="姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="角色" prop="role">
          <el-select v-model="form.role" placeholder="请选择角色">
            <el-option
              v-for="item in roles"
              :key="item.code"
              :label="item.name"
              :value="item.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="部门" prop="departmentId">
          <el-select v-model="form.departmentId" placeholder="请选择部门">
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.id"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="form.email" placeholder="请输入邮箱" />
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="form.phone" placeholder="请输入电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status" v-if="form.id">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">正常</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="roleDialogVisible"
      title="分配角色"
      width="500px"
      @close="handleRoleDialogClose"
    >
      <div class="role-assign-section">
        <p class="assign-tip">为用户 {{ currentUser?.username }} 分配角色</p>
        <el-checkbox-group v-model="selectedRoleIds">
          <el-checkbox
            v-for="role in roles"
            :key="role.id"
            :label="role.id"
            :value="role.id"
            class="role-checkbox-item"
          >
            <span class="role-name">{{ role.name }}</span>
            <span class="role-code">{{ role.code }}</span>
          </el-checkbox>
        </el-checkbox-group>
      </div>
      <template #footer>
        <el-button @click="roleDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSaveRoles" :loading="submitLoading">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Key } from '@element-plus/icons-vue'
import { getUserList, createUser, updateUser, deleteUser } from '@/api/user'
import { getDepartmentList } from '@/api/department'
import { getItemsByCategory } from '@/api/dict'
import { getRoleList, getUserRoles, setUserRoles } from '@/api/role'

const ROLE_CATEGORY_ID = 2

const loading = ref(false)
const users = ref([])
const departments = ref([])
const roles = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const submitLoading = ref(false)
const formRef = ref(null)

const roleDialogVisible = ref(false)
const currentUser = ref(null)
const selectedRoleIds = ref([])

const form = reactive({
  id: null,
  username: '',
  password: '',
  realName: '',
  role: '',
  departmentId: null,
  email: '',
  phone: '',
  status: 1
})

const rules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' }
  ],
  realName: [
    { required: true, message: '请输入姓名', trigger: 'blur' }
  ],
  role: [
    { required: true, message: '请选择角色', trigger: 'change' }
  ]
}

async function loadUsers() {
  loading.value = true
  try {
    const res = await getUserList()
    if (res.code === 200) {
      users.value = res.data || []
    } else {
      ElMessage.error(res.message || '加载用户列表失败')
    }
  } catch (error) {
    ElMessage.error('加载用户列表失败')
  } finally {
    loading.value = false
  }
}

async function loadDepartments() {
  try {
    const res = await getDepartmentList()
    if (res.code === 200) {
      departments.value = res.data || []
    }
  } catch (error) {
    console.error('加载部门列表失败', error)
  }
}

async function loadRoles() {
  try {
    const res = await getRoleList()
    if (res.code === 200) {
      roles.value = res.data || []
    }
  } catch (error) {
    console.error('加载角色列表失败', error)
  }
}

function getDepartmentName(departmentId) {
  if (!departmentId) return '-'
  const dept = departments.value.find(d => d.id === departmentId)
  return dept ? dept.name : '-'
}

function handleCreate() {
  dialogTitle.value = '新增用户'
  form.id = null
  form.username = ''
  form.password = ''
  form.realName = ''
  form.role = ''
  form.departmentId = null
  form.email = ''
  form.phone = ''
  form.status = 1
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑用户'
  form.id = row.id
  form.username = row.username
  form.password = ''
  form.realName = row.realName
  form.role = row.role
  form.departmentId = row.departmentId
  form.email = row.email
  form.phone = row.phone
  form.status = row.status
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除用户"${row.username}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await deleteUser(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadUsers()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch {
  }
}

async function handleAssignRoles(row) {
  currentUser.value = row
  selectedRoleIds.value = []
  
  try {
    const res = await getUserRoles(row.id)
    if (res.code === 200) {
      selectedRoleIds.value = res.data || []
    }
  } catch (error) {
    console.error('加载用户角色失败', error)
  }
  
  roleDialogVisible.value = true
}

async function handleSaveRoles() {
  try {
    submitLoading.value = true
    const res = await setUserRoles(currentUser.value.id, selectedRoleIds.value)
    if (res.code === 200) {
      ElMessage.success('角色分配成功')
      roleDialogVisible.value = false
    } else {
      ElMessage.error(res.message || '角色分配失败')
    }
  } catch (error) {
    ElMessage.error('角色分配失败')
  } finally {
    submitLoading.value = false
  }
}

function handleRoleDialogClose() {
  selectedRoleIds.value = []
  currentUser.value = null
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
    submitLoading.value = true

    const data = {
      username: form.username,
      password: form.password,
      realName: form.realName,
      role: form.role,
      departmentId: form.departmentId,
      email: form.email,
      phone: form.phone,
      status: form.status
    }

    let res
    if (form.id) {
      delete data.username
      delete data.password
      res = await updateUser(form.id, data)
    } else {
      res = await createUser(data)
    }

    if (res.code === 200) {
      ElMessage.success(form.id ? '修改成功' : '新增成功')
      dialogVisible.value = false
      loadUsers()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
    // 验证失败
  } finally {
    submitLoading.value = false
  }
}

function handleDialogClose() {
  formRef.value?.resetFields()
}

onMounted(() => {
  loadUsers()
  loadDepartments()
  loadRoles()
})
</script>

<style scoped>
.user-view {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  margin: 0;
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.table-card {
  margin-top: 20px;
}

.el-select {
  width: 100%;
}

.role-assign-section {
  padding: 10px 0;
}

.assign-tip {
  margin-bottom: 20px;
  font-size: 16px;
  color: #303133;
}

.role-checkbox-item {
  display: block;
  margin-bottom: 15px;
  margin-right: 0;
}

.role-name {
  margin-right: 10px;
  font-weight: 500;
}

.role-code {
  color: #909399;
  font-size: 12px;
}
</style>
