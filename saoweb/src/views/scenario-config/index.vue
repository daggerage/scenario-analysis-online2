<template>
  <div class="app-container">
    <el-row :gutter="32">
      <el-col :xs="24" :sm="12" :lg="8">
        <p>请进行BMP情景配置，详细说明请参考 <a href="#" style="color: #0af;">帮助</a></p>
        <el-form :rules="rules" ref="form" :model="form" label-width="80px" label-position="top">
          <el-form-item label="单元划分方法 / 配置规则" prop="unitConf">
            <el-cascader
              :options="unitConfs"
              v-model="form.unitConf"
            />
            <el-tooltip
              placement="right"
              effect="dark">
              <div slot="content">
                <pre style="font-size: 14px;">{{question.bmp}}</pre>
              </div>
              <i class="el-icon-question" style="margin: 0 10px;"></i>
            </el-tooltip>
          </el-form-item>
          <el-form-item label="智能优化算法" prop="algorithm">
            <el-select v-model="form.algorithm">
              <el-option v-for="a in algorithms" :key="a.value" :label="a.name" :value="a.value"/>
            </el-select>
          </el-form-item>
          <div v-if="form.algorithm==='NSGAII'">
            <el-form-item v-model="form.generationNum" label="优化代数">
              <el-slider v-model="form.generationNum" show-input/>
            </el-form-item>
            <el-form-item v-model="form.poluationSize" label="种群规模">
              <el-slider v-model="form.poluationSize" :step="4" show-input/>
            </el-form-item>
          </div>
          <el-form-item>
            <el-button type="primary" @click="onSubmit('form')">提交</el-button>
          </el-form-item>
        </el-form>
      </el-col>

      <el-col :xs="24" :sm="12" :lg="16">
        <Map
          id="map-overview"
          class="overview"
          dataUrl="/data/field.geojson"/>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import Map from '@/components/Map/MapOverviewComponent'

const configMethods = {
  RAND: { value: 'RAND', label: 'Random' },
  SUIT: { value: 'SUIT', label: 'Suit' },
  UPDOWN: { value: 'UPDOWN', label: 'Up-Down dsadsadsadasasdsada' },
  HILLSLP: { value: 'HILLSLP', label: 'Hillslope' }
}
export default {
  name: 'Scenario',
  components:{Map},
  data() {
    return {
      form: {
        unitConf: [],
        algorithm: '',
        generationNum: 2,
        poluationSize: 4
      },
      delineations: [
        { name: '坡位', value: 'SLOPPOS' },
        { name: '地块', value: 'CONNFIELD' },
        { name: 'HRU', value: 'HRU' }
      ],
      confRules: [
        { name: 'Random', value: 'RAND' },
        { name: 'Suit', value: 'SUIT' },
        { name: 'Up-Down', value: 'UPDOWN' },
        { name: 'HillSlope', value: 'HILLSLP' }
      ],
      unitConfs: [{
        value: 'HRU',
        label: 'HRU',
        children: [configMethods.RAND, configMethods.SUIT]
      }, {
        value: 'CONNFIELD',
        label: 'Connected Field',
        children: [configMethods.RAND, configMethods.SUIT]
      }, {
        value: 'SLPPOS',
        label: 'Slope Position',
        children: [configMethods.RAND, configMethods.SUIT, configMethods.UPDOWN, configMethods.HILLSLP]
      }],
      algorithms: [
        { name: 'None', value: 'NONE' },
        { name: 'NSGAII', value: 'NSGAII' }
      ],
      rules:{
        unitConf:[{required:true, message:"请选择配置单元与配置规则", trigger:"change"}],
        algorithm:[{required:true, message:"请选择智能优化算法",trigger:"change"}]
      },
      question:{
        bmp:"The following pairs are supported:\n" +
          "BMPsCfgUnit  BMPsCfgMethod\n" +
          "HRU          RAND, SUIT\n" +
          "EXPLICITHRU  RAND, SUIT\n" +
          "CONNFIELD    RAND, SUIT, UPDOWN\n" +
          "SLPPOS       RAND, SUIT, UPDOWN, HILLSLP"
      }
    }
  },
  methods:{
    onSubmit(formName){
      this.$refs[formName].validate((valid)=>{
        if(valid){
          this.$message({
            message:" 提交成功",
            type:"success"
          })
        }else{
          this.$message({
            message:"提交失败",
            type:"error"
          })
        }
      })
    },
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
