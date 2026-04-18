import request from '@/utils/request'

export function getDinnerList() {
  return request({
    url: '/dinner/list',
    method: 'get'
  })
}

export function getDinnerListByDateRange(startDate, endDate) {
  return request({
    url: '/dinner/list/date-range',
    method: 'get',
    params: { startDate, endDate }
  })
}

export function getDinnerListByDepartment(department) {
  return request({
    url: '/dinner/list/department',
    method: 'get',
    params: { department }
  })
}

export function getDinner(id) {
  return request({
    url: `/dinner/${id}`,
    method: 'get'
  })
}

export function createDinner(data) {
  return request({
    url: '/dinner/create',
    method: 'post',
    data
  })
}

export function updateDinner(id, data) {
  return request({
    url: `/dinner/update/${id}`,
    method: 'put',
    data
  })
}

export function deleteDinner(id) {
  return request({
    url: `/dinner/delete/${id}`,
    method: 'delete'
  })
}

export function getDinnerStatistics(startDate, endDate) {
  return request({
    url: '/dinner/statistics',
    method: 'get',
    params: { startDate, endDate }
  })
}

export function getDinnerTotalAmount(startDate, endDate) {
  return request({
    url: '/dinner/total-amount',
    method: 'get',
    params: { startDate, endDate }
  })
}
