CREATE TABLE IF NOT EXISTS chat_message  (
  id bigint NOT NULL COMMENT '主键',
  message_id varchar(255) NOT NULL COMMENT '消息 id',
  parent_message_id varchar(255) NULL DEFAULT NULL COMMENT '父级消息 id',
  message_type varchar(64) NOT NULL COMMENT '消息类型',
  chat_room_id bigint NOT NULL COMMENT '聊天室 id',
  model_name varchar(50) NULL DEFAULT NULL COMMENT '模型名称',
  api_key varchar(5120) NULL DEFAULT NULL COMMENT 'ApiKey',
  api_type varchar(20)  NULL COMMENT 'API 类型',
  content text NOT NULL COMMENT '消息内容',
  original_data text NULL COMMENT '消息的原始请求或响应数据',
  response_error_data text NULL COMMENT '错误的响应数据',
  ip varchar(255) NULL DEFAULT NULL COMMENT 'ip',
  create_by int NOT NULL COMMENT '用户 id',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX unq_message_id(message_id ASC) USING BTREE
) ENGINE = InnoDB COMMENT = '聊天消息表' ;

CREATE TABLE IF NOT EXISTS chat_room  (
  id bigint NOT NULL COMMENT '主键',
  message_id varchar(255) NOT NULL COMMENT '第一条消息id',
  title varchar(1024) NULL DEFAULT NULL COMMENT '对话标题，从第一条消息截取',
  status tinyint NOT NULL DEFAULT 1 COMMENT '聊天室状态;1-开启，2-关闭',
  create_by int NOT NULL COMMENT '用户 id',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX unq_message_id(message_id ASC) USING BTREE
) ENGINE = InnoDB COMMENT = '聊天室表' ;

CREATE TABLE IF NOT EXISTS log_email_send  (
  id bigint NOT NULL AUTO_INCREMENT COMMENT '主键',
  from_email_address varchar(64) NOT NULL COMMENT '发件人邮箱',
  to_email_address varchar(64) NOT NULL COMMENT '收件人邮箱',
  biz_type varchar(64) NULL DEFAULT NULL COMMENT '业务类型',
  request_ip varchar(255) NOT NULL COMMENT '请求 ip',
  content text NOT NULL COMMENT '发送内容',
  message_id varchar(128) NOT NULL COMMENT '发送后会返回一个messageId',
  status tinyint NOT NULL COMMENT '发送状态，0失败，1成功',
  message varchar(1024) NOT NULL COMMENT '发送后的消息，用于记录成功/失败的信息，成功默认为success',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB COMMENT = '邮箱发送日志' ;

CREATE TABLE IF NOT EXISTS log_user_login  (
  id int NOT NULL AUTO_INCREMENT COMMENT '主键',
  user_id int NOT NULL COMMENT '登录的用户ID',
  login_type varchar(32) NOT NULL COMMENT '登录方式（注册方式），邮箱登录，手机登录等等',
  login_username varchar(128) NOT NULL COMMENT '登录的用户名',
  login_ip varchar(32) NOT NULL COMMENT '登录的IP地址',
  login_status tinyint(1) NOT NULL COMMENT '登录状态，1登录成功，0登录失败',
  message varchar(64) NOT NULL COMMENT '登录结果',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB  COMMENT = '前端用户登录日志表' ;

CREATE TABLE IF NOT EXISTS sys_dict  (
  id bigint NOT NULL AUTO_INCREMENT COMMENT 'id',
  dict_type varchar(255) NOT NULL COMMENT '字典类型',
  dict_code varchar(255) NOT NULL COMMENT '字典编码',
  dict_value varchar(5120) NOT NULL COMMENT '字典值',
  description varchar(255) NULL DEFAULT NULL COMMENT '描述',
  create_time datetime NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (id) USING BTREE,
  INDEX idx_dict_type(dict_type ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3 COMMENT = '字典表' ;

CREATE TABLE IF NOT EXISTS sys_user  (
  id int NOT NULL AUTO_INCREMENT COMMENT '主键',
  nickname varchar(16) NOT NULL COMMENT '用户昵称',
  description varchar(64) NULL DEFAULT NULL COMMENT '描述',
  avatar_url varchar(255) NULL DEFAULT NULL COMMENT '头像',
  last_login_ip varchar(64) NULL DEFAULT NULL COMMENT '上一次登录 IP',
  member_number int NOT NULL DEFAULT 0 COMMENT '会员次数',
  expire_time datetime NULL DEFAULT NULL COMMENT '到期时间',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 100000 COMMENT = '前端用户基础信息表' ;

CREATE TABLE IF NOT EXISTS sys_user_email  (
  id int NOT NULL AUTO_INCREMENT COMMENT '主键',
  email varchar(64) NOT NULL COMMENT '邮箱账号',
  password varchar(128) NOT NULL COMMENT '加密后的密码',
  salt varchar(16) NOT NULL COMMENT '加密盐',
  user_id int NOT NULL COMMENT '用户表的ID',
  create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  update_time datetime NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id) USING BTREE,
  UNIQUE INDEX unq_email(email ASC) USING BTREE COMMENT '邮箱唯一'
) ENGINE = InnoDB AUTO_INCREMENT = 100000 COMMENT = '前端用户邮箱登录' ;

CREATE TABLE IF NOT EXISTS sys_user_invite  (
    id int NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id int NULL DEFAULT NULL COMMENT '用户id',
    user_name varchar(255) NULL DEFAULT NULL COMMENT '用户名',
    invite_user_id int NULL DEFAULT NULL COMMENT '邀请的用户id',
    invite_user_name varchar(255) NULL DEFAULT NULL COMMENT '邀请的用户名',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE
    ) ENGINE = InnoDB COMMENT = '用户邀请记录表' ;

CREATE TABLE IF NOT EXISTS platform_cdkey_exchange  (
    id int NOT NULL AUTO_INCREMENT COMMENT 'id',
    user_id int NOT NULL COMMENT '用户id',
    source varchar(16) NOT NULL COMMENT '来源:invite,buy',
    cdkey varchar(64) NULL DEFAULT NULL COMMENT 'cdkey',
    number int NOT NULL COMMENT '次数',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE
    ) ENGINE = InnoDB COMMENT = '平台cdkey兑换表' ;

CREATE TABLE IF NOT EXISTS platform_cdkey  (
    id int NOT NULL AUTO_INCREMENT COMMENT 'id',
    cdkey varchar(64) NULL DEFAULT NULL COMMENT 'cdkey',
    number int NOT NULL COMMENT '次数',
    is_use TINYINT NOT NULL DEFAULT 0 COMMENT '是否使用，0-未使用，1-已使用',
    ip varchar(64) NULL DEFAULT NULL COMMENT 'IP',
    create_time datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (id) USING BTREE,
    UNIQUE INDEX unq_cdkey(cdkey ASC) USING BTREE
    ) ENGINE = InnoDB COMMENT = '平台cdkey记录表' ;


