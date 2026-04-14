import { defineStore } from 'pinia'
import { ref, computed } from 'vue'
import { login as loginApi, logout as logoutApi, getCurrentUser } from '@/api/auth'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('token') || '')
  const userInfo = ref(null)
  const roles = ref([])

  const isLoggedIn = computed(() => !!token.value)

  async function login(loginForm) {
    const res = await loginApi(loginForm)
    token.value = res.data?.token || ''
    localStorage.setItem('token', token.value)
    return res
  }

  async function logout() {
    try {
      await logoutApi()
    } finally {
      token.value = ''
      userInfo.value = null
      roles.value = []
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
      logout()
      return null
    }
  }

  function hasRole(role) {
    return roles.value.includes(role)
  }

  return {
    token,
    userInfo,
    roles,
    isLoggedIn,
    login,
    logout,
    fetchUserInfo,
    hasRole
  }
})
