<template>
  <div>
    <el-tabs v-model="activeName">
      <el-tab-pane label="部门管理" name="first">部门管理</el-tab-pane>
      <el-tab-pane label="职位管理" name="positionMana">
        <position-mana></position-mana>
      </el-tab-pane>
      <el-tab-pane label="职称管理" name="third">职称管理</el-tab-pane>
      <el-tab-pane label="奖惩规则" name="fourth">奖惩规则</el-tab-pane>
      <el-tab-pane label="权限组" name="fourth">权限组</el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>

import {reactive, toRefs} from "vue";
import PositionMana from "@/components/system/PositionMana.vue";


const data = reactive({
  activeName: 'positionMana'
})

const {activeName} = toRefs(data);

</script>

<style scoped>

</style>
