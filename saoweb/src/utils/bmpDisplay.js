export function replaceBmpName(name) {
  var BMPNamesDict = {
    FENGJIN: '封禁',
    SHENGTAILINCAO: '生态林草',
    DIXIAOLINGAIZAO: '低效林改造',
    JINGJILINGUO: '经济林果',
    RICEPADDYCROPROTATION: '稻麦轮作'
  }
  if (name in BMPNamesDict) {
    return BMPNamesDict[name]
  } else {
    return name
  }
}
