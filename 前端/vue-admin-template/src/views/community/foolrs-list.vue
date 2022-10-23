<template>
  <div class="app-container">

    <!-- 表头控件 -->
    <div class="controls">
      <!-- 左边按钮 -->
      <div class="left-botton">
        <el-row>
          <el-tooltip class="item" effect="light" content="新增" placement="top-start">
            <el-button type="primary" icon="el-icon-circle-plus-outline" circle @click="handleOpendialog('save')" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="批量删除" placement="top-start">
            <el-button type="danger" icon="el-icon-delete" circle @click="handleDeleteAllFloors" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="导出数据" placement="top-start">
              <el-button  type="success" icon="el-icon-download" circle @click="handleExportXls('楼栋信息')"/>
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button type="info" icon="el-icon-refresh" circle @click="handleReload" />
          </el-tooltip>

        </el-row>
      </div>
      <!-- 右边框 -->
      <div class="right-input">
        <!-- 输入框 -->
        <el-input v-model.trim="keyword" placeholder="请输入内容" clearable style="width: 30%;margin-left: 200px;" />
        <div class="">
          <el-button type="primary" icon="el-icon-search" @click="handleQuery">搜索</el-button>
          <el-button type="primary" icon="el-icon-refresh-right" @click="handleClear">重置</el-button>
        </div>
      </div>

    </div>

    <!-- 表格 -->
    <el-table :data="tableData" border style="width: 100%" stripe max-height="520px" @selection-change="handleSelectionChange">
      <el-table-column align="center" show-overflow-tooltip type="selection" width="55" />
      <el-table-column align="center" show-overflow-tooltip type="index" fixed prop="index" label="#" width="55" />
      <el-table-column align="center" show-overflow-tooltip fixed prop="floorsName" label="楼栋名称" width="150" />
      <el-table-column align="center" show-overflow-tooltip prop="roomsTotal" label="房子总数" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="soldTotal" label="已售总数" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="floorsAdmin" label="楼栋管理员" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="floorsAdminPhone" label="管理员电话" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="note" label="备注" width="250" />
      <el-table-column align="center" show-overflow-tooltip prop="createBy" label="创建人" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="createTime" label="创建时间" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.createTime | formatDate }}</span>
        </template>
      </el-table-column>
      <el-table-column align="center" show-overflow-tooltip prop="updateBy" label="更新人" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="updateTime" label="更新时间" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.updateTime != null">{{ scope.row.updateTime | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" fixed="right" label="操作" width="150">
        <template slot-scope="scope">
          <el-button type="text" size="small" @click="handleClick(scope.row)">查看</el-button>
          <el-button type="text" size="small" @click="handleOpendialog('update',scope.row.id)">编辑</el-button>
          <el-button type="text" size="small" @click="handleDeleteFloors(scope.row.id)" style="color: red;">删除</el-button>
        </template>
      </el-table-column>

    </el-table>
    <div class="page">
      <el-pagination
        background
        :current-page="pageCurrent"
        :page-size="pageSize"
        layout="total,prev, pager, next"
        :total="total"
        @size-change="handleSizeChange"
        @current-change="handleCurrentChange"
      />
    </div>

    <!-- 弹出层 -->
    <el-dialog :title="title" :visible.sync="dialogFormVisible" @close="handleCloseDialog">
      <el-form ref="floors" :inline="true" :model="floors" :rules="rules">

        <el-input v-show="false" v-model="floors.id" autocomplete="off" />

        <el-form-item label="楼栋名称" label-width="120px" prop="floorsName">
          <el-input v-model="floors.floorsName" style="width: 200px;" autocomplete="off" />
        </el-form-item>

        <el-form-item label="房子总量" label-width="120px" prop="roomsTotal">
          <el-input v-model="floors.roomsTotal" style="width: 200px;" autocomplete="off" />
        </el-form-item>

        <el-form-item label="楼栋管理员" label-width="120px" prop="floorsAdmin">
          <el-input v-model="floors.floorsAdmin" style="width: 200px;" autocomplete="off" />
        </el-form-item>

        <el-form-item label="联系方式" label-width="120px" prop="floorsAdminPhone">
          <el-input v-model="floors.floorsAdminPhone" style="width: 200px;" autocomplete="off" />
        </el-form-item>

        <el-form-item label="备注" label-width="120px">
          <el-input v-model="floors.note" type="textarea" rows="7" style="width: 275%;" />
        </el-form-item>

      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCloseDialog">取 消</el-button>
        <el-button v-if="dialogOpt === 'save'" type="primary" @click="handleSaveFloors">确 定</el-button>
        <el-button v-if="dialogOpt === 'update'" type="primary" @click="handelUpdateFoolrs">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog
      :visible.sync="centerDialogVisible"
      width="60%"
      :before-close="handleClose">

        <el-descriptions title="楼栋详情">
           <el-descriptions-item label="楼栋名称">{{floorsInfo.floorsName}}</el-descriptions-item>
           <el-descriptions-item label="楼栋管理员">{{floorsInfo.floorsAdmin}}</el-descriptions-item>
           <el-descriptions-item label="管理员电话">{{floorsInfo.floorsAdminPhone}}</el-descriptions-item>
           <el-descriptions-item label="房间总数">{{floorsInfo.roomsTotal}} 间</el-descriptions-item>
           <el-descriptions-item label="已售房间" span="3">{{floorsInfo.managerName == null ? '0':floorsInfo.managerName}} 间</el-descriptions-item>
          <el-descriptions-item label="备注" span="3">{{floorsInfo.note}}</el-descriptions-item>
        </el-descriptions>

      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { formatDate } from '@/utils/formatDate'
