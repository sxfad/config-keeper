# 配置中心服务端

## 一、 数据库表设计

[suixingpay-config.mwb](./doc/suixingpay-config.mwb)

可以通过 sql 目录 下的sql文件进行初始化数据库

## 二、 权限说明

用户分为超级管理员和普通管理员两类：

1. profile, application, user 三张表的数据只有超级管理员才能进行管理； 
2. global_config, global_config_log, application_config, application_config_log等表，除超级管理员外，需要分配相应的权限才能进行管理；


## 三. 服务端(suixingpay-config-server)部署

### 1. 修改配置文件

需要根据实际情况修改：
application.yml

### 1. 本地开发环境启动
使用gradle 命令启动：

    gradle bootRun  
 
访问方式：http://localhost:8080  
登陆用户名密码： admin 123456

### 2. 不同服务器环境启动
上面通过bootstrap-dev.yml、bootstrap-test.yml、bootstrap-rc.yml、bootstrap-prod.yml等文件来切换不同环境的配置，但需要通过设置spring.profiles.active来激活。

激活Spring boot profile的方式很多，我们统一使用jvm参数指定：  
java -jar xxx.jar --profile=test	
java -jar xxx.jar --profile=rc  
java -jar xxx.jar --profile=prod