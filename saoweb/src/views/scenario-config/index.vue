<template>
  <div class="app-container">
    <el-row :gutter="32">
      <el-col :xs="24" :sm="12" :lg="8">
        <p>请进行BMP情景配置，详细说明请参考 <a href="#" style="color: #0af;">帮助</a></p>

        <el-form ref="form" :rules="rules" :model="form" label-width="80px" label-position="top">

          <el-form-item label="应用哪些最佳管理措施">
            <el-select v-model="form.bmps" multiple clearable prop="bmps" placeholder="请选择" style="width: 70%;">
              <el-option-group
                v-for="group in bmps"
                :key="group.label"
                :label="group.label">
                <el-option
                  v-for="item in group.options"
                  :key="item.value"
                  :label="item.label"
                  :value="item.value">
                </el-option>
              </el-option-group>
            </el-select>
          </el-form-item>

          <el-form-item label="单元划分方法 / 配置规则" prop="unitConf" >
            <el-cascader
              style="width: 70%;"
              :options="unitConfs"
              v-model="form.unitConf"
            />
            <el-tooltip
              placement="right"
              effect="dark">
              <div slot="content">
                <pre style="font-size: 14px;">{{ question.bmp }}</pre>
              </div>
              <i class="el-icon-question" style="margin: 0 10px;"/>
            </el-tooltip>
          </el-form-item>

          <el-form-item label="智能优化算法" prop="algorithm">
            <el-select v-model="form.algorithm">
              <el-option v-for="a in algorithms" :key="a.value" :label="a.name" :value="a.value"/>
            </el-select>
          </el-form-item>

          <!--<div v-if="form.algorithm==='NSGAII'">-->
            <!--<el-form-item v-model="form.generationNum" label="优化代数">-->
              <!--<el-slider v-model="form.generationNum" show-input/>-->
            <!--</el-form-item>-->
            <!--<el-form-item v-model="form.poluationSize" label="种群规模">-->
              <!--<el-slider v-model="form.poluationSize" :step="4" show-input/>-->
            <!--</el-form-item>-->
          <!--</div>-->

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
import {fetchStructBMPs,fetchPlantBMPs} from "@/api/bmp";
import {scenarioAnalysis} from "@/api/scenario";
import {replaceBmpName} from '@/utils/bmpDisplay'

const configMethods = {
  RAND: { value: 'RAND', label: 'Random' },
  SUIT: { value: 'SUIT', label: 'Suit' },
  UPDOWN: { value: 'UPDOWN', label: 'Up-Down' },
  HILLSLP: { value: 'HILLSLP', label: 'Hillslope' }
}
export default {
  name: 'Scenario',
  components: { Map },
  data() {
    return {
      form: {
        bmps:[],
        unitConf: [],
        algorithm: '',
        generationNum: 2,
        poluationSize: 4
      },
      bmps:[{
        label:'结构性措施',
        options:[]
      },{
        label:'非结构性措施',
        options:[
          {value:'RICEPADDYCROPROTATION',label:'作物管理措施-稻麦轮作'}
        ]
      }],
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
      rules: {
        selectedBmps: [{ required: true, message: '请选择应用哪些最佳管理措施', trigger: 'change' }],
        unitConf: [{ required: true, message: '请选择配置单元与配置规则', trigger: 'change' }],
        algorithm: [{ required: true, message: '请选择智能优化算法', trigger: 'change' }]
      },
      question: {
        bmp: 'The following pairs are supported:\n' +
          'BMPsCfgUnit  BMPsCfgMethod\n' +
          'HRU          RAND, SUIT\n' +
          'EXPLICITHRU  RAND, SUIT\n' +
          'CONNFIELD    RAND, SUIT, UPDOWN\n' +
          'SLPPOS       RAND, SUIT, UPDOWN, HILLSLP'
      }
    }
  },
  methods: {
    onSubmit(formName) {
      this.$refs[formName].validate((valid) => {
        if (valid) {
          let usePlantBmp = false;
          console.log(this.form.bmps)
          if (this.form.bmps.includes('RICEPADDYCROPROTATION')) {
            usePlantBmp = true
            this.form.bmps.splice(this.form.bmps.lastIndexOf('RICEPADDYCROPROTATION'), 1)
          }
          let selectedBmpString=''
          for(let bmp of this.form.bmps){
            console.log(bmp);
            selectedBmpString+=bmp+','
          }
          return new Promise((resolve, reject) => {
            scenarioAnalysis(
              selectedBmpString,
              usePlantBmp,
              this.form.unitConf[0],
              this.form.unitConf[1],
              this.form.algorithm
            ).then(response => {
              if (response.data.status === 200) {
                this.$message({
                  message: ' 提交成功',
                  type: 'success'
                })
                // console.log(response)
                resolve()
              }
            }).catch(error => {
              this.$message({
                message: '提交失败',
                type: 'error'
              })
              reject(error)
            })
          })
        }
      })
    },
    fillStructBMPs() {
      fetchStructBMPs().then(res => {
        if (res.data.status === 200) {
          for(let bmp of res.data.data){
            let option={
              value:bmp.id,
              label:replaceBmpName(bmp.name)
            }
            this.bmps[0].options.push(option)
          }
        }
      })
    },
    replaceBmpName,
    selectAllBmp(){
      for(let group of this.bmps){
        for(let item of group.options){
          this.form.bmps.push(item.value)
        }
      }
    }
  },
  created(){
    this.fillStructBMPs()
    // this.selectAllBmp()
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>

</style>