import { getFloorsList, saveFloors, getFloorsById, updateFloors, deleteFloors, deleteFloorsAll, exportFloors } from '@/api/floors'
import { MessageBox, Message } from 'element-ui'
export default {
  // 时间转换器
  filters: {
    formatDate(time) {
      const date = new Date(time)
      return formatDate(date, 'yyyy-MM-dd')
    }
  },
  data() {
    /**
     * 表单验证
     */
    const validateFloorsName = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入楼栋名称'))
      } else {
        callback()
      }
    }

    const validateRoomsTotal = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入房子总数'))
      } else {
        callback()
      }
    }

    const validateFloorsAdmin = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请输入楼栋管理员'))
      } else {
        callback()
      }
    }

    const validateFloorsAdminPhone = (rule, value, callback) => {
      // 手机号正则表达式
      const phoneReg = /^[1][3,5,7,8,9][0-9]{9}$/
      if (!phoneReg.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }

    return {
      centerDialogVisible:false,
      // 列表数据
      tableData: [],
      // 分页参数
      pageSize: 10,
      pageCurrent: 1,
      total: 0,
      keyword: '',
      // 弹出层标题
      title: '',
      // 弹出层状态
      dialogFormVisible: false,
      // 弹出层需要进行的操作
      dialogOpt: 'save',
      ids: '',
      // 弹出层表单对象
      floors: {
        id: '',
        floorsName: '',
        roomsTotal: '',
        floorsAdmin: '',
        floorsAdminPhone: '',
        note:''
      },
      floorsInfo:{},
      // 表单验证规则
      rules: {
        floorsName: [{ validator: validateFloorsName, trigger: 'blur' }],
        roomsTotal: [{ validator: validateRoomsTotal, trigger: 'blur' }],
        floorsAdmin: [{ validator: validateFloorsAdmin, trigger: 'blur' }],
        floorsAdminPhone: [{ validator: validateFloorsAdminPhone, trigger: 'blur' }]
      }

    }
  },
  created() {
    this.handleFloorsList(this.pageCurrent, this.pageSize)
  },

  methods: {

    // 多选与反选
    handleSelectionChange(val) {
      const idList = new Array()
      for (let i = 0; i < val.length; i++) {
        // 存放选中的id
        idList.push(val[i].id)
      }
      this.ids = idList.toString()
    },

    // 重新刷新页面
    handleReload() {
      // 刷新页面
      location.reload()
    },

    // 查询按钮点击事件
    handleQuery() {
      // 关键字和权限为空时不能查询
      if (this.keyword === '') {
        Message({
          message: '请输入关键字或选择权限查询',
          type: 'wranning',
          duration: 3 * 1000
        })
      } else {
        this.handleFloorsList(this.pageCurrent, this.pageSize, this.keyword)
      }
    },

    // 重置按钮
    handleClear() {
      // 清空输入框跟选择框
      this.keyword = ''
      // 重新刷新列表
      this.handleFloorsList(this.pageCurrent, this.pageSize)
    },

    // 将单行数据封装为对象，进行打印
    handleClick(row) {
      this.floorsInfo = row
      this.centerDialogVisible = true
    },

    // 获取所有楼栋信息
    handleFloorsList(pageCurrent, pageSize, keyword) {
      // 发起请求
      getFloorsList(pageCurrent, pageSize, keyword).then((res) => {
        if (res.result.records.length === 0) {
          Message({
            message: '无相关数据',
            type: 'warning',
            duration: 5 * 1000
          })
          this.tableData = res.result.records
          //总记录数赋值
          this.total = res.result.total
        } else {
          this.tableData = res.result.records
          // 总记录数赋值
          this.total = res.result.total
        }
      })
    },

    //导出所有楼栋
    handleExportXls(fileName){
       if (!fileName || typeof fileName != 'string') {
      				fileName = '导出文件'
      		}
      exportFloors(fileName).then((res) => {
          //
          const aLink = document.createElement("a");

          let blob = new Blob([res], {type: "application/vnd.ms-excel;charset=utf-8"})

          aLink.href = URL.createObjectURL(blob)

          aLink.download = fileName + '.xlsx'

          aLink.click()

          document.body.appendChild(aLink)
          })
    },

    // 分页，每页显示的条数
    handleSizeChange(val) {
      this.pageSize = val
      this.handleFloorsList(this.pageCurrent, this.pageSize, this.keyword)
    },
    // 分页，切换上下页
    handleCurrentChange(val) {
      this.pageCurrent = val
      this.handleFloorsList(this.pageCurrent, this.pageSize, this.keyword)
    },

    // 打开弹出层
    handleOpendialog(type, id) {
      if (type === 'save') {
        this.title = '新增楼栋'
        this.dialogOpt = 'save'
      } else {
        // 获取单个楼栋信息
        this.handelGetFloorsById(id)
        this.dialogOpt = 'update'
        this.title = '更新楼栋信息'

      }
      this.dialogFormVisible = true
    },

    handleClose(){
      this.centerDialogVisible = false
    },

    //关闭弹出层
    handleCloseDialog(){
      // 清空表单
      this.$refs.floors.resetFields()
      this.floors.id = ''
      this.dialogFormVisible = false
    },

    // 新增楼栋信息
    handleSaveFloors() {
      this.$refs.floors.validate(valid => {
        if (valid) {
          saveFloors(this.floors).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 关闭弹出层
              this.dialogFormVisible = false
              // 更新列表
              this.handleFloorsList(this.pageCurrent, this.pageSize)
              // 清空表单
              this.$refs.floors.resetFields()
            } else {
              Message({
                message: res.message,
                type: 'error',
                duration: 5 * 1000
              })
            }
          })
        } else {
          Message({
            message: '请完善表单',
            type: 'warning',
            duration: 5 * 1000
          })
        }
      })
    },

    // 根据楼栋id获取楼栋信息
    handelGetFloorsById(id) {
      getFloorsById(id).then((res) => {
        this.floors = res.result
      })
    },

    // 更新楼栋信息
    handelUpdateFoolrs() {
      this.$refs.floors.validate(valid => {
        if (valid) {
          updateFloors(this.floors).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 关闭弹出层
              this.dialogFormVisible = false
              // 更新列表
              this.handleFloorsList(this.pageCurrent, this.pageSize)
              // 清空表单
              this.$refs.floors.resetFields()
            } else {
              Message({
                message: res.message,
                type: 'error',
                duration: 5 * 1000
              })
            }
          })
        } else {
          Message({
            message: '请完善表单',
            type: 'warning',
            duration: 5 * 1000
          })
        }
      })
    },

    // 删除楼栋信息
    handleDeleteFloors(id) {
      MessageBox.confirm('确定删除该楼栋？', '删除楼栋', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then((action) => {
        // 成功回调
        if (action === 'confirm') {
          deleteFloors(id).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 刷新列表
              this.handleFloorsList(this.pageCurrent, this.pageSize)
            } else {
              Message({
                message: res.message,
                type: 'error',
                duration: 5 * 1000
              })
            }
          })
        }
      }).catch((err) => {
        if (err === 'cancel') {
          Message({
            message: '取消删除',
            type: 'info',
            duration: 5 * 1000
          })
        }
        if (err === 'close') {
          Message({
            message: '取消删除',
            type: 'info',
            duration: 5 * 1000
          })
        }
      })
    },

    // 批量删除楼栋信息
    handleDeleteAllFloors() {
      if (this.ids === '') {
        Message({
          message: '请选择需要删除的楼栋',
          type: 'warning',
          duration: 5 * 1000
        })
      } else {
        MessageBox.confirm('您确定删除选中楼栋吗？', '批量删除', {
          confirmButtonText: '确认删除',
          cancelButtonText: '取消删除',
          type: 'warning'
        }).then((action) => {
          // 确认按钮的回调
          if (action === 'confirm') {
            console.log(this.i)
            // 批量删除接口
            deleteFloorsAll(this.ids).then((res) => {
              if (res.success === true) {
                // 刷新楼栋列表
                this.handleFloorsList(this.pageCurrent, this.pageSize)
                Message({
                  message: res.message,
                  type: 'success',
                  duration: 5 * 1000
                })
              } else {
                // 刷新楼栋列表
                this.handleFloorsList(this.pageCurrent, this.pageSize)
                Message({
                  message: res.message,
                  type: 'error',
                  duration: 5 * 1000
                })
              }
            })
          }
        }).catch((err) => {
          // 取消按钮的回调
          if (err === 'cancel') {
            Message({
              message: '取消批量删除',
              type: 'info',
              duration: 5 * 1000
            })
          }
          // 关闭按钮的回调
          if (err === 'close') {
            Message({
              message: '取消批量删除',
              type: 'info',
              duration: 5 * 1000
            })
          }
        })
      }
    }

  }
}
</script>
<style>
  .controls {
    width: 100%;
    height: 3.375rem;
    display: flex;
    line-height: 3.375rem;
    justify-content: space-between;
    /* align-items: center; */
  }

  .left-botton {
    width: 40%;
  }

  .right-input {
    display: inherit;
    justify-content: space-around;
    width: 50%;
  }

  .page {
    width: 100%;
    margin-top: 1rem;
    text-align: center;
  }
</style>
