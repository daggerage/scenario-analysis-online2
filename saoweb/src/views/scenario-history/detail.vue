<template>
  <div class="chart-container">
    <chart height="100%" width="100%" :result-data="this.data"/>
  </div>
</template>

<script>
  import Chart from '@/components/Charts/resultDetailChart'

  export default {
    name: 'MixChart',
    components: { Chart },
    data(){
      return{
        data:this.$route.query.data
      }
    }
  }
</script>

<style scoped>
  .chart-container{
    position: relative;
    width: 100%;
    height: calc(100vh - 84px);
  }
</style>

