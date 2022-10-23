<!-- 管理员面板组件 -->
<template>
  <div id="app-container">
    <div class="data-info">
      <el-row :gutter="15">

        <el-col :span="4" >
          <el-card shadow="hover">
            业主人数：{{userNum}}
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover">
            楼栋总数：{{floorNum}}
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover">
            消息总数：{{msgNum}}
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover">
            公告总数：{{annoNum}}
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover">
            建议投诉：{{annoNum}}
          </el-card>
        </el-col>

      </el-row>
    </div>

    <div class="data-pic">
      <!-- 统计图容器 -->
      <div id="barOne" style="width: 600px;height: 400px;" />
      <div id="barTow" style="width: 600px;height: 400px;" />
    </div>

    <div class="info">
      <div class="msg">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>用户投诉建议</span>
            <el-badge :value = "comp" class="compBtn">
              <el-button style="float: right; padding: 3px 0" type="text" @click="handleMoreComp">更多</el-button>
            </el-badge>
          </div>
          <div v-for="item in complaintsList" :key="item.id" class="text item">
            <div class="info-content">
              <div class="content-title">
                {{item.content}}
              </div>
              <div class="content-date">
                {{item.submitTime}}
              </div>
            </div>

          </div>
        </el-card>
      </div>

      <div class="cop">
        <el-card class="box-card">
          <div slot="header" class="clearfix">
            <span>最新公告</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="handleMoreAnno">更多</el-button>
          </div>
          <div v-for="item in annoList" :key="item.id" class="text item">
            <div class="info-content">
              <div class="content-title">
                {{item.title}}
              </div>
              <div class="content-date">
                {{item.releaseTime}}
              </div>
            </div>
          </div>
        </el-card>
      </div>

    </div>

  </div>
</template>

<script>

  import { data } from '@/api/dataInit'

  export default {
    data() {
      return {

        //统计面板
        annoNum:0,
        compNum:0,
        floorNum:0,
        msgNum:0,
        userNum:0,

        //折线图日期
        dateListByLogin:[],
        //折线图数据
        dataListByLogin:[],
        //柱状图日期
        dateListByAmount:[],
        //柱状图数据
        dataListByAmount:[],

        //信息面板
        complaintsList:[],
        annoList:[],

        //小标识
        comp:0
      }
    },
    mounted() {
      this.handleDataInit()
    },
    methods: {
      //用户投诉
      handleMoreComp(){
        this.$router.push('/community/comp-list')
      },
      //更多公告
      handleMoreAnno(){
        this.$router.push('/community/anno-list')
      },

      //数据初始化
      handleDataInit(){
        data().then((res) =>{
          //数据赋值
          this.annoNum = res.result.annoNum
          this.compNum = res.result.compNum
          this.floorNum = res.result.floorNum
          this.msgNum = res.result.msgNum
          this.userNum = res.result.userNum
          this.comp = res.result.countIsRed
          this.complaintsList = res.result.complaintsList
          this.annoList = res.result.annoList

          //拆分登录数据list
          for(let i = 0; i < res.result.loginNum.length;i++){
            this.dateListByLogin.push(res.result.loginNum[i].loginTime)
            this.dataListByLogin.push(res.result.loginNum[i].loginNum)
          }
          //拆分缴费数据list
          for(let i = 0; i < res.result.amountNum.length;i++){
            this.dateListByAmount.push(res.result.amountNum[i].amountTime)
            this.dataListByAmount.push(res.result.amountNum[i].amountCount)
          }

          //折线图传入数据
          this.initeBarOne(this.dateListByLogin,this.dataListByLogin)
          //柱状图传入数据
          this.initeBarTow(this.dateListByAmount,this.dataListByAmount)
        })
      },

      //折线图
      initeBarOne(dateList,dataList) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('barOne'));
        // 绘制图表
        myChart.setOption({
          tooltip: {
             trigger: 'axis',
             axisPointer: { type: 'cross' },
             formatter: "{a} <br/>{b} : {c}"
           },
          title: {
            text: '最近七天登录情况统计'
          },
          xAxis: {
            axisLabel: {
              interval:0,
              rotate:40
              },
              //刻度
           axisTick: {
              alignWithLabel: true
              },
            type: 'category',
            data:  dateList//['周一', '周二', '周三', '周四', '周五', '周六', '周日']
          },
          yAxis: {
            type: 'value'
          },
          series: [{
            name:'访问次数',
            data: dataList, //[150, 230, 224, 218, 135, 147, 260],
            type: 'line'
          }]
        });
      },
      //柱状图
      initeBarTow(dateList,dataList) {
        // 基于准备好的dom，初始化echarts实例
        var myChart = this.$echarts.init(document.getElementById('barTow'));
        // 绘制图表
        myChart.setOption({
           tooltip: {
              trigger: 'axis',
              axisPointer: { type: 'cross' },
              formatter: "{a} <br/>{b} : {c}"
            },

          title: {
            text: '最近七天缴费金额统计'
          },
          xAxis: {
            type: 'category',
            axisLabel: {
              interval:0,
              rotate:40
              },
            axisTick: {
               alignWithLabel: true
               },
            data: dateList //['周一', '周二', '周三', '周四', '周五', '周六', '周日']

          },
          yAxis: {
            type: 'value',
            name:'元/天'
          },
          series: [{
            name:'缴费金额',
            data: dataList, //[120, 200, 150, 80, 70, 110, 130],
            type: 'bar',
            showBackground: true,
            backgroundStyle: {
              color: 'rgba(255, 255, 255, 0.2)'
            }
          }]
        });
      }
    }
  }
</script>

<style lang="scss" scoped>
  .data-info {
    width: 100%;
    height: 30%;
  }

  .data-pic {
    display: flex;
    margin-top: 5%;
  }
  .info{
    width: 90%;
    margin-top: 3%;
    display: flex;
    justify-content: space-between;

  }
   .text {
      font-size: 14px;
    }

    .item {
      margin-bottom: 18px;
    }

    .clearfix:before,
    .clearfix:after {
      display: table;
      content: "";
    }
    .clearfix:after {
      clear: both
    }

    .box-card {
      width: 480px;
    }
    .compBtn{
      margin-left: 70%;
    }
    .info-content{
      display: flex;
      width: 100%;
      justify-content: space-between;
    }
    .content-title{
      width: 60%;
      text-overflow: ellipsis;
      white-space: nowrap;
      overflow: hidden;
    }
    .content-date{
      width: 30%;
    }
</style>
