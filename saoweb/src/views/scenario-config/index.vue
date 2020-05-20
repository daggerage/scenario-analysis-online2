<template>
  <div class="app-container">

    <el-row :gutter="32">
      <el-col :xs="24" :sm="{span:16,offset:4}" :lg="{span:12,offset:6}">
        <p>请进行BMP情景配置，详细说明请参考 <a href="#" style="color: #0af;">帮助</a></p>


        <el-form ref="form" :rules="rules" :model="form" label-width="80px" label-position="left">

          <el-card :body-style="subCardBodySytle" shadow="hover" class="sub-card">
            <div class="card-header" slot="header">
              <span>优化目标</span>
            </div>
            <el-form-item label="最高预算" label-width="120px">
              <el-input-number v-model="form.maxEconomy" :min="50"></el-input-number>
              <el-tooltip
                placement="right"
                effect="dark">
                <div slot="content">
                  <p>{{ question.economy }}</p>
                </div>
                <i class="el-icon-question"/>
              </el-tooltip>
            </el-form-item>

            <el-form-item label="最低环境效益" label-width="120px" style="margin: auto;">
              <el-input-number v-model="form.minEnvironment" :min="0" :max="0.50" :precision="2"
                               :step="0.01"></el-input-number>
            </el-form-item>
          </el-card>

          <el-card :body-style="subCardBodySytle" shadow="hover" class="sub-card">
            <div class="card-header" slot="header">
              <span>选择最佳管理措施</span>
            </div>
            <!--<el-form-item label="工程性措施" label-width="120px" prop="structBmps">-->
              <!--<el-select-->
                <!--:options="structBmps"-->
                <!--v-model="form.structBmps"-->
                <!--multiple-->
                <!--class="inner-select"-->
                <!--placeholder="选择工程性措施">-->
                <!--<el-option-->
                  <!--v-for="bmp in structBmps"-->
                  <!--:key="bmp.value"-->
                  <!--:label="bmp.label"-->
                  <!--:value="bmp.value">-->
                  <!--<span class="bmp-label">{{ bmp.label }}</span>-->
                  <!--<span class="bmp-category">{{ bmp.category }}</span>-->
                <!--</el-option>-->
              <!--</el-select>-->
            <!--</el-form-item>-->
            <!--<el-form-item label="非工程性措施" label-width="120px" prop="plantBmps">-->
              <!--<el-select-->
                <!--:options="plantBmps"-->
                <!--v-model="form.plantBmps"-->
                <!--multiple-->
                <!--class="inner-select"-->
                <!--placeholder="选择作物管理措施">-->
                <!--<el-option-->
                  <!--v-for="bmp in plantBmps"-->
                  <!--:key="bmp.value"-->
                  <!--:label="bmp.label"-->
                  <!--:value="bmp.value">-->
                  <!--<span class="bmp-label">{{ bmp.label }}</span>-->
                  <!--<span class="bmp-category">{{ bmp.category }}</span>-->
                <!--</el-option>-->
              <!--</el-select>-->
            <!--</el-form-item>-->
            <el-form-item label-width="0" prop="structBmps">
              <el-select
                :options="structBmps"
                v-model="form.structBmps"
                multiple
                class="inner-select"
                placeholder="选择工程性措施">
                <el-option
                  v-for="bmp in structBmps"
                  :key="bmp.value"
                  :label="bmp.label"
                  :value="bmp.value"
                  :disabled="bmp.disabled">
                  <span class="bmp-label">{{ bmp.label }}</span>
                  <span class="bmp-category">{{ bmp.category }}</span>
                </el-option>
              </el-select>
            </el-form-item>
          </el-card>


          <el-row>

            <el-col>
              <el-card :body-style="subCardBodySytle" shadow="hover" class="sub-card">
                <div class="card-header" slot="header">
                  <span>选择单元划分方法 / 配置规则</span>
                </div>
                <el-form-item label-width="0" prop="unitConf">
                  <el-cascader
                    :options="unitConfs"
                    v-model="form.unitConf"
                    style="width: 90%;"
                  />
                  <el-tooltip
                    placement="right"
                    effect="dark">
                    <div slot="content">
                      <pre style="font-size: 14px;">{{ question.configUnitAndMethod }}</pre>
                    </div>
                    <i class="el-icon-question"/>
                  </el-tooltip>
                </el-form-item>
              </el-card>
            </el-col>

            <!--<el-col >-->
              <!--<Map-->
                <!--id="map-overview"-->
                <!--class="overview"-->
                <!--dataUrl="/data/field.geojson"/>-->
            <!--</el-col>-->
          </el-row>

          <el-card :body-style="subCardBodySytle" shadow="hover" class="sub-card">
            <div class="card-header" slot="header">
              <span>智能优化算法</span>
            </div>
            <el-form-item label-width="0" prop="algorithm">
              <el-select v-model="form.algorithm" placeholder="请选择智能优化算法">
                <el-option v-for="a in algorithms" :key="a.value" :label="a.name" :value="a.value"/>
              </el-select>
            </el-form-item>

            <div v-if="form.algorithm==='NSGA2'">
              <el-form-item v-model="form.generationNum" label="优化代数">
                <el-slider v-model="form.generationNum" max="100" show-input/>
              </el-form-item>
              <el-form-item v-model="form.populationSize" label="种群规模">
                <el-slider v-model="form.populationSize" max="500" :step="4" show-input/>
              </el-form-item>
            </div>
          </el-card>


          <!--<el-card :body-style="subCardBodySytle" shadow="hover" class="sub-card">-->
            <!--<div class="card-header" slot="header">-->
              <!--<span>命名</span>-->
            <!--</div>-->

          <!--</el-card>-->
          <el-form-item label="配置标题">

            <!--TODO: placeholder不起作用-->

            <el-input v-model="form.title" autosize type="textarea" :placeholder="computedTitle"
                      style="width: 100%;"></el-input>
            <el-button type="primary" @click="onSubmit('form')" style="float:right;margin:10px 0 0 0">提交</el-button>
            <el-button @click="generateTitle" type="info" style="float:right;margin:10px 25px 0 0">生成默认标题</el-button>
          </el-form-item>

          <el-form-item label-width="0" style="margin: auto;">

          </el-form-item>

        </el-form>
      </el-col>


    </el-row>
  </div>
