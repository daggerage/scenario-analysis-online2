import request from '@/utils/request'

export function scenarioAnalysis(structBmp, plantBmp, configUnit, configMethod, algorithm, generationNum, populationSize,maxEconomy, minEnvironment,title) {
  return request({
    url: 'v1/scenario/analysis',
    method: 'post',
    params: {
      structBmp: structBmp,
      plantBmp: plantBmp,
      configUnit: configUnit,
      configMethod: configMethod,
      algorithm: algorithm,
      generationNum: generationNum,
      populationSize: populationSize,
      maxEconomy: maxEconomy,
      minEnvironment: minEnvironment,
      title: title
    }
  })
}
export function fetchRecords() {
  return request({
    url: 'v1/scenario/record',
    method: 'get'
  })
}
export function fetchResults(resultIds) {
  let idStr = ''
  for (let item of resultIds) {
    idStr += item + ','
  }
  return request({
    url: 'v1/scenario/result',
    method: 'get',
    params: { resultIds : idStr }
  })
}
