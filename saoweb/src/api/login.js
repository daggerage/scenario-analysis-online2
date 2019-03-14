import request from '@/utils/request'

export function loginByUsername(username, password) {
  const data = {
    name: username,
    password: password
  }
  return request({
    url: '/v1/user/login',
    method: 'post',
    data
  })
}

export function logout(token) {
  return request({
    url: 'v1/user/login',
    method: 'delete'
  })
}

export function getUserInfo(token) {
  return request({
    url: 'v1/user/account',
    method: 'get'
  })
}

