<template>
  <div class="dict-view">
    <div class="page-header">
      <h1 class="page-title">数据字典管理</h1>
    </div>

    <el-row :gutter="20">
      <el-col :span="6">
        <el-card class="category-card">
          <template #header>
            <div class="card-header">
              <span>字典分类</span>
              <el-button type="primary" size="small" @click="handleAddCategory">
                <el-icon><Plus /></el-icon>
              </el-button>
            </div>
          </template>
          <div class="category-list">
            <el-tree
              :data="categoryTree"
              :props="{ label: 'name', children: 'children' }"
              node-key="id"
              :expand-on-click-node="false"
              :highlight-current="true"
              @node-click="handleCategoryClick"
            >
              <template #default="{ node, data }">
                <span class="tree-node">
                  <span>{{ data.name }}</span>
                  <span class="node-actions">
                    <el-icon size="14" @click.stop="handleEditCategory(data)">
                      <Edit />
                    </el-icon>
                    <el-icon size="14" @click.stop="handleDeleteCategory(data)">
                      <Delete />
                    </el-icon>
                  </span>
                </span>
              </template>
            </el-tree>
          </div>
        </el-card>
      </el-col>

      <el-col :span="18">
        <el-card class="item-card">
          <template #header>
            <div class="card-header">
              <span>字典项列表</span>
              <div class="header-actions">
                <el-input
                  v-model="searchKeyword"
                  placeholder="搜索字典项"
                  size="small"
                  style="width: 200px; margin-right: 10px"
                  @keyup.enter="handleSearch"
                >
                  <template #prefix>
                    <el-icon><Search /></el-icon>
                  </template>
                </el-input>
                <el-button type="primary" size="small" @click="handleAddItem" :disabled="!currentCategory">
                  <el-icon><Plus /></el-icon>
                  新增字典项
                </el-button>
              </div>
            </div>
          </template>

          <el-table :data="currentItems" style="width: 100%">
            <el-table-column prop="code" label="字典代码" width="150" />
            <el-table-column prop="name" label="字典名称" width="150" />
            <el-table-column prop="description" label="说明" />
            <el-table-column prop="sortOrder" label="排序" width="80" />
            <el-table-column label="状态" width="80">
              <template #default="{ row }">
                <el-tag :type="row.status === 1 ? 'success' : 'info'" size="small">
                  {{ row.status === 1 ? '启用' : '禁用' }}
                </el-tag>
              </template>
            </el-table-column>
            <el-table-column label="操作" width="180" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" size="small" @click="handleEditItem(row)">
                  编辑
                </el-button>
                <el-button
                  link
                  :type="row.status === 1 ? 'warning' : 'success'"
                  size="small"
                  @click="handleToggleStatus(row)"
                >
                  {{ row.status === 1 ? '禁用' : '启用' }}
                </el-button>
                <el-button link type="danger" size="small" @click="handleDeleteItem(row)">
                  删除
                </el-button>
              </template>
            </el-table-column>
          </el-table>
        </el-card>
      </el-col>
    </el-row>

    <el-dialog
      v-model="categoryDialogVisible"
      :title="categoryForm.id ? '编辑分类' : '新增分类'"
      width="500px"
    >
      <el-form :model="categoryForm" :rules="categoryRules" ref="categoryFormRef" label-width="100px">
        <el-form-item label="分类代码" prop="code">
          <el-input v-model="categoryForm.code" placeholder="请输入分类代码" />
        </el-form-item>
        <el-form-item label="分类名称" prop="name">
          <el-input v-model="categoryForm.name" placeholder="请输入分类名称" />
        </el-form-item>
        <el-form-item label="排序号" prop="sortOrder">
          <el-input-number v-model="categoryForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="categoryForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="categoryDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitCategoryForm">确定</el-button>
      </template>
    </el-dialog>

    <el-dialog
      v-model="itemDialogVisible"
      :title="itemForm.id ? '编辑字典项' : '新增字典项'"
      width="500px"
    >
      <el-form :model="itemForm" :rules="itemRules" ref="itemFormRef" label-width="100px">
        <el-form-item label="所属分类">
          <el-input v-model="currentCategoryName" disabled />
        </el-form-item>
        <el-form-item label="字典代码" prop="code">
          <el-input v-model="itemForm.code" placeholder="请输入字典代码" />
        </el-form-item>
        <el-form-item label="字典名称" prop="name">
          <el-input v-model="itemForm.name" placeholder="请输入字典名称" />
        </el-form-item>
        <el-form-item label="说明" prop="description">
          <el-input
            v-model="itemForm.description"
            type="textarea"
            :rows="3"
            placeholder="请输入说明"
          />
        </el-form-item>
        <el-form-item label="排序号" prop="sortOrder">
          <el-input-number v-model="itemForm.sortOrder" :min="0" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="itemForm.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="itemDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="submitItemForm">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import {
  getCategoryList,
  getItemsByCategory,
  createCategory,
  updateCategory,
  deleteCategory,
  createItem,
  updateItem,
  deleteItem,
  enableItem,
  disableItem,
  searchItems
} from '@/api/dict'
import { Plus, Edit, Delete, Search } from '@element-plus/icons-vue'

const categoryTree = ref([])
const allItems = ref([])
const currentCategory = ref(null)
const searchKeyword = ref('')

const categoryDialogVisible = ref(false)
const categoryFormRef = ref(null)
const categoryForm = ref({
  id: null,
  code: '',
  name: '',
  parentId: null,
  sortOrder: 0,
  status: 1
})

