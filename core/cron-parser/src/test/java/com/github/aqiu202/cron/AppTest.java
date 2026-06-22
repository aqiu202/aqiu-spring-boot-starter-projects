package com.github.aqiu202.cron;


import com.github.aqiu202.cron.core.CustomCronExpression;
import com.github.aqiu202.cron.core.NewCronExpression;
import com.github.aqiu202.cron.quartz.DateQuartzCronExpression;
import org.junit.Test;

import java.time.Clock;
import java.time.ZonedDateTime;
import java.util.Date;

/**
 * Unit test for simple App.
 */
public class AppTest {

    @Test
    public void test() {
        testCrons(
                "0 0 10,15,16 * * ?",
                "0 0/30 9-17 * * ?",
                "0 0 12 ? * WED",
                "0 0 12 * * ?",
                "0 15 10 ? * *",
                "0 15 10 * * ?",
                "0 15 10 * * ?",
                "0 * 14 * * ?",
                "0 0/5 14 * * ?",
                "0 0/5 14,18 * * ?",
                "0 0-5 14 * * ?",
                "0 10,44 14 ? 3 MON-FRI",
                "0 10,44 14 ? 3 WED",
                "0 15 10 ? * MON-FRI",
                "0 15 10 15 * ?",
                "0 15 10 L * ?",
                "0 15 10 ? * 6L",
                "0 15 10 ? * 6L 2032-2035",
                "0 15 10 29 1,3,2 ? 2134-2137"
        );
    }

    private void testCrons(String... crons) {
        for (String cron : crons) {
            Date now = new Date();
            Clock clock = Clock.systemDefaultZone();
            //基于Quartz算法的Cron表达式解析（基于Date）
            DateQuartzCronExpression cronExpression = new DateQuartzCronExpression(cron);
            // 基于内置算法的Cron表达式解析（基于Date）
            CustomCronExpression customCronExpression = new CustomCronExpression(cron);
            // 基于内置算法的Cron表达式解析（基于ZonedDateTime）
            NewCronExpression newCronExpression = new NewCronExpression(cron);
            Date quartzComputedVal = cronExpression.nextExecution(now);
            Date myComputedVal = customCronExpression.nextExecution(now);
            ZonedDateTime zonedDateTime = newCronExpression.nextExecution(clock);
            System.out.printf("表达式%s的计算结果是否一致：%s%n", cron, quartzComputedVal.getTime() == myComputedVal.getTime() &&
                    myComputedVal.getTime() / 1000 == zonedDateTime.toEpochSecond());
        }
    }
}
