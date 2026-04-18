<template>
  <div class="dinner-view">
    <div class="page-header">
      <h1 class="page-title">会餐费管理</h1>
      <el-button type="primary" @click="handleCreate">
        <el-icon><Plus /></el-icon>
        新增记录
      </el-button>
    </div>

    <el-card class="filter-card">
      <el-form :inline="true" :model="filterForm" class="filter-form">
        <el-form-item label="日期范围">
          <el-date-picker
            v-model="filterForm.dateRange"
            type="daterange"
            range-separator="至"
            start-placeholder="开始日期"
            end-placeholder="结束日期"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="部门">
          <el-select v-model="filterForm.department" placeholder="请选择部门" clearable>
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleFilter">
            <el-icon><Search /></el-icon>
            查询
          </el-button>
          <el-button @click="handleReset">
            <el-icon><Refresh /></el-icon>
            重置
          </el-button>
        </el-form-item>
      </el-form>
    </el-card>

    <el-card class="statistics-card">
      <el-row :gutter="20">
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-label">记录总数</div>
            <div class="stat-value">{{ statistics.count || 0 }} 条</div>
          </div>
        </el-col>
        <el-col :span="6">
          <div class="stat-item">
            <div class="stat-label">费用总计</div>
            <div class="stat-value">¥ {{ statistics.totalAmount || 0 }}</div>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="table-card">
      <el-table :data="records" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="recordDate" label="日期" width="120">
          <template #default="{ row }">
            {{ formatDate(row.recordDate) }}
          </template>
        </el-table-column>
        <el-table-column prop="department" label="部门" width="120" />
        <el-table-column prop="participantCount" label="人数" width="80" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">
            ¥ {{ row.amount }}
          </template>
        </el-table-column>
        <el-table-column prop="purpose" label="用途" show-overflow-tooltip />
        <el-table-column prop="responsiblePerson" label="负责人" width="120" />
        <el-table-column prop="createdByName" label="创建人" width="100" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
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
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="日期" prop="recordDate">
          <el-date-picker
            v-model="form.recordDate"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="部门" prop="department">
          <el-select v-model="form.department" placeholder="请选择部门">
            <el-option
              v-for="dept in departments"
              :key="dept.id"
              :label="dept.name"
              :value="dept.name"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="人数" prop="participantCount">
          <el-input-number v-model="form.participantCount" :min="1" :max="100" />
        </el-form-item>
        <el-form-item label="金额" prop="amount">
          <el-input v-model="form.amount" placeholder="请输入金额">
            <template #prepend>¥</template>
          </el-input>
        </el-form-item>
        <el-form-item label="用途" prop="purpose">
          <el-input v-model="form.purpose" placeholder="请输入用途" />
        </el-form-item>
        <el-form-item label="负责人" prop="responsiblePerson">
          <el-input v-model="form.responsiblePerson" placeholder="请输入负责人" />
        </el-form-item>
        <el-form-item label="发票路径" prop="invoicePath">
          <el-input v-model="form.invoicePath" placeholder="请输入发票路径" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">
          确定
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Search, Refresh } from '@element-plus/icons-vue'
import { getDinnerList, createDinner, updateDinner, deleteDinner, getDinnerStatistics } from '@/api/dinner'
import { getDepartmentList } from '@/api/department'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const records = ref([])
const departments = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('新增记录')
const submitLoading = ref(false)
const formRef = ref(null)

const statistics = ref({})

const filterForm = reactive({
  dateRange: [],
  department: ''
})

const form = reactive({
  id: null,
  recordDate: '',
  department: '',
  participantCount: 1,
  amount: '',
  purpose: '',
  responsiblePerson: '',
  invoicePath: ''
})

const rules = {
  recordDate: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ],
  department: [
    { required: true, message: '请选择部门', trigger: 'change' }
  ],
  participantCount: [
    { required: true, message: '请输入人数', trigger: 'blur' }
  ],
  amount: [
    { required: true, message: '请输入金额', trigger: 'blur' }
  ],
  purpose: [
    { required: true, message: '请输入用途', trigger: 'blur' }
  ]
}

async function loadRecords() {
  loading.value = true
  try {
    let res
    if (filterForm.dateRange && filterForm.dateRange.length === 2) {
      res = await getDinnerListByDateRange(filterForm.dateRange[0], filterForm.dateRange[1])
    } else if (filterForm.department) {
      res = await getDinnerListByDepartment(filterForm.department)
    } else {
      res = await getDinnerList()
    }

    if (res.code === 200) {
      records.value = res.data || []
    } else {
      ElMessage.error(res.message || '加载会餐记录失败')
    }
  } catch (error) {
    ElMessage.error('加载会餐记录失败')
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

async function loadStatistics() {
  try {
    const today = new Date()
    const startOfMonth = new Date(today.getFullYear(), today.getMonth(), 1)
    const endOfMonth = new Date(today.getFullYear(), today.getMonth() + 1, 0)

    const startDate = startOfMonth.toISOString().split('T')[0]
    const endDate = endOfMonth.toISOString().split('T')[0]

    const res = await getDinnerStatistics(startDate, endDate)
    if (res.code === 200) {
      statistics.value = res.data || {}
    }
  } catch (error) {
    console.error('加载统计信息失败', error)
  }
}

function handleCreate() {
  dialogTitle.value = '新增记录'
  form.id = null
  form.recordDate = ''
  form.department = ''
  form.participantCount = 1
  form.amount = ''
  form.purpose = ''
  form.responsiblePerson = ''
  form.invoicePath = ''
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑记录'
  form.id = row.id
  form.recordDate = row.recordDate
  form.department = row.department
  form.participantCount = row.participantCount
  form.amount = row.amount.toString()
  form.purpose = row.purpose
  form.responsiblePerson = row.responsiblePerson
  form.invoicePath = row.invoicePath
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除该会餐记录吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await deleteDinner(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadRecords()
      loadStatistics()
    } else {
      ElMessage.error(res.message || '删除失败')
    }
  } catch {
    // 用户取消
  }
}

async function handleSubmit() {
  try {
    await formRef.value.validate()
    submitLoading.value = true

    const data = {
      recordDate: form.recordDate,
      department: form.department,
      participantCount: form.participantCount,
      amount: form.amount,
      purpose: form.purpose,
      responsiblePerson: form.responsiblePerson,
      invoicePath: form.invoicePath,
      createdBy: userStore.userInfo?.id || 1
    }

    let res
    if (form.id) {
      res = await updateDinner(form.id, data)
    } else {
      res = await createDinner(data)
    }

    if (res.code === 200) {
      ElMessage.success(form.id ? '修改成功' : '新增成功')
      dialogVisible.value = false
      loadRecords()
      loadStatistics()
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
  form.id = null
}

function handleFilter() {
  loadRecords()
}

function handleReset() {
  filterForm.dateRange = []
  filterForm.department = ''
  loadRecords()
}

function formatDate(dateStr) {
  if (!dateStr) return ''
  const date = new Date(dateStr)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: '2-digit',
    day: '2-digit'
  })
}

onMounted(() => {
  loadRecords()
  loadDepartments()
  loadStatistics()
})
</script>

<style scoped>
.dinner-view {
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

.filter-card {
  margin-bottom: 20px;
}

.filter-form {
  margin-bottom: 0;
}

.statistics-card {
  margin-bottom: 20px;
  background-color: #f5f7fa;
}

.stat-item {
  text-align: center;
  padding: 10px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}

.table-card {
  margin-top: 20px;
}

.el-select {
  width: 200px;
}

.el-input-number {
  width: 200px;
}
</style>
