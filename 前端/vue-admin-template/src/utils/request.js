import axios from 'axios'
import { MessageBox, Message } from 'element-ui'
import store from '@/store'
import { getToken } from '@/utils/auth'

// create an axios instance
const service = axios.create({
  //请求地址
  baseURL: process.env.VUE_APP_BASE_API,
  //请求等待时间 5秒
  // withCredentials: true, // send cookies when cross-domain requests
  timeout: 5000 // request timeout
})

// request过滤器
service.interceptors.request.use(
  config => {
    // do something before request is sent

    if (store.getters.token) {
      //设置请求头携带token   key名称为Token
      config.headers['Token'] = getToken()
    }
    return config
  },
  error => {
    // do something with request error
    //console.log(error) // for debug
    return Promise.reject(error)
  }
)

// response过滤器
service.interceptors.response.use(

  //响应拦截
  response => {
    const res = response.data
    //if the custom code is not 20000, it is judged as an error.
    if (response.status !== 200) {
      Message({
        message: res.message || 'Error',
        type: 'error',
        duration: 5 * 1000
      })
      //500报错
      // if (response.data.code === 500) {

      //   Message({
      //     message: res.message || 'Error',
      //     type: 'error',
      //     duration: 5 * 1000
      //   })

        // to re-login
        // MessageBox.confirm('您已注销，您可以取消停留在此页面，或重新登录', '确认注销', {
        //   confirmButtonText: '重新登录',
        //   cancelButtonText: '退出',
        //   type: 'warning'
        // }).then(() => {
        //   store.dispatch('user/resetToken').then(() => {
        //     location.reload()
        //   })
        // })
      //}
      return Promise.reject(new Error(res.message || 'Error'))
     } else {
       if(res.code === 500){
           Message({
             message: res.message || 'Error',
             type: 'error',
             duration: 5 * 1000
           })
       }
        return res
     }

  },
  error => {
    //发生未知错误的处理
    //console.log('err' + error) // for debug
    // Message({
    //   message: error.message,
    //   type: 'error',
    //   duration: 5 * 1000
    // })
    return Promise.reject(error)
  }
)

export default service
