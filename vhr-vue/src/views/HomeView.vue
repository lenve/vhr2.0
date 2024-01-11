<template>
  <div>
    <el-container>
      <el-header style="background-color: #02adff;align-items: center;display: flex;justify-content: space-between">
        <div style="font-family: 华文行楷;font-size: 30px">微人事</div>
        <div>
          <el-dropdown style="cursor:pointer;" @command="menuHandle">
    <span class="el-dropdown-link" style="display: flex;align-items: center">
      {{ hr.name }}
      <img :src="hr.userface" style="width: 48px;height: 48px;border-radius: 24px" alt="">
    </span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item command="usercenter">个人中心</el-dropdown-item>
                <el-dropdown-item command="settings">设置</el-dropdown-item>
                <el-dropdown-item command="logout" divided>注销登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>
      <el-container>
        <el-aside width="200px">
          <el-menu router>
            <template v-for="(menu,indexi) in menus">
              <el-sub-menu :index="indexi+''" v-if="!menu.hidden" :key="indexi">
                <template #title>
                  <el-icon>
                    <avatar/>
                  </el-icon>
                  <span>{{ menu.name }}</span>
                </template>
                <el-menu-item :index="child.path" v-for="(child,indexj) in menu.children" :key="indexj">{{
                    child.name
                  }}
                </el-menu-item>
              </el-sub-menu>
            </template>
          </el-menu>
        </el-aside>
        <el-main>
          <el-breadcrumb :separator-icon="ArrowRight" v-if="proxy.$router.currentRoute.value.path!='/home'">
            <el-breadcrumb-item :to="{ path: '/home' }">主页</el-breadcrumb-item>
            <el-breadcrumb-item>{{ proxy.$router.currentRoute.value.name }}</el-breadcrumb-item>
          </el-breadcrumb>
          <div v-if="proxy.$router.currentRoute.value.path=='/home'" style="font-size: 35px;font-family: 华文行楷;color: red;text-align: center">欢迎来到微人事脚手架！</div>
          <RouterView/>
        </el-main>
      </el-container>
    </el-container>
  </div>
</template>
<script setup>
import {ArrowRight} from '@element-plus/icons-vue'

import {RouterView} from 'vue-router'
import {
  Document,
  Menu as IconMenu,
  Location,
  Avatar,
  Setting,
} from '@element-plus/icons-vue'
import {reactive, toRefs} from "vue";
import {ElMessage, ElMessageBox} from "element-plus";
import {logout} from "@/api/login.js";
import {getCurrentInstance} from "vue";
import {loadMenus} from "@/api/menus.js";
import HomeView from "@/views/HomeView.vue";
import {menusStore} from "@/stores/index.js";

const mStore = menusStore();
const {proxy} = getCurrentInstance();
//加载指定路径下的所有 .vue 组件，modules 变量类似于 map，其中 key 就是组件的路径（/src/views/emp/EmpBasic.vue），value 则是组件对象
const modules = import.meta.glob('@/views/**/*.vue');


const data = reactive({
  hr: JSON.parse(window.sessionStorage.getItem("hr")),
  menus: mStore.menus,
})
const {hr, menus} = toRefs(data);

// function menuSelect(index, indexPath) {
//   proxy.$router.push(index);
// }

function loadAllMenus() {
  loadMenus().then(res => {
    menus.value = res.data;
    let fmtMenus = formatMenus(res.data);
    console.log(fmtMenus);
    fmtMenus.forEach(m => {
      proxy.$router.addRoute(m)
    })
  })
}

function formatMenus(menus) {
  let result = [];
  menus.forEach(menu => {
    let {path, name, children, component} = menu;
    if (children && children instanceof Array) {
      //递归去格式化 children
      children = formatMenus(children);
    }
    let formatM = {
      path: path,
      name: name,
      children: children,
      component: loadView(component)
    }
    result.push(formatM);
  })
  return result;
}

function loadView(viewPath) {
  if (viewPath == '/src/views/HomeView.vue') {
    return HomeView;
  } else {
    return modules[viewPath];
  }
}

function menuHandle(cmd) {
  if (cmd === 'logout') {
    ElMessageBox.confirm(
        '此操作将注销登录，是否继续?',
        '提示',
        {
          confirmButtonText: '确认',
          cancelButtonText: '取消',
          type: 'warning',
        }
    )
        .then(() => {
          logout().then(res => {
            //1. 注销成功，两件事
            window.sessionStorage.removeItem('hr');
            //跳转到登录页面
            proxy.$router.replace('/');
            //清空菜单数组
            mStore.clearMenus();
          })
        })
        .catch(() => {
          ElMessage.info('已取消操作')
        })
  }
}

console.log('==============')
// loadAllMenus();
</script>
