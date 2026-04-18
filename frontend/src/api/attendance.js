import request from '@/utils/request'

export function getAttendanceList() {
  return request({
    url: '/attendance/list',
    method: 'get'
  })
}

export function getAttendanceByUserId(userId) {
  return request({
    url: `/attendance/user/${userId}`,
    method: 'get'
  })
}

export function getAttendanceCalendar(year, month) {
  return request({
    url: '/attendance/calendar',
    method: 'get',
    params: { year, month }
  })
}

export function getAttendance(id) {
  return request({
    url: `/attendance/${id}`,
    method: 'get'
  })
}

export function createAttendance(data) {
  return request({
    url: '/attendance/create',
    method: 'post',
    data
  })
}

export function updateAttendance(id, data) {
  return request({
    url: `/attendance/update/${id}`,
    method: 'put',
    data
  })
}

export function deleteAttendance(id) {
  return request({
    url: `/attendance/delete/${id}`,
    method: 'delete'
  })
}

export function markAttendance(data) {
  return request({
    url: '/attendance/mark',
    method: 'post',
    data
  })
}

export function getStatistics(year, month) {
  return request({
    url: '/attendance/statistics',
    method: 'get',
    params: { year, month }
  })
}

export function getUserStatistics(userId, year, month) {
  return request({
    url: `/attendance/user/${userId}/statistics`,
    method: 'get',
    params: { year, month }
  })
}
