import request from '@/utils/request'

export function fetchStructBMPs() {
  return request({
    url: 'v1/bmp/struct',
    method: 'get'
  })
}

export function fetchPlantBMPs() {
  return request({
    url: 'v1/bmp/plant',
    method: 'get'
  })
}
