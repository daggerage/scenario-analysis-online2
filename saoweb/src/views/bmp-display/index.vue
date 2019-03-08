<template>
  <div class="app-container">
    <el-row >
      <el-col span="24">
        <el-tabs class="bmp-tab-first">

          <el-tab-pane label="结构化措施">
            <display-table

              :data-brief="dataBrief"
              :data-detail="dataDetail"
            >
            </display-table>
          </el-tab-pane>

          <el-tab-pane label="非结构化措施" >
            <el-tabs type="border-card" class="bmp-tab-second">
              <el-tab-pane label="Plant Management">...</el-tab-pane>
            </el-tabs>
          </el-tab-pane>

        </el-tabs>
      </el-col>
    </el-row>

  </div>
</template>
<script>
import { fetchStructBMPs } from '@/api/bmp'
import DisplayTable from './DisplayTableComponent'
export default {
  name: 'BMPDisplay',
  components:{DisplayTable},
  data() {
    return {
      dataBrief: [],
      dataDetail: []
    }
  },
  created() {
    this.fetchStructBMPs()
  },
  methods: {
    fetchStructBMPs() {
      fetchStructBMPs().then(res => {
        if(res.data.status&&res.data.status===200){
          dataBrief=res.data.brief;
          dataDetail=res.data.detail;
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
