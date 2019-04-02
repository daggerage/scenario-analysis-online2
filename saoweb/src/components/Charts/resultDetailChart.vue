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
    }
  },
  data() {
    return {
      chart: null,
      allData:{},
      dataSeries:[],
      selected:{},
      colors:['#f1c40f','#1abc9c','#3498db','#9b59b6','#e74c3c','#ecf0f1','#d35400','#7f8c8d']
    }
  },
  mounted() {
    this.initDataSeries()
    this.initChart()
    this.addClickEvent()
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
      var that = this
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
          trigger: 'item',
          axisPointer: {
            textStyle: {
              color: '#fff'
            }
          }
        },
        toolbox: {
          itemSize:40,
          itemGap:30,
          right:50,
          top:5,
          feature: {
            mySelected: {
              show: true,
              title: '查看已选择的点',
              icon: 'image://static/chart.png',
              onclick: that.displaySelected
            },
            myBmp: {
              show: true,
              title: '查看选中点的情景配置',
              icon: 'image://static/loupe.png',
              onclick: that.displayBmp
            },
            myCancle:{
              show: true,
              title: '取消所有选中',
              icon: 'image://static/cancel.png',
              onclick: that.cancelSelected
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
            show: true
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
            show: true
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
    initDataSeries() {
      var that = this
      let data = this.$store.records
      this.allData=data
      let keys = Object.keys(data)
      for (let i = 0; i < keys.length; i++) {
        let bb = []
        for (let item of data[keys[i]]) {
          bb.push([item['economy'], item['environment']])
        }
        bb.sort((a, b) => a[0] - b[0])
        let item = {
          name: keys[i],
          type: 'scatter',
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
            },
            emphasis: {
              borderColor: '#000000',
              borderWidth: 4
            }
          },
          data: bb,
        }
        this.dataSeries.push(item)
      }
    },
    addClickEvent(){
      var that=this
      var data=this.$store.records
      this.chart.on('click',function(params){
        if(!that.selected[params.seriesName]){
          that.selected[params.seriesName]={}
        }
        if(that.selected[params.seriesName][params.dataIndex]){
          delete that.selected[params.seriesName][params.dataIndex]
          that.chart.dispatchAction({
            type: 'downplay',
            seriesIndex: params.seriesIndex,
            dataIndex:params.dataIndex
          })
          that.$message({
            message:'已取消选择: '+params.seriesName+' - '+'第 '+params.dataIndex+' 个点',
            type:'warning'
          })
        }else{
          that.selected[params.seriesName][params.dataIndex]={}
          that.selected[params.seriesName][params.dataIndex]['pairs']=data[params.seriesName][params.dataIndex]['pairs']
          // that.selected[params.seriesName][params.dataIndex]['gene']=data[params.seriesName][params.dataIndex]['gene']
          that.chart.dispatchAction({
            type: 'highlight',
            seriesIndex: params.seriesIndex,
            dataIndex:params.dataIndex
          })
          that.$message({
            message:'已选择: '+params.seriesName+' - '+'第 '+params.dataIndex+' 个点',
            type:'success',
            duration:5000
          })
        }
      })

      document.oncontextmenu = function () {
        return false;
      };
      this.chart.on('contextmenu', function (params) {
        that.chart.dispatchAction({
          type: 'downplay',
          seriesIndex: params.seriesIndex,
          dataIndex:params.dataIndex
        })
      })
    },
    displaySelected(){
      var that=this
      for(let seriesName of Object.keys(that.selected)){
        for(let index of Object.keys(that.selected[seriesName])){
          that.chart.dispatchAction({
            type: 'highlight',
            seriesName: seriesName,
            dataIndex:index
          })
          setTimeout(function () {
            that.chart.dispatchAction({
              type: 'downplay',
              seriesName: seriesName,
              dataIndex:index
            })
          },100)
          setTimeout(function () {
            that.chart.dispatchAction({
              type: 'highlight',
              seriesName: seriesName,
              dataIndex:index
            })
          },200)
        }
      }
    },
    cancelSelected(){
      var that=this
      for (let i = 0; i < Object.keys(that.selected).length; i++) {
        let seriesName=Object.keys(that.selected)[i]
        let series = that.selected[seriesName]
        that.chart.dispatchAction({
          type: 'downplay',
          seriesName: seriesName,
        })
        delete that.selected[seriesName]
      }
    },
    displayBmp(){
      this.$store.selected=this.selected
      this.$router.push({
        path:'bmp-map'
      })
    }
  }
}
</script>
