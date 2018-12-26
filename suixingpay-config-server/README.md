# 配置中心服务端

## 一、 数据库表设计

[suixingpay-config.mwb](./doc/suixingpay-config.mwb)

可以通过 sql 目录 下的sql文件进行初始化数据库

## 二、 权限说明

用户分为超级管理员和普通管理员两类：

1. profile, application, user 三张表的数据只有超级管理员才能进行管理； 
2. global_config, global_config_log, application_config, application_config_log等表，除超级管理员外，需要分配相应的权限才能进行管理；


## 三. 服务端(suixingpay-config-server)部署步骤

### 1. 初始化数据库

### 2. 准备好Redis环境

### 3. 修改配置文件

需要根据实际情况修改或增加：application-xxx.yml 文件(xxx指spring profile)

### 4. 构建


服务端使用前后端分离架构，为了降低学习成本，建议大家将前端静态资源文件（suixingpay-config-front中的文件）使用gradle assemble 或mvn package，将前端静态页面也打进可执行的jar包中。


通过下面命令进行构建打包：

gradle:

    gradle assemble
    
maven:

    mvn clean package -Dmaven.test.skip=true

### 5. 运行方法

#### 1. 本地开发环境启动
使用gradle 命令启动：

    gradle bootRun 
 
访问方式：http://localhost:8080  
登陆用户名密码： admin 123456

#### 2. 通过jar包进行启动，并指定服务器运行环境
使用jvm参数激活Spring boot profile：（比如：dev 为开发环境；prod 为生产环境）

开发环境：java -jar config.jar --profile=dev 

生产环境(需要增加application-prod.yml文件)：java -jar config.jar --profile=prod
