<template>
  <div class="app-container">
    <el-row>
      <el-col :xs="12" :sm="12" :lg="12">
        <el-select v-model="unit" placeholder="请选择划分单元">
          <el-option v-for="d of delineations" :key="d.value" :label="d.name" :value="d.url"/>
        </el-select>
      </el-col>
      <el-col :xs="24" :sm="18" :lg="18">
        <map-overview2
          id="map-overview"
          :interactive="true"
          :emit-feature="true"
          class="overview"
          v-bind:data-url="unit"
          @displayFeature="fillFeatureProps"/>
      </el-col>

      <el-col :xs="24" :sm="6" :lg="6">
        <el-table
          :data="featureProps"
          class="data-tbl"
          height="382px"
          border
          style="width: 100%;">
          <el-table-column
            prop="key"
            label="属性"
            style="width: 30%;"/>
          <el-table-column
            prop="value"
            label="值"
            style="width: 70%;"/>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import MapOverview2 from '@/components/Map/MapOverviewComponent'

export default {
  name: 'MapOverview3',
  components: {MapOverview2},
  data() {
    return {
      featureProps: [],
      delineations: [
        {name: '坡位', value: 'SLPPOS', url: '/data/slppos_merged_out.geojson'},
        {name: '地块', value: 'FIELD', url: '/data/field_15_merged.geojson'},
        {name: 'HRU', value: 'HRU', url: '/data/spatial_nonunique_hrus.geojson'}
      ],
      unit: '/data/field_15_merged.geojson',
    }
  },
  methods: {
    //将feature中的值展示在表格中
    fillFeatureProps(feature) {
      if (!feature) {
        return
      }
      // if (!feature || feature.values_['Id'] === this.featureProps['Id']) { return }
      this.featureProps = []
      for (const key in feature.values_) {
        if (!(feature.values_[key] instanceof Object)) {
          this.featureProps.push({key: key, value: feature.values_[key]})
        }
      }
    }
  }
}
</script>

<style rel="stylesheet/scss" scoped>
.data-tbl {
  height: 80vh;
}
</style>
