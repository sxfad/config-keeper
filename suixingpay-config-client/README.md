# 应用接入配置中心说明

[![Maven Central](https://img.shields.io/maven-central/v/com.suixingpay.config-keeper/suixingpay-config-client.svg?style=flat-square)](https://maven-badges.herokuapp.com/maven-central/com.suixingpay.config-keeper/suixingpay-config-client/)


应用经常需要在不停机、不重新打包的情况下，动态修改一些配置，比如：一些功能的开关、性能配置等。为了方便动态更新应用配置，需要把需要动态更新配置放到应用之外的配置中心。

配置内容，分为全局配置（global config）和应用配置(application config)。全局配置指的是所有应用都是共用的配置，但它的优先及低于应用配置。一个运行环境下会有一个全局配置。

## 接入步骤

### 1. 引入依赖jar包

    compile("com.suixingpay.config-keeper:suixingpay-config-client:1.1.0")
    compile("org.springframework.cloud:spring-cloud-commons")
    compile("org.springframework.cloud:spring-cloud-context")
    compile("org.springframework.boot:spring-boot-starter-web")
    compile("org.springframework.boot:spring-boot-starter-security")
    compile("org.springframework.boot:spring-boot-starter-actuator")
    
    
### 2. 配置 src/main/resources/bootstrap.yml
以下内容是必须配置的：

    spring:
      application:
        name: config-keeper-demo        # 设置应用名称，不能与其它应用重名，必须配置
      profiles:
        active: ${profile:dev}          # 设置生效的profile, 必须配置
    # 启用spring boot的 endpoints，用于监控，刷新配置等
    management:
      security:
        enabled: true
      context-path: /ops
    security:
      basic:
        enabled: true
        path: /ops/**
      user:
        name: admin
        password: 123456
            
通常应用是运行在不同的环境（或机房）中，比如：开发环境(dev)、测试环境(test)、RC环境(rc)、生产环境(prod)。那么需要根据不同的环境配置不同的配置中心服务地址，比如，开发环境的配置，配置到src/main/resources/bootstrap-dev.yml文件中：
    
    suixingpay:
      config:
        enabled: true                 # 是否启用配置中心，默认值为：true;为了方便开发，建议开发环境设置为false
        uris:
        - http://127.0.0.1:8080/       # 配置中心服务地址，必须配置
        username: admin                # 调用接口用户名(非配置中心登录的用户名和密码)
        password: 123456               # 调用接口密码
        cachePath: ./config            # 配置缓存路径，默认值为：./config
        cacheTimeOut: 0                # 本地缓存过期时间(单位：秒),如果小于等于0时，一直有效
        failFast: false                # 是否快速失败，如果为true时，当访问配置中心时立即抛异常；如果为false时，会尝试加载3次，并会尝试获取本地缓存，最终还没有配置，才会抛异常。默认值：false
        ipAddress:                     # 应用实例IP
        managementPort:                # management.port
        managementContextPath:         # management.context-path
        
测试环境的配置文件为：src/main/resources/bootstrap-test.yml、RC环境的配置文件为：src/main/resources/bootstrap-rc.yml、生产环境的配置文件为：src/main/resources/bootstrap-prod.yml 参考开发环境的配置进行设置。    
             
### 3. 激活profile
上面通过bootstrap-dev.yml、bootstrap-test.yml、bootstrap-rc.yml、bootstrap-prod.yml等文件来切换不同环境的配置，但需要通过设置spring.profiles.active来激活。

激活Spring boot profile的方式很多，我们统一使用jvm参数指定：
	
	java -jar xxx.jar --profile=prod
   
### 4. java配置代码实例

spring boot中读取配置项内容的方法很多，最常用的有两种方式使用:@Value 和 @ConfigurationProperties

建议使用@ConfigurationProperties，使用它能更好对配置内容进行分类，更加便于管理，例如：

1). 配置内容例子：

    suixingpay: 
      defaultuser: 
        id: 3
        name: name3
    
2). java代码例子：
   
    @Data
    @ConfigurationProperties(prefix = "suixingpay.defaultuser")
    public class DefaultUserProperties {
        
        private Long id;
    
        private String name;
    }
    
    @Configuration
    @EnableConfigurationProperties(DefaultUserProperties.class)
    public class ConfigTest {
    
        @Autowired
        private DefaultUserProperties userProperties;
        
        @RefreshScope // 支持动态刷新
        @Bean(name="defaultUser")
        public UserDO defaultUser() {
            UserDO userDO=new UserDO();
            userDO.setId(userProperties.getId());
            userDO.setName(userProperties.getName());
            return userDO;
        }
        
        // @RefreshScope 这种手动注入的bean, 不需要加@RefreshScope 也会动态刷新
        @Bean
        public DefaultUserWapper defaultUserWapper(@Qualifier("defaultUser") UserDO defaultUser) {
            DefaultUserWapper wapper=new DefaultUserWapper();
            wapper.setUserDO(defaultUser);
            return wapper;
        }
    }
    
    @RestController
    // @RefreshScope  // 如果不加此，下面的@Value 无法刷新, 不建议使用
    public class ConfigTestController {
    
        @Autowired
        @Qualifier("defaultUser")
        private UserDO defaultUser;
    
        @Value("${configSwitch:false}")
        private boolean configSwitch = false;
        
        @Autowired
        private DefaultUserWapper defaultUserWapper;
    
        @GetMapping({ "/", "index" })
        public String getConfig() {
            return defaultUser.toString() + "--" + configSwitch+";defaultUserWapper-->"+defaultUserWapper;
        }
    }

使用 @Value 读取配置内容，如果要实现动态刷新的话，需要结合**@RefreshScope** 一起使用，不建议使用。

[@RefreshScope官方说明](http://cloud.spring.io/spring-cloud-static/Edgware.SR2/single/spring-cloud.html#_refresh_scope)
    
### 5. 配置中心后台管理

到配置中心管理后台填写配置内容

开发等环境的配置, 请登录：http://127.0.0.1/ 用户名：admin, 密码： 123456
 

### 6. 基于spring boot endpoint 实现监控，刷新配置等功能

1. refresh ：用于从配置中心刷新配置内容；注意：需要使用POST方法调用，调用成功会返回已被修改的属性key值数组；如果本地缓存与远程版本相同，会使用本地缓存中的配置。
    
    **注意**：应用程序没有实现自动监控配置版本是否发生变更，而进行自动刷新配置的功能。主要是考虑如果全自动刷新，不太可控，当配置内容有问题等原因造成刷新失败时，会造成所有应用不可用。而通过手动刷新，就可以先刷新几个实例，当验证没有问题后，再刷新其它实例。 动态刷新功能一定要做好测试。
    
2. configversion : 用于查看当前应用配置的本地缓存版本。
3. env : 查看所有配置信息。
    
   注：这里只是看配置内容，但不一定是程序的真实使用的配置值，因为有些情况是不能被刷新，比如：@Value没结合@RefreshScope使用。
    
4. configprops 查看所有使用@ConfigurationProperties 配置的值，用于检查配置是否生效。
