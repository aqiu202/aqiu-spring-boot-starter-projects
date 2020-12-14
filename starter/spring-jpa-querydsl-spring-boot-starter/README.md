# spring-jpa-plus-spring-boot-starter
### 描述
基于spring data jpa的功能扩展，整合了queryDSL插件，更灵活和方便。 

### 引用
maven坐标 
```xml
<dependency>
    <groupId>com.github.aqiu202</groupId>
    <artifactId>spring-jpa-plus-spring-boot-starter</artifactId>
</dependency>
```
maven插件配置
```xml
<build>
    <plugins>
        <plugin>
            <groupId>com.mysema.maven</groupId>
            <artifactId>apt-maven-plugin</artifactId>
            <executions>
                <execution>
                    <goals>
                        <goal>process</goal>
                    </goals>
                    <configuration>
                        <outputDirectory>target/generated-sources/java</outputDirectory>
                        <processor>com.querydsl.apt.jpa.JPAAnnotationProcessor</processor>
                    </configuration>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```
【注】：配置好maven以后，在编码之前需要执行一下maven
compile，不然queryDSL生成的QueryEntity没法使用，实体类 默认会生成在项目的
target/generated-sources/java 目录里。 

### 使用
##### 启用spring-jpa-plus
将spring-data-jpa的注解 @EnableJpaRepositories 注解更换为
@EnableBaseJpaRepositories 注解即可。
```java
@Documented
@Retention(RUNTIME)
@Target(TYPE)
@EnableJpaRepositories
@Import({JpaDSLSelector.class})
public @interface EnableBaseJpaRepositories {

    //...
    //是否开启SqlExecutor和JPAQueryExecutor功能
    boolean enableExecutors() default true;
    //是否开启乐观锁重试机制
    boolean enableRetry() default true;
}
``` 

##### Repository使用
所有的Repository继承自BaseRepository

例如：
``` java
public interface UserRepository extends BaseRepository<User, Integer>{
    
}
```
##### spring-jpa-plus的封装方法
Repository继承自BaseRepository后，它相比基础的Spring-data-jpa的JpaRepository多的方法，
可查看JpaSpecificationExecutor和QuerydslPredicateExecutor。

##### 除了Repository外，其他的功能封装
spring data
jpa是用hibernate实现的，而hibernate常被人诟病的就是自动生成的SQL太长影响性能，
全自动操作但不够灵活，学习成本太高（太难了）等等原因，这也就是为什么很多人知道hibernate
后都转去学习相对简单的Mybatis，因为Mybatis是面向SQL的所以简单易用，但是hibernate还是
有一些Mybatis不具有的优点，比如平台的可移植性、更好的二级缓存和更好的对象维护和缓存等。
所以我们就想，能不能把Mybatis的一些优点或者特性，我们借鉴一下，封装一下呢？于是我找到了一个
插件 queryDSL 它的使用我们可以查看它的
[官方文档](http://www.querydsl.com/static/querydsl/latest/reference/html/index.html)
但是在钻研了它一阵之后发现它也不是很完美的，比如它的分页没有jpa自带的pageable方便，所以又
在它的基础上封装了一点，详情查看JPAQueryExecutor，但是在使用的过程中发现它还是有缺陷的它对
一些函数的使用是有方言（Dialect）限制的，如果在方言中没有注册该函数它就会报错，所以又封装了
SqlExecutor用来去拼写SQL，通过SQL去操作数据。

##### 使用
基础的queryDSL-JPA插件的使用可以查看
[教程](https://www.jianshu.com/p/69dcb1b85bbb)，其中教程中的QueryDslPredicateExecutor的功能
我们已经封装在了BaseRepository中，JPAQueryFactory（JPAQueryExecutor扩展了JPAQueryFactory的部分功能）
已经自动装配，而且装配了自定义的 SqlExecutor，执行自定义的SQL和HQL语句，下面我们用例子来分别介绍一下：

1. JPAQueryExecutor的扩展方法selects（用于自定义返回结果和分页的封装）

```java
public class JPAQueryExecutor extends JPAQueryFactory {
    
    // ...
    
    public SimpleQueryBuilder<?> selects(Expression<?>... expressions) {
        return new SimpleQueryBuilder<>(this.query(), expressions);
    }

    public SimpleQueryBuilder<?> selects(List<Expression<?>> expressions) {
        return new SimpleQueryBuilder<>(this.query(), expressions);
    }

    public <T> SimpleQueryBuilder<T> selects(Expression<T> expression) {
        return new SimpleQueryBuilder<>(this.query(), expression);
    }
    
    public SimpleQueryBuilder<?> selectsDistinct(Expression<?>... expressions) {
        return selects(expressions).distinct();
    }

    public SimpleQueryBuilder<?> selectsDistinct(List<Expression<?>> expressions) {
        return selects(expressions).distinct();
    }

    public <T> SimpleQueryBuilder<T> selectsDistinct(Expression<T> expression) {
        return selects(expression).distinct();
    }
}
```
demo: 
```java
public class OrderService{
    @Autowired
    private JPAQueryExecutor jpaQueryExecutor;
    
    public PageResult<OrderSimpleOutVo> simpleList(String status) {
        QOrderForm f = QOrderForm.orderForm;
        QOrderRfid r = QOrderRfid.orderRfid;
        //构造JPA queryDsl的分页参数
        DslPageable pageable = PageParam.of(1, 20, DslSort.desc(f.id));
        BooleanBuilder query = new BooleanBuilder();
        if (StringUtils.hasText(status)) {
            query.and(f.status.in(Arrays.stream(status.split(",")).map(Byte::valueOf)
                    .collect(Collectors.toList())));
        }
        QueryResults<OrderSimpleOutVo> info = jpaQueryExecutor
                .selects(f.orderNo, f.num, f.status,
                        r.id.count().as("inUse"))
                .as(OrderSimpleOutVo.class).paging(pageable).build().from(f).leftJoin(r)
                .on(r.orderNo.eq(f.orderNo)).where(query).groupBy(f.id).fetchResults();
        return PageResult.of(info);
    }
}
```

2. SqlExecutor封装用来执行自定义SQL 

demo:
```java
public class OrderService{
    @Autowired
    private SqlExecutor sqlExecutor;
    public PageResult<OrderSimpleOutVo> simpleList(String status) {
        ParamMap param = new ParamMap();
            SqlBuilder sql = BaseSqlBuilder.select("f.order_no","f.num","f.status", "count(r.id) inUse")
                    .from("order_form").as("f").leftJoin("order_rfid").as("r").on("r.order_no").eq("f.order_no");
            if (StringUtils.hasText(status)) {
                sql.where("f.status").inParam("status");
                param.set("status", status);
            }
            //构造JPA的分页参数
            Pageable pageable = PageParam.of(1, 20, Sort.by(Order.desc("f.id")));
            Page<OrderSimpleOutVo> info = sqlExecutor.of(sql, param)
                .unwrap(OrderSimpleOutVo.class).pagingList(pageable);
        return PageResult.of(info);
    }
}
```
