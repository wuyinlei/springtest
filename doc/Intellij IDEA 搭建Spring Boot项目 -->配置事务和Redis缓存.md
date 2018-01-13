# Intellij IDEA 搭建Spring Boot项目 -->配置事务和Redis缓存

标签（空格分隔）： SpringBoot JAVA后台

---

### 一、开始配置事物
类似我们前一篇配置spring-dao的时候,我们可以看见在ssm中配置事物的时候是如下配置的
```
    //这个不需要我们在进行配置  因为当我们@Configuration的时候  会全package扫描的  
 <!-- 扫描service包下所有使用注解的类型 -->
    <context:component-scan base-package="com.ruolan.o2o.service" />

    <!-- 配置事务管理器 -->
    <bean id="transactionManager"
        class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
        <!-- 注入数据库连接池 -->
        <property name="dataSource" ref="dataSource" />
    </bean>

    <!-- 配置基于注解的声明式事务 -->
    <tx:annotation-driven transaction-manager="transactionManager" />
```
因此我们需要创建一个TransactionManagementConfiguration类
```
@Configuration
//首先使用注解@EnableTransactionManagement 开启事务支持后
//在Service方法上添加@Transactional就可以
@EnableTransactionManagement
public class TransactionManagementConfiguration implements TransactionManagementConfigurer {

    @Autowired
    //注入DataSourceConfiguration里面的dataSource 通过createDataSource获取
    private DataSource dataSource;

    /**
     * 关于事务管理  需要返回PlatformTransactionManager
     *
     * @return PlatformTransactionManager
     */
    @Override
    public PlatformTransactionManager annotationDrivenTransactionManager() {
        return new DataSourceTransactionManager(dataSource);
    }
}
```

### 二、配置redis缓存
>我们下面的两个类就是相关的redis缓存工具类

* [JedisPoolWriper][1]
* [JedisUtil][2]

在application.peoperties文件中添加以下关于redis的相关配置
```
#redis缓存的相关配置
#host
redis.hostname=127.0.0.1
#redis端口号  默认6379
redis.port=6379
redis.database=0
redis.pool.maxActive=600
redis.pool.maxIdle=300
redis.pool.maxWait=3000
redis.pool.testOnBorrow=true
```

