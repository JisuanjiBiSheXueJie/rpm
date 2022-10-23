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
            <el-button type="danger" icon="el-icon-delete" circle @click="handleDeleteAllCarpo" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="导出数据" placement="top-start">
              <el-button style="margin-left: 10px;" type="success" icon="el-icon-download" circle @click="handleExportXls('车位信息')"/>
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="刷新" placement="top-start">
            <el-button type="info" style="margin-left: 10px;" icon="el-icon-refresh" circle @click="handleReload" />
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
      <el-table-column align="center" show-overflow-tooltip fixed prop="carNum" label="车位编号" width="150" />
      <el-table-column align="center" show-overflow-tooltip prop="carLicense" label="车牌号" width="150" />
      <el-table-column align="center" show-overflow-tooltip prop="area" label="面积(平方米)" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="floorsName" label="对应楼栋" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="realname" label="所有者" width="120" />

      <el-table-column align="center" show-overflow-tooltip prop="startTime" label="启用时间" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="endTime" label="弃用时间" width="120">
        <template slot-scope="scope">
          <span v-if="scope.row.endTime != null">{{ scope.row.endTime | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="amount" label="费用(万元)" width="100" />

      <el-table-column align="center" show-overflow-tooltip prop="status" label="状态" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0" style="color: #67C23A;"> 使用中 </span>
          <span v-if="scope.row.status === 1" style="color: #E6A23C;"> 未使用 </span>
          <span v-if="scope.row.status === 2" style="color: #F56C6C;"> 损坏 </span>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="imgUrl" label="车位照片" width="90">
        <template slot-scope="scope">
          <el-popover placement="right" title="" trigger="hover">
            <img :src="scope.row.imgUrl" style="height: 200px;width: 200px">
            <img slot="reference" :src="scope.row.imgUrl" :alt="scope.row.imgUrl" style="height: 35px;width: 35px">
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="manager" label="管理员" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="managerPhone" label="管理员电话" width="120" />
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
          <el-button type="text" size="small" @click="handleDeleteCarpo(scope.row.id)" style="color: red;">删除</el-button>
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
    <el-dialog :title="title" :visible.sync="dialogFormVisible" @close="handleCloesDialog">
      <div class="dialog">
        <div class="left-from">
          <el-form ref="carpo" :model="carpo" :rules="rules">

            <el-input v-show="false" v-model="carpo.id" autocomplete="off" />

            <el-form-item label="车位编号" label-width="150px" prop="carNum">
              <el-input v-model="carpo.carNum" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="车牌号" label-width="150px" prop="carLicense">
              <el-input v-model="carpo.carLicense" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="面积(平方米)" label-width="150px" prop="area">
              <el-input v-model="carpo.area" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="费用(万元)" label-width="150px" prop="amount">
              <el-input v-model="carpo.amount" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="启用日期" label-width="150px">
              <el-date-picker
                    v-model="carpo.startTime"
                    placeholder="选择日期">
                  </el-date-picker>
            </el-form-item>

            <el-form-item label="所属楼栋" label-width="150px" prop="floorId">
              <el-select v-model="carpo.floorId" placeholder="请选择" style="width: 70%;">
                <el-option
                  v-for="item in options"
                  :key="item.id"
                  :label="item.floorsName"
                  :value="item.id"
                />
              </el-select>
            </el-form-item>

            <el-form-item label="状态" label-width="150px">
              <el-radio-group v-model="carpo.status">
                  <el-radio :label="0"><span style="color: #67C23A;font-weight: 600;">使用中</span></el-radio>
                  <el-radio :label="1"><span style="color: #E6A23C;font-weight: 600;">未使用</span></el-radio>
                  <el-radio :label="2"><span style="color: #F56C6C;font-weight: 600;">损坏</span></el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item label="管理员" label-width="150px" prop="manager">
              <el-input v-model="carpo.manager" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="管理员电话" label-width="150px" prop="managerPhone">
              <el-input v-model="carpo.managerPhone" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="备注" label-width="150px">
              <el-input v-model="carpo.note" type="textarea" rows="5" style="width: 70%;" />
            </el-form-item>

          </el-form>
        </div>

        <div class="img-upload">
          <el-upload
            class="avatar-uploader"
            :action="action"
            :show-file-list="false"
            :on-success="handleAvatarSuccess"
            :before-upload="beforeAvatarUpload"
          >
            <img v-if="imageUrl" :src="imageUrl" class="avatar">
            <i v-else class="el-icon-plus avatar-uploader-icon" />
            <div slot="tip" class="el-upload__tip">上传车位照片</div>
          </el-upload>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCloesDialog">取 消</el-button>
        <el-button v-if="dialogOpt === 'save'" type="primary" @click="handleSaveCarpo">确 定</el-button>
        <el-button v-if="dialogOpt === 'update'" type="primary" @click="handelUpdateCarpo">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog
      :visible.sync="centerDialogVisible"
      width="60%"
      :before-close="handleCloseDialog">
      <div class="user-info">
        <div class="info">
        <el-descriptions title="设施详情">
           <el-descriptions-item label="车位拥有者">{{carpoInfo.realname}}</el-descriptions-item>
           <el-descriptions-item label="车位价值">{{carpoInfo.amount}}</el-descriptions-item>
           <el-descriptions-item label="车位面积">{{carpoInfo.area}}</el-descriptions-item>
           <el-descriptions-item label="车牌号">{{carpoInfo.carLicense}}万元</el-descriptions-item>
           <el-descriptions-item label="车位编号">{{carpoInfo.carNum}}</el-descriptions-item>
           <el-descriptions-item label="对应楼栋名称">{{carpoInfo.floorsName}}</el-descriptions-item>
           <el-descriptions-item label="启用时间">{{carpoInfo.startTime | formatDate}}</el-descriptions-item>
           <el-descriptions-item label="弃用时间">{{carpoInfo.endTime | formatDate}}</el-descriptions-item>
           <el-descriptions-item label="管理员">{{carpoInfo.manager}}</el-descriptions-item>
           <el-descriptions-item label="管理员电话">{{carpoInfo.managerPhone}}</el-descriptions-item>
           <el-descriptions-item label="目前状态" span="3">
               <span v-if="carpoInfo.status === 0" style="color: #67C23A;"> 使用中 </span>
               <span v-if="carpoInfo.status === 1" style="color: #E6A23C;"> 未使用 </span>
               <span v-if="carpoInfo.status === 2" style="color: #F56C6C;"> 损坏 </span>
           </el-descriptions-item>
           <el-descriptions-item label="备注" span="3">{{carpoInfo.note}}</el-descriptions-item>
        </el-descriptions>
        </div>
        <div class="avatar">
          <el-avatar :size="100">
                <img :src="carpoInfo.imgUrl"/>
              </el-avatar>
        </div>
      </div>

      <span slot="footer" class="dialog-footer">
        <el-button @click="centerDialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="centerDialogVisible = false">确 定</el-button>
      </span>
    </el-dialog>

  </div>
</template>

<script>
import { formatDate } from '@/utils/formatDate'
import { getCarpoList, saveCarpo, getCarPositionById, updateCarpo, deleteCarpo, deleteAllCarpo, exportCarPosition } from '@/api/carpo'
import { MessageBox, Message } from 'element-ui'
import { getFloorsListNoPage } from '@/api/floors'
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
    const validateCarNum = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入车位编号'))
      } else {
        callback()
      }
    }

    const validateCarLicense = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入车牌'))
      } else {
        callback()
      }
    }

    const validateManager = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入管理员'))
      } else {
        callback()
      }
    }

    const validateFloorId = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请选择楼栋'))
      } else {
        callback()
      }
    }

    const validateArea = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入面积'))
      }
      setTimeout(() => {
        var pattern = /^\d+.?\d{0,2}$/;
          if (!pattern.test(value)) {
          callback(new Error('请输入数字值'));
        } else {
          callback();
        }
      }, 500);
    }

    const validateAmount = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入车位价值'))
      }
    setTimeout(() => {
      var pattern = /^\d+.?\d{0,2}$/;
        if (!pattern.test(value)) {
          callback(new Error('请输入数字值'));
        } else {
          callback();
        }
      }, 500);
    }

    const validateManagerPhone = (rule, value, callback) => {
      // 手机号正则表达式
      const phoneReg = /^[1][3,5,7,8,9][0-9]{9}$/
      if (!phoneReg.test(value)) {
        callback(new Error('请输入正确的手机号码'))
      } else {
        callback()
      }
    }

    return {
      // 列表数据
      tableData: [],
      imageUrl: '',
      centerDialogVisible:false,
      radio:1,
      tip: '上传车位图片',

      // 弹出层下拉框
      options: [],
      value: '',

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
      //上传图片路径地址
      action:'http://localhost:9001/upload/img',
      // 弹出层表单对象
      carpo: {
        id: '',
        carNum: '',
        carLicense: '',
        area: '',
        floorId: '',
        imgUrl: '',
        ownerId: '',
        amount: '',
        startTime: '',
        status: 1,
        manager: '',
        managerPhone: '',
        note: ''
      },
      carpoInfo:{},
      // 表单验证规则
      rules: {
        carNum: [{ validator: validateCarNum, trigger: 'blur' }],
        carLicense: [{ validator: validateCarLicense, trigger: 'blur' }],
        area: [{ validator: validateArea, trigger: 'blur' }],
        floorId: [{ validator: validateFloorId, trigger: 'change' }],
        manager: [{ validator: validateManager, trigger: 'blur' }],
        managerPhone: [{ validator: validateManagerPhone, trigger: 'blur' }],
        amount: [{ validator: validateAmount, trigger: 'blur' }]
      }

    }
  },
  created() {
    this.handleCarPoList(this.pageCurrent, this.pageSize)
    this.handleGetFloorsList()
  },

  methods: {


    //获取楼栋信息
    handleGetFloorsList(){
      getFloorsListNoPage().then((res)=>{
        this.options = res.result
      })
    },

   // 图片上传
   handleAvatarSuccess(res, file) {
     // 图片回显地址
     this.imageUrl = res.result.imgPath + res.result.fileName
     // 图片地址
     this.carpo.imgUrl = this.imageUrl
   },
   beforeAvatarUpload(file) {
     // 设置图片格式
    	const img_mimetype = ['image/jpeg', 'image/jpg', 'image/png']
     const isJPG = img_mimetype.includes(file.type)
    		// 限定图片大小
     const isLt2M = file.size / 1024 / 1024 < 2

     if (!isJPG) {
       this.$message.error('上传只能图片格式')
     }
     if (!isLt2M) {
       this.$message.error('上传图片大小不能超过 2MB!')
     }
     return isJPG && isLt2M
   },

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
        this.handleCarPoList(this.pageCurrent, this.pageSize, this.keyword)
      }
    },

    // 重置按钮
    handleClear() {
      // 清空输入框跟选择框
      this.keyword = ''
      // 重新刷新列表
      this.handleCarPoList(this.pageCurrent, this.pageSize)
    },

    // 将单行数据封装为对象，进行打印
    handleClick(row) {
      this.carpoInfo = row
      this.centerDialogVisible = true
    },

    // 获取所有车位信息
    handleCarPoList(pageCurrent, pageSize, keyword) {
      // 发起请求
      getCarpoList(pageCurrent, pageSize, keyword).then((res) => {
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

    // 分页，每页显示的条数
    handleSizeChange(val) {
      this.pageSize = val
      this.handleCarPoList(this.pageCurrent, this.pageSize, this.keyword)
    },
    // 分页，切换上下页
    handleCurrentChange(val) {
      this.pageCurrent = val
      this.handleCarPoList(this.pageCurrent, this.pageSize, this.keyword)
    },

    // 打开弹出层
    handleOpendialog(type, id) {
      if (type === 'save') {
        this.title = '新增车位信息'
        this.dialogOpt = 'save'
      } else {
        // 获取单个车位信息
        this.handelGetCarPositionById(id)
        this.dialogOpt = 'update'
        this.title = '更新车位信息'
      }
      this.dialogFormVisible = true
    },

    //关闭弹出层
    handleCloesDialog(){
      // 清空表单
      this.$refs.carpo.resetFields()
      this.carpo.status = 1
      this.carpo.startTime = ''
      this.imageUrl = ''
      this.dialogFormVisible = false
    },

    // 新增车位信息
    handleSaveCarpo() {
      //赋值
      this.carpo.imgUrl = this.imageUrl
      this.$refs.carpo.validate(valid => {
        if (valid) {
          saveCarpo(this.carpo).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 关闭弹出层
              this.dialogFormVisible = false
              // 更新列表
              this.handleCarPoList(this.pageCurrent, this.pageSize)
              // 清空表单
              this.$refs.carpo.resetFields()
              this.carpo.status = 1
              this.carpo.startTime = ''
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

    //导出所有楼栋
    handleExportXls(fileName){
       if (!fileName || typeof fileName != 'string') {
      				fileName = '导出文件'
      		}
      exportCarPosition(fileName).then((res) => {
          //
          const aLink = document.createElement("a");

          let blob = new Blob([res], {type: "application/vnd.ms-excel;charset=utf-8"})

          aLink.href = URL.createObjectURL(blob)

          aLink.download = fileName + '.xlsx'

          aLink.click()

          document.body.appendChild(aLink)
          })
    },

    // 根据车位id获取车位信息
    handelGetCarPositionById(id) {
      getCarPositionById(id).then((res) => {
        this.carpo = res.result
        this.imageUrl = this.carpo.imgUrl
      })
    },

    //详情弹出层
    handleCloseDialog(){
      this.centerDialogVisible = false
    },

    // 更新车位信息
    handelUpdateCarpo() {
      this.carpo.imgUrl = this.imageUrl
      this.$refs.carpo.validate(valid => {
        if (valid) {
          updateCarpo(this.carpo).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 关闭弹出层
              this.dialogFormVisible = false
              // 更新列表
              this.handleCarPoList(this.pageCurrent, this.pageSize)
              // 清空表单
              this.$refs.carpo.resetFields()
              this.carpo.status = 1
              this.carpo.startTime = ''

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

    // 删除车位信息
    handleDeleteCarpo(id) {
      MessageBox.confirm('确定删除该车位？', '删除车位', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then((action) => {
        // 成功回调
        if (action === 'confirm') {
          deleteCarpo(id).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 刷新列表
              this.handleCarPoList(this.pageCurrent, this.pageSize)
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

    // 批量删除车位信息
    handleDeleteAllCarpo() {
      if (this.ids === '') {
        Message({
          message: '请选择需要删除的车位',
          type: 'warning',
          duration: 5 * 1000
        })
      } else {
        MessageBox.confirm('您确定删除选中车位吗？', '批量删除', {
          confirmButtonText: '确认删除',
          cancelButtonText: '取消删除',
          type: 'warning'
        }).then((action) => {
          // 确认按钮的回调
          if (action === 'confirm') {
            // 批量删除接口
            deleteAllCarpo(this.ids).then((res) => {
              if (res.success === true) {
                // 刷新车位列表
                this.handleCarPoList(this.pageCurrent, this.pageSize)
                Message({
                  message: res.message,
                  type: 'success',
                  duration: 5 * 1000
                })
              } else {
                // 刷新车位列表
                this.handleCarPoList(this.pageCurrent, this.pageSize)
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

   .avatar-uploader .el-upload {
      border: 1px dashed #d9d9d9;
      border-radius: 6px;
      cursor: pointer;
      position: relative;
      overflow: hidden;
    }
    .avatar-uploader .el-upload:hover {
      border-color: #409EFF;
    }
    .avatar-uploader-icon {
      font-size: 28px;
      color: #8c939d;
      width: 178px;
      height: 178px;
      line-height: 178px;
      text-align: center;

    }
    .avatar {
      width: 178px;
      height: 178px;
      display: block;
    }

    .dialog{
      display: flex;
    }
    .img-upload{
      width: 25%;

    }
    .left-from{
      width: 65%;
    }
    .user-info{
      width: 100%;
      display: flex;
      .info{
        width: 85%;
      }
      .avatar{
        width: 20%;
        text-align: center;
        margin-top: 35px;
      }
    }
</style>
