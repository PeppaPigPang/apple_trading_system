// src/utils/request.js
import axios from 'axios'

// 创建 Axios 实例
const service = axios.create({
  // baseURL: import.meta.env.API_BASE_URL, // 从环境变量中读取接口基础地址
  baseURL: "http://localhost:8080/api",
  timeout: 50000, // 请求超时时间
  headers: {
    'Content-Type': 'application/json'
  },
  withCredentials: true // 允许携带跨域 Cookie
})

// 请求拦截器：在发送请求前做些什么
service.interceptors.request.use(
  (config) => {
    // 添加 Token 到请求头
    const token = localStorage.getItem('token')
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    // 处理请求错误
    return Promise.reject(error)
  }
)

// 响应拦截器
service.interceptors.response.use(
  (response) => {
    // 直接返回响应数据
    return response.data
  },
  (error) => {
    // 统一处理接口错误（如 401 未授权、500 服务器错误等）
    if (error.response) {
        // const errorMsg = error.response.data.respMsg;
        // 根据状态码做不同处理
        switch (error.response.status) {
        case 500:
            // 服务器错误，提示用户
            // alert(errorMsg)
            return Promise.resolve(error.response.data);
        default:
            // 其他错误，直接抛出
            return Promise.reject(error)
        }
    }
    return Promise.reject(error)
  }
)

export default service