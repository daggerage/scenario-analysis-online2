<template>
  <div class="app-container">
<!--    <el-row style="margin-bottom: 20px">-->
<!--      <el-col>-->
<!--        <el-button type="primary" @click="download">下载选中地图</el-button>-->
<!--      </el-col>-->
<!--    </el-row>-->
    <el-row class="main-box" :gutter="20">
      <el-col class="select-box" :xs="24" :sm="8" :lg="8">
        <el-collapse v-model="activeNames">
          <el-collapse-item v-for="s in Object.keys(selected.v)" :key="s" :title="records[s].title" :name="s">
            <el-radio v-for="i in Array.from(selected.v[s].points)" :key="i" v-model="radio"
                      :label="records[s].id+','+s+','+i" @change="display">{{ i }}
            </el-radio>
          </el-collapse-item>
        </el-collapse>
      </el-col>
      <el-col class="map-box" :xs="24" :sm="16" :lg="16">
        <map-overview
          id="map-overview"
          :interactive="false"
          :emit-feature="false"
          v-bind:data-url="activeUnit"
          v-bind:gene="activeGene"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import MapOverview from '@/components/Map/MapOverviewComponent'
import {fetchMapFile, fetchConfigUnit, scenarioAnalysis} from "@/api/scenario";
import Selected from '@/store/entity/selected'

export default {
  name: 'MixChart',
  components: {MapOverview},
  data() {
    return {
      radio: '',
      selected: {},
      records: {},
      activeNames: ['1'],
      unitUrlMap: {
        'HRU': 'data/spatial_nonunique_hrus.geojson',
        'CONNFIELD': 'data/field_15_merged.geojson',
        'SLPPOS': 'data/slppos_merged_out.geojson'
      },
      activeUnit: '',
      activeGene: [],
      downloadLoading: false
    }
  },
  computed: {
    selectedSeriesId: function() {
      //TODO: 改掉！现在切割label。。。
      return this.radio.split(',')[0]
    },
    selectedSeriesIndex: function() {
      return this.radio.split(',')[1]
    },
    selectedDataIndex: function() {
      return this.radio.split(',')[2]
    }

  },
  watch: {
    selectedSeriesId(newId, oldId) {
      let unitName = 'HRU'
      if (newId) {
        fetchConfigUnit(newId).then(res => {
          if (res.data.status === 200) {
            unitName = res.data.data[0].scenarioUnitDelineation
            this.activeUnit = this.unitUrlMap[unitName]
            // return this.unitUrlMap[unitName]
          }
        })
      }
    },

    selectedDataIndex(newId, oldId) {
      if (!(this.selectedSeriesId && this.selectedSeriesIndex && this.selectedDataIndex)) {
        return []
      }
      const r = this.records
      const gene = r[this.selectedSeriesIndex].pops[newId].pairs;
      this.activeGene = gene
    }
  },
  methods: {
    display() {

    },
    download() {
      const scenario = this.$store.records[this.selectedSeriesId][this.selectedDataIndex]['scenario']
      fetchMapFile(scenario).then(res => {

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
    },
  },
  mounted() {
    this.selected = this.$store.selected
    this.records = this.$store.records
    for (let s in this.selected.v) {
      // console.log(s)
      for (let i of this.selected.v[s]) {
        console.log(this.records[s].title + ',' + s + ',' + i)
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.main-box {
  .select-box {
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1)
  }
}
</style>

