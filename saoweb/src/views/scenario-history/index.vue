<template>
  <div class="app-container">
    <el-table
      ref="multipleTable"
      :data="records"
      :default-sort="{prop:'createdOn',order:'descending'}"
      tooltip-effect="dark"
      style="width: 100%"
      stripe
      @selection-change="handleSelectionChange">
      <el-table-column
        :selectable="isColumnSelectable"
        type="selection"
        width="55"/>
      <el-table-column
        label="编号"
        width="70"
        prop="id_gen"/>
      <el-table-column
        label="名称"
        prop="title"/>
      <el-table-column
        prop="createdOn"
        label="创建时间"
        width="200"
        sortable/>
      <el-table-column
        prop="scenarioUnitDelineation"
        label="单元划分方法"
        width="150"/>
      <el-table-column
        prop="scenarioConfigStrategy"
        label="BMP配置策略"
        width="150"/>
      <el-table-column
        prop="optimizeAlgorithm"
        label="优化算法"
        width="150"/>
      <el-table-column
        prop="result"
        label="执行状态">
        <template slot-scope="scope">
          <el-tag :type="scope.row.result | statusDescFilter | descFilter">{{ scope.row.result | statusDescFilter }}</el-tag>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top: 20px">
      <el-button @click="compare">对比</el-button>
    </div>
  </div>
</template>

<script>
import { fetchRecords, fetchResults } from '@/api/scenario'

export default {
  filters: {
    descFilter(status) {
      const statusMap = {
        running: 'warning',
        finish: 'success'
      }
      return statusMap[status]
    },
    statusDescFilter(status) {
      const statusMap = {
        0: 'running',
        1: 'finish'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      multipleSelection: [],
      records: []
    }
  },
  created() {
    this.fillRecords()
  },

  methods: {
    toggleSelection(rows) {
      if (rows) {
        rows.forEach(row => {
          this.$refs.multipleTable.toggleRowSelection(row)
        })
      } else {
        this.$refs.multipleTable.clearSelection()
      }
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    fillRecords() {
      fetchRecords().then(res => {
        if (res.data.status === 200) {
          const data = res.data.data
          for (let i = 0; i < data.length; i++) {
            data[i]['id_gen'] = i + 1
            this.records.push(data[i])
          }
        }
      })
    },
    isColumnSelectable(row, index) {
      return row.result === 1
    },
    compare(){
      let recordIds = this.multipleSelection.map((item) => item.scenarioAnalysisResultId)
      fetchResults(recordIds).then(res=>{
        if(res.data.status===200){
          this.$store.records=res.data.data
          this.$router.push({
            path:'detail'
          })
        }
      })
    }
  }
}
</script>
