-- -----------------------------------------------------
-- Table `suixingpay_config`.`global_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`global_config` (
  `profile` VARCHAR(10) NOT NULL COMMENT '环境名称。关联profile.profile',
  `source_type` VARCHAR(10) NOT NULL COMMENT '配置类型：PROPERTIES 或 YAML',
  `property_source` TEXT NOT NULL COMMENT '配置信息',
  `version` INT NOT NULL DEFAULT 0 COMMENT '版本，每修改一次版本号加1，并添加新的一条记录',
  `memo` VARCHAR(450) NULL COMMENT '备注说明',
  `modify_time` TIMESTAMP NOT NULL COMMENT '修改时间',
  `user_id` INT NOT NULL COMMENT '修改用户',
  PRIMARY KEY (`profile`))
ENGINE = InnoDB
COMMENT = '全局配置';


-- -----------------------------------------------------
-- Table `suixingpay_config`.`profile`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`profile` (
  `profile` VARCHAR(10) NOT NULL COMMENT '环境名称',
  `name` VARCHAR(45) NOT NULL COMMENT '环境显示说明',
  PRIMARY KEY (`profile`))
ENGINE = InnoDB
COMMENT = '环境表';


-- -----------------------------------------------------
-- Table `suixingpay_config`.`application`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`application` (
  `name` VARCHAR(45) NOT NULL COMMENT '应用程序名称，对应spring.appliction.name',
  `description` VARCHAR(45) NOT NULL COMMENT '应用程序说明',
  UNIQUE INDEX `application_name_UNIQUE` (`name` ASC),
  PRIMARY KEY (`name`))
ENGINE = InnoDB
COMMENT = '应用表';


-- -----------------------------------------------------
-- Table `suixingpay_config`.`application_config`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`application_config` (
  `profile` VARCHAR(10) NOT NULL COMMENT 'spring.profiles.active,环境名称。关联profile.profile',
  `application_name` VARCHAR(45) NOT NULL COMMENT 'spring.application.name',
  `property_source` TEXT NOT NULL COMMENT '配置内容',
  `source_type` VARCHAR(10) NOT NULL COMMENT '配置类型：PROPERTIES 或 YAML',
  `version` INT NOT NULL DEFAULT 0 COMMENT '版本，每修改一次版本号加1，并增加一条记录。',
  `memo` VARCHAR(500) NULL COMMENT '备注说明',
  `modify_time` TIMESTAMP NOT NULL COMMENT '数据创建时间',
  `user_id` INT NOT NULL,
  PRIMARY KEY (`profile`, `application_name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `suixingpay_config`.`global_config_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`global_config_log` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `profile` VARCHAR(10) NOT NULL COMMENT '环境名称。关联profile.profile',
  `property_source` TEXT NOT NULL COMMENT '配置信息',
  `source_type` VARCHAR(10) NOT NULL COMMENT '配置类型：PROPERTIES 或 YAML',
  `version` INT NOT NULL DEFAULT 0 COMMENT '版本，每修改一次版本号加1，并添加新的一条记录',
  `memo` VARCHAR(450) NULL COMMENT '备注说明',
  `modify_time` TIMESTAMP NOT NULL  COMMENT '数据修改时间',
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `profile` (`profile` ASC))
ENGINE = InnoDB
COMMENT = '全局配置日志表';


-- -----------------------------------------------------
-- Table `suixingpay_config`.`application_config_log`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`application_config_log` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `profile` VARCHAR(10) NOT NULL COMMENT 'spring.profiles.active环境名称。关联profile.profile',
  `application_name` VARCHAR(45) NOT NULL COMMENT 'spring.application.name',
  `property_source` TEXT NOT NULL COMMENT '配置内容',
  `source_type` VARCHAR(10) NOT NULL COMMENT '配置类型：PROPERTIES 或 YAML',
  `version` INT NOT NULL DEFAULT 0 COMMENT '版本，每修改一次版本号加1，并增加一条记录。',
  `memo` VARCHAR(500) NULL COMMENT '备注说明',
  `modify_time` TIMESTAMP NOT NULL COMMENT '数据创建时间',
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `user_id` INT NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `application_name_profile` (`application_name` ASC, `profile` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `suixingpay_config`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`user` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '自增主键',
  `name` VARCHAR(45) NOT NULL COMMENT '登录名称',
  `password` VARCHAR(32) NOT NULL COMMENT '登录密码',
  `email` VARCHAR(45) NULL COMMENT '邮箱',
  `administrator` VARCHAR(3) NOT NULL DEFAULT 0 COMMENT '是否超级管理员，YES是超级管理员，NO不是超级管理员',
  `status` VARCHAR(10) NOT NULL DEFAULT 1 COMMENT '状态VALID：有效；INVALID；无效',
  `created_date` TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `name_UNIQUE` (`name` ASC))
ENGINE = InnoDB
COMMENT = '用户';


-- -----------------------------------------------------
-- Table `suixingpay_config`.`user_application_config_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`user_application_config_role` (
  `user_id` INT NOT NULL,
  `application_name` VARCHAR(45) NOT NULL,
  `profile` VARCHAR(10) NOT NULL COMMENT '环境名称。关联profile.profile',
  PRIMARY KEY (`user_id`, `application_name`, `profile`))
ENGINE = InnoDB
COMMENT = '应用配置权限';


-- -----------------------------------------------------
-- Table `suixingpay_config`.`user_global_config_role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`user_global_config_role` (
  `user_id` INT NOT NULL COMMENT '用户ID',
  `profile` VARCHAR(10) NOT NULL COMMENT '环境名称。关联profile.profile',
  PRIMARY KEY (`user_id`, `profile`))
ENGINE = InnoDB
COMMENT = '全局配置权限';

-- -----------------------------------------------------
-- Table `suixingpay_config`.`application_instance`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `suixingpay_config`.`application_instance` (
  `id` INT NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `application_name` VARCHAR(45) NOT NULL COMMENT '应用名',
  `profile` VARCHAR(10) NOT NULL COMMENT '环境',
  `ip` VARCHAR(15) NOT NULL COMMENT '实例ip地址',
  `port` INT(11) NOT NULL COMMENT '端口号',
  `manager_path` VARCHAR(200) NOT NULL COMMENT 'manager端点路径',
  `username` VARCHAR(45) NULL COMMENT '端点用户名',
  `password` VARCHAR(32) NULL COMMENT '端点密码',
  `created_date` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `modify_time` TIMESTAMP NULL COMMENT '上次修改时间',
  PRIMARY KEY (`id`),
  UNIQUE INDEX `application_profile_ip_port` (`application_name` ASC, `profile` ASC, `ip` ASC, `port` ASC))
ENGINE = InnoDB
COMMENT = '应用实例信息表'
