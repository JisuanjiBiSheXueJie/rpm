**项目描述:**

### 小区管理系统

### 核心代码已删除 需要完整代码请私信企鹅号

### 加好友前帮忙start一下，并备注git有偿获取源码

### 我的QQ号是302920276 或者 1033848108

### 加qq好友说明（被部分 网友整得心力交瘁）：

```
1.加好友务必按照格式备注
2.避免浪费各自的时间！
3.当“客服”不容易，repo 主是体面人，不爆粗，性格好，文明人。
```

#### 1、所用技术

本项目所使用的是前后端分离技术

开发环境：idea编辑器、apache-maven-3.6.3、Vscode、node.js

后端：springboot、shiro、token、mysql、mybatis、mybatis-plus、redis、druid、swagger2

前端：基于vue的开发框架基于vue-admin-template进行二次开发、样式基于element ui 的组件

#### 2、目录介绍

项目后端：rpm

sql目录存放的是数据库sql脚本文件

src/main/java/com/rpm目录下存放的是项目源代码

```
annotations目录：自定义注解类，存放自定义注解
aop目录：存放日志切面
config目录：存放相关配置，如mybatis-plus配置，跨域配置，druid配置，redis配置等等
controller目录：项目控制层
domain目录：项目实体类
enums：自定义枚举
filter目录：过滤器
listenter目录：监听器
mapper目录：项目数据持久层
service目录：项目服务层
utils目录：工具包
vo目录：实体类扩展包
RpmApplication：主启动类
```

src/main/java/resource目录下存放的是项目资源文件

```
mapper目录：存放mybatis的xml文件
static目录：静态资源文件，目前没有使用到
templates目录：页面模板目录，目前没有使用到
application.yml：项目主配置文件，公共配置写在此配置文件汇总（默认环境为本地开发环境  dev）
application-dev.yml：开发环境配置文件，本地开发使用该配置文件
application-pro.yml：线上部署配置文件，需要线上部署时要修改application.yml中的配置文件名
```

src/test/java/com/rpm目录下存放的是本地测试接口文件

```
AutoCode：mybatis-plus代码生成器（生成后端基本代码）
RpmApplicationTests：项目主测试类
```

依赖包导入通过 pom.xml 文件进行导入

项目前端：vue-admin-template

主要目录是src目录，src目录下

```
api目录：存放接口
router目录：存放路由配置
utils目录：工具包
views目录：视图目录
```

views目录下

```
community目录：管理员视图
dashboard目录：控制台视图
log目录：日志视图
user目录：管理员管理用户信息视图
useropt目录：用户视图
login目录：登录视图
```

配置文件

```
.env.development：本地开发配置文件，进行本地开发时在该配置文件汇总配置接口路径
.env.production：打包线上部署文件
```

#### 3、项目本地部署

后端：拿到项目后，首先配置好maven环境，maven3.6以上版本，springboot2.5以上版本，打开idea等待依赖导入，依赖导入完成后运行项目即可。项目打包后可以将jar包上传服务器进行部署

注意点：

- 不需要swagger的可将src/main/java/com/rpm/config/swagger2中Swagger2Config类中的@Configuration注解注释掉

- 不需要druid监控的可将src/main/java/com/rpm/config/druid中DruidConfig类中的@Configuration注解注释掉
- 需要将redis开启

前端：前端项目是没有依赖的，首次启动前需要进入项目根目录，也就是vue-admin-template目录下，在窗口地址栏cmd进入命令窗口，使用命令  npm install  安装依赖，等待依赖安装完毕后即可启动项目，本地启动项目命令为  npm run dev ，打包上线命令为  npm run build:prod ，打包完成后会在根目录上生成一个新的目录：dist，线上部署就将该目录拷贝到服务器对应位置，使用 nginx 进行代理即可

#### 4、功能列表

- 控制台中心
  - 人员管理
    - 用户列表
    - 已删除用户
    - 已冻结用户
  - 小区管理
    - 整栋信息
    - 消息列表
    - 公告列表
    - 车位信息
    - 缴费明细
    - 公共设施
    - 投诉建议
  - 系统管理
    - 登录情况
    - 系统更新日志
    - 数据源监控
    - 用户操作日志

**开发语言:**

Java Node  Vue javascript

**技术框架:**

SpringBoot，Vue ElementUI Mybatis 

**开发工具:**

Idea,vscode 

**数据库:**

Mysql8.0/MySQL5.7均可

**项目截图:**
![2022年10月23日14时:21分:03秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:21分:03秒)
![2022年10月23日14时:18分:33秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:18分:33秒)
![2022年10月23日14时:19分:11秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:19分:11秒)
![2022年10月23日14时:19分:23秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:19分:23秒)
![2022年10月23日14时:19分:30秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:19分:30秒)
![2022年10月23日14时:19分:42秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:19分:42秒)
![2022年10月23日14时:19分:50秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:19分:50秒)
![2022年10月23日14时:19分:59秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:19分:59秒)
![2022年10月23日14时:20分:07秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:20分:07秒)
![2022年10月23日14时:20分:14秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:20分:14秒)
![2022年10月23日14时:20分:21秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:20分:21秒)
![2022年10月23日14时:20分:28秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:20分:28秒)
![2022年10月23日14时:20分:36秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:20分:36秒)
![2022年10月23日14时:20分:50秒](https://cyymacbookpro.oss-cn-shanghai.aliyuncs.com/Macbookpro/2022年10月23日14时:20分:50秒)