const categoryRules = {
  code: [{ required: true, message: '请输入分类代码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入分类名称', trigger: 'blur' }]
}

const itemDialogVisible = ref(false)
const itemFormRef = ref(null)
const itemForm = ref({
  id: null,
  categoryId: null,
  code: '',
  name: '',
  description: '',
  sortOrder: 0,
  status: 1
})

const itemRules = {
  code: [{ required: true, message: '请输入字典代码', trigger: 'blur' }],
  name: [{ required: true, message: '请输入字典名称', trigger: 'blur' }]
}

const currentCategoryName = computed(() => {
  return currentCategory.value ? currentCategory.value.name : ''
})

const currentItems = computed(() => {
  if (searchKeyword.value) {
    return allItems.value
  }
  if (!currentCategory.value) {
    return []
  }
  return allItems.value.filter(
    item => item.categoryId === currentCategory.value.id
  )
})

const loadCategories = async () => {
  try {
    const res = await getCategoryList()
    if (res.code === 200) {
      categoryTree.value = res.data || []
    }
  } catch (error) {
    console.error('加载分类失败:', error)
  }
}

const loadItems = async () => {
  try {
    const res = await getItemsByCategory(currentCategory.value?.id)
    if (res.code === 200) {
      allItems.value = res.data || []
    }
  } catch (error) {
    console.error('加载字典项失败:', error)
  }
}

const loadAllItems = async () => {
  try {
    const res = await searchItems(searchKeyword.value)
    if (res.code === 200) {
      allItems.value = res.data || []
    }
  } catch (error) {
    console.error('搜索字典项失败:', error)
  }
}

const handleCategoryClick = (data) => {
  currentCategory.value = data
  searchKeyword.value = ''
  loadItems()
}

const handleAddCategory = () => {
  categoryForm.value = {
    id: null,
    code: '',
    name: '',
    parentId: null,
    sortOrder: 0,
    status: 1
  }
  categoryDialogVisible.value = true
}

const handleEditCategory = (data) => {
  categoryForm.value = {
    id: data.id,
    code: data.code,
    name: data.name,
    parentId: data.parentId,
    sortOrder: data.sortOrder,
    status: data.status
  }
  categoryDialogVisible.value = true
}

const handleDeleteCategory = async (data) => {
  try {
    await ElMessageBox.confirm('确定要删除该分类吗？', '提示', {
      type: 'warning'
    })
    await deleteCategory(data.id)
    ElMessage.success('删除成功')
    loadCategories()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const submitCategoryForm = async () => {
  try {
    await categoryFormRef.value.validate()
    if (categoryForm.value.id) {
      await updateCategory(categoryForm.value)
      ElMessage.success('更新成功')
    } else {
      await createCategory(categoryForm.value)
      ElMessage.success('创建成功')
    }
    categoryDialogVisible.value = false
    loadCategories()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

const handleAddItem = () => {
  if (!currentCategory.value) {
    ElMessage.warning('请先选择一个分类')
    return
  }
  itemForm.value = {
    id: null,
    categoryId: currentCategory.value.id,
    code: '',
    name: '',
    description: '',
    sortOrder: 0,
    status: 1
  }
  itemDialogVisible.value = true
}

const handleEditItem = (row) => {
  itemForm.value = {
    id: row.id,
    categoryId: row.categoryId,
    code: row.code,
    name: row.name,
    description: row.description,
    sortOrder: row.sortOrder,
    status: row.status
  }
  itemDialogVisible.value = true
}

const handleDeleteItem = async (row) => {
  try {
    await ElMessageBox.confirm('确定要删除该字典项吗？', '提示', {
      type: 'warning'
    })
    await deleteItem(row.id)
    ElMessage.success('删除成功')
    loadItems()
  } catch (error) {
    if (error !== 'cancel') {
      ElMessage.error(error.message || '删除失败')
    }
  }
}

const handleToggleStatus = async (row) => {
  try {
    if (row.status === 1) {
      await disableItem(row.id)
      ElMessage.success('已禁用')
    } else {
      await enableItem(row.id)
      ElMessage.success('已启用')
    }
    loadItems()
  } catch (error) {
    ElMessage.error(error.message || '操作失败')
  }
}

const submitItemForm = async () => {
  try {
    await itemFormRef.value.validate()
    if (itemForm.value.id) {
      await updateItem(itemForm.value)
      ElMessage.success('更新成功')
    } else {
      await createItem(itemForm.value)
      ElMessage.success('创建成功')
    }
    itemDialogVisible.value = false
    loadItems()
  } catch (error) {
    console.error('提交失败:', error)
  }
}

const handleSearch = () => {
  if (searchKeyword.value) {
    loadAllItems()
  } else if (currentCategory.value) {
    loadItems()
  }
}

onMounted(() => {
  loadCategories()
})
</script>

<style scoped>
.dict-view {
  padding: 20px;
}

.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  margin: 0;
}

.category-card {
  height: calc(100vh - 200px);
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header-actions {
  display: flex;
  align-items: center;
}

.category-list {
  height: calc(100% - 50px);
  overflow-y: auto;
}

.tree-node {
  display: flex;
  justify-content: space-between;
  align-items: center;
  width: 100%;
}

.node-actions {
  display: flex;
  gap: 8px;
  opacity: 0;
  transition: opacity 0.3s;
}

.tree-node:hover .node-actions {
  opacity: 1;
}

.node-actions .el-icon {
  cursor: pointer;
}

.node-actions .el-icon:hover {
  color: #409eff;
}

.item-card {
  height: calc(100vh - 200px);
}

.el-input-number {
  width: 200px;
}
</style>
