<template>
  <div class="attendance-view">
    <div class="page-header">
      <h1 class="page-title">考勤管理</h1>
      <el-button type="primary" @click="handleMark">
        <el-icon><Calendar /></el-icon>
        考勤打卡
      </el-button>
    </div>

    <el-card class="calendar-card">
      <template #header>
        <div class="card-header">
          <el-button @click="prevMonth" :icon="ArrowLeft">上月</el-button>
          <span class="current-month">{{ currentYear }}年{{ currentMonth }}月</span>
          <el-button @click="nextMonth">下月<el-icon class="el-icon--right"><ArrowRight /></el-icon></el-button>
        </div>
      </template>
      <el-calendar v-model="calendarDate">
        <template #date-cell="{ data }">
          <div class="calendar-cell">
            <div class="date-number">{{ data.day.split('-').slice(-1)[0] }}</div>
            <div v-if="getAttendanceStatus(data.day)" class="attendance-status">
              <el-tag :type="getAttendanceType(getAttendanceStatus(data.day))" size="small">
                {{ getAttendanceStatus(data.day) }}
              </el-tag>
            </div>
          </div>
        </template>
      </el-calendar>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">考勤记录</span>
          <el-button type="primary" size="small" @click="handleAdd">
            <el-icon><Plus /></el-icon>
            新增记录
          </el-button>
        </div>
      </template>
      <el-table :data="attendances" v-loading="loading" stripe>
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column prop="userName" label="用户名" width="120" />
        <el-table-column label="日期" width="150">
          <template #default="{ row }">
            {{ row.year }}-{{ row.month }}-{{ row.day }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="120">
          <template #default="{ row }">
            <el-tag :type="getAttendanceType(row.status)">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
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
      width="500px"
      @close="handleDialogClose"
    >
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px">
        <el-form-item label="日期" prop="date">
          <el-date-picker
            v-model="form.date"
            type="date"
            placeholder="选择日期"
            format="YYYY-MM-DD"
            value-format="YYYY-MM-DD"
          />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-select v-model="form.status" placeholder="请选择状态">
            <el-option label="正常上班" value="正常上班" />
            <el-option label="迟到" value="迟到" />
            <el-option label="早退" value="早退" />
            <el-option label="请假" value="请假" />
            <el-option label="旷工" value="旷工" />
            <el-option label="出差" value="出差" />
            <el-option label="加班" value="加班" />
          </el-select>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input
            v-model="form.remark"
            type="textarea"
            :rows="3"
            placeholder="请输入备注"
          />
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
import { ref, reactive, onMounted, watch } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Calendar, ArrowLeft, ArrowRight } from '@element-plus/icons-vue'
import {
  getAttendanceCalendar,
  markAttendance,
  updateAttendance,
  deleteAttendance
} from '@/api/attendance'
import { getUserList } from '@/api/user'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const loading = ref(false)
const attendances = ref([])
const users = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('考勤打卡')
const submitLoading = ref(false)
const formRef = ref(null)

const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth() + 1)
const calendarDate = ref(new Date())

const form = reactive({
  date: '',
  status: '正常上班',
  remark: ''
})

const rules = {
  date: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

function getAttendanceStatus(dateStr) {
  const date = new Date(dateStr)
  const day = date.getDate()
  const attendance = attendances.value.find(a =>
    a.year === date.getFullYear() &&
    a.month === date.getMonth() + 1 &&
    a.day === day
  )
  return attendance ? attendance.status : ''
}

function getAttendanceType(status) {
  switch (status) {
    case '正常上班':
      return 'success'
    case '迟到':
    case '早退':
    case '旷工':
      return 'danger'
    case '请假':
      return 'warning'
    case '出差':
    case '加班':
      return 'info'
    default:
      return ''
  }
}

async function loadAttendances() {
  loading.value = true
  try {
    const res = await getAttendanceCalendar(currentYear.value, currentMonth.value)
    if (res.code === 200) {
      attendances.value = res.data || []
    } else {
      ElMessage.error(res.message || '加载考勤记录失败')
    }
  } catch (error) {
    ElMessage.error('加载考勤记录失败')
  } finally {
    loading.value = false
  }
}

async function loadUsers() {
  try {
    const res = await getUserList()
    if (res.code === 200) {
      users.value = res.data || []
    }
  } catch (error) {
    console.error('加载用户列表失败', error)
  }
}

function handleMark() {
  dialogTitle.value = '考勤打卡'
  const today = new Date()
  form.date = today.toISOString().split('T')[0]
  form.status = '正常上班'
  form.remark = ''
  dialogVisible.value = true
}

function handleAdd() {
  dialogTitle.value = '新增考勤记录'
  form.date = ''
  form.status = '正常上班'
  form.remark = ''
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑考勤记录'
  const date = new Date(row.year, row.month - 1, row.day)
  form.date = date.toISOString().split('T')[0]
  form.status = row.status
  form.remark = row.remark
  form.id = row.id
  dialogVisible.value = true
}

async function handleDelete(row) {
  try {
    await ElMessageBox.confirm(
      `确定要删除该考勤记录吗？`,
      '删除确认',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }
    )

    const res = await deleteAttendance(row.id)
    if (res.code === 200) {
      ElMessage.success('删除成功')
      loadAttendances()
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

    const dateParts = form.date.split('-')
    const data = {
      userId: userStore.userInfo?.id || 1,
      year: parseInt(dateParts[0]),
      month: parseInt(dateParts[1]),
      day: parseInt(dateParts[2]),
      status: form.status,
      remark: form.remark
    }

    let res
    if (form.id) {
      res = await updateAttendance(form.id, data)
    } else {
      res = await markAttendance(data)
    }

    if (res.code === 200) {
      ElMessage.success(form.id ? '修改成功' : '打卡成功')
      dialogVisible.value = false
      loadAttendances()
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

function prevMonth() {
  if (currentMonth.value === 1) {
    currentYear.value--
    currentMonth.value = 12
  } else {
    currentMonth.value--
  }
  loadAttendances()
}

function nextMonth() {
  if (currentMonth.value === 12) {
    currentYear.value++
    currentMonth.value = 1
  } else {
    currentMonth.value++
  }
  loadAttendances()
}

watch(calendarDate, () => {
  const date = new Date(calendarDate.value)
  currentYear.value = date.getFullYear()
  currentMonth.value = date.getMonth() + 1
  loadAttendances()
})

onMounted(() => {
  loadAttendances()
  loadUsers()
})
</script>

<style scoped>
.attendance-view {
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

.calendar-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.current-month {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.calendar-cell {
  height: 100%;
  padding: 4px;
}

.date-number {
  font-size: 14px;
  color: #303133;
}

.attendance-status {
  margin-top: 4px;
}

.table-card {
  margin-top: 20px;
}

.el-select {
  width: 100%;
}
</style>
