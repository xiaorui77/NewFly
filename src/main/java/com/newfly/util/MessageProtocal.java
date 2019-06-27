package com.newfly.util;

import com.sun.org.apache.bcel.internal.classfile.ConstantValue;

import java.util.Arrays;

public class MessageProtocal
{
    private int head_data;

    /**
     * 协议类型
     * 1	登录相关协议（连接服务器检测,登录，注册...）
     * 2	心跳协议
     * 3	大厅内协议（查看战绩，排行，查找，添加好友等...）
     * 4	游戏（创建房间,解散房间等）
     */
    private byte type;

    // 协议号
    private int protocloNumber;

    // 消息长度
    private int contentLength;

    // 消息内容
    private byte[] content;


    public MessageProtocal(byte type, int protocloNumber, int contentLength, byte[] content) {
        this.type = type;
        this.protocloNumber = protocloNumber;
        this.contentLength = contentLength;
        this.content = content;
    }


    /*public MessageProtocal(byte type, MsgRsponse rsp) {
        this.type = type;
        this.protocloNumber = rsp.toClassName();
        this.content = JSONObject.toJSONString(rsp).getBytes();
        this.contentLength = content.length;
    }*/


    public int getHead_data() {
        return head_data;
    }

    public void setHead_data(int head_data) {
        this.head_data = head_data;
    }

    public byte getType() {
        return type;
    }

    public void setType(byte type) {
        this.type = type;
    }

    public int getProtocloNumber() {
        return protocloNumber;
    }

    public void setProtocloNumber(int protocloNumber) {
        this.protocloNumber = protocloNumber;
    }

    public int getContentLength() {
        return contentLength;
    }

    public void setContentLength(int contentLength) {
        this.contentLength = contentLength;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "SmartCarProtocol [head_data=" + head_data + ", type=" + type +
                ",protocloNumber=" + protocloNumber + ",contentLength="
                + contentLength + ", content=" + Arrays.toString(content) + "]";
    }
}
