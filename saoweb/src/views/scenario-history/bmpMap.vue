<template>
  <div class="app-container">
    <el-row style="margin-bottom: 20px">
      <el-col>
        <el-button type="primary" @click="download">下载选中地图</el-button>
      </el-col>
    </el-row>
    <el-row class="main-box" :gutter="20">
      <el-col class="select-box" :xs="24" :sm="8" :lg="8">
        <el-collapse v-model="activeNames">
          <el-collapse-item v-for="s in Object.keys(selected.v)" :key="s" :title="records[s].title" :name="s">
            <el-radio v-for="i in Array.from(selected.v[s])" :key="i" v-model="radio" :label="records[s].title+','+s+','+i" @change="display">{{i}}</el-radio>
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
  import {fetchMapFile} from "@/api/scenario";
  import Selected from '@/store/entity/selected'

  export default {
    name: 'MixChart',
    components: { MapOverview },
    data(){
      return{
        radio:'',
        selected:{},
        records:{},
        activeNames:['1'],
        unitUrlMap:{
          CONNFIELD:'data/field_15_merged.geojson',
          SLPPOS:'data/slppos_merged_out.geojson'
        },
        downloadLoading:false
      }
    },
    computed: {
      selectedSeriesName:function(){
        //TODO: 改掉！现在切割label。。。
        return this.radio.split(',')[0]
      },
      selectedSeriesIndex:function(){
        return this.radio.split(',')[1]
      },
      selectedDataIndex:function(){
        return this.radio.split(',')[2]
      },
      selectedUnit:function(){
        var that=this
        if(this.selectedSeriesName) {
          //TODO: 改掉！现在切割标题获取配置单元名称。。。
          console.log(that.unitUrlMap[that.selectedSeriesName.split('_')[1]])
          return that.unitUrlMap[that.selectedSeriesName.split('_')[1]]
        }
      },
      selectedGene:function(){
        if(!(this.selectedSeriesName&&this.selectedSeriesIndex&&this.selectedDataIndex)){
          return []
        }
        let r=this.records
        let s=this.selected
        console.log(r)
        console.log(s)
        var gene=r[this.selectedSeriesIndex].pops[this.selectedDataIndex].pairs
        if(this.selectedSeriesName&&gene) {
          console.log(gene)
          return gene
        }
      }
    },
    methods:{
      display(){

      },
      download(){
        var scenario = this.$store.records[this.selectedSeriesName][this.selectedDataIndex]['scenario']
        fetchMapFile(scenario).then(res=>{

        })
      },
      handleDownload() {
        this.downloadLoading = true
        import('@/vendor/Export2Zip').then(zip => {
          const tHeader = ['Id', 'Title', 'Author', 'Readings', 'Date']

          const filterVal = ['id', 'title', 'author', 'pageviews', 'display_time']
          const list = this.list
          const data = this.formatJson(filterVal, list)
          zip.export_txt_to_zip(tHeader, data, this.filename, this.filename)
          this.downloadLoading = false
        })
      }
    },
    mounted(){
      console.log(this.$store.selected.v)
      console.log(this.$store.records)
      this.selected=this.$store.selected
      this.records=this.$store.records
      for (let s in this.selected.v) {
        // console.log(s)
        for (let i of this.selected.v[s]) {
          console.log(this.records[s].title+','+s+','+i)
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

