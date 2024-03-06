create table user
(
    id            bigint                             not null comment '用户编号'
        primary key,
    username      varchar(256)                       null comment '用户名',
    gender        tinyint  default 0                 null comment '性别',
    userAccount   varchar(256)                       null comment '用户账号',
    userPassword  varchar(512)                       not null comment '密码',
    avatarUrl     varchar(1024)                      null comment '用户头像',
    createTime    datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime    datetime default CURRENT_TIMESTAMP null on update CURRENT_TIMESTAMP comment '更新时间',
    userStatus    int      default 0                 not null comment '账号状态，0为正常',
    isDelete      tinyint  default 0                 not null comment '逻辑删除，0为未删除',
    email         varchar(512)                       null comment '邮箱',
    phone         varchar(128)                       null comment '手机号码',
    communityCode varchar(512)                       null comment '社区编号',
    userRole      int      default 0                 not null comment '0代表默认用户'
)
    comment '用户表';