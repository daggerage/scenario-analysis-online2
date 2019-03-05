<template>
  <div class="app-container">
    <el-row >
      <el-col span="24">
        <el-tabs class="bmp-tab-first">
          <el-tab-pane label="正常">
            <el-tabs type="border-card" class="bmp-tab-second">
              <el-tab-pane label="用户管理"/>
              <el-tab-pane label="配置管理"/>
              <el-tab-pane label="角色管理"/>
              <el-tab-pane label="定时任务dsaas"/>
            </el-tabs>
          </el-tab-pane>
          <el-tab-pane label="茄化" >
            <el-tabs type="border-card" class="bmp-tab-second">
              <el-tab-pane label="qzqzqz">wdnmd</el-tab-pane>
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
</script>
<style rel="stylesheet/scss">
  .bmp-tab-second{
    height: calc(100vh - 190px);
    /*height: 75vh;*/
  }
</style>
