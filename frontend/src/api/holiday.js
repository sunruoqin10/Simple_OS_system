import request from '@/utils/request'

export function getHolidayList() {
  return request({
    url: '/holiday/list',
    method: 'get'
  })
}

export function getHolidaysByYear(year) {
  return request({
    url: `/holiday/year/${year}`,
    method: 'get'
  })
}

export function checkDate(year, month, day) {
  return request({
    url: '/holiday/check',
    method: 'get',
    params: { year, month, day }
  })
}

export function getHoliday(id) {
  return request({
    url: `/holiday/${id}`,
    method: 'get'
  })
}

export function createHoliday(data) {
  return request({
    url: '/holiday/create',
    method: 'post',
    data
  })
}

export function updateHoliday(id, data) {
  return request({
    url: `/holiday/update/${id}`,
    method: 'put',
    data
  })
}

export function deleteHoliday(id) {
  return request({
    url: `/holiday/delete/${id}`,
    method: 'delete'
  })
}

export function deleteHolidaysByYear(year) {
  return request({
    url: `/holiday/year/${year}`,
    method: 'delete'
  })
}