</template>

<script>
  import Map from '@/components/Map/MapOverviewComponent'
  import {fetchPlantBMPs, fetchStructBMPs} from '@/api/bmp'
  import {scenarioAnalysis} from '@/api/scenario'
  import {replaceBmpName} from '@/utils/bmpDisplay'

  const configMethods = {
    RAND: {value: 'RAND', label: '随机配置策略'},
    SUIT: {value: 'SUIT', label: '适宜配置策略'},
    UPDOWN: {value: 'UPDOWN', label: '上下游配置策略'},
    HILLSLP: {value: 'HILLSLP', label: '坡面配置策略'}
  }
  export default {
    name: 'Scenario',
    components: {Map},
    data() {
      return {
        form: {
          structBmps: ['RICEPADDYCROPROTATION'],
          plantBmps: ['RICEPADDYCROPROTATION'],
          unitConf: ['CONNFIELD', 'UPDOWN'],
          algorithm: 'NSGA2',
          generationNum: 2,
          populationSize: 4,
          maxEconomy: 300,
          minEnvironment: 0,
          title: ''
        },
        structBmps: [{value: 'RICEPADDYCROPROTATION', label: '稻麦轮作', disabled:true}],
        plantBmps: [{value: 'RICEPADDYCROPROTATION', label: '稻麦轮作', category: '作物管理措施'}],
        delineations: [
          {name: '坡位', value: 'SLOPPOS'},
          {name: '地块', value: 'CONNFIELD'},
          {name: '水文响应单元', value: 'HRU'}
        ],
        unitConfs: [{
          value: 'HRU',
          label: '水文响应单元',
          children: [configMethods.RAND, configMethods.SUIT]
        }, {
          value: 'CONNFIELD',
          label: '具有上下游关系的地块',
          children: [configMethods.RAND, configMethods.SUIT, configMethods.UPDOWN]
        }, {
          value: 'SLPPOS',
          label: '坡位',
          children: [configMethods.RAND, configMethods.SUIT, configMethods.UPDOWN, configMethods.HILLSLP]
        }],
        algorithms: [
          {name: '不使用优化算法', value: 'NONE'},
          {name: 'NSGA2', value: 'NSGA2'}
        ],
        rules: {
          structBmps: [{required: true, message: '请选择结构性措施', trigger: 'change'}],
          plantBmps: [{required: true, message: '请选择非结构性措施', trigger: 'change'}],
          unitConf: [{required: true, message: '请选择配置单元与配置规则', trigger: 'change'}],
          algorithm: [{required: true, message: '请选择智能优化算法', trigger: 'change'}]
        },
        question: {
          configUnitAndMethod: 'The following pairs are supported:\n' +
            'BMPsCfgUnit  BMPsCfgMethod\n' +
            'HRU          RAND, SUIT\n' +
            'EXPLICITHRU  RAND, SUIT\n' +
            'CONNFIELD    RAND, SUIT, UPDOWN\n' +
            'SLPPOS       RAND, SUIT, UPDOWN, HILLSLP',
          economy: '单位：人民币万元'
        },
        mainCardBodySytle: {
          'padding': '20px',
          'background-color': '#ffeaa7'
        },
        subCardBodySytle: {
          'padding': '15px'
        }
      }
    },
    created() {
      this.fillStructBMPs()
      // this.selectAllBmp()
    },
    computed:{
      computedTitle:function () {
        return this.generateTitle()
      }
    },
    methods: {
      onSubmit(formName) {
        this.$refs[formName].validate((valid) => {
          if (valid) {
            const usePlantBmp = this.form.plantBmps.length > 0
            let selectedBmpString = ''
            for (const bmp of this.form.structBmps) {
              selectedBmpString += bmp + ','
            }
            return new Promise((resolve, reject) => {
              scenarioAnalysis(
                selectedBmpString,
                usePlantBmp,
                this.form.unitConf[0],
                this.form.unitConf[1],
                this.form.algorithm,
                this.form.generationNum,
                this.form.populationSize,
                this.form.maxEconomy,
                this.form.minEnvironment,
                this.form.title
              ).then(response => {
                if (response.data.status === 200) {
                  this.$message({
                    message: ' 提交成功',
                    type: 'success'
                  })
                  this.$router.push({path: '/scenario-config'})
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
            for (const bmp of res.data.data) {
              const option = {
                value: bmp.id,
                label: replaceBmpName(bmp.name),
                // category: '工程性措施'
              }
              this.structBmps.push(option)
              this.form.structBmps.push(bmp.id)// default
            }
          }
        })
      },
      replaceBmpName,
      generateTitle() {
        let title = this.form.algorithm + '_' +
          this.form.unitConf[0] + '_' +
          this.form.unitConf[1]
        if (this.form.algorithm !== 'NONE') {
          title += "_Gen_" + this.form.generationNum + '_' +
            "Pop_" + this.form.populationSize
        }
        this.form.title = title
      }
    }
  }
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .bmp-label {
    float: left;
  }

  .bmp-category {
    float: right;
    color: #8492a6;
    font-size: 13px;
    margin-right: 20px;
  }

  .clearfix {
    text-align: center;

    span {
      font-size: 18px;
      font-weight: 900;
    }
  }

  .clearfix:before,
  .clearfix:after {
    display: table;
    content: "";
  }

  .clearfix:after {
    clear: both
  }

  .inner-select {
    margin-bottom: 0;
    width: 100%;
  }

  .sub-card {
    margin-bottom: 20px;

    .card-header {
      text-align: center;

      span {
        font-size: 15px;
        font-weight: 600;
      }
    }
  }

  .el-icon-question {
    margin: 0 10px;
  }

</style>
