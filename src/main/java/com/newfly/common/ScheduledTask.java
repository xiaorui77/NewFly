package com.newfly.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


@Component
public class ScheduledTask
{
    private static final Logger logger = LoggerFactory.getLogger(ScheduledTask.class);
    private static int count = 0;

    @Scheduled(fixedRate = 3000)
    public void test() {
        count++;
        logger.info("ScheduledTask " + count + " 在线人数：" + count * 2);
    }

}// end
