import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, logout as logoutApi, getCurrentUser } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  const roles = ref([])
  const permissions = ref([])

  const isLoggedIn = computed(() => !!token.value)

  async function login(loginForm) {
    const res = await loginApi(loginForm)
    if (res.code === 200 && res.data?.token) {
      token.value = res.data.token
      localStorage.setItem('token', res.data.token)
      userInfo.value = res.data.user
      permissions.value = res.data.permissions || []
    }
    return res
  }

  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = null
      roles.value = []
      permissions.value = []
      localStorage.removeItem('token')
    }
  }

  async function fetchUserInfo() {
    if (!token.value) return null
    try {
      const res = await getCurrentUser()
      userInfo.value = res.data
      roles.value = res.data?.roles || []
      return res.data
    } catch (error) {
      return null
    }
  }

  function hasRole(role) {
    if (!role) return true
    return roles.value.includes(role)
  }

  function hasPermission(permission) {
    if (!permission) return true
    if (permissions.value.includes('ROLE_ADMIN')) return true
    return permissions.value.includes(permission)
  }

  function hasAnyPermission(permissionList) {
    if (!permissionList || permissionList.length === 0) return true
    if (permissions.value.includes('ROLE_ADMIN')) return true
    return permissionList.some(p => permissions.value.includes(p))
  }

  return {
    token,
    userInfo,
    roles,
    permissions,
    isLoggedIn,
    login,
    logout,
    fetchUserInfo,
    hasRole,
    hasPermission,
    hasAnyPermission
  }
})