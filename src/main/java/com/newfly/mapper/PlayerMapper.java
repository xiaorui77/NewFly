package com.newfly.mapper;

import com.newfly.pojo.Player;
import com.newfly.pojo.Task;

public interface PlayerMapper
{
    Player login(Player player);

    Task queryTask(String id);
}
