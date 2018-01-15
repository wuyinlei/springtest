# Redis的相关命令

标签（空格分隔）： JAVA后台 Redis

---

#### redis命令-String命令
>字符串类型是Redis中最为基础/常用的存储类型,字符串在Redis中是**二进制安全**的,这意味着该类型**存入和获取的数据相同**,在Redis中字符串类型的Value最多可以容纳的数据长度是512MB。

##### 二进制安全和数据安全是没有关系的
* MySql是关系型数据库,二进制不安全
    * ![image.png](http://upload-images.jianshu.io/upload_images/1316820-4004de68b33daccd.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
* redis非关系型数据库
    * ![image.png](http://upload-images.jianshu.io/upload_images/1316820-c4c3072e9d432623.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### Redis的String命令

* 赋值命令(map,put)
    * `set key value`   
    * key值一样,value后面的会覆盖前面的value

* 取值名(map.get)
    * `get key`
    * 获取key的value,如果与该key关联的value不是String类型,redis将返回错误信息,因为get命令只能用于获取String value:如果该key不存在,返回(nil)
* 删除(map.remove)
    * `del key`
    * 返回值是数据类型,返回几表示删了几条数据
* `getset key` vaule:先获取该key的值,然后在设置key的值
* `incr key`
    * 将制定的key的value原子性的递增
        * 1、如果该key不存在,其初始化为0,在incr之后其值为1
        * 2、如果value的值不能转成整形,如hello,该操作将执行失败并返回相应的错误信息
        * 3、相当于++
* `decr key`
    * 将指定的key的value原子性的递减1
        * 1、如果该key不存在,其初始化为0,在incr之后其值为-1
        * 2、如果value的值不能转成整形,如hello,该操作将执行失败并返回相应的错误信息
        * 3、相当于i--
* `append key value`
    * 拼凑字符串
        * 1、如果该key存在,则在原有的value后追加至
        * 2、如果该key不存在,则重新创建一个key/value

* String类型使用环境
    * 主要用于保存json格式的字符串

![image.png](http://upload-images.jianshu.io/upload_images/1316820-0174713c2b7c3e74.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

#### Redis的Hash命令
>Redis中的Hash类型可以看成具有String Key和String Value的map容器,所以该类型非常适合用于存储值对象信息,比如username、password和Age等,如果Hash中包含很少的字段,那么该类型的数据也将仅占有很少的磁盘空间,每一个Hash可以存储4294967295个键值对
**Hash--->{username:"张三",age:"30"} ---->对应JavaBean**

* 赋值操作
    * `hset key filed value `
        * 为制定的key设定filed/value对(键值对)
        * `hset hash1 uname zhangsan`
        
    * `hmset key field value [filed2 value2...]`
        * 设置key重点额讴歌filed/value 
        * `hmset hash2 uname zhangsan age 18`
        
* 取值操作
    * `hget key field`
        * 返回指定的key重点额field的值
        * ``
        
    * `hmget key fields`
        * 获取key中的多个field的值
        * `hmget hash2 uname age`
        
    * `hgetall key`
        * 获取key中所有的field-value
        * `hgetall hash2`
        
* 删除操作
    * hdel key field [field...] 
        * 1、可以删除一个或者多个字段,返回值是被删除的字段个数
        * 2、如果没有字段了,那么会把这个key给删除了
        
    * del key
        * 删除整个list
        
![image.png](http://upload-images.jianshu.io/upload_images/1316820-7d74b39e8c2b1a2d.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)



### Redis-LinkedList
* Java List:
    * 数组:ArrayList
    * 链表:LinkedList

#### 赋值
* `lpush key values[value1 value2...]`
    * 1、在指定的key所关联的list的头部插入所有的values
    * 2、如果key不存在,该命令在插入之前创建一个与该key关联的空链表
    * 3、之后再向该链表的头部插入数据
    * 4、插入成功,返回元素的个数
* `rpush key values[value1 value2...]`
    * 在该list的尾部添加元素 

```
lpush list1  a b c d
rpush list2  a b c d 
得到的结果如下
key        value
list1      [d c b a]
list2      [a b c d]
```
            
#### 取值
* `lrange key start end` 
    * 获取链表中从start到end的元素的值
    * start、end从0开始计数,也可以为负数
    * 若为-1则表示链表尾部的元素,-2则表示倒数第二个,依次类推
    
```
key       value
list1     [d,c,a,b]
list2     [a,b,c,d]

#查询所有元素
lrange list1 0 3
lrange list1 0 -1
#只显示list1中的c、a元素
lrange list1 1 2
```

#### 删值
* `lpop key`
    * 返回并弹出指定的key关联的链表中的第一个元素,即头部元素
    * 如果该key不存在,返回nil
    * 如果key存在,则返回链表的头部元素
    
* `rpop key`
    * 从尾部弹出元素 

* `lrem key count value`
    * 1、删除count个值为value的元素
    * 2、如果count大于0,从头向尾遍历并删除count个值为value的元素
    * 3、如果count小于0,则从尾向头遍历并删除
    * 4、如果count等于0,则删除链表中所有等于value的元素
       
``` 
得到的结果如下
key        value
list1      [d c b a]
list2      [a b c d]
#从list1中删除元素d
list1      [c b a]
```

#### 获取list的个数
* `llen key`

#### 扩展命令
* `lpoplpush resource destination`
    * 将链表中的尾部元素弹出并添加到头部
    
### set相关
* 无序、不重复

#### 赋值
* **`sadd key values [value1 value2...]`**
    * 1、向set中添加数据 
    * 2、如果该key的值已经有了则不会重复添加
    
#### 取值
* **`srem key members [member1、member2...]`**
    * 删除set中指定的成员 
    * 如果成员不存在则不删除


#### 删值
* **`smembers key`**
    * 获取set中所有成员

* **`sismember key member`**
    * 1、判断参数中指定的成员是否在该set中
    * 2、1表示存在,0表示不存在
    * 3、无论集合中有多少个元素都可以急速的返回结果
    
#### 运算
* 差集运算
    * `sdiff key1 key2 ...`返回key1  key2中相差的成员,而且与key的顺序相关,即返回差集
        * 属于key1集合 但是不属于key2集合 
    
* 交集运算
    * `sinter key1 key2 key3...`返回交集
    
* 并集运算
    * `sunion key1 key2 key3..`
    * 返回并集

#### 扩展命令
* `scard key`
    * 获取set中成员的数量

* `srandmember key`随机返回set中的一个成员
* `sdiffstore destination key1 key2...`
    * 将key1、key2相差的成员存贮在destination上
* `sinterstore destination key1 key2...`
    * 将返回的交集存储在 destination上
* `sunionstore destination key[key...]`
    * 将返回的并集存储在destination上

### 有序set集合
>有序不重复

#### 赋值
* `zadd key score member score2 member2 ...`
    * 1、将所有成员以及该成员的分数存放到sorted-set中,
    * 2、如果该元素已经存在则会用新的分数替换原有的分数
    * 3、返回值是新加入到集合中的元素个数,不包含之前已经存在的元素

#### 获得元素
* `zscore key member`
    * 返回指定成员的分数

* `zcard key`
    * 获取集合中的成员数量

#### 删除元素
* `zrem key member[member...]`
    * 1、移除集合中指定的成员
    * 2、可以指定多个成员

#### 查询范围
* `zrange key start end [withscores]`
    * 1、获取集合中脚标为start-end的成员
    * 2、[withscores]参数表明返回的成员包含其分数

* `zrevrange key start stop [withscores]` 
    * 照元素分数从大到小的顺序返回元素索引从start到stop之间的所有元素


### 通用命令
* `keys pattern`
    * 1、获取所有与pattern匹配的key
    * 2、返回所有与key匹配的keys
    * 3、*表示任意一个或者多个字符
    * 4、?表示任意一个字符
    
* `del key1 key2 ...`
    * 删除之定义的key
    
* `exists key `
    * 判断该key是否存在
        * 1、1代表存在
        * 2、0代表不存在
        
* `rename key newkey`
    * 为当前的key重命名
    
* `expire key 30`
    * 设置过期时间
        * 单位:秒
        
* `ttl key`
    * 1、获取该key所剩的超时时间
    * 2、如果没有设置超时返回-1
    * 3、如果返回-2 表示超时不存在
    
* `type key`
    * 1、获取指定的key的类型
    * 2、该命令将以字符串的格式返回
    * 3、返回的字符串为string、list、set、hash和zset
        * 如果key不存在返回none
