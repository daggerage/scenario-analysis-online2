import request from '@/utils/request'

export function fetchStructBMPs() {
  return request({
    url: 'v1/bmp/struct',
    method: 'get'
  })
}
export function countStructBMPs() {
  return request({
    url: 'v1/bmp/struct',
    method: 'get',
    params: { countOnly: true }
  })
}
export function fetchPlantBMPs() {
  return request({
    url: 'v1/bmp/plant',
    method: 'get'
  })
}
export function countPlantBMPs() {
  return request({
    url: 'v1/bmp/plant',
    method: 'get',
    params: { countOnly: true }
  })
}
