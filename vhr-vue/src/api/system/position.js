import request from "@/utils/request.js";

export function loadAllPositions(params) {
    return request({
        url: '/api/system/basic/position',
        method: 'get',
        params: params
    })
}
export function getPositionById(id) {
    return request({
        url: '/api/system/basic/position/'+id,
        method: 'get'
    })
}

export function deletePositionById(id) {
    return request({
        url: '/api/system/basic/position/'+id,
        method: 'delete'
    })
}

export function updatePosition(data) {
    return request({
        url: '/api/system/basic/position',
        method: 'put',
        data: data
    })
}

export function addPosition(data) {
    return request({
        url: '/api/system/basic/position',
        method: 'post',
        data: data
    })
}
