<template>
  <div :class="className" :id="id" :style="{height:height,width:width}"/>
</template>

<script>
import echarts from 'echarts'
import resize from './mixins/resize'

export default {
  mixins: [resize],
  props: {
    className: {
      type: String,
      default: 'chart'
    },
    id: {
      type: String,
      default: 'chart'
    },
    width: {
      type: String,
      default: '200px'
    },
    height: {
      type: String,
      default: '200px'
    },
    resultData:{
      type: Object,
      default:{}
    }
  },
  data() {
    return {
      chart: null,
      dataSeries:[],
      colors:['#f1c40f','#1abc9c','#3498db','#9b59b6','#e74c3c','#ecf0f1','#d35400','#7f8c8d']
    }
  },
  mounted() {
    this.initDataSeries()
    this.initChart()
  },
  beforeDestroy() {
    if (!this.chart) {
      return
    }
    this.chart.dispose()
    this.chart = null
  },
  methods: {
    initChart() {
      this.chart = echarts.init(document.getElementById(this.id))
      this.chart.setOption({
        backgroundColor: '#344b58',
        title: {
          text: '情景最终优化结果对比',
          x: '20',
          top: '20',
          textStyle: {
            color: '#fff',
            fontSize: '22'
          },
          subtextStyle: {
            color: '#90979c',
            fontSize: '18'
          }
        },
        tooltip: {
          trigger: 'axis',
          axisPointer: {
            textStyle: {
              color: '#fff'
            }
          }
        },
        grid: {
          left: '5%',
          right: '5%',
          borderWidth: 0,
          top: 150,
          bottom: 95,
          textStyle: {
            color: '#fff'
          }
        },
        legend: {
          x: '5%',
          type:'scroll',
          top: '10%',
          textStyle: {
            color: '#90979c'
          },
        },
        calculable: true,
        xAxis: [{
          name:'economy',
          nameTextStyle:{
            fontSize:16
          },
          nameLocation:'middle',
          type: 'value',
          axisLine: {
            lineStyle: {
              color: '#90979c'
            }
          },
          splitLine: {
            show: false
          },
          axisTick: {
            show: false
          },
          splitArea: {
            show: false
          },
          axisLabel: {
            interval: 5
          }
        }],
        yAxis: [{
          name:'environment',
          nameTextStyle:{
            fontSize:16
          },
          type: 'value',
          splitLine: {
            show: false
          },
          axisLine: {
            lineStyle: {
              color: '#90979c'
            }
          },
          axisTick: {
            show: false
          },
          axisLabel: {
            interval: 0
          },
          splitArea: {
            show: false
          }
        }],
        dataZoom: [{
          show: true,
          height: 30,
          xAxisIndex: [
            0
          ],
          bottom: 30,
          handleIcon: 'path://M306.1,413c0,2.2-1.8,4-4,4h-59.8c-2.2,0-4-1.8-4-4V200.8c0-2.2,1.8-4,4-4h59.8c2.2,0,4,1.8,4,4V413z',
          handleSize: '110%',
          handleStyle: {
            color: '#d3dee5'

          },
          textStyle: {
            color: '#fff' },
          borderColor: '#90979c'

        }, {
          type: 'inside',
          show: true,
          height: 15,
          start: 1,
          end: 35
        }],
        series: this.dataSeries
      })
    },
    initDataSeries(){
      let data=this.resultData
      let keys=Object.keys(data)
      for (let i = 0; i < keys.length; i++) {
        let bb=[]
        for(let item of data[keys[i]]){
          bb.push([item['economy'],item['environment']])
        }
        bb.sort((a,b) => a[0]-b[0])
        console.log(bb);
        let item={
          name: keys[i],
          type: 'line',
          symbolSize: 10,
          symbol: 'circle',
          itemStyle: {
            normal: {
              color: this.colors[i],
              barBorderRadius: 0,
              label: {
                show: true,
                position: 'top',
                formatter(p) {
                  return p.value > 0 ? p.value : ''
                }
              }
            }
          },
          data: bb
        }
        this.dataSeries.push(item)
      }
    }
  }
}
</script>
