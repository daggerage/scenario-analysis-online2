<template>
  <div class="app-container">
    <el-row class="main-box" :gutter="20">
      <el-col class="select-box" :xs="24" :sm="8" :lg="8">
        <el-collapse v-model="activeNames">
          <el-collapse-item v-for="s of Object.keys(series)" :key="s" :title="s" :name="s">
            <el-radio v-for="i of Object.keys(series[s])" :key="i" v-model="radio" :label="s+','+i" @change="display">{{i}}</el-radio>
          </el-collapse-item>
        </el-collapse>
      </el-col>
      <el-col class="map-box" :xs="24" :sm="16" :lg="16">
        <map-overview
          id="map-overview"
          :interactive="false"
          :emit-feature="false"
          v-bind:data-url="selectedUnit"
          v-bind:gene="selectedGene"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
  import MapOverview from '@/components/Map/MapOverviewComponent'
  export default {
    name: 'MixChart',
    components: { MapOverview },
    data(){
      return{
        radio:'',
        series:{},
        activeNames:['1'],
        unitUrlMap:{
          CONNFIELD:'data/field_merged.geojson',
          SLPPOS:'data/slppos_merged_out.geojson'
        }
      }
    },
    computed: {
      selectedSeriesName:function(){
        return this.radio.split(',')[0]
      },
      selectedDataIndex:function(){
        return this.radio.split(',')[1]
      },
      selectedUnit:function(){
        var that=this
        if(this.selectedSeriesName) {
          //切割标题获取配置单元名称。。。
          console.log(that.unitUrlMap[that.selectedSeriesName.split('_')[1]])
          return that.unitUrlMap[that.selectedSeriesName.split('_')[1]]
        }
      },
      selectedGene:function(){
        if(this.selectedSeriesName&&this.series[this.selectedSeriesName]) {
          console.log(this.series[this.selectedSeriesName][this.selectedDataIndex])
          return this.series[this.selectedSeriesName][this.selectedDataIndex]['pairs']
        }
      }
    },
    methods:{
      display(){

      }
    },
    created(){
      this.series=this.$store.selected
      for(let s in this.series){
        console.log(s)
        for (let i in this.series[s]){
          console.log(i)
        }
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .main-box{
    .select-box{
      box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
    }
  }
</style>

