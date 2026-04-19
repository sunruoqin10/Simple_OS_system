import request from '@/utils/request'

export function getRoleList() {
  return request({
    url: '/role/list',
    method: 'get'
  })
}

export function getRoleById(id) {
  return request({
    url: `/role/${id}`,
    method: 'get'
  })
}

export function createRole(data) {
  return request({
    url: '/role/create',
    method: 'post',
    data
  })
}

export function updateRole(data) {
  return request({
    url: '/role/update',
    method: 'put',
    data
  })
}

export function deleteRole(id) {
  return request({
    url: `/role/${id}`,
    method: 'delete'
  })
}

export function getRolePermissions(roleId) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: 'get'
  })
}

export function getRolePermissionCodes(roleId) {
  return request({
    url: `/role/${roleId}/permission-codes`,
    method: 'get'
  })
}

export function assignPermissions(roleId, permissionIds) {
  return request({
    url: `/role/${roleId}/permissions`,
    method: 'put',
    data: { permissionIds }
  })
}

export function getPermissionList() {
  return request({
    url: '/permission/list',
    method: 'get'
  })
}

export function getPermissionCategories() {
  return request({
    url: '/permission/categories',
    method: 'get'
  })
}

export function getPermissionsByCategory(category) {
  return request({
    url: `/permission/category/${category}`,
    method: 'get'
  })
}

export function getUserPermissions(userId) {
  return request({
    url: `/user/${userId}/permissions`,
    method: 'get'
  })
}

export function getUserRoles(userId) {
  return request({
    url: `/user/${userId}/roles`,
    method: 'get'
  })
}

export function setUserRoles(userId, roleIds) {
  return request({
    url: `/user/${userId}/roles`,
    method: 'put',
    data: { roleIds }
  })
}