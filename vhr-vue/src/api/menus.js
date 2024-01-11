import request from "@/utils/request.js";

export function loadMenus() {
    return request({
        url: '/api/menus',
        method: 'get'
    })
}
