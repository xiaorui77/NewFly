package com.newfly.mapper;

import com.newfly.pojo.Message;

import java.util.List;

public interface MessageMapper
{
    int save(Message message);

    List<Message> queryByFrom(int from);

    List<Message> queryByTarget(int target);
}
