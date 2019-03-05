<template>
  <div class="app-container homepage-container">
    <el-row
      class="home-title">
      <el-col><p>流域管理措施空间配置方案优化系统</p></el-col>
    </el-row>
    <el-row
      class="home-title">

      <ul type="none" class="home-list">
        <li v-for="f in featureData" :key="f.feature">{{ f.feature }}</li>
      </ul>
    </el-row>

  </div>
</template>

<script>
export default {
  name: 'Homepage',
  data() {
    return {
      featureData: [
        { feature: '1' },
        { feature: '222' },
        { feature: '33333333' },
        { feature: '444444444444' }
      ]
    }
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
  .home-title {
    width: 100%;
    margin: 30px auto;
    text-align: center;
    font-size: 20px;
  }
  .home-list{
    margin: 0;
    padding: 0;
  }
  .home-list li{
    line-height: 60px;
  }
</style>
