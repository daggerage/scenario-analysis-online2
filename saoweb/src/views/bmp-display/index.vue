<template>
  <div class="app-container">
    <el-row >
      <el-col span="24">
        <el-tabs class="bmp-tab-first">
          <el-tab-pane label="结构化措施">
            <el-tabs type="border-card" class="bmp-tab-second">
              <el-tab-pane v-for="s in structInfo" :key="s._id" :label="s.NAME">
                <div v-for="(value,key) in s">{{key+'====='+value}}</div>
                <!--<el-table-->
                  <!--:data="s"-->
                  <!--style="width: 100%">-->
                  <!--<el-table-column-->
                    <!--v-for="(item,index) in s"-->
                    <!--:key="index"-->
                    <!--:label="item">-->
                    <!--<template slot-scope="scope">-->
                      <!--<span>{{scope.row[index]}}</span>-->
                    <!--</template>-->
                  <!--</el-table-column>-->
                  <!--</el-table>-->
              </el-tab-pane>
            </el-tabs>
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
const nonStructuralInfo = [{
  name: '等高草灌带',
  reason: '在强度侵蚀区，往往植被稀少而地表裸露，导致坡面径流冲刷强烈，因此可以采用等高草灌带技术。',
  content: '在强度水土流失山地，沿等高线挖小水平沟，水平沟的断面挖成梯形，水平沟按品字形排列，沟间距为200cm，开挖土用于外侧作梗。植苗播种前好施肥，均匀撒施于沟底。在沟内种植灌木、乔木，乔木以乡土树种为主，灌木以胡枝子为主，沟埂及沟内撒播草籽，以宽叶雀稗种子或百喜草种子为主。',
  influence: [
    '促进沟内的乔、灌、草快速覆盖地表，增大冠层截流量',
    '可以拦蓄较多的地表径流',
    '降低径流速率'
  ]
}, {
  name: '低效林改造',
  reason: '侵蚀裸露坡地近地表干热化的小气候和地表无法拦蓄径流是低效林形成的重要原因。可以通过施肥，改造改善小气候和微地形来促进马尾松生长。',
  content: ''
}]
export default {
  name:'bmp-display',
  data(){
    return{
      structInfo:[
        /* 1 */
        {
          _id : "5c48418a32aca72b891a482e",
          SUBSCENARIO : 1,
          NAME : "FENGJIN",
          REFERENCE : "FENGJIN",
          PARAMETERS : "OM:ORGANIC MATTER:RC:1.22-DENSITY:BULK DENSITY:RC:0.98-POROSITY:TOTAL POROSITY:RC:1.02-USLE_K:USLE SOIL ERODIBILITY:RC:1.01-CONDUCTIVITY:SOIL HYDRAULIC CONDUCTIVITY:RC:0.81-FIELDCAP:SOIL FIELD CAPACITY:RC:1.02-WILTINGPOINT:WILTINGPOINT:RC:1.02-SOL_AWC:SOIL AVAILABLE WATER:RC:1.02-SOL_UL:SOIL SATURATED WATER:RC:1.02-SOL_CBN:SOIL CARBON CONTENT:RC:1.22-USLE_P:USLE PRACTICE FACTOR:RC:0.9",
          EFFECTIVENESS : 3,
          OPEX : 1.5,
          INCOME : 2,
          LANDUSE : "8-6",
          SLPPOS : "1-4",
          CAPEX : 15.5,
          DESC : "FENGJIN"
        },

        /* 2 */
        {
          _id : "5c48418a32aca72b891a4830",
          SUBSCENARIO : 2,
          NAME : "SHENGTAILINCAO",
          REFERENCE : "SHENGTAILINCAO",
          PARAMETERS : "OM:ORGANIC MATTER:RC:1.45-DENSITY:BULK DENSITY:RC:0.93-POROSITY:TOTAL POROSITY:RC:1.07-USLE_K:USLE SOIL ERODIBILITY:RC:0.82-CONDUCTIVITY:SOIL HYDRAULIC CONDUCTIVITY:RC:1.81-FIELDCAP:SOIL FIELD CAPACITY:RC:1.07-WILTINGPOINT:WILTINGPOINT:RC:1.07-SOL_AWC:SOIL AVAILABLE WATER:RC:1.07-SOL_UL:SOIL SATURATED WATER:RC:1.07-SOL_CBN:SOIL CARBON CONTENT:RC:1.45-USLE_P:USLE PRACTICE FACTOR:RC:0.5",
          EFFECTIVENESS : 5,
          OPEX : 1.5,
          INCOME : 6.9,
          LANDUSE : "4-8-6",
          SLPPOS : "1-4-16",
          CAPEX : 87.5,
          DESC : "SHENGTAILINCAO"
        },

        /* 3 */
        {
          _id : "5c48418a32aca72b891a4832",
          SUBSCENARIO : 3,
          NAME : "DIXIAOLINGAIZAO",
          REFERENCE : "DIXIAOLINGAIZAO",
          PARAMETERS : "OM:ORGANIC MATTER:RC:1.05-DENSITY:BULK DENSITY:RC:0.87-POROSITY:TOTAL POROSITY:RC:1.13-USLE_K:USLE SOIL ERODIBILITY:RC:0.81-CONDUCTIVITY:SOIL HYDRAULIC CONDUCTIVITY:RC:1.71-FIELDCAP:SOIL FIELD CAPACITY:RC:1.13-WILTINGPOINT:WILTINGPOINT:RC:1.13-SOL_AWC:SOIL AVAILABLE WATER:RC:1.13-SOL_UL:SOIL SATURATED WATER:RC:1.13-SOL_CBN:SOIL CARBON CONTENT:RC:1.05-USLE_P:USLE PRACTICE FACTOR:RC:0.75",
          EFFECTIVENESS : 4,
          OPEX : 1.5,
          INCOME : 3.9,
          LANDUSE : "8-6",
          SLPPOS : 4,
          CAPEX : 45.5,
          DESC : "DIXIAOLINGAIZAO"
        },

        /* 4 */
        {
          _id : "5c48418a32aca72b891a4834",
          SUBSCENARIO : 4,
          NAME : "JINGJILINGUO",
          REFERENCE : "JINGJILINGUO",
          PARAMETERS : "OM:ORGANIC MATTER:RC:2.05-DENSITY:BULK DENSITY:RC:0.96-POROSITY:TOTAL POROSITY:RC:1.03-USLE_K:USLE SOIL ERODIBILITY:RC:0.88-CONDUCTIVITY:SOIL HYDRAULIC CONDUCTIVITY:RC:1.63-FIELDCAP:SOIL FIELD CAPACITY:RC:1.03-WILTINGPOINT:WILTINGPOINT:RC:1.03-SOL_AWC:SOIL AVAILABLE WATER:RC:1.03-SOL_UL:SOIL SATURATED WATER:RC:1.03-SOL_CBN:SOIL CARBON CONTENT:RC:2.05-USLE_P:USLE PRACTICE FACTOR:RC:0.5",
          EFFECTIVENESS : 4,
          OPEX : 20,
          INCOME : 60.3,
          LANDUSE : "4-8-6",
          SLPPOS : 16,
          CAPEX : 420,
          DESC : "JINGJILINGUO"
        }
      ]
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
