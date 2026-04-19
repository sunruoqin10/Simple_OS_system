<template>
  <div class="role-view">
    <div class="page-header">
      <h1 class="page-title">角色管理</h1>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增角色
      </el-button>
    </div>

    <el-card class="table-card">
      <el-table :data="roles" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="code" label="角色代码" width="150" />
        <el-table-column prop="name" label="角色名称" width="150" />
        <el-table-column prop="description" label="描述" />
        <el-table-column prop="permissionCount" label="权限数" width="100" align="center">
          <template #default="{ row }">
            <el-tag type="info">{{ row.permissionCount || 0 }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'info'">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="250" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEditPermissions(row)">
              <el-icon><Key /></el-icon>
              分配权限
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
      width="500px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="角色代码" prop="code">
          <el-input v-model="form.code" placeholder="请输入角色代码" :disabled="!!form.id" />
        </el-form-item>
        <el-form-item label="角色名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入角色名称" />
        </el-form-item>
        <el-form-item label="描述" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="3" placeholder="请输入描述" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
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
      v-model="permissionDialogVisible"
      title="分配权限"
      width="700px"
      @close="handlePermissionDialogClose"
    >
      <div class="permission-section">
        <div class="section-title">角色：{{ currentRole?.name }}</div>
        <el-checkbox-group v-model="selectedPermissionIds">
          <el-tabs>
            <el-tab-pane
              v-for="category in permissionCategories"
              :key="category"
              :label="category"
              :name="category"
            >
              <el-checkbox
                v-for="perm in getPermissionsByCategory(category)"
                :key="perm.id"
                :label="perm.id"
                :value="perm.id"
                class="permission-item"
              >
                <span class="permission-name">{{ perm.name }}</span>
                <span class="permission-code">{{ perm.code }}</span>
              </el-checkbox>
            </el-tab-pane>
          </el-tabs>
        </el-checkbox-group>
      </div>
      <template #footer>
        <el-button @click="permissionDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSavePermissions" :loading="submitLoading">
          保存
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Key } from '@element-plus/icons-vue'
import {
  getRoleList,
  getRoleById,
  createRole,
  updateRole,
  deleteRole,
  getRolePermissionCodes,
  assignPermissions,
  getPermissionList
} from '@/api/role'

const loading = ref(false)
const roles = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增角色')
const submitLoading = ref(false)
const formRef = ref(null)

const permissionDialogVisible = ref(false)
const currentRole = ref(null)
const allPermissions = ref([])
const selectedPermissionIds = ref([])

const form = reactive({
  id: null,
  code: '',
  name: '',
  description: '',
  status: 1
})

const rules = {
  code: [
    { required: true, message: '请输入角色代码', trigger: 'blur' }
  ],
  name: [
    { required: true, message: '请输入角色名称', trigger: 'blur' }
  ]
}

const permissionCategories = computed(() => {
  const categories = new Set()
  allPermissions.value.forEach(p => {
    if (p.category) categories.add(p.category)
  })
  return Array.from(categories)
})

function getPermissionsByCategory(category) {
  return allPermissions.value.filter(p => p.category === category)
}

async function loadRoles() {
  loading.value = true
  try {
    const res = await getRoleList()
    if (res.code === 200) {
      roles.value = res.data || []
    } else {
      ElMessage.error(res.message || '加载角色列表失败')
    }
  } catch (error) {
    ElMessage.error('加载角色列表失败')
  } finally {
    loading.value = false
  }
}

async function loadPermissions() {
  try {
    const res = await getPermissionList()
    if (res.code === 200) {
      allPermissions.value = res.data || []
    }
  } catch (error) {
    console.error('加载权限列表失败', error)
  }
}

function handleCreate() {
  dialogTitle.value = '新增角色'
  form.id = null
  form.code = ''
  form.name = ''
  form.description = ''
  form.status = 1
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑角色'
  form.id = row.id
  form.code = row.code
  form.name = row.name
  form.description = row.description || ''
  form.status = row.status
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除角色"${row.name}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await deleteRole(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadRoles()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch {
  }
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
    submitLoading.value = true

    const data = {
      code: form.code,
      name: form.name,
      description: form.description,
      status: form.status,
      sortOrder: 0
    }

    let res
    if (form.id) {
      data.id = form.id
      res = await updateRole(data)
    } else {
      res = await createRole(data)
    }

    if (res.code === 200) {
      ElMessage.success(form.id ? '修改成功' : '新增成功')
      dialogVisible.value = false
      loadRoles()
    } else {
      ElMessage.error(res.message || '操作失败')
    }
  } catch (error) {
  } finally {
    submitLoading.value = false
  }
}

function handleDialogClose() {
  formRef.value?.resetFields()
  form.id = null
}

async function handleEditPermissions(row) {
  currentRole.value = row
  selectedPermissionIds.value = []

  try {
    const res = await getRolePermissionCodes(row.id)
    if (res.code === 200) {
      const codes = res.data || []
      selectedPermissionIds.value = allPermissions.value
        .filter(p => codes.includes(p.code))
        .map(p => p.id)
    }
  } catch (error) {
    console.error('加载角色权限失败', error)
  }

  permissionDialogVisible.value = true
}

async function handleSavePermissions() {
  try {
    submitLoading.value = true
    const res = await assignPermissions(currentRole.value.id, selectedPermissionIds.value)
    if (res.code === 200) {
      ElMessage.success('权限分配成功')
      permissionDialogVisible.value = false
      loadRoles()
    } else {
      ElMessage.error(res.message || '权限分配失败')
    }
  } catch (error) {
    ElMessage.error('权限分配失败')
  } finally {
    submitLoading.value = false
  }
}

function handlePermissionDialogClose() {
  selectedPermissionIds.value = []
  currentRole.value = null
}

onMounted(() => {
  loadRoles()
  loadPermissions()
})
</script>

<style scoped>
.role-view {
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

.permission-section {
  max-height: 400px;
  overflow-y: auto;
}

.section-title {
  margin-bottom: 15px;
  font-size: 16px;
  font-weight: 600;
}

.permission-item {
  margin-bottom: 10px;
  margin-right: 20px;
}

.permission-name {
  margin-right: 10px;
}

.permission-code {
  color: #909399;
  font-size: 12px;
}

:deep(.el-checkbox-group) {
  display: flex;
  flex-wrap: wrap;
}

:deep(.el-tabs__content) {
  max-height: 300px;
  overflow-y: auto;
}
</style>