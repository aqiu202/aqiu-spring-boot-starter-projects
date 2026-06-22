# Cron Parser
> 一个轻量级的Cron表达式解析器，内置了对Date和Java8新的Time API的支持，
> Cron解析算法完全自主实现，也内置了Quartz的实现作为参考和验证，
> 用户可以基于自己的需求选择不同的算法去解析自己的Cron表达式

### 具体的使用案例可以参考测试用例
```java
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
                "0 15 10 ? * 6L 2032-2035"
        );
    }

    private void testCrons(String... crons) {
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
```