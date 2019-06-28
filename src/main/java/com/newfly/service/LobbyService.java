package com.newfly.service;

import com.newfly.mapper.LobbyMapper;
import com.newfly.pojo.ResultMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LobbyService
{
    @Autowired
    LobbyMapper lobbyMapper;

    public ResultMessage login(String body) {
        String name = "name";
        String password = "password";

        String id = String.valueOf(lobbyMapper.queryByPlayer(name, password));
        int length = id.length();
        return new ResultMessage((short) length, (short) 202, id);
    }
}
