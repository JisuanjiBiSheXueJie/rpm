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
            <el-button type="danger" icon="el-icon-delete" circle @click="handleDeleteAllAsset" />
          </el-tooltip>

          <el-tooltip class="item" effect="light" content="导出数据" placement="top-start">
            <el-link :href="href">
              <el-button style="margin-left: 10px;" type="success" icon="el-icon-download" circle />
            </el-link>
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
      <el-table-column align="center" show-overflow-tooltip fixed prop="assetName" label="设施名称" width="150" />
      <el-table-column align="center" show-overflow-tooltip prop="assetModel" label="规格型号" width="150" />
      <el-table-column align="center" show-overflow-tooltip prop="assetType" label="所属类别" width="120" />
      <el-table-column align="center" show-overflow-tooltip prop="assetValue" label="财产价值(万元)" width="120" />

      <el-table-column align="center" show-overflow-tooltip prop="startTime" label="启用时间" width="120">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime | formatDate }}</span>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="number" label="数量" width="80"/>

      <el-table-column align="center" show-overflow-tooltip prop="position" label="所处位置" width="300"/>


      <el-table-column align="center" show-overflow-tooltip prop="status" label="目前状态" width="90">
        <template slot-scope="scope">
          <span v-if="scope.row.status === 0" style="color: #67C23A;"> 正常 </span>
          <span v-if="scope.row.status === 1" style="color: #E6A23C;"> 维修 </span>
          <span v-if="scope.row.status === 2" style="color: #F56C6C;"> 损坏 </span>
          <span v-if="scope.row.status === 3" style="color: #F56C6C;"> 报废 </span>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="imgUrl" label="图片" width="90">
        <template slot-scope="scope">
          <el-popover placement="right" title="" trigger="hover">
            <img :src="scope.row.imgUrl" style="height: 200px;width: 200px">
            <img slot="reference" :src="scope.row.imgUrl" :alt="scope.row.imgUrl" style="height: 35px;width: 35px">
          </el-popover>
        </template>
      </el-table-column>

      <el-table-column align="center" show-overflow-tooltip prop="managerName" label="管理员" width="120" />
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
          <el-button type="text" size="small" @click="handleDeleteAsset(scope.row.id)" style="color: red;">删除</el-button>
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
          <el-form ref="asset" :model="asset" :rules="rules">

            <el-input v-show="false" v-model="asset.id" autocomplete="off" />

            <el-form-item label="名称" label-width="150px" prop="assetName">
              <el-input v-model="asset.assetName" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="型号" label-width="150px" prop="assetModel">
              <el-input v-model="asset.assetModel" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="所属类别" label-width="150px" prop="assetType">
              <el-input v-model="asset.assetType" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="价值" label-width="150px" prop="assetValue">
              <el-input v-model="asset.assetValue" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="数量" label-width="150px" prop="number">
              <el-input v-model.number="asset.number" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="位置" label-width="150px" prop="position">
              <el-input v-model="asset.position" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="启用日期" label-width="150px">
              <el-date-picker
                    v-model="asset.startTime"
                    placeholder="选择日期">
                  </el-date-picker>
            </el-form-item>


            <el-form-item label="状态" label-width="150px">
              <el-radio-group v-model="asset.status">
                  <el-radio :label="0"><span style="color: #67C23A;font-weight: 600;">正常</span></el-radio>
                  <el-radio :label="1"><span style="color: #E6A23C;font-weight: 600;">维修</span></el-radio>
                  <el-radio :label="2"><span style="color: #F56C6C;font-weight: 600;">损坏</span></el-radio>
                  <el-radio :label="3"><span style="color: #F56C6C;font-weight: 600;">报废</span></el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item label="管理员" label-width="150px" prop="managerName">
              <el-input v-model="asset.managerName" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="管理员电话" label-width="150px" prop="managerPhone">
              <el-input v-model="asset.managerPhone" style="width: 70%;" autocomplete="off" />
            </el-form-item>

            <el-form-item label="备注" label-width="150px">
              <el-input v-model="asset.note" type="textarea" rows="5" style="width: 70%;" />
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
            <div slot="tip" class="el-upload__tip">上传设施照片</div>
          </el-upload>
        </div>

      </div>

      <div slot="footer" class="dialog-footer">
        <el-button @click="handleCloesDialog">取 消</el-button>
        <el-button v-if="dialogOpt === 'save'" type="primary" @click="handleSaveAsset">确 定</el-button>
        <el-button v-if="dialogOpt === 'update'" type="primary" @click="handelUpdateAsset">确 定</el-button>
      </div>
    </el-dialog>


    <el-dialog
      :visible.sync="centerDialogVisible"
      width="60%"
      :before-close="handleCloseDialog">
      <div class="user-info">
        <div class="info">
        <el-descriptions title="设施详情">
           <el-descriptions-item label="设施规格">{{assetInfo.assetModel}}</el-descriptions-item>
           <el-descriptions-item label="设施名称">{{assetInfo.assetName}}</el-descriptions-item>
           <el-descriptions-item label="设施类型">{{assetInfo.assetType}}</el-descriptions-item>
           <el-descriptions-item label="设施价值">{{assetInfo.assetValue}}万元</el-descriptions-item>
           <el-descriptions-item label="设施管理员">{{assetInfo.managerName}}</el-descriptions-item>
           <el-descriptions-item label="电话">{{assetInfo.managerPhone}}</el-descriptions-item>
           <el-descriptions-item label="存放位置">{{assetInfo.position}}</el-descriptions-item>
           <el-descriptions-item label="数量">{{assetInfo.number}}</el-descriptions-item>
           <el-descriptions-item label="启用时间">{{assetInfo.startTime}}</el-descriptions-item>
           <el-descriptions-item label="目前状态" span="3">
               <span v-if="assetInfo.status === 0" style="color: #67C23A;"> 正常 </span>
               <span v-if="assetInfo.status === 1" style="color: #E6A23C;"> 维修 </span>
               <span v-if="assetInfo.status === 2" style="color: #F56C6C;"> 损坏 </span>
               <span v-if="assetInfo.status === 3" style="color: #F56C6C;"> 报废 </span>
           </el-descriptions-item>
           <el-descriptions-item label="备注" span="3">{{assetInfo.note}}</el-descriptions-item>
        </el-descriptions>
        </div>
        <div class="avatar">
          <el-avatar :size="100">
                <img :src="assetInfo.imgUrl"/>
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
import { getAssetList, getAssetById, saveAsset, updateAsset, deleteAsset, deleteAllAsset } from '@/api/asset'
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
    const validateAssetName = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入设施名称'))
      } else {
        callback()
      }
    }

    const validateAssetModel = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入型号'))
      } else {
        callback()
      }
    }

    const validateAssetType = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入类别'))
      } else {
        callback()
      }
    }

    const validateAssetValue = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入价值'))
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

    const validateNumber = (rule, value, callback) => {
      if (!value) {
        return callback(new Error('请输入数量'))
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

    const validatePosition = (rule, value, callback) => {
      if (!value) {
        callback(new Error('请输入存放位置'))
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
      href: 'http://localhost:9001/asset/exportAsset?fileName=设施信息数据',
      //上传图片路径地址
      action:'http://localhost:9001/upload/img',
      // 弹出层表单对象
      asset: {
        id: '',
        assetName: '',
        assetModel: '',
        assetType: '',
        assetValue: '',
        startTime: '',
        position: '',
        status: 0,
        number:'',
        managerName: '',
        managerPhone: '',
        note: '',
        imgUrl:''
      },
      //设施详细对象
      assetInfo:{},
      // 表单验证规则
      rules: {
        assetName: [{ validator: validateAssetName, trigger: 'blur' }],
        assetModel: [{ validator: validateAssetModel, trigger: 'blur' }],
        assetType: [{ validator: validateAssetType, trigger: 'blur' }],
        assetValue: [{ validator: validateAssetValue, trigger: 'blur' }],
        position: [{ validator: validatePosition, trigger: 'blur' }],
        managerName: [{ validator: validateManager, trigger: 'blur' }],
        managerPhone: [{ validator: validateManagerPhone, trigger: 'blur' }],
        number: [{ validator: validateNumber, trigger: 'blur' }]
      }

    }
  },
  created() {
    this.handleAssetList(this.pageCurrent, this.pageSize)
  },

  methods: {

   // 图片上传
   handleAvatarSuccess(res, file) {
     // 图片回显地址
     this.imageUrl = res.result.imgPath + res.result.fileName
     // 图片地址
     this.asset.imgUrl = this.imageUrl
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

   //关闭详情弹出层
   handleCloseDialog(){
     this.centerDialogVisible = false
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
        this.handleAssetList(this.pageCurrent, this.pageSize, this.keyword)
      }
    },

    // 重置按钮
    handleClear() {
      // 清空输入框跟选择框
      this.keyword = ''
      // 重新刷新列表
      this.handleAssetList(this.pageCurrent, this.pageSize)
    },

    // 将单行数据封装为对象，进行打印
    handleClick(row) {
      this.assetInfo = row
      this.centerDialogVisible = true
    },

    // 获取所有设施信息
    handleAssetList(pageCurrent, pageSize, keyword) {
      // 发起请求
      getAssetList(pageCurrent, pageSize, keyword).then((res) => {
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
          //总记录数赋值
          this.total = res.result.total
        }
      })
    },

    // 分页，每页显示的条数
    handleSizeChange(val) {
      this.pageSize = val
      this.handleAssetList(this.pageCurrent, this.pageSize, this.keyword)
    },
    // 分页，切换上下页
    handleCurrentChange(val) {
      this.pageCurrent = val
      this.handleAssetList(this.pageCurrent, this.pageSize, this.keyword)
    },

    // 打开弹出层
    handleOpendialog(type, id) {
      if (type === 'save') {
        this.title = '新增设施信息'
        this.dialogOpt = 'save'
      } else {
        // 获取单个设施信息
        this.handelGetAssetById(id)
        this.dialogOpt = 'update'
        this.title = '更新设施信息'
      }
      this.dialogFormVisible = true
    },

    //关闭弹出层
    handleCloesDialog(){
      // 清空表单
      this.$refs.asset.resetFields()
      this.asset.status = 0
      this.asset.startTime = ''
      this.imageUrl = ''
      this.dialogFormVisible = false
    },

    // 新增设施信息
    handleSaveAsset() {
      //赋值
      this.asset.imgUrl = this.imageUrl
      this.$refs.asset.validate(valid => {
        if (valid) {
          saveAsset(this.asset).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 关闭弹出层
              this.dialogFormVisible = false
              // 更新列表
              this.handleAssetList(this.pageCurrent, this.pageSize)
              // 清空表单
              this.$refs.asset.resetFields()
              this.asset.status = 0
              this.asset.startTime = ''
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

    // 根据设施id获取设施信息
    handelGetAssetById(id) {
      getAssetById(id).then((res) => {
        this.asset = res.result
        this.imageUrl = this.asset.imgUrl
      })
    },

    // 更新设施信息
    handelUpdateAsset() {
      this.asset.imgUrl = this.imageUrl
      this.$refs.asset.validate(valid => {
        if (valid) {
          updateAsset(this.asset).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 关闭弹出层
              this.dialogFormVisible = false
              // 更新列表
              this.handleAssetList(this.pageCurrent, this.pageSize)
              // 清空表单
              this.$refs.asset.resetFields()
              this.asset.status = 1
              this.asset.startTime = ''

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

    // 删除设施信息
    handleDeleteAsset(id) {
      MessageBox.confirm('确定删除该设施？', '删除设施', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then((action) => {
        // 成功回调
        if (action === 'confirm') {
          deleteAsset(id).then((res) => {
            if (res.success === true) {
              Message({
                message: res.message,
                type: 'success',
                duration: 5 * 1000
              })
              // 刷新列表
              this.handleAssetList(this.pageCurrent, this.pageSize)
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

    // 批量删除设施信息
    handleDeleteAllAsset() {
      if (this.ids === '') {
        Message({
          message: '请选择需要删除的设施',
          type: 'warning',
          duration: 5 * 1000
        })
      } else {
        MessageBox.confirm('您确定删除选中设施吗？', '批量删除', {
          confirmButtonText: '确认删除',
          cancelButtonText: '取消删除',
          type: 'warning'
        }).then((action) => {
          // 确认按钮的回调
          if (action === 'confirm') {
            // 批量删除接口
            deleteAllAsset(this.ids).then((res) => {
              if (res.success === true) {
                Message({
                  message: res.message,
                  type: 'success',
                  duration: 5 * 1000
                })
                // 刷新设施列表
                this.handleAssetList(this.pageCurrent, this.pageSize)
              } else {
                // 刷新设施列表
                this.handleAssetList(this.pageCurrent, this.pageSize)
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
<style lang="scss" scoped>
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