类似数据连接池的创建,创建RedisConfiguration配置类
```

@Configuration
public class RedisConfiguration {

    @Value("${redis.hostname}")
    private String hostname;

    @Value("${redis.port}")
    private int port;

    @Value("${redis.database}")
    private int database;

    @Value("${redis.pool.maxActive}")
    private int maxActive;

    @Value("${redis.pool.maxIdle}")
    private int maxIdle;

    @Value("${redis.pool.maxWait}")
    private long maxWait;

    @Value("${redis.pool.testOnBorrow}")
    private boolean testOnBorrow;

    @Autowired
    private JedisPoolConfig jedisPoolConfig;

    @Autowired
    private JedisPoolWriper jedisPoolWriper;

    @Autowired
    private JedisUtil jedisUtil;

    @Bean(name = "jedisPoolConfig")
    public JedisPoolConfig createJedisPoolConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        //控制一个pool可以分配的多少个jedis实例
        jedisPoolConfig.setMaxTotal(maxActive);
        //连接池中最多可空闲的maxIdle个连接  这里取值20
        //表示即使没有数据库连接时候依然可以保持20个空闲的连接  而不被清除 随时处于待命状态
        jedisPoolConfig.setMaxIdle(maxIdle);
        //最大等待时间:当没有可以用的连接的时候  连接池等待连接被归还的最大时间(以毫秒技术)  超过时间则抛出异常
        jedisPoolConfig.setMaxWaitMillis(maxWait);
        //在获取连接的时候检查有效性
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        return jedisPoolConfig;
    }

    @Bean(name = "jedisPoolWriper")
    public JedisPoolWriper createJedisPoolWriper() {
        JedisPoolWriper jedisPoolWriper = new JedisPoolWriper(jedisPoolConfig, hostname, port);
        return jedisPoolWriper;
    }

    /**
     * 创建Redis工具类  封装好Redis的连接以进行相关操作
     *
     * @return
     */
    @Bean(name = "jedisUtil")
    public JedisUtil createJedisUtil() {
        JedisUtil jedisUtil = new JedisUtil();
        jedisUtil.setJedisPool(jedisPoolWriper);
        return jedisUtil;
    }

    @Bean(name = "jedisKeys")
    public JedisUtil.Keys createJedisKeys() {
        JedisUtil.Keys jedisKeys = jedisUtil.new Keys();
        return jedisKeys;
    }

    @Bean(name = "jedisStrings")
    public JedisUtil.Strings createJedisStrings() {
        JedisUtil.Strings jedisStrings = jedisUtil.new Strings();
        return jedisStrings;
    }

    @Bean(name = "jedisLists")
    public JedisUtil.Lists createJedisLists() {
        JedisUtil.Lists jedisLists = jedisUtil.new Lists();
        return jedisLists;
    }

    @Bean(name = "jedisSets")
    public JedisUtil.Sets createJedisSets() {
        JedisUtil.Sets jedisSets = jedisUtil.new Sets();
        return jedisSets;
    }

    @Bean(name = "jedisHash")
    public JedisUtil.Hash createJedisHash() {
        JedisUtil.Hash jedisHash = jedisUtil.new Hash();
        return jedisHash;
    }
}

```
上述两个类(事务和Redis配置类)的目录结构图如下图所示:
![image.png](http://upload-images.jianshu.io/upload_images/1316820-c096b8408158011e.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

我们在service里面这样使用Redis缓存,代码如下:
```
 @Autowired
    private JedisUtil.Strings jedisStrings;
    
    
    // Redis的安装和配置      启动之后再来运行这个是可以的  也就是走缓存了
    //    https://www.jianshu.com/p/6b5eca8d908b

    
    @Autowired
    private JedisUtil.Keys jedisKeys;

    @Autowired
    private AreaDao areaDao;

    private static String AREALISTKEY = "arealist";

    @Override
    public List<Area> getAreaList() throws IOException {
        String key = AREALISTKEY;
        List<Area> areaList = null;
        ObjectMapper mapper = new ObjectMapper();
        //判断是否有缓存
        if (!jedisKeys.exists(key)) {
            //没哟缓存  则查询数据库
            areaList = areaDao.queryArea();
            String jsonString = mapper.writeValueAsString(areaList);
            jedisStrings.set(key, jsonString);
        } else {
            //有缓存  这个时候从缓存中通过key获取到缓存的数据  然后转换为我们需要的数据
            String jsonString = jedisStrings.get(key);
            JavaType javaType = mapper.getTypeFactory()
                    .constructParametricType(ArrayList.class, Area.class);
            areaList = mapper.readValue(jsonString, javaType);
        }
        return areaList;
    }

```

### 测试service
```

//测试类加入如下的两个注解
@RunWith(SpringRunner.class)
@SpringBootTest
public class AreaServiceTest {

    @Autowired
    private AreaService areaService;

    // Redis的安装和配置      启动之后再来运行这个是可以的  也就是走缓存了
    //    https://www.jianshu.com/p/6b5eca8d908b

    @Test
    public void testGetAreaList() throws IOException {
        List<Area> areaList = areaService.getAreaList();
        System.out.println("查询出来的地区集合的个数是:" + areaList.size());
    }
}

```
我们看下输入的结果:

![image.png](http://upload-images.jianshu.io/upload_images/1316820-1f218c7f6daf5346.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

### 三、遇到的bug
>刚开始没了解redis是什么,只是感觉是是一个缓存,并没想到需要各种配置-->导致没有没有开启redis--server,在测试的时候会报
`Could not get a resource from the pool`的错误,在配置好redis的相关之后并启动之后再次运行时可以正常运行的。

### springboot测试项目地址
* https://github.com/wuyinlei/springtest
* https://github.com/wuyinlei/springtest
* https://github.com/wuyinlei/springtest




#### 相关链接文章
* [Mac环境下安装Redis][3]


  [1]: https://github.com/wuyinlei/springtest/blob/master/src/main/java/com/ruolan/springtest/cache/JedisPoolWriper.java
  [2]: https://github.com/wuyinlei/springtest/blob/master/src/main/java/com/ruolan/springtest/cache/JedisUtil.java
  [3]: https://www.jianshu.com/p/6b5eca8d908b
