package com.rpm;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.generator.config.DataSourceConfig;
import com.baomidou.mybatisplus.generator.config.GlobalConfig;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.config.PackageConfig;
import com.baomidou.mybatisplus.generator.config.StrategyConfig;
import com.baomidou.mybatisplus.generator.config.po.TableFill;
import com.baomidou.mybatisplus.generator.config.rules.DateType;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;

public class AutoCode {
    public static void main(String[] args) {
        //构建一个代码生成器
        AutoGenerator mpg = new AutoGenerator();

        //配置策略

        //1、全局配置
        GlobalConfig gc = new GlobalConfig();
        String proPath = System.getProperty("user.dir"); //用户目录
        gc.setOutputDir(proPath+"/src/main/java"); //生成后所在目录
        gc.setAuthor("rpm"); //设置作者
        gc.setOpen(false); //是否打开资源管理器
        gc.setFileOverride(false);//是否覆盖之前生成的
        //gc.setServiceName("%sService");//去掉service的I前缀
        gc.setIdType(IdType.ASSIGN_ID); //初始的默认算法
        gc.setDateType(DateType.ONLY_DATE);//日期类型
        gc.setSwagger2(true);//是否配置swagger文档

        mpg.setGlobalConfig(gc);//将全局配置添加到代码生成器中

        //2、设置数据源
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setUrl("jdbc:mysql://localhost:3306/pm?useSSL=false&useUnicode=true&characterEncoding=UTF-8");
        dsc.setDriverName("com.mysql.jdbc.Driver");
        dsc.setUsername("root");
        dsc.setPassword("123456");
        dsc.setDbType(DbType.MYSQL);//数据库类型
        mpg.setDataSource(dsc);

        //3、包的配置
        PackageConfig pc = new PackageConfig();

        pc.setModuleName("rpm");//模块名
        pc.setParent("com.rpm");//生成路径
        pc.setEntity("domain");//实体类名
        pc.setMapper("mapper");//接口名
        pc.setService("service");//服务层名
        pc.setController("controller");//控制层

        mpg.setPackageInfo(pc);

        //4、策略配置
        StrategyConfig strategy = new StrategyConfig();

        strategy.setInclude(""); //表名映射

        strategy.setNaming(NamingStrategy.underline_to_camel);
        strategy.setColumnNaming(NamingStrategy.underline_to_camel);
        strategy.setEntityLombokModel(true); //使用lombok开启注解
        strategy.setLogicDeleteFieldName("is_del");//逻辑删除字段

        //自动填充策略
        TableFill createTime = new TableFill("create_time", FieldFill.INSERT);
        TableFill updateTime = new TableFill("update_time", FieldFill.INSERT_UPDATE);

        //将策略加到list，传到策略配置中
        ArrayList<TableFill> tableFills = new ArrayList<>();
        tableFills.add(createTime);
        tableFills.add(updateTime);

        strategy.setTableFillList(tableFills); //需要list

        //strategy.setVersionFieldName("version");//乐观锁
        strategy.setRestControllerStyle(true);//驼峰命名

        strategy.setControllerMappingHyphenStyle(true);//访问路径下划线

        mpg.setStrategy(strategy);

        mpg.execute();//执行
    }
}