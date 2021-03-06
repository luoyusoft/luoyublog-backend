CREATE DATABASE luoyu_blog;

use luoyu_blog;

CREATE TABLE USER(
                     USER_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                     EMAIL VARCHAR (32) UNIQUE COMMENT '邮箱(设置唯一值,一个邮箱绑定一个账号)',
                     PHONE VARCHAR (32) UNIQUE COMMENT '手机号(设置唯一值,一个手机号绑定一个账号)',
                     BAN TINYINT(1) COMMENT '封号状态(正常使用：false 被封禁：true)',
                     STATUS TINYINT(1) COMMENT '账号状态(正常使用：true 注销：false)',
                     USER_NAME VARCHAR (32) UNIQUE COMMENT '用户名(设置唯一值)',
                     PASSWORD VARCHAR (100) COMMENT '密码',
                     NAME VARCHAR(32) COMMENT '用户昵称',
                     AGE INT COMMENT '年龄',
                     SEX INT COMMENT '性别(保密：1 男：2 女：3 其他：4)',
                     ARTICLE INT COMMENT '文章量',
                     ARTICLE_COMMENT INT COMMENT '文章评论量',
                     ARTICLE_PRAISE INT COMMENT '文章点赞量',
                     REGION VARCHAR (200) COMMENT '地址',
                     OCCUPATION VARCHAR (32) COMMENT '职业',
                     DYNAMIC VARCHAR (400) COMMENT '简介',
                     CREATE_TIME DATETIME COMMENT '创建时间',
                     UPDATE_TIME DATETIME COMMENT '更新时间',
                     UPDATER_ID BIGINT COMMENT '更新人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '用户表';

CREATE TABLE ROLE(
                     ROLE_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                     NAME VARCHAR(32) COMMENT '角色名称',
                     CODE INT COMMENT '角色编码(管理员：1 普通用户：2 VIP用户：3 )',
                     CREATE_TIME DATETIME COMMENT '创建时间',
                     CREATER_ID BIGINT COMMENT '创建人ID',
                     UPDATE_TIME DATETIME COMMENT '更新时间',
                     UPDATER_ID BIGINT COMMENT '更新人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '角色表';

CREATE TABLE USER_ROLE(
                          USER_ROLE_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                          CODE INT COMMENT '关联角色编码(管理员：1 普通用户：2 VIP用户：3 )',
                          USER_ID BIGINT COMMENT '关联用户ID',
                          CREATE_TIME DATETIME COMMENT '创建时间',
                          CREATER_ID BIGINT COMMENT '创建人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '用户角色表';

CREATE TABLE PERMISSION(
                           PERMISSION_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                           NAME VARCHAR(32) COMMENT '权限名称(查看文章详情、查看他人主页等)',
                           TYPE VARCHAR(32) COMMENT '权限类型(管理员操作：A 普通用户操作：B VIP用户操作：C )',
                           CODE INT COMMENT '权限编码(1、2、3)',
                           CREATE_TIME DATETIME COMMENT '创建时间',
                           CREATER_ID BIGINT COMMENT '创建人ID',
                           UPDATE_TIME DATETIME COMMENT '更新时间',
                           UPDATER_ID BIGINT COMMENT '更新人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '权限表';

CREATE TABLE USER_PERMISSION(
                                USER_PERMISSION_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                                TYPE VARCHAR(32) COMMENT '关联权限类型(管理员操作：A 普通用户操作：B VIP用户操作：C )',
                                CODE INT COMMENT '关联权限编码(管理员：1 普通用户：2 VIP用户：3 )',
                                USER_ID BIGINT COMMENT '关联用户ID',
                                CREATE_TIME DATETIME COMMENT '创建时间',
                                CREATER_ID BIGINT COMMENT '创建人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '用户权限表';

CREATE TABLE MESSAGE(
                        MESSAGE_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                        CONTENT VARCHAR(400) COMMENT '内容',
                        USER_ID BIGINT COMMENT '关联用户ID',
                        CREATE_TIME DATETIME COMMENT '创建时间'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '消息表';

CREATE TABLE PRIMARY_LABEL(
                              PRIMARY_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                              NAME VARCHAR(32) COMMENT '标签名称',
                              CODE INT COMMENT '标签编码(个人成长：1 心情随笔：2 技术分享：3 资源分享：4 )',
                              CREATE_TIME DATETIME COMMENT '创建时间'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '一级标签表';

CREATE TABLE SECONDARY_LABEL(
                                SECONDARY_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                                PRIMARY_CODE INT COMMENT '归属一级标签编码(个人成长：1 心情随笔：2 技术分享：3 资源分享：4 )',
                                NAME VARCHAR(32) COMMENT '标签名称',
                                CODE INT COMMENT '标签编码(后台：1 前端：2 数据库：3 等)',
                                CREATE_TIME DATETIME COMMENT '创建时间'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '二级标签表';

CREATE TABLE ARTICLE_LABEL(
                              LABEL_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                              ARTICLE_ID BIGINT COMMENT '关联文章ID',
                              PRIMARY_CODE INT COMMENT '关联一级标签编码(个人成长：1 心情随笔：2 技术分享：3 资源分享：4 )',
                              SECONDARY_CODE INT COMMENT '关联二级标签编码(后台：1 前端：2 数据库：3 等)',
                              CREATE_TIME DATETIME COMMENT '创建时间',
                              UPDATE_TIME DATETIME COMMENT '更新时间',
                              UPDATER_ID BIGINT COMMENT '更新人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '文章标签表';

CREATE TABLE ARTICLE(
                        ARTICLE_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                        AUTHOR_ID BIGINT COMMENT '作者ID',
                        TITLE VARCHAR(32) COMMENT '文章标题',
                        CONTENT TEXT COMMENT '内容',
                        PRAISE_AMOUNT INT COMMENT '获赞量',
                        COMMENT_AMOUNT INT COMMENT '评论量',
                        STATUS INT COMMENT '文章状态(草稿：0 已发表：1 )',
                        READ_AMOUNT INT COMMENT '阅读量',
                        CREATE_TIME DATETIME COMMENT '创建时间',
                        UPDATE_TIME DATETIME COMMENT '更新时间',
                        UPDATER_ID BIGINT COMMENT '更新人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '文章表';

CREATE TABLE ARTICLE_COMMENT(
                                COMMENT_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                                FLOOR INT COMMENT '楼层',
                                CONTENT VARCHAR(200) COMMENT '内容',
                                ARTICLE_ID BIGINT COMMENT '评论文章ID',
                                FROM_ID BIGINT COMMENT '评论用户ID',
                                TO_ID BIGINT COMMENT '评论目标用户ID',
                                CREATE_TIME DATETIME COMMENT '创建时间'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '文章评论表';

CREATE TABLE ARTICLE_PRAISE(
                               PRAISE_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                               ARTICLE_ID BIGINT COMMENT '点赞文章ID',
                               USER_ID BIGINT COMMENT '点赞用户ID',
                               CREATE_TIME DATETIME COMMENT '创建时间'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '文章点赞表';

CREATE TABLE TASK(
                     TASK_ID BIGINT PRIMARY KEY COMMENT '主键ID',
                     TYPE INT COMMENT '类型（0：系统，1：用户）',
                     PARAMS JSON COMMENT '参数',
                     EXECUTION_TIME DATETIME COMMENT '执行时间',
                     EXECUTION_STATUS TINYINT(1) COMMENT '执行状态（false：未执行，true：已执行）',
                     JOB_CLASS VARCHAR (32) COMMENT '任务类名',
                     CREATE_TIME DATETIME COMMENT '创建时间',
                     UPDATE_TIME DATETIME COMMENT '更新时间',
                     UPDATER_ID BIGINT COMMENT '更新人ID'
)CHARSET=utf8 ENGINE=InnoDB COMMENT '任务表';