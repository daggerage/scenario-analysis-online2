<template>
  <div class="app-container">
    <el-row >
      <el-col span="24">
        <el-tabs class="bmp-tab-first">

          <el-tab-pane label="工程性措施">
            <struct-bmp-table
              :data="structBMPData"
              bmp-type="struct"/>
          </el-tab-pane>

          <el-tab-pane label="作物管理措施">
            <el-tag>植物管理措施</el-tag>
            <plant-bmp-table
              :data="plantBMPData"
              bmp-type="plant"/>
          </el-tab-pane>

        </el-tabs>
      </el-col>
    </el-row>

  </div>
</template>
<script>
import {fetchStructBMPs,fetchPlantBMPs} from '@/api/bmp';

import StructBmpTable from './StructBmpTable'
import PlantBmpTable from './PlantBmpTable'

export default {
  name: 'BMPDisplay',
  components: { StructBmpTable,PlantBmpTable },
  data() {
    return {
      structBMPData: [],
      plantBMPData: []
    }
  },
  created() {
    this.fetchStructBMPs()
    this.fetchPlantBMPs()
  },
  methods: {
    fetchStructBMPs() {
      fetchStructBMPs().then(res => {
        if (res.data.status === 200) {
          this.structBMPData = res.data.data
        }
      })
    },
    fetchPlantBMPs() {
      fetchPlantBMPs().then(res => {
        console.log('now fetch plant BMP')
        if (res.data.status === 200) {
          this.plantBMPData = res.data.data
        }
      })
    }
  }
}
</script>
<style rel="stylesheet/scss">
  .bmp-tab-second{
    height: calc(100vh - 190px);
    /*height: 75vh;*/
  }
</style>
