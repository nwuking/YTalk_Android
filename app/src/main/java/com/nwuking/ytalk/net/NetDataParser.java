package com.nwuking.ytalk.net;

public class NetDataParser {
    public int      mCmd;
    public int      mSeq;
    public String   mJson;
    //存储额外数据的两个参数，如存储聊天消息的senderid(消息接受者), targetid(消息发送者)
    public int      mArg1;
    public int      mArg2;
    public String   mArg3;
}
