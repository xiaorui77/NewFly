package com.newfly.pojo;

import java.sql.Timestamp;

public class Message
{
    private int id;
    private int from;
    private int target;
    private String title;
    private String body;
    Timestamp create_time;

    public Message() {
    }

    public Message(int from, int target, String body) {
        this.from = from;
        this.target = target;
        this.body = body;
    }

    public int getId() {
        return id;
    }

    public int getFrom() {
        return from;
    }

    public int getTarget() {
        return target;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Timestamp getCreate_time() {
        return create_time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setCreate_time(Timestamp create_time) {
        this.create_time = create_time;
    }
}
