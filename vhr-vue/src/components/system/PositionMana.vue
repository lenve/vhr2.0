<template>
  <div>
    <div>
      <el-input
          @keydown.enter.native="handleAdd"
          v-model="addPos.name"
          style="width: 300px"
          placeholder="请输入职位名称"
          :prefix-icon="Plus"
      />
      <el-button type="primary" @click="handleAdd">
        添加
        <el-icon class="el-icon--right">
          <Plus/>
        </el-icon>
      </el-button>
    </div>
    <div style="margin-top: 10px">
      <el-table :data="positions" border stripe style="width: 100%">
        <el-table-column prop="id" label="编号" width="180"/>
        <el-table-column prop="name" label="职位名称" width="180"/>
        <el-table-column prop="createDate" label="创建日期"/>
        <el-table-column label="是否启用">
          <template #default="scope">
            <el-switch
                @change="handleUpdate(scope.row)"
                v-model="scope.row.enabled"
                inline-prompt
                active-text="是"
                inactive-text="否"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button size="small" @click="handleEdit(scope.$index, scope.row)"
            >编辑
            </el-button
            >
            <el-button
                size="small"
                type="danger"
                @click="handleDelete(scope.$index, scope.row)"
            >删除
            </el-button
            >
          </template>
        </el-table-column>
      </el-table>
      <div style="display: flex;justify-content: flex-end">
        <el-pagination background
                       @change="paginationChange"
                       :page-sizes="[5,10,20,30,50,100]"
                       layout="sizes,prev, pager, next, jumper, ->, total" :total="total"/>
      </div>
    </div>
    <el-dialog
        v-model="dialogVisible"
        title="编辑职位"
        width="30%"
    >
      <div>
        <table>
          <tr>
            <td>职位名称：</td>
            <td>
              <el-input v-model="updatePos.name" placeholder="Please input"/>
            </td>
          </tr>
          <tr>
            <td>是否启用：</td>
            <td>
              <el-switch
                  v-model="updatePos.enabled"
                  inline-prompt
                  active-text="是"
                  inactive-text="否"
              />
            </td>
          </tr>
        </table>
      </div>
      <template #footer>
      <span class="dialog-footer">
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleUpdate(updatePos)">
          确认
        </el-button>
      </span>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import {reactive, toRefs} from "vue";
import {
  loadAllPositions,
  updatePosition,
  getPositionById,
  addPosition,
  deletePositionById
} from "@/api/system/position.js";
import {Plus} from '@element-plus/icons-vue'
import {ElMessage, ElMessageBox} from "element-plus";


const data = reactive({
  positions: [],
  total: 0,
  page: 1,
  size: 10,
  dialogVisible: false,
  updatePos: undefined,
  addPos: {name: ''}
})
const {positions, total, page, size, updatePos, dialogVisible, addPos} = toRefs(data);

function handleAdd() {
  addPosition(addPos.value).then(res => {
    positionList();
    addPos.value.name = '';
  })
}

function handleEdit(index, data) {
  getPositionById(data.id).then(res => {
    dialogVisible.value = true;
    updatePos.value = res.data;
  })
}

function handleUpdate(row) {
  updatePosition(row).then(res => {
    dialogVisible.value = false;
    //更新完毕，刷新
    positionList();
  })
}

function paginationChange(newPage, newSize) {
  page.value = newPage;
  size.value = newSize;
  positionList();
}

function positionList() {
  loadAllPositions({page: page.value, size: size.value}).then(res => {
    positions.value = res.data;
    total.value = res.total;
  })
}

function handleDelete(index, row) {
  ElMessageBox.confirm(
      '此操作将删除【' + row.name + '】部门，是否继续?',
      'Warning',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
  )
      .then(() => {
        deletePositionById(row.id).then(res => {
          positionList();
        })
      })
      .catch(() => {
        ElMessage({
          type: 'info',
          message: '取消删除',
        })
      })
}

positionList();
</script>

<style scoped>

</style>
