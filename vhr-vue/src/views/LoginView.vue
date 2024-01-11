<template>
  <div style="display: flex;justify-content: center">
    <el-card
        v-loading="loading"
        element-loading-text="拼命登录中..."
        :element-loading-spinner="svg"
        element-loading-svg-view-box="-10, -10, 50, 50"
        element-loading-background="rgba(122, 122, 122, 0.8)"
        class="box-card" style="width: 400px;margin-top: 150px">
      <template #header>
        <div class="card-header">
          <span>登录</span>
        </div>
      </template>
      <div>
        <table>
          <tr>
            <td>用户名：</td>
            <td>
              <el-input v-model="hr.username" placeholder="请输入用户名..." clearable/>
            </td>
          </tr>
          <tr>
            <td>用户密码：</td>
            <td>
              <el-input
                  v-model="hr.password"
                  type="password"
                  placeholder="请输入用户密码..."
                  show-password
              />
            </td>
          </tr>
        </table>
      </div>
      <template #footer>
        <div style="display: flex;justify-content: center">
          <el-button>重置</el-button>
          <el-button type="primary" @click="loginHandle">登录</el-button>
        </div>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import {reactive, toRefs} from "vue";
import {login} from "@/api/login.js";
import {getCurrentInstance} from "vue";

const {proxy} = getCurrentInstance();


const data = reactive({
  hr: {
    username: 'admin',
    password: '123'
  },
  loading: false
})
const {hr, loading} = toRefs(data);
const svg = `
        <path class="path" d="
          M 30 15
          L 28 17
          M 25.61 25.61
          A 15 15, 0, 0, 1, 15 30
          A 15 15, 0, 1, 1, 27.99 7.5
          L 15 15
        " style="stroke-width: 4px; fill: rgba(0, 0, 0, 0)"/>
      `

function loginHandle() {
  loading.value = true;
  login(hr.value).then(data => {
    loading.value = false;
    //1. 先把用户信息保存起来，后面用
    //这里保存的数据格式是 key-value 形式的，value 只能是字符串，不能是 JSON 对象
    window.sessionStorage.setItem("hr", JSON.stringify(data.data));
    //获取到请求地址栏的 redirect 参数 http://localhost:5173/?redirect=/per/emp
    let redirect = proxy.$router.currentRoute.value.query.redirect;
    //2. 跳转
    proxy.$router.replace(redirect ? redirect : '/home');
  }).catch(err => {
    // alert(JSON.stringify(err));
    loading.value = false;
  })
}
</script>

<style scoped>

</style>
