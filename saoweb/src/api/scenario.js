import request from '@/utils/request'

export function scenarioAnalysis(structBmp, plantBmp, configUnit, configMethod, algorithm) {
  return request({
    url: 'v1/scenario/analysis',
    method: 'post',
    params: {
      structBMP: structBmp,
      plantBmp: plantBmp,
      configUnit: configUnit,
      configMethod: configMethod,
      algorithm: algorithm
    }
  })
}
