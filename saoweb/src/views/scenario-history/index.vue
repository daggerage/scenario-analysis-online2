<template>
  <div class="app-container">

    <el-table :data="history" border fit highlight-current-row style="width: 100%">

      <el-table-column align="center" label="ID" width="80" prop="id"></el-table-column>

      <el-table-column width="180px" align="center" label="Date" prop="date"></el-table-column>

      <el-table-column width="120px" align="center" label="config" >
        <template slot-scope="scope">
          <span>{{scope.row.config[0]+' / '+scope.row.config[1]}}</span>
        </template>
      </el-table-column>

      <el-table-column width="100px" label="Algorithm">
        <template slot-scope="scope">
          <span>{{scope.row.algorithm[0]+' - '+scope.row.algorithm[1]}}</span>
        </template>
      </el-table-column>

      <el-table-column class-name="status-col" label="Status" width="200px">
        <template slot-scope="scope">
          <el-progress :percentage="scope.row.progress"></el-progress>
        </template>
      </el-table-column>

      <el-table-column min-width="300px" label="Title">
        <template slot-scope="scope">
          <template v-if="scope.row.edit">
            <el-input v-model="scope.row.title" class="edit-input" size="small"/>
            <el-button class="cancel-btn" size="small" icon="el-icon-refresh" type="warning" @click="cancelEdit(scope.row)">cancel</el-button>
          </template>
          <span v-else>{{ scope.row.
            title }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" label="Actions" width="120">
        <template slot-scope="scope">
          <el-button v-if="scope.row.edit" type="success" size="small" icon="el-icon-circle-check-outline" @click="confirmEdit(scope.row)">Ok</el-button>
          <el-button v-else type="primary" size="small" icon="el-icon-edit" @click="scope.row.edit=!scope.row.edit">Edit</el-button>
        </template>
      </el-table-column>

    </el-table>
  </div>
</template>

<script>
  import { fetchList } from '@/api/article'

  export default {
    name: 'InlineEditTable',
    filters: {
      statusFilter(status) {
        const statusMap = {
          published: 'success',
          draft: 'info',
          deleted: 'danger'
        }
        return statusMap[status]
      }
    },
    data() {
      return {
        list: null,
        listLoading: true,
        listQuery: {
          page: 1,
          limit: 10
        },
        history:[{
          id:1,
          title:'Untitled',
          date:'2019-3-4 17:52:30',
          config:['HRU','SUIT'],
          algorithm:[2,4],
          progress:50
        }]
      }
    },
    created() {
      this.getList()
    },
    methods: {
      getList() {
        this.listLoading = true
        fetchList(this.listQuery).then(response => {
          const items = response.data.items
          this.list = items.map(v => {
            this.$set(v, 'edit', false) // https://vuejs.org/v2/guide/reactivity.html
            v.originalTitle = v.title //  will be used when user click the cancel botton
            return v
          })
          this.listLoading = false
        })
      },
      cancelEdit(row) {
        row.title = row.originalTitle
        row.edit = false
        this.$message({
          message: 'The title has been restored to the original value',
          type: 'warning'
        })
      },
      confirmEdit(row) {
        row.edit = false
        row.originalTitle = row.title
        this.$message({
          message: 'The title has been edited',
          type: 'success'
        })
      }
    }
  }
</script>

<style scoped>
  .edit-input {
    padding-right: 100px;
  }
  .cancel-btn {
    position: absolute;
    right: 15px;
    top: 10px;
  }
</style>

