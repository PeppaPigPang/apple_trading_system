// src/api/apple_order.js
import request from '/src/utils/request.js' // 导入上面创建的 Axios 实例

// 获取所有订单列表
export const getAllOrder = (params) => {
  return request({
    url: '/orders',
    method: 'get',
    params // URL 参数，会拼接在地址后（如 ?page=1&size=10）
  })
}

// 新增订单
export const addOrder = (data) => {
  return request({
    url: '/add',
    method: 'post',
    data // 请求体参数，会放在请求体中
  })
}

// 更新订单
export const updateOrder = (data) => {
  return request({
    url: '/update',
    method: 'post',
    data // 请求体参数，会放在请求体中
  })
}

// 删除用户
export const deleteOrder = (data) => {
  return request({
    url: '/delete',
    method: 'post',
    data // 请求体参数，会放在请求体中
  })
}