//这个就是这个前端工程的入口文件，类似我们 Java 中的 main 方法
// import './assets/main.css'

import {createApp} from 'vue'
import {createPinia} from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

import App from './App.vue'
//这个是导入路由文件，按理说应该是 ./router/index.js ，但是后缀是 .js 可以省略，进一步，后缀如果是 index 也可以省略
import router from './router'
import {menusStore} from "@/stores/index.js";
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'

//将 App 这个组建展示在 index.html 中一个 id 为 app 的 div 中
// 即项目启动成功之后，看到的内容都是 App 中的内容
const app = createApp(App)

app.use(createPinia())
app.use(router)
app.use(ElementPlus, {locale: zhCn})

const mStore = menusStore();


/**
 * 这个是路由导航守卫，其实就相当于是一个监听器，监听页面之间的跳转，从 A 页面跳转到 B 页面，在到达 B 页面之前，会先被这个导航守卫拦截下来
 * from:表示原页面，即从哪个页面跳转来的
 * to：表示目标页面，即要去的页面
 */
router.beforeEach((to, from, next) => {
    if (to.path == '/') {
        //说明要去登录页面，登录页面可以直接去
        //这个就表示继续去下个页面
        next();
        return;
    }
    if (window.sessionStorage.getItem("hr")) {
        //说明用户已经登录
        //在这里做一个判断，判断当前的跳转是普通的页面点击跳转还是浏览器按 F5 刷新跳转（按 F5 的特点就是内存中的数据没有了）
        if (mStore.menus && mStore.menus.length != 0) {
            //说明是普通的页面跳转
            next();
        } else {
            //说明用户点击了浏览器刷新按钮进行跳转的
            //此时要先去初始化菜单，然后再去跳转
            mStore.initMenus().then(res => {
                res.forEach(r => {
                    router.addRoute(r);
                })
                //这个还是去下个页面，但是和无参的相比，这里表示终止当前的跳转，重新开启一次新的跳转
                //这种写法有一个好处，可以确保前面的路由已经动态添加完成了
                next({...to});
            })
        }
    } else {
        //说明用户未登录，去登录
        next({path: '/', query: {redirect: to.path}});
    }
})

app.mount('#app')
