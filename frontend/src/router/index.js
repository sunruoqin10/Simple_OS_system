import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/login',
    name: 'Login',
    component: () => import('@/views/login/LoginView.vue'),
    meta: { title: '登录', requiresAuth: false }
  },
  {
    path: '/',
    component: () => import('@/views/layout/MainLayout.vue'),
    redirect: '/dashboard',
    meta: { title: '主布局', requiresAuth: true },
    children: [
      {
        path: '/dashboard',
        name: 'Dashboard',
        component: () => import('@/views/DashboardView.vue'),
        meta: { title: '首页', requiresAuth: true }
      },
      {
        path: '/departments',
        name: 'Department',
        component: () => import('@/views/system/DepartmentView.vue'),
        meta: { title: '部门管理', requiresAuth: true }
      },
      {
        path: '/users',
        name: 'User',
        component: () => import('@/views/system/UserView.vue'),
        meta: { title: '用户管理', requiresAuth: true }
      },
      {
        path: '/dict',
        name: 'Dict',
        component: () => import('@/views/system/DictView.vue'),
        meta: { title: '数据字典', requiresAuth: true }
      },
      {
        path: '/roles',
        name: 'Role',
        component: () => import('@/views/system/RoleView.vue'),
        meta: { title: '角色管理', requiresAuth: true }
      },
      {
        path: '/attendance',
        name: 'Attendance',
        component: () => import('@/views/attendance/AttendanceView.vue'),
        meta: { title: '考勤管理', requiresAuth: true }
      },
      {
        path: '/holiday',
        name: 'Holiday',
        component: () => import('@/views/system/HolidayView.vue'),
        meta: { title: '节假日管理', requiresAuth: true }
      },
      {
        path: '/dinner',
        name: 'Dinner',
        component: () => import('@/views/dinner/DinnerRecordView.vue'),
        meta: { title: '会餐费管理', requiresAuth: true }
      }
    ]
  },
  {
    path: '/:pathMatch(.*)*',
    redirect: '/login'
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')

  if (to.meta.requiresAuth === false) {
    next()
  } else if (!token) {
    next('/login')
  } else {
    next()
  }
})

export default router
