<template>
  <el-table
    :data="BMPData"
    style="width: 100%">

    <el-table-column type="expand">
      <template slot-scope="props">
        <p>参数修改</p>
        <el-form label-position="left" inline class="demo-table-expand">
          <el-form-item v-for="detail of props.row.paramDetail" :key="detail[0]" :label="detail[0]">
            <span>{{ detail[1] }}</span>
          </el-form-item>
        </el-form>
      </template>
    </el-table-column>

    <el-table-column label="措施名称" width="250px">
      <template slot-scope="props">
        <span>{{ replaceBmpName(props.row.name) }}</span>
      </template>
    </el-table-column>

    <el-table-column
      label="序列号"
      prop="sequence"
      width="80px"/>
    <el-table-column
      label="BaseHU"
      prop="baseHu"/>
    <el-table-column
      label="HUSC"
      prop="husc"/>
    <el-table-column
      label="landuse"
      prop="landuseId"/>
    <el-table-column
      label="管理操作编号"
      prop="mgtOp"/>
    <el-table-column
      label="年"
      prop="year"/>
    <el-table-column
      label="月"
      prop="month"/>
    <el-table-column
      label="日"
      prop="day"/>

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
      BmpNameDict:{
        RICE_PADDY_CROP_ROTATION:'Rice Paddy Crop Rotation'
      },
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
    }
  }
}
</script>
