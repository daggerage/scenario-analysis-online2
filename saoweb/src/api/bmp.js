import request from '@/utils/request'

export function fetchStructBMPs() {
  return request({
    url: 'structbmp',
    method: 'get'
  })
}
