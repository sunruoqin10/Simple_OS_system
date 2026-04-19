<template>
  <div class="attendance-view">
    <div class="page-header">
      <h1 class="page-title">考勤管理</h1>
      <div class="header-actions">
        <el-select v-if="hasPermission('ATT_VIEW_ALL')" v-model="selectedUserId" placeholder="选择用户" clearable style="width: 150px; margin-right: 10px" @change="handleUserChange">
          <el-option label="查看全部" :value="null" />
          <el-option v-for="user in users" :key="user.id" :label="user.realName || user.username" :value="user.id" />
        </el-select>
        <el-button v-if="hasPermission('ATT_EXPORT')" type="success" @click="handleExport">
          <el-icon><Download /></el-icon>
          导出Excel
        </el-button>
        <el-button v-if="hasPermission('ATT_EDIT_OWN')" type="primary" @click="handleMark">
          <el-icon><Calendar /></el-icon>
          考勤打卡
        </el-button>
      </div>
    </div>

    <el-row :gutter="20" class="statistics-row">
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon normal"><el-icon><User /></el-icon></div>
          <div class="stat-content">
            <div class="stat-label">正常上班</div>
            <div class="stat-value">{{ statistics.normal || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon late"><el-icon><Clock /></el-icon></div>
          <div class="stat-content">
            <div class="stat-label">迟到/早退/旷工</div>
            <div class="stat-value">{{ statistics.abnormal || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon leave"><el-icon><Calendar /></el-icon></div>
          <div class="stat-content">
            <div class="stat-label">请假/休假</div>
            <div class="stat-value">{{ statistics.leave || 0 }}</div>
          </div>
        </el-card>
      </el-col>
      <el-col :span="6">
        <el-card class="stat-card">
          <div class="stat-icon other"><el-icon><More /></el-icon></div>
          <div class="stat-content">
            <div class="stat-label">出差/加班</div>
            <div class="stat-value">{{ statistics.other || 0 }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>

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
          <div class="calendar-cell" :class="{ 'is-holiday': isHoliday(data.day), 'is-weekend': isWeekend(data.day) }">
            <div class="date-number">{{ data.day.split('-').slice(-1)[0] }}</div>
            <div v-if="getAttendanceStatus(data.day)" class="attendance-status">
              <el-tag :type="getAttendanceType(getAttendanceStatus(data.day))" size="small">
                {{ getAttendanceStatus(data.day) }}
              </el-tag>
            </div>
            <div v-else-if="isHoliday(data.day)" class="holiday-label">节假日</div>
          </div>
        </template>
      </el-calendar>
    </el-card>

    <el-card v-if="hasPermission('ATT_VIEW_ALL')" class="matrix-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">当月考勤明细</span>
        </div>
      </template>
      <el-table :data="matrixData" border size="small" :max-height="400">
        <el-table-column prop="userName" label="用户" width="100" fixed />
        <el-table-column
          v-for="day in monthDays"
          :key="day"
          :label="day.toString()"
          width="40"
          align="center"
        >
          <template #default="{ row }">
            <span
              v-if="getDayStatus(row.days, day)"
              :class="'status-' + getCellType(getDayStatus(row.days, day))"
              :title="getDayStatus(row.days, day)"
            >
              {{ getCellSymbol(getDayStatus(row.days, day)) }}
            </span>
            <span v-else class="no-record">-</span>
          </template>
        </el-table-column>
      </el-table>
    </el-card>

    <el-card class="table-card">
      <template #header>
        <div class="card-header">
          <span class="card-title">考勤记录</span>
          <el-button v-if="hasPermission('ATT_EDIT_OWN')" type="primary" size="small" @click="handleAdd">
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
              {{ getStatusSymbol(row.status) }} {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="remark" label="备注" show-overflow-tooltip />
        <el-table-column v-if="hasPermission('ATT_EDIT_OWN') || hasPermission('ATT_VIEW_ALL')" label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" size="small" @click="handleEdit(row)">
              <el-icon><Edit /></el-icon>
              编辑
            </el-button>
            <el-button v-if="hasPermission('ATT_EDIT_OWN')" type="danger" size="small" @click="handleDelete(row)">
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
        <el-form-item label="用户" prop="userId" v-if="hasPermission('ATT_VIEW_ALL')">
          <el-select v-model="form.userId" placeholder="请选择用户">
            <el-option v-for="user in users" :key="user.id" :label="user.realName || user.username" :value="user.id" />
          </el-select>
        </el-form-item>
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
            <el-option
              v-for="item in attendanceStatusList"
              :key="item.code"
              :label="item.name"
              :value="item.name"
            />
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
import { ref, reactive, onMounted, watch, computed } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Plus, Edit, Delete, Calendar, ArrowLeft, ArrowRight, Download, User, Clock, More } from '@element-plus/icons-vue'
import {
  getAttendanceCalendar,
  markAttendance,
  updateAttendance,
  deleteAttendance,
  getStatistics,
  exportAttendance,
  getAttendanceMatrix
} from '@/api/attendance'
import { getUserList } from '@/api/user'
import { getItemsByCategory } from '@/api/dict'
import { getHolidaysByYear } from '@/api/holiday'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()
const { hasPermission } = userStore

const ATT_CATEGORY_ID = 1

const loading = ref(false)
const attendances = ref([])
const users = ref([])
const attendanceStatusList = ref([])
const holidays = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('考勤打卡')
const submitLoading = ref(false)
const formRef = ref(null)
const selectedUserId = ref(null)

const currentDate = new Date()
const currentYear = ref(currentDate.getFullYear())
const currentMonth = ref(currentDate.getMonth() + 1)
const calendarDate = ref(new Date())

const statistics = ref({
  normal: 0,
  abnormal: 0,
  leave: 0,
  other: 0
})

const matrixData = ref([])
const monthDays = computed(() => {
  const year = currentYear.value
  const month = currentMonth.value
  const daysInMonth = new Date(year, month, 0).getDate()
  return Array.from({ length: daysInMonth }, (_, i) => i + 1)
})

const form = reactive({
  id: null,
  userId: null,
  date: '',
  status: '正常上班',
  remark: ''
})

const rules = {
  userId: [
    { required: true, message: '请选择用户', trigger: 'change' }
  ],
  date: [
    { required: true, message: '请选择日期', trigger: 'change' }
  ],
  status: [
    { required: true, message: '请选择状态', trigger: 'change' }
  ]
}

function isWeekend(dateStr) {
  const date = new Date(dateStr)
  const dayOfWeek = date.getDay()
  return dayOfWeek === 0 || dayOfWeek === 6
}

function isHoliday(dateStr) {
  const date = new Date(dateStr)
  const year = date.getFullYear()
  const month = date.getMonth() + 1
  const day = date.getDate()

  const holidayItem = holidays.value.find(h =>
    h.year === year && h.month === month && h.day === day
  )

  if (holidayItem) {
    if (holidayItem.type === '调休') {
      return false
    }
    if (holidayItem.type === '法定节假日') {
      return true
    }
  }

  return isWeekend(dateStr)
}

function getStatusSymbol(status) {
  const statusItem = attendanceStatusList.value.find(s => s.name === status)
  if (statusItem && statusItem.description) {
    const desc = statusItem.description
    if (desc.includes('正常出勤')) return '●'
    if (desc.includes('迟到') || desc.includes('早退')) return '⏰'
    if (desc.includes('旷工')) return '✗'
    if (desc.includes('全天请假') || desc.includes('全天休假')) return '○'
    if (desc.includes('半天请假') || desc.includes('半天休假')) return '◐'
    if (desc.includes('加班')) return '■'
    if (desc.includes('出差')) return '✈'
  }
  if (status.includes('正常')) return '●'
  if (status.includes('迟到') || status.includes('早退')) return '⏰'
  if (status.includes('旷工')) return '✗'
  if (status.includes('假')) return '○'
  if (status.includes('加班')) return '■'
  if (status.includes('出差')) return '✈'
  return ''
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
  const statusItem = attendanceStatusList.value.find(s => s.name === status)
  if (statusItem && statusItem.description) {
    const desc = statusItem.description
    if (desc.includes('正常')) return 'success'
    if (desc.includes('迟到') || desc.includes('早退') || desc.includes('旷工')) return 'danger'
    if (desc.includes('休假') || desc.includes('请假')) return 'warning'
    if (desc.includes('出差') || desc.includes('加班')) return 'info'
  }
  return ''
}

async function loadAttendances() {
  loading.value = true
  try {
    const params = {
      year: currentYear.value,
      month: currentMonth.value
    }
    if (selectedUserId.value) {
      params.userId = selectedUserId.value
    }
    const res = await getAttendanceCalendar(params.year, params.month, params.userId)
    if (res.code === 200) {
      attendances.value = res.data || []
      calculateStatistics()
    } else {
      ElMessage.error(res.message || '加载考勤记录失败')
    }
  } catch (error) {
    ElMessage.error('加载考勤记录失败')
  } finally {
    loading.value = false
  }
}

async function loadStatistics() {
  try {
    const params = {
      year: currentYear.value,
      month: currentMonth.value
    }
    if (selectedUserId.value) {
      params.userId = selectedUserId.value
    }
    const res = await getStatistics(params.year, params.month)
    if (res.code === 200) {
      const data = res.data || {}
      statistics.value = {
        normal: data.normal || 0,
        abnormal: data.abnormal || 0,
        leave: data.leave || 0,
        other: data.other || 0
      }
    }
  } catch (error) {
    console.error('加载统计数据失败', error)
  }
}

async function loadMatrixData() {
  try {
    const res = await getAttendanceMatrix(currentYear.value, currentMonth.value)
    if (res.code === 200) {
      matrixData.value = res.data || []
    }
  } catch (error) {
    console.error('加载矩阵数据失败', error)
  }
}

function getDayStatus(userDays, day) {
  return userDays ? userDays[day] : null
}

function getCellSymbol(status) {
  return getStatusSymbol(status)
}

function getCellType(status) {
  return getAttendanceType(status)
}

function calculateStatistics() {
  const stats = {
    normal: 0,
    abnormal: 0,
    leave: 0,
    other: 0
  }

  attendances.value.forEach(a => {
    const statusItem = attendanceStatusList.value.find(s => s.name === a.status)
    let category = 'other'
    if (statusItem && statusItem.description) {
      const desc = statusItem.description
      if (desc.includes('正常')) category = 'normal'
      else if (desc.includes('迟到') || desc.includes('早退') || desc.includes('旷工')) category = 'abnormal'
      else if (desc.includes('假') || desc.includes('休假')) category = 'leave'
      else category = 'other'
    }
    stats[category]++
  })

  statistics.value = stats
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

async function loadAttendanceStatus() {
  try {
    const res = await getItemsByCategory(ATT_CATEGORY_ID)
    if (res.code === 200) {
      attendanceStatusList.value = res.data || []
    }
  } catch (error) {
    console.error('加载考勤状态字典失败', error)
  }
}

async function loadHolidays() {
  try {
    const res = await getHolidaysByYear(currentYear.value)
    if (res.code === 200) {
      holidays.value = res.data || []
    }
  } catch (error) {
    console.error('加载节假日数据失败', error)
  }
}

function handleUserChange() {
  loadAttendances()
  loadStatistics()
}

async function handleExport() {
  try {
    ElMessage.info('正在导出考勤数据...')
    const response = await exportAttendance(currentYear.value, currentMonth.value, selectedUserId.value)
    const blob = new Blob([response.data], { type: 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' })
    const url = window.URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `考勤记录_${currentYear.value}年${currentMonth.value}月.xlsx`
    link.click()
    window.URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  } catch (error) {
    ElMessage.error('导出失败')
  }
}

function handleMark() {
  dialogTitle.value = '考勤打卡'
  form.id = null
  form.userId = selectedUserId.value || userStore.userInfo?.id || null
  const today = new Date()
  form.date = today.toISOString().split('T')[0]
  form.status = '正常上班'
  form.remark = ''
  dialogVisible.value = true
}

function handleAdd() {
  dialogTitle.value = '新增考勤记录'
  form.id = null
  form.userId = selectedUserId.value || null
  form.date = ''
  form.status = '正常上班'
  form.remark = ''
  dialogVisible.value = true
}

function handleEdit(row) {
  dialogTitle.value = '编辑考勤记录'
  form.id = row.id
  form.userId = row.userId
  const date = new Date(row.year, row.month - 1, row.day)
  form.date = date.toISOString().split('T')[0]
  form.status = row.status
  form.remark = row.remark
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

    const dateParts = form.date.split('-')
    const data = {
      userId: form.userId || userStore.userInfo?.id || 1,
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
  form.userId = null
}

function prevMonth() {
  if (currentMonth.value === 1) {
    currentYear.value--
    currentMonth.value = 12
  } else {
    currentMonth.value--
  }
  loadAttendances()
  loadStatistics()
  loadMatrixData()
}

function nextMonth() {
  if (currentMonth.value === 12) {
    currentYear.value++
    currentMonth.value = 1
  } else {
    currentMonth.value++
  }
  loadAttendances()
  loadStatistics()
  loadMatrixData()
}

onMounted(() => {
  loadUsers()
  loadAttendances()
  loadStatistics()
  loadAttendanceStatus()
  loadHolidays()
  if (hasPermission('ATT_VIEW_ALL')) {
    loadMatrixData()
  }
})

watch(calendarDate, () => {
  const date = new Date(calendarDate.value)
  currentYear.value = date.getFullYear()
  currentMonth.value = date.getMonth() + 1
  loadAttendances()
  loadStatistics()
  loadHolidays()
  if (hasPermission('ATT_VIEW_ALL')) {
    loadMatrixData()
  }
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

.header-actions {
  display: flex;
  align-items: center;
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

.calendar-cell.is-holiday {
  background-color: #f0f9eb;
}

.calendar-cell.is-weekend:not(.is-holiday) {
  background-color: #f5f5f5;
}

.date-number {
  font-size: 14px;
  color: #303133;
}

.attendance-status {
  margin-top: 4px;
}

.holiday-label {
  margin-top: 4px;
  font-size: 10px;
  color: #67c23a;
}

.table-card {
  margin-top: 20px;
}

.matrix-card {
  margin-top: 20px;
}

.no-record {
  color: #c0c4cc;
}

.status-success {
  color: #67c23a;
  font-weight: bold;
}

.status-danger {
  color: #f56c6c;
  font-weight: bold;
}

.status-warning {
  color: #e6a23c;
  font-weight: bold;
}

.status-info {
  color: #409eff;
  font-weight: bold;
}

.el-select {
  width: 100%;
}

.statistics-row {
  margin-bottom: 20px;
}

.stat-card {
  display: flex;
  align-items: center;
  padding: 10px;
}

.stat-icon {
  width: 50px;
  height: 50px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  margin-right: 15px;
}

.stat-icon.normal {
  background-color: #f0f9eb;
  color: #67c23a;
}

.stat-icon.late {
  background-color: #fef0f0;
  color: #f56c6c;
}

.stat-icon.leave {
  background-color: #fdf6ec;
  color: #e6a23c;
}

.stat-icon.other {
  background-color: #ecf5ff;
  color: #409eff;
}

.stat-content {
  flex: 1;
}

.stat-label {
  font-size: 14px;
  color: #909399;
  margin-bottom: 5px;
}

.stat-value {
  font-size: 24px;
  font-weight: 600;
  color: #303133;
}
</style>