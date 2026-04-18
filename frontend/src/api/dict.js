import request from '@/utils/request'

export function getCategoryList() {
  return request({
    url: '/dict/category/list',
    method: 'get'
  })
}

export function getCategoryById(id) {
  return request({
    url: `/dict/category/${id}`,
    method: 'get'
  })
}

export function getCategoryByCode(code) {
  return request({
    url: `/dict/category/code/${code}`,
    method: 'get'
  })
}

export function createCategory(data) {
  return request({
    url: '/dict/category/create',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/dict/category/update',
    method: 'put',
    data
  })
}

export function deleteCategory(id) {
  return request({
    url: `/dict/category/${id}`,
    method: 'delete'
  })
}

export function getItemList() {
  return request({
    url: '/dict/item/list',
    method: 'get'
  })
}

export function getItemsByCategory(categoryId) {
  return request({
    url: `/dict/item/category/${categoryId}`,
    method: 'get'
  })
}

export function getEnabledItemsByCategory(categoryId) {
  return request({
    url: `/dict/item/category/${categoryId}/enabled`,
    method: 'get'
  })
}

export function getItemById(id) {
  return request({
    url: `/dict/item/${id}`,
    method: 'get'
  })
}

export function createItem(data) {
  return request({
    url: '/dict/item/create',
    method: 'post',
    data
  })
}

export function updateItem(data) {
  return request({
    url: '/dict/item/update',
    method: 'put',
    data
  })
}

export function deleteItem(id) {
  return request({
    url: `/dict/item/${id}`,
    method: 'delete'
  })
}

export function enableItem(id) {
  return request({
    url: `/dict/item/${id}/enable`,
    method: 'put'
  })
}

export function disableItem(id) {
  return request({
    url: `/dict/item/${id}/disable`,
    method: 'put'
  })
}

export function searchItems(keyword) {
  return request({
    url: '/dict/item/search',
    method: 'get',
    params: { keyword }
  })
}
