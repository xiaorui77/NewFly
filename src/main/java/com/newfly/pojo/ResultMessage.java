package com.newfly.pojo;

public class ResultMessage
{
    private short length;
    private short type;
    private String body;


    public ResultMessage() {
    }

    public ResultMessage(int type, String body) {
        this.type = (short) type;
        this.body = body;
    }

    public ResultMessage(int length, int type, String body) {
        this.length = (short) length;
        this.type = (short) type;
        this.body = body;
    }

    public short getLength() {
        return this.length;
    }

    public short getType() {
        return this.type;
    }

    public String getBody() {
        return this.body;
    }

    public void setLength(int length) {
        this.length = (short) length;
    }

    public void setType(int type) {
        this.type = (short) type;
    }

    public void setBody(String body) {
        this.body = body;
    }

}
