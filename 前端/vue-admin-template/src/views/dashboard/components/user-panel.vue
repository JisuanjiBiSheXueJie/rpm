<template>
  <div id="app-container">
    <div class="data-info">
      <el-row :gutter="15">

        <el-col :span="4">
          <el-card shadow="hover">
            水费：<span>{{cost.water}}</span> 元
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover">
            电费：<span>{{cost.electricity}}</span> 元
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover">
            燃气费：<span>{{cost.gasfee}}</span> 元
          </el-card>
        </el-col>

        <el-col :span="4">
          <el-card shadow="hover">
            物业费：<span>{{cost.pmf}}</span> 元
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
            <span>最新消息</span>
            <el-badge :value = "msg" class="compBtn">
              <el-button style="float: right; padding: 3px 0" type="text" @click="handleMoreMsg">更多</el-button>
            </el-badge>
          </div>
          <div v-for="item in msgList" :key="item.id" class="text item">
            <div class="info-content">
              <div class="content-title">
                {{item.msgTitle}}
              </div>
              <div class="content-date">
                {{item.sendTime}}
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

export default{
  data(){
    return{
      //费用数据对象
      cost:{},
      annoList:[],
      msgList:[],
      //折线图日期list
      dateList:[],
      //折线图数据
      dataList:[],
      msg:0
    }
  },
  mounted() {
    this.handleDataInit()
    this.initeBarTow()
  },
  methods:{
 //用户投诉
      handleMoreMsg(){
        this.$router.push('/msg/user-msg')
      },
      //更多公告
      handleMoreAnno(){
        this.$router.push('/anno/user-anno')
      },
    //数据初始化
    handleDataInit(){
      data().then((res) =>{
        this.msg = res.result.msgIsRedCount
        this.cost = res.result.cost
        this.annoList = res.result.annoList
        this.msgList = res.result.msgList

        //拆分list
        for(let i = 0; i < res.result.loginNum.length;i++){
          this.dateList.push(res.result.loginNum[i].loginTime)
          this.dataList.push(res.result.loginNum[i].loginNum)
        }
        //折线图传入数据
        this.initeBarOne(this.dateList,this.dataList)
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
    initeBarTow() {
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
          text: '缴费统计'
        },
        xAxis: {
          type: 'category',
          data: ['周一', '周二', '周三', '周四', '周五', '周六', '周日'],

        },
        yAxis: {
          type: 'value',

        },
        series: [{
          name:'缴费金额',
          data: [120, 200, 150, 80, 70, 110, 130],
          type: 'bar',
          showBackground: true,
          backgroundStyle: {
            color: 'rgba(180, 180, 180, 0.2)'
          }
        }]
      });
    }
  }
}

</script>

<style>
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
