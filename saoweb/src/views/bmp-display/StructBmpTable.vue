<template>
  <el-table
    :data="BMPData"
    style="width: 100%">

      <el-table-column type="expand">
        <template slot-scope="props">
          <p>参数修改</p>
          <el-form label-position="left" inline class="demo-table-expand">
            <el-form-item v-for="detail of props.row.paramDetail" :key="detail[0]" :label="detail[1]">
              <span>{{ detail[2].replace(/RC/,'*') }}{{ detail[3] }}</span>
            </el-form-item>
          </el-form>
        </template>
      </el-table-column>

    <el-table-column label="措施名称">
      <template slot-scope="props">
        <span>{{ replaceBmpName(props.row.name) }}</span>
      </template>
    </el-table-column>
      <el-table-column
        label="效用"
        prop="effectiveness"/>
      <el-table-column
        label="opex"
        prop="opex"/>
      <el-table-column
        label="landuse"
        prop="landuse"/>
      <el-table-column
        label="slppos"
        prop="slppos"/>
      <el-table-column
        label="capex"
        prop="capex"/>


  </el-table>
</template>

<style>
  .demo-table-expand {
    font-size: 0;
  }
  .demo-table-expand label {
    width: 250px;
    color: #99a9bf;
  }
  .demo-table-expand .el-form-item {
    margin-right: 0;
    margin-bottom: 0;
    width: 50%;
  }
</style>

<script>
import {replaceBmpName} from '@/utils/bmpDisplay'

export default {
  name: 'DisplayTable',
  props: {
    data: {
      type: Array,
      defalut: []
    },
    bmpType:{
      type: String,
      default: ''
    }
  },
  data() {
    return {
      structHeaders:[
        {label:"效用",prop:'effectiveness'},
        {label:"opex",prop:'opex'},
        {label:"landuse",prop:'landuse'},
        {label:"slppos",prop:'slppos'},
        {label:"capex",prop:'capex'},
      ],
      plantHeaders:[
        {label:"序列号",prop:'sequence'},
        {label:"BaseHU",prop:'baseHu'},
        {label:"HUSC",prop:'husc'},
        {label:"landuse",prop:'landuseId'},
        {label:"管理操作编号",prop:'mgtOp'},
        {label:"年",prop:'year'},
        {label:"月",prop:'month'},
        {label:"日",prop:'day'},
      ]
    }
  },
  methods:{
    replaceBmpName
  },
  computed:{
    BMPData:function () {
      return this.data
    },
  }
}
</script>
