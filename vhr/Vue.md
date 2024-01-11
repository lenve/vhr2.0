三大主流框架：

- Vue
  - Vue2(https://v2.cn.vuejs.org/v2/guide/)
  - Vue3(https://cn.vuejs.org/guide/quick-start.html)
  - uniapp
- React
- Angular


SPA（single page web application）

前后端分离开发前端跨域

- 浏览器要求 Ajax 发送的请求，必须发送到相同的域中（请求协议、域名、端口号都相同）。
- 开发时候前端跨域问题：这个问题不能按照传统的跨域思路来解决，因为项目部署之后就不存在跨域了，现在所谓的跨域问题仅仅是开发时候的问题。

前后端分离项目部署有两种方式：

1. 前后端不分的部署：将前端编译打包（编译打包之后就是 HTML/CSS/JS 等），将这些文件拷贝到服务端，然后将服务端代码编译打包为 jar 进行部署。
2. 前后端分离部署：一般会引入 Nginx，前端资源放在 Nginx 上，浏览器访问的时候，Nginx 直接返回，后端资源单独部署，浏览器需要访问接口的时候，也是访问 Nginx，Nginx 分析之后，将请求转发到后端服务上。这种情况，浏览器所有请求都是发送给 Nginx，因此也不存在跨域问题了。