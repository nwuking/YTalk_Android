package com.nwuking.ytalk;

/**
 * @desc   通信协议号定义
 *
 */

//为了和服务器保持一致，这里所有常量都使用下划线连接的小写形式
public class MsgType {
    public static final int msg_type_unknown          = 0;
    public static final int msg_networker_disconnect  = 100;
    public static final int msg_type_heartbeart       = 1000;
    public static final int msg_type_register         = 1001;
    public static final int msg_type_login            = 1002;
    public static final int msg_type_getfriendlist    = 1003;
    public static final int msg_type_finduser         = 1004;
    public static final int msg_type_operatefriend    = 1005;
    public static final int msg_type_userstatuschange = 1006;
    public static final int msg_type_updateuserinfo   = 1007;
    public static final int msg_type_modifypassword   = 1008;
    public static final int msg_type_creategroup      = 1009;
    public static final int msg_type_getgroupmembers  = 1010;
    public static final int msg_type_chat             = 1100;        //单聊消息
    public static final int msg_type_multichat        = 1101;        //群发消息
    public static final int msg_type_kickuser         = 1102;        //被踢下线

    // YTalk服务端
    public static final int MSG_ORDER_UNKNOW = 0;
    public static final int MSG_ORDER_REGISTER = 1;                             // 注册
    public static final int MSG_ORDER_LOGIN = 2;                              // 登录
    public static final int MSG_ORDER_KICK = 3;                                 // 踢人
    public static final int MSG_ORDER_HEARTBEAT = 4;                            // 心跳包
    public static final int MSG_ORDER_FIND_FRIEND = 5;                          // 查找好友
    public static final int MSG_ORDER_RESPONSE_FRIEND_APPLY = 6;                // 回复好友申请
    public static final int MSG_ORDER_ADD_FRIEND = 7;                           // 加好友或者群
    public static final int MSG_ORDER_DEL_FRIEND = 8;                           // 删好友
    public static final int MSG_ORDER_CHAT = 9;                                 // 单聊
    public static final int MSG_ORDER_GROUP_CHAT = 10;                           // 群聊
    public static final int MSG_ORDER_CREATE_GROUP = 11;                         // 创建群
    public static final int MSG_ORDER_USER_STATUS_CHANGE = 12;                   // 用户在线状态改变
    public static final int MSG_ORDER_USER_INFO_UPDATE = 13;                     // 用户信息更新
    public static final int MSG_ORDER_CHANGE_PASSWORD = 14;                      // 修改密码
    public static final int MSG_ORDER_TEAM_INFO_UPDATE = 15;                     // 好友分组信息更新
    public static final int MSG_ORDER_MOVE_FRIEND_TO_OTHER_TEAM = 16;            // 将好友移至其它分组
    public static final int MSG_ORDER_FRIEND_REMARKS_CHANGE = 17;                // 修改好友备注
    public static final int MSG_ORDER_GET_FRIENDS_LIST = 18;                     // 获取好友列表
    public static final int MSG_ORDER_GET_GROUP_MEMBER = 19;                     // 获取指定的群成员信息
    public static final int MSG_ORDER_ERROR  = 20;                               // 指令错误

    //错误码
    public static final int ERROR_CODE_SUCCESS               = 0;
    public static final int ERROR_CODE_UNKNOWNFAILED         = 1;
    public static final int ERROR_CODE_REGISTERFAILED        = 100;
    public static final int ERROR_CODE_REGISTERED            = 101;
    public static final int ERROR_CODE_UNREGISTER            = 102;
    public static final int ERROR_CODE_INCORRECTPASSWORD     = 103;
    public static final int ERROR_CODE_UPDATEUSERINFOFAILED  = 104;
    public static final int ERROR_CODE_MODIFYPASSWORDFAILED  = 105;
    public static final int ERROR_CODE_CREATEGROUPFAILED     = 106;
}