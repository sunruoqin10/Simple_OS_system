<template>
  <div class="holiday-view">
    <div class="page-header">
      <h1 class="page-title">节假日管理</h1>
    </div>

    <el-card class="filter-card">
      <el-row :gutter="20">
        <el-col :span="8">
          <div class="filter-item">
            <span class="filter-label">选择年份：</span>
            <el-select v-model="selectedYear" placeholder="请选择年份" @change="handleYearChange">
              <el-option
                v-for="year in yearOptions"
                :key="year"
                :label="year + '年'"
                :value="year"
              />
            </el-select>
          </div>
        </el-col>
        <el-col :span="16">
          <div class="filter-actions">
            <el-button type="primary" @click="handleAdd">
              <el-icon><Plus /></el-icon>
              新增节假日
            </el-button>
            <el-button @click="loadHolidays">
              <el-icon><Refresh /></el-icon>
              刷新
            </el-button>
          </div>
        </el-col>
      </el-row>
    </el-card>

    <el-card class="table-card">
      <el-table :data="holidays" v-loading="loading" stripe>
        <el-table-column prop="year" label="年份" width="100" />
        <el-table-column label="日期" width="150">
          <template #default="{ row }">
            {{ row.year }}-{{ String(row.month).padStart(2, '0') }}-{{ String(row.day).padStart(2, '0') }}
          </template>
        </el-table-column>
        <el-table-column prop="name" label="节假日名称" width="200" />
        <el-table-column prop="type" label="类型" width="120">
          <template #default="{ row }">
            <el-tag :type="row.type === '法定节假日' ? 'success' : 'warning'" size="small">
              {{ row.type }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
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
      width="500px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="100px">
        <el-form-item label="年份" prop="year">
          <el-input-number
            v-model="form.year"
            :min="2000"
            :max="2100"
            :disabled="form.id != null"
          />
        </el-form-item>
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
            @change="handleDateChange"
          />
        </el-form-item>
        <el-form-item label="节假日名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入节假日名称" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-radio-group v-model="form.type">
            <el-radio label="法定节假日">法定节假日</el-radio>
            <el-radio label="调休">调休（周末上班）</el-radio>
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
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Refresh } from '@element-plus/icons-vue'
import {
  getHolidaysByYear,
  createHoliday,
  updateHoliday,
  deleteHoliday
} from '@/api/holiday'

const loading = ref(false)
const holidays = ref([])
const selectedYear = ref(new Date().getFullYear())
const dialogVisible = ref(false)
const dialogTitle = ref('新增节假日')
const submitLoading = ref(false)
const formRef = ref(null)

const currentYear = new Date().getFullYear()
const yearOptions = Array.from({ length: 11 }, (_, i) => currentYear - 5 + i)

const form = reactive({
  id: null,
  year: currentYear,
  month: null,
  day: null,
  name: '',
  type: '法定节假日',
  date: ''
})

const rules = {
  date: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ],
  name: [
    { required: true, message: '请输入节假日名称', trigger: 'blur' }
  ],
  type: [
    { required: true, message: '请选择类型', trigger: 'change' }
  ]
}

async function loadHolidays() {
  loading.value = true
  try {
    const res = await getHolidaysByYear(selectedYear.value)
    if (res.code === 200) {
      holidays.value = res.data || []
    } else {
      ElMessage.error(res.message || '加载节假日数据失败')
    }
  } catch (error) {
    ElMessage.error('加载节假日数据失败')
  } finally {
    loading.value = false
  }
}

function handleYearChange() {
  loadHolidays()
}

function handleAdd() {
  dialogTitle.value = '新增节假日'
  form.id = null
  form.year = selectedYear.value
  form.month = null
  form.day = null
  form.name = ''
  form.type = '法定节假日'
  form.date = ''
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑节假日'
  form.id = row.id
  form.year = row.year
  form.month = row.month
  form.day = row.day
  form.name = row.name
  form.type = row.type
  form.date = `${row.year}-${String(row.month).padStart(2, '0')}-${String(row.day).padStart(2, '0')}`
  dialogVisible.value = true
}

function handleDateChange(date) {
  if (date) {
    const parts = date.split('-')
    form.year = parseInt(parts[0])
    form.month = parseInt(parts[1])
    form.day = parseInt(parts[2])
  }
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除"${row.name}"吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await deleteHoliday(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadHolidays()
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
      year: form.year,
      month: form.month,
      day: form.day,
      name: form.name,
      type: form.type
    }

    let res
    if (form.id) {
      res = await updateHoliday(form.id, data)
    } else {
      res = await createHoliday(data)
    }

    if (res.code === 200) {
      ElMessage.success(form.id ? '修改成功' : '添加成功')
      dialogVisible.value = false
      loadHolidays()
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
  form.year = selectedYear.value
  form.month = null
  form.day = null
  form.name = ''
  form.type = '法定节假日'
  form.date = ''
}

onMounted(() => {
  loadHolidays()
})
</script>

<style scoped>
.holiday-view {
  padding: 20px;
}

.page-header {
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

.filter-item {
  display: flex;
  align-items: center;
}

.filter-label {
  margin-right: 10px;
  font-weight: 500;
}

.filter-actions {
  display: flex;
  justify-content: flex-end;
}

.table-card {
  margin-top: 20px;
}

.el-select {
  width: 150px;
}

.el-input-number {
  width: 150px;
}
</style>
