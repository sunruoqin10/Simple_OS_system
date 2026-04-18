import request from '@/utils/request'

export function getUserList() {
  return request({
    url: '/user/list',
    method: 'get'
  })
}

export function getUser(id) {
  return request({
    url: `/user/${id}`,
    method: 'get'
  })
}

export function createUser(data) {
  return request({
    url: '/user/create',
    method: 'post',
    data
  })
}

export function updateUser(id, data) {
  return request({
    url: `/user/update/${id}`,
    method: 'put',
    data
  })
}

export function deleteUser(id) {
  return request({
    url: `/user/delete/${id}`,
    method: 'delete'
  })
}

export function getUserCount() {
  return request({
    url: '/user/count',
    method: 'get'
  })
}
