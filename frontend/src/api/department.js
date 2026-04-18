import request from '@/utils/request'

export function getDepartmentList() {
  return request({
    url: '/department/list',
    method: 'get'
  })
}

export function getDepartment(id) {
  return request({
    url: `/department/${id}`,
    method: 'get'
  })
}

export function createDepartment(data) {
  return request({
    url: '/department/create',
    method: 'post',
    data
  })
}

export function updateDepartment(id, data) {
  return request({
    url: `/department/update/${id}`,
    method: 'put',
    data
  })
}

export function deleteDepartment(id) {
  return request({
    url: `/department/delete/${id}`,
    method: 'delete'
  })
}

export function getDepartmentCount() {
  return request({
    url: '/department/count',
    method: 'get'
  })
}
