# **Redis**

## **Redis基础**

```bash
概念
```

Redis是C语言编写的一个开源的、基于内存的（key—value）数据结构存储器，可以用作数据库、缓存和消息中间件。

```bash
网站
```

官网：https://redis.io/

中文官网：http://www.redis.cn/

```bash
安装
```

1. 下载最新稳定版本
2. 解压文件 tar xzf redis-版本号.tar.gz
3. 编译 进入解压后的文件夹 make

```bash
其他
```

Redis的Server是单线程服务器，基于Event-Loop模式来处理Client的请求。单线程的好处包括：不必考虑线程安全问题。减少线程切换损耗的时间。

默认带有16个数据库，使用第0个。可以使用 select index 进行数据库切换

 ## **Redis 进阶**

### **数据库操作命令**

选择数据库：select index

清空当前数据库：flushdb

清空所有数据库：flushall

### **键操作命令**

当前数据库key的个数：dbsize

显示所有key：keys *  返回值所有的key集合

显示key过期时间：ttl key 返回值秒 ； pttl key 返回值毫秒

设置key过期时间：expire key seconds；pexpire key milliseconds

备注：key的设置不要太长，尽量不要超过1024个字节。数据过大不仅消耗内存，同时降低查询效率。key的设置不要太短，识别度不高。例：user:10:passwd

### **数据结构**

#### **字符串（Strings）**

字符串是一种最基本的Redis值类型。Redis字符串是二进制安全的，这意味着一个Redis字符串能包含任意类型的数据，例如： 一张JPEG格式的图片或者一个序列化的Ruby对象。一个字符串类型的值最多能存储512M字节的内容。

---

---

##### 常用命令

SET：	# 将键`key`设定为指定的“字符串”值。如果key已经保存值了，那么将覆盖原值。同时如果原值有设置过期时间，执行set命令后过期时间失效

从2.6.12版本开始，redis为`SET`命令增加了一系列选项:

- `EX` *seconds* – 设置键key的过期时间，单位时秒
- `PX` *milliseconds* – 设置键key的过期时间，单位时毫秒
- `NX` – 只有键key不存在的时候才会设置key的值
- `XX` – 只有键key存在的时候才会设置key的值

**注意:** 由于`SET`命令加上选项已经可以完全取代[SETNX](http://www.redis.cn/commands/setnx.html), [SETEX](http://www.redis.cn/commands/setex.html), [PSETEX](http://www.redis.cn/commands/psetex.html)的功能，所以在将来的版本中，redis可能会不推荐使用并且最终抛弃这几个命令。

GET：返回`key`的`value`。如果key不存在，返回特殊值`nil`。如果`key`的`value`不是string，就返回错误，因为`GET`只处理string类型的`values`。

STRLEN：返回key的string类型value的长度。如果key对应的非string类型，就返回错误。

APPEND：如果 `key` 已经存在，并且值为字符串，那么这个命令会把 `value` 追加到原来值（value）的结尾。 如果 `key` 不存在，那么它将首先创建一个空字符串的`key`，再执行追加操作，这种情况 [APPEND](http://www.redis.cn/ommands/append.html) 将类似于 [SET](http://www.redis.cn/ommands/set.html) 操作。

GETRANGE：返回key对应的字符串value的子串，这个子串是由start和end位移决定的（两者都在string内）。可以用负的位移来表示从string尾部开始数的下标。所以-1就是最后一个字符，-2就是倒数第二个，以此类推。如果start=0，end=-1，那么相当于GET

SETRANGE：覆盖key对应的string的一部分，从指定的offset处开始，覆盖value的长度。如果offset比当前key对应string还要长，那这个string后面就补0以达到offset。不存在的keys被认为是空字符串，所以这个命令可以确保key有一个足够大的字符串，能在offset处设置value。

MSET：对应给定的keys到他们相应的values上。`MSET`会用新的value替换已经存在的value，就像普通的[SET](http://www.redis.cn/commands/set.html)命令一样。如果你不想覆盖已经存在的values，请参看命令[MSETNX](http://www.redis.cn/commands/msetnx.html)。

MGET：返回所有指定的key的value。对于每个不对应string或者不存在的key，都返回特殊值`nil`。

GETSET：将key对应到value并且返回原来key对应的value。[GETSET](http://www.redis.cn/commands/getset.html)可以和[INCR](http://www.redis.cn/commands/incr.html)一起使用实现支持重置的计数功能。举个例子：每当有事件发生的时候，一段程序都会调用[INCR](http://www.redis.cn/commands/incr.html)给key mycounter加1，但是有时我们需要获取计数器的值，并且自动将其重置为0。这可以通过GETSET mycounter “0”

INCR：对存储在指定`key`的数值执行原子的加1操作。如果指定的key不存在，那么在执行incr操作之前，会先将它的值设定为`0`。如果指定的key中存储的值不是字符串类型或者存储的字符串类型不能表示为一个整数，那么执行这个命令时服务器会返回一个错误(eq:(error) ERR value is not an integer or out of range)。

INCRBY：将key对应的数字加decrement。如果key不存在，操作之前key就会被置为0。如果key的value类型错误或者是个不能表示成数字的字符串就返回错误。

DECR：对key对应的数字做减1操作。如果key不存在，那么在操作之前，这个key对应的值会被置为0。如果key有一个错误类型的value或者是一个不能表示成数字的字符串，就返回错误。

DECRBY：将key对应的数字减decrement。如果key不存在，操作之前，key就会被置为0。如果key的value类型错误或者是个不能表示成数字的字符串，就返回错误。

---

---

##### 示例

```bash
127.0.0.1:6379> set name chenxd
OK
127.0.0.1:6379> get name
"chenxd"
127.0.0.1:6379> strlen name
(integer) 6
127.0.0.1:6379> append name ", hello world"
(integer) 19
127.0.0.1:6379> get name
"chenxd, hello world"
127.0.0.1:6379> getrange name 0 -1
"chenxd, hello world"
127.0.0.1:6379> getrange name 0 5
"chenxd"
127.0.0.1:6379> setrange name 8 H
(integer) 19
127.0.0.1:6379> get name
"chenxd, Hello world"

127.0.0.1:6379> mset name chenxd age 29
OK
127.0.0.1:6379> mget name age
1) "chenxd"
2) "29"
127.0.0.1:6379> mset name chenxd age 28
OK
127.0.0.1:6379> mget name age
1) "chenxd"
2) "28"
127.0.0.1:6379> incr count
(integer) 1
127.0.0.1:6379> get count
"1"
127.0.0.1:6379> incrby count 100
(integer) 101
127.0.0.1:6379> get count
"101"
127.0.0.1:6379> decr count
(integer) 100
127.0.0.1:6379> decrby count 99
(integer) 1
```

#### **列表（Lists）**

Redis列表是简单的字符串列表，按照插入顺序排序。 你可以添加一个元素到列表的头部（左边）或者尾部（右边）。一个列表最多可以包含2<sup>32</sup>-1个元素

---

---

##### 常用命令

LPUSH：将所有指定的值插入到存于 key 的列表的头部。如果 key 不存在，那么在进行 push 操作前会创建一个空列表。 如果 key 对应的值不是一个 list 的话，那么会返回一个错误。可以使用一个命令把多个元素 push 进入列表，只需在命令末尾加上多个指定的参数。元素是从最左端的到最右端的、一个接一个被插入到 list 的头部。

RPUSH：向存于 key 的列表的尾部插入所有指定的值。如果 key 不存在，那么会创建一个空的列表然后再进行 push 操作。 当 key 保存的不是一个列表，那么会返回一个错误。可以使用一个命令把多个元素打入队列，只需要在命令后面指定多个参数。元素是从左到右一个接一个从列表尾部插入。

LRANGE：返回存储在 key 的列表里指定范围内的元素。 start 和 end 偏移量都是基于0的下标，即list的第一个元素下标是0（list的表头），第二个元素下标是1，以此类推。偏移量也可以是负数，表示偏移量是从list尾部开始计数。 例如， -1 表示列表的最后一个元素，-2 是倒数第二个，以此类推。

LINDEX：返回列表里的元素的索引 index 存储在 key 里面。 下标是从0开始索引的，所以 0 是表示第一个元素， 1 表示第二个元素，并以此类推。 负数索引用于指定从列表尾部开始索引的元素。在这种方法下，-1 表示最后一个元素，-2 表示倒数第二个元素，并以此往前推。当 key 位置的值不是一个列表的时候，会返回一个error。请求的对应元素，或者当 index 超过范围的时候返回 nil。

LPOP：移除并且返回 key 对应的 list 的第一个元素。返回第一个元素的值，或者当 key 不存在时返回 nil。

RPOP：移除并返回存于 key 的 list 的最后一个元素。返回最后一个元素的值，或者当 key 不存在的时候返回 nil。

LLEN：返回存储在 key 里的list的长度。 如果 key 不存在，那么就被看作是空list，并且返回长度为 0。 当存储在 key 里的值不是一个list的话，会返回error。

LREM：从存于 key 的列表里移除前 count 次出现的值为 value 的元素。

- count > 0: 从头往尾移除值为 value 的元素。
- count < 0: 从尾往头移除值为 value 的元素。
- count = 0: 移除所有值为 value 的元素。

LTRIM：修剪(trim)或截取一个已存在的 list，这样 list 就会只包含指定范围的指定元素。start 和 stop 都是由0开始计数的， 这里的 0 是列表里的第一个元素（表头），1 是第二个元素，以此类推。

LINSERT：把 value 插入存于 key 的列表中在基准值 pivot 的前面或后面。当 key 不存在时，这个list会被看作是空list，任何操作都不会发生。当 key 存在，但保存的不是一个list的时候，会返回error。

LSET：设置 index 位置的list元素的值为 value。当index超出范围时会返回一个error。

RPOPLPUSH：原子性地返回并移除存储在 source 的列表的最后一个元素（列表尾部元素）， 并把该元素放入存储在 destination 的列表的第一个元素位置（列表头部）。

---

---

##### 示例

```bash
127.0.0.1:6379> lpush list a
(integer) 1
127.0.0.1:6379> lpush list b c
(integer) 3
127.0.0.1:6379> lrange list 0 -1
1) "c"
2) "b"
3) "a"
127.0.0.1:6379> rpush list d e f
(integer) 6
127.0.0.1:6379> lrange list 0 -1
1) "c"
2) "b"
3) "a"
4) "d"
5) "e"
6) "f"
127.0.0.1:6379> lrange list 0 2
1) "c"
2) "b"
3) "a"
127.0.0.1:6379> lindex list 3
"d"
127.0.0.1:6379> lrange list 0 -1
1) "c"
2) "b"
3) "a"
4) "d"
5) "e"
6) "f"
127.0.0.1:6379> lpop list
"c"
127.0.0.1:6379> lrange list 0 -1
1) "b"
2) "a"
3) "d"
4) "e"
5) "f"
127.0.0.1:6379> rpop list
"f"
127.0.0.1:6379> lrange list 0 -1
1) "b"
2) "a"
3) "d"
4) "e"
127.0.0.1:6379> llen list
(integer) 4
127.0.0.1:6379> lpush letter a b c a d e a f g
(integer) 9
127.0.0.1:6379> lrange letter 0 -1
1) "g"
2) "f"
3) "a"
4) "e"
5) "d"
6) "a"
7) "c"
8) "b"
9) "a"
127.0.0.1:6379> lrem letter 2 a
(integer) 2
127.0.0.1:6379> lrange letter 0 -1
1) "g"
2) "f"
3) "e"
4) "d"
5) "c"
6) "b"
7) "a"
127.0.0.1:6379> ltrim letter 0 2
OK
127.0.0.1:6379> lrange letter 0 -1
1) "g"
2) "f"
3) "e"
127.0.0.1:6379> linsert letter before f a
(integer) 4
127.0.0.1:6379> lrange letter 0 -1
1) "g"
2) "a"
3) "f"
4) "e"
127.0.0.1:6379> lset letter 1 c
OK
127.0.0.1:6379> lrange letter 0 -1
1) "g"
2) "c"
3) "f"
4) "e"
127.0.0.1:6379> rpoplpush letter list
"e"
127.0.0.1:6379> lrange letter 0 -1
1) "g"
2) "c"
3) "f"
127.0.0.1:6379> lrange list 0 -1
1) "e"
```

#### **集合（Sets）**

Redis集合是一个无序的字符串合集。你可以以**O(1)** 的时间复杂度（无论集合中有多少元素时间复杂度都为常量）完成 添加，删除以及测试元素是否存在的操作。Redis集合有着不允许相同成员存在的优秀特性。向集合中多次添加同一元素，在集合中最终只会存在一个此元素。实际上这就意味着，在添加元素前，你并不需要事先进行检验此元素是否已经存在的操作。一个Redis列表十分有趣的事是，它们支持一些服务端的命令从现有的集合出发去进行集合运算。 所以你可以在很短的时间内完成合并（union）,求交(intersection), 找出不同元素的操作。一个集合最多可以包含2<sup>32</sup>-1 个key-value键值对

---

---

##### 常用命令

SADD：添加一个或多个指定的member元素到集合的 key中。指定的一个或者多个元素member 如果已经在集合key中存在则忽略.如果集合key 不存在，则新建集合key,并添加member元素到集合key中。如果key 的类型不是集合则返回错误.

SMEMBERS：返回key集合所有的元素。该命令的作用与使用一个参数的[SINTER](http://www.redis.cn/commands/sinter.html) 命令作用相同.

SRANDMEMBER：随机返回key集合中的一个元素。

SISMEMBER：返回成员 member 是否是存储的集合 key的成员。

SCARD：返回集合存储的key的基数 (集合元素的数量)。

SREM：在key集合中移除指定的元素.。如果指定的元素不是key集合中的元素则忽略，如果key集合不存在则被视为一个空的集合，该命令返回0。如果key的类型不是一个集合,则返回错误。

SPOP：从存储在`key`的集合中移除并返回一个或多个随机元素。

SDIFF：返回一个集合与给定集合的差集的元素。

SINTER：返回指定所有的集合的成员的交集。

SUINON：返回给定的多个集合的并集中的所有成员。

---

---

##### 示例

```bash
127.0.0.1:6379> sadd sets a b c d e f g h
(integer) 8
127.0.0.1:6379> smembers sets
1) "b"
2) "f"
3) "h"
4) "e"
5) "d"
6) "a"
7) "c"
8) "g"
127.0.0.1:6379> srandmember sets
"d"
127.0.0.1:6379> srandmember sets 2
1) "b"
2) "h"
127.0.0.1:6379> sismember sets a
(integer) 1
127.0.0.1:6379> sismember sets x
(integer) 0
127.0.0.1:6379> scard sets
(integer) 8
127.0.0.1:6379> srem sets a
(integer) 1
127.0.0.1:6379> smembers sets
1) "b"
2) "f"
3) "h"
4) "e"
5) "d"
6) "c"
7) "g"
127.0.0.1:6379> spop sets
"h"
127.0.0.1:6379> smembers sets
1) "b"
2) "f"
3) "e"
4) "d"
5) "c"
6) "g"
127.0.0.1:6379> spop sets 2
1) "d"
2) "e"
127.0.0.1:6379> smembers sets
1) "b"
2) "f"
3) "c"
4) "g"
127.0.0.1:6379> sadd set1 a b c d
(integer) 4
127.0.0.1:6379> sadd set2 c d e f
(integer) 4
127.0.0.1:6379> sdiff set1 set2
1) "a"
2) "b"
127.0.0.1:6379> sdiff set2 set1
1) "f"
2) "e"
127.0.0.1:6379> sinter set1 set2
1) "d"
2) "c"
127.0.0.1:6379> sunion set1 set2
1) "b"
2) "f"
3) "e"
4) "d"
5) "a"
6) "c"
```

#### **哈希（Hashes）**

Redis Hashes是字符串字段和字符串值之间的映射，所以它们是完美的表示对象（eg:一个有名，姓，年龄等属性的用户）的数据类型。一个拥有少量（100个左右）字段的hash需要 很少的空间来存储，所有你可以在一个小型的 Redis实例中存储上百万的对象。一个哈希最多可以包含2<sup>32</sup>-1 个key-value键值对。

---

---

##### 常用命令

HSET：设置 key 指定的哈希集中指定字段的值。如果 key 指定的哈希集不存在，会创建一个新的哈希集并与 key 关联。如果字段在哈希集中存在，它将被重写。

HGET：返回 key 指定的哈希集中该字段所关联的值。

HMSET：设置 `key` 指定的哈希集中指定字段的值。该命令将重写所有在哈希集中存在的字段。如果 `key` 指定的哈希集不存在，会创建一个新的哈希集并与 `key` 关联

HMGET：返回 `key` 指定的哈希集中指定字段的值。

HGETALL：返回 key 指定的哈希集中所有的字段和值。

HSETNX：只在 `key` 指定的哈希集中不存在指定的字段时，设置字段的值。如果 `key` 指定的哈希集不存在，会创建一个新的哈希集并与 `key` 关联。如果字段已存在，该操作无效果。

HDEL：从 key 指定的哈希集中移除指定的域。在哈希集中不存在的域将被忽略。如果 key 指定的哈希集不存在，它将被认为是一个空的哈希集，该命令将返回0。

HEXISTS：返回hash里面field是否存在。

HKEYS：返回 key 指定的哈希集中所有字段的名字。

HLEN：返回 `key` 指定的哈希集包含的字段的数量。

HVALS：返回 key 指定的哈希集中所有字段的值。

---

---

##### 示例

```bash
127.0.0.1:6379> hset hashes name chenxd
(integer) 1
127.0.0.1:6379> hget hashes name
"chenxd"
127.0.0.1:6379> hmset hashes age 29 hobby play
OK
127.0.0.1:6379> hmget age hobby
1) (nil)
127.0.0.1:6379> hmget hashes age hobby
1) "29"
2) "play"
127.0.0.1:6379> hgetall hashes
1) "name"
2) "chenxd"
3) "age"
4) "29"
5) "hobby"
6) "play"
127.0.0.1:6379> hsetnx hashes name chenxd
(integer) 0
127.0.0.1:6379> hdel hashes hobby
(integer) 1
127.0.0.1:6379> hgetall hashes
1) "name"
2) "chenxd"
3) "age"
4) "29"
127.0.0.1:6379> hexists hashes name
(integer) 1
127.0.0.1:6379> hexists hashes hobby
(integer) 0
127.0.0.1:6379> hkeys hashes
1) "name"
2) "age"
127.0.0.1:6379> hlen hashes
(integer) 2
127.0.0.1:6379> hvals hashes
1) "chenxd"
2) "29"
```

#### **有序集合（Sorted sets）**

Redis有序集合和Redis集合类似，是不包含 相同字符串的合集。它们的差别是，每个有序集合 的成员都关联着一个评分，这个评分用于把有序集 合中的成员按最低分到最高分排列。

---

-----

##### 常用命令

ZADD：将所有指定成员添加到键为`key`有序集合（sorted set）里面。 添加时可以指定多个分数/成员（score/member）对。 如果指定添加的成员已经是有序集合里面的成员，则会更新改成员的分数（scrore）并更新到正确的排序位置。如果`key`不存在，将会创建一个新的有序集合（sorted set）并将分数/成员（score/member）对添加到有序集合，就像原来存在一个空的有序集合一样。如果`key`存在，但是类型不是有序集合，将会返回一个错误应答。分数值是一个双精度的浮点型数字字符串。`+inf`（正无穷）和`-inf`（负无穷）都是有效值。

ZRANGE：返回存储在有序集合`key`中的指定范围的元素。 返回的元素可以认为是按得分从最低到最高排列。 如果得分相同，将按字典排序。

ZCARD：返回key的有序集元素个数。

ZCOUNT：返回有序集key中，score值在min和max之间(默认包括score值等于min或max)的成员。

ZRANK：返回有序集key中成员member的排名。排名从0开始

ZREM：从排序集合中删除一个或多个成员。

ZSCORE：返回有序集key中，成员member的score值。

----

----

##### 示例

```bash
127.0.0.1:6379> zadd myset 1 "one"
(integer) 1
127.0.0.1:6379> zadd myset 2 "two"
(integer) 1
127.0.0.1:6379> zadd myset 3 "three"
(integer) 1
127.0.0.1:6379> zrange myset 0 -1
1) "one"
2) "two"
3) "three"
127.0.0.1:6379> zcard myset
(integer) 3
127.0.0.1:6379> zrank myset two
(integer) 1
127.0.0.1:6379> zrank myset one
(integer) 0
127.0.0.1:6379> zrem myset two
(integer) 1
127.0.0.1:6379> zrange myset 0 -1
1) "one"
2) "three"
127.0.0.1:6379> zscore myset three
"3"
```

#### **地理空间索引（Geospatial indexes）**

Redis地理空间索引用于存储经纬度坐标

-------

----------

##### 常用命令

GEOADD：将指定的地理空间位置（纬度、经度、名称）添加到指定的`key`中。

- 有效的经度从-180度到180度。
- 有效的纬度从-85.05112878度到85.05112878度。

GEODIST：返回两个给定位置之间的距离。如果用户没有显式地指定单位参数， 那么 `GEODIST` 默认使用米作为单位。`GEODIST` 命令在计算距离时会假设地球为完美的球形， 在极限情况下， 这一假设最大会造成 0.5% 的误差。

GEOHASH：返回一个或多个位置元素的 [Geohash](https://en.wikipedia.org/wiki/Geohash) 表示。

GEOPOS：从`key`里返回所有给定位置元素的位置

GEORADIUS：以给定的经纬度为中心， 返回键包含的位置元素当中， 与中心的距离不超过给定最大距离的所有位置元素。

GEORADIUSBYMEMBER ：指定成员的位置被用作查询的中心。

---------

--------

##### 示例

```bash
127.0.0.1:6379> geoadd city 116.23128 40.22077 beijing
(integer) 1
127.0.0.1:6379> geoadd city 121.48941 31.40527 shanghai
(integer) 1
127.0.0.1:6379> geoadd city 120.21201 30.2084 hangzhou
(integer) 1
127.0.0.1:6379> geoadd city 104.10194 30.65984 chengdu
(integer) 1
127.0.0.1:6379> geoadd city 108.93425 34.23053 xian
(integer) 1
127.0.0.1:6379> geodist city beijing shanghai
"1088644.3544"
127.0.0.1:6379> geodist city beijing shanghai km
"1088.6444"
127.0.0.1:6379> geohash city beijing
1) "wx4sucvncn0"
127.0.0.1:6379> georadius city 121.48941 31.40527 500 km
1) "hangzhou"
2) "shanghai"
127.0.0.1:6379> georadiusbymember city shanghai 500 km
1) "hangzhou"
2) "shanghai"
```

#### **HyperLogLogs**

Redis HyperLogLog 是用来做基数统计的算法，HyperLogLog 的优点是，在输入元素的数量或者体积非常非常大时，计算基数所需的空间总是固定 的、并且是很小的。

-------------

-----------

##### 常用命令

PFADD：将指定元素添加到HyperLogLog。

PFCOUNT：返回存储在HyperLogLog结构体的该变量的近似基数，如果该变量不存在,则返回0。

PFMERGE：将多个 HyperLogLog 合并（merge）为一个 HyperLogLog ， 合并后的 HyperLogLog 的基数接近于所有输入 HyperLogLog 的可见集合（observed set）的并集。合并得出的 HyperLogLog 会被储存在目标变量（第一个参数）里面， 如果该键并不存在， 那么命令在执行之前， 会先为该键创建一个空的.

--------

--------

##### 示例

```bash
127.0.0.1:6379> pfadd myhyper a b c d a b e f
(integer) 1
127.0.0.1:6379> pfcount myhyper
(integer) 6
127.0.0.1:6379> pfadd myhyper1 g h j k
(integer) 1
127.0.0.1:6379> pfmerge newhyper myhyper myhyper1
OK
```



#### **Bitmaps**

##### 常用命令



--------

--------

##### 示例

```bash

```

### 事务

事务可以一次执行多个命令，同时保证事务是一个单独的隔离操作（事务中的所有命令都会序列化、按顺序地执行。事务在执行的过程中，不会被其他客户端发送来的命令请求所打断。），事务是一个原子操作（事务中的命令要么全部被执行，要么全部都不执行）。

#### 常用命令

[MULTI](http://www.redis.cn/commands/multi.html) 、 [EXEC](http://www.redis.cn/commands/exec.html) 、 [DISCARD](http://www.redis.cn/commands/discard.html) 和 [WATCH](http://www.redis.cn/commands/watch.html) 是 Redis 事务相关的命令。

#### 用法

MULTI用于开启事务，执行后，客户端可以像服务端发送任意多条命令。这些命令不会立即被执行，而是存放到一个队列中，当调用EXEC命令时，队列中所有的命令才会被执行。同时可以调用DISCARD命令清空事务队列，放弃执行事务。

#### 示例

```bash
127.0.0.1:6379> multi
OK
127.0.0.1:6379> set count 0
QUEUED
127.0.0.1:6379> incr count
QUEUED
127.0.0.1:6379> get count
QUEUED
127.0.0.1:6379> exec
1) OK
2) (integer) 1
3) "1"

127.0.0.1:6379> multi
OK
127.0.0.1:6379> set name chenxd
QUEUED
127.0.0.1:6379> set age 29
QUEUED
127.0.0.1:6379> discard
OK
```

WATCH

WATCH 命令可以为 Redis 事务提供 check-and-set （CAS）行为。 可以监控一个或多个键，一旦其 中有一个键被修改（或删除），之后的事务就不会执行，监控一直持续到EXEC命令 

示例

```bash
客户端1：
127.0.0.1:6379> set age 29
OK
127.0.0.1:6379> watch age
OK
127.0.0.1:6379> multi
OK
127.0.0.1:6379> incr age
QUEUED
-----------------------------------------------------------------------------------------------------------------------------------------
客户端2
127.0.0.1:6379> incr age
(integer) 30
-----------------------------------------------------------------------------------------------------------------------------------------
客户端1：
127.0.0.1:6379> exec
(nil)

```

### 发布/订阅

#### 常用命令

SUBSCRIBE：订阅给指定频道的信息。

PSUBSCRIBE：订阅给定的模式(patterns)。

PUBLISH：将信息发送到指定的频道，返回接收客户端的数量。

UNSUBSCRIBE：指示客户端退订给定的频道，若没有指定频道，则退订所有频道。

PUNSUBSCRIBE：指示客户端退订指定模式，若果没有提供模式则退出所有模式。

#### 示例

```bash
客户端1订阅消息
127.0.0.1:6379> subscribe mychannel
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "mychannel"
3) (integer) 1
客户端2订阅消息
127.0.0.1:6379> subscribe mychannel
Reading messages... (press Ctrl-C to quit)
1) "subscribe"
2) "mychannel"
3) (integer) 1
客户端3发送消息
127.0.0.1:6379> publish mychannel "hello world"
(integer) 2

客户端1接收消息
1) "message"
2) "mychannel"
3) "hello world"
客户端2接收消息
1) "message"
2) "mychannel"
3) "hello world"

```



### Spring Boot集成Redis

1. 新建Spring Boot项目

2. 添加Maven依赖

   ```yaml
   <dependency>
   	<groupId>org.springframework.boot</groupId>
   	<artifactId>spring-boot-starter-data-redis</artifactId>
   </dependency>
   ```

3. spring配置文件

   ```yaml
   spring.redis.database=0
   spring.redis.host=127.0.0.1
   spring.redis.port=6379
   spring.redis.timeout=5000
   spring.redis.password=
   ```

   Spring Boot2.x默认使用lettuce

   Lettuce和Jedis区别：

   1. Jedis 是直连模式，在多个线程间共享一个 Jedis 实例时是线程不安全的，每个线程都去拿自己的 Jedis 实例，当连接数量增多时，物理连接成本就较高了。Jedis可以使用连接池解决线程不安全问题
   2. Lettuce的连接是基于Netty的，连接实例可以在多个线程间共享，如果你不知道Netty也没事，大致意思就是一个多线程的应用可以使用同一个连接实例，而不用担心并发线程的数量。通过异步的方式可以让我们更好地利用系统资源。

4. 重新RedisTemplate

   默认配置，使用的是JdkSerializationRedisSerializer序列化方式

   ```java
       @Bean
       @ConditionalOnMissingBean(
           name = {"redisTemplate"}
       )
       public RedisTemplate<Object, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) throws UnknownHostException {
           RedisTemplate<Object, Object> template = new RedisTemplate();
           template.setConnectionFactory(redisConnectionFactory);
           return template;
       }
   ```

   重写默认配置，采用Jackson2JsonRedisSerializer序列化方式

   ```java
   @Configuration
   public class RedisConfiguration {
   
       @Bean
       public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory){
           RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
   
           Jackson2JsonRedisSerializer jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer(Object.class);
           ObjectMapper om = new ObjectMapper();
           om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
           om.activateDefaultTyping(om.getPolymorphicTypeValidator());
           jackson2JsonRedisSerializer.setObjectMapper(om);
           redisTemplate.setKeySerializer(RedisSerializer.string());
           redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
           redisTemplate.setHashKeySerializer(RedisSerializer.string());
           redisTemplate.setHashValueSerializer(jackson2JsonRedisSerializer);
   
           redisTemplate.setConnectionFactory(redisConnectionFactory);
           redisTemplate.afterPropertiesSet();
           return redisTemplate;
       }
   }
   ```

5. 示例

   ```java
    	@Autowired
       private RedisTemplate<String, Object> redisTemplate;
       @Test
       public void redisTest(){
           redisTemplate.opsForValue().set("name", "chenxd");
           System.out.println(redisTemplate.opsForValue().get("name"));
       }
   ```

## Redis高级

### 配置文件

修改Redis配置三种方式：

1. 配置文件
2. 命令行传参：./redis-server --port 6380 --slaveof 127.0.0.1 6379
3. 运行时更改：CONFIG SET （CONFIG GET 获取配置）

配置文件

```
# Redis configuration file example

# Note on units: when memory size is needed, it is possible to specify
# it in the usual form of 1k 5GB 4M and so forth:
#
# 1k => 1000 bytes
# 1kb => 1024 bytes
# 1m => 1000000 bytes
# 1mb => 1024*1024 bytes
# 1g => 1000000000 bytes
# 1gb => 1024*1024*1024 bytes
#
# units are case insensitive so 1GB 1Gb 1gB are all the same.
# Redis配置文件中单位不区分大小写

################################## INCLUDES ###################################

# Include one or more other config files here.  This is useful if you
# have a standard template that goes to all Redis servers but also need
# to customize a few per-server settings.  Include files can include
# other files, so use this wisely.
#
# Notice option "include" won't be rewritten by command "CONFIG REWRITE"
# from admin or Redis Sentinel. Since Redis always uses the last processed
# line as value of a configuration directive, you'd better put includes
# at the beginning of this file to avoid overwriting config change at runtime.
#
# If instead you are interested in using includes to override configuration
# options, it is better to use include as the last line.
#
# include .\path\to\local.conf
# include c:\path\to\other.conf
#Redis配置文件中可以引入其他配置文件

################################## NETWORK #####################################

# By default, if no "bind" configuration directive is specified, Redis listens
# for connections from all the network interfaces available on the server.
# It is possible to listen to just one or multiple selected interfaces using
# the "bind" configuration directive, followed by one or more IP addresses.
#
# Examples:
#
# bind 192.168.1.100 10.0.0.1
# bind 127.0.0.1 ::1
#
# ~~~ WARNING ~~~ If the computer running Redis is directly exposed to the
# internet, binding to all the interfaces is dangerous and will expose the
# instance to everybody on the internet. So by default we uncomment the
# following bind directive, that will force Redis to listen only into
# the IPv4 lookback interface address (this means Redis will be able to
# accept connections only from clients running into the same computer it
# is running).
#
# IF YOU ARE SURE YOU WANT YOUR INSTANCE TO LISTEN TO ALL THE INTERFACES
# JUST COMMENT THE FOLLOWING LINE.
# ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
bind 127.0.0.1
#Redis接收指定IP的请求，如果关闭Redis将会接收所有请求

# Protected mode is a layer of security protection, in order to avoid that
# Redis instances left open on the internet are accessed and exploited.
#
# When protected mode is on and if:
#
# 1) The server is not binding explicitly to a set of addresses using the
#    "bind" directive.
# 2) No password is configured.
#
# The server only accepts connections from clients connecting from the
# IPv4 and IPv6 loopback addresses 127.0.0.1 and ::1, and from Unix domain
# sockets.
#
# By default protected mode is enabled. You should disable it only if
# you are sure you want clients from other hosts to connect to Redis
# even if no authentication is configured, nor a specific set of interfaces
# are explicitly listed using the "bind" directive.
protected-mode yes
#Redis默认开启保护模式

# Accept connections on the specified port, default is 6379 (IANA #815344).
# If port 0 is specified Redis will not listen on a TCP socket.
port 6379
#Redis端口号

# TCP listen() backlog.
#
# In high requests-per-second environments you need an high backlog in order
# to avoid slow clients connections issues. Note that the Linux kernel
# will silently truncate it to the value of /proc/sys/net/core/somaxconn so
# make sure to raise both the value of somaxconn and tcp_max_syn_backlog
# in order to get the desired effect.
tcp-backlog 511

# Unix socket.
#
# Specify the path for the Unix socket that will be used to listen for
# incoming connections. There is no default, so Redis will not listen
# on a unix socket when not specified.
#
# unixsocket /tmp/redis.sock
# unixsocketperm 700

# Close the connection after a client is idle for N seconds (0 to disable)
timeout 0
#客户端连接超时时间，如果客户端在设置时间内没有发出任何命令将关闭连接。0为关闭该功能

# TCP keepalive.
#
# If non-zero, use SO_KEEPALIVE to send TCP ACKs to clients in absence
# of communication. This is useful for two reasons:
#
# 1) Detect dead peers.
# 2) Take the connection alive from the point of view of network
#    equipment in the middle.
#
# On Linux, the specified value (in seconds) is the period used to send ACKs.
# Note that to close the connection the double of the time is needed.
# On other kernels the period depends on the kernel configuration.
#
# A reasonable value for this option is 60 seconds.
tcp-keepalive 0

################################# GENERAL #####################################

# By default Redis does not run as a daemon. Use 'yes' if you need it.
# Note that Redis will write a pid file in /var/run/redis.pid when daemonized.
# NOT SUPPORTED ON WINDOWS daemonize no

# If you run Redis from upstart or systemd, Redis can interact with your
# supervision tree. Options:
#   supervised no      - no supervision interaction
#   supervised upstart - signal upstart by putting Redis into SIGSTOP mode
#   supervised systemd - signal systemd by writing READY=1 to $NOTIFY_SOCKET
#   supervised auto    - detect upstart or systemd method based on
#                        UPSTART_JOB or NOTIFY_SOCKET environment variables
# Note: these supervision methods only signal "process is ready."
#       They do not enable continuous liveness pings back to your supervisor.
# NOT SUPPORTED ON WINDOWS supervised no

# If a pid file is specified, Redis writes it where specified at startup
# and removes it at exit.
#
# When the server runs non daemonized, no pid file is created if none is
# specified in the configuration. When the server is daemonized, the pid file
# is used even if not specified, defaulting to "/var/run/redis.pid".
#
# Creating a pid file is best effort: if Redis is not able to create it
# nothing bad happens, the server will start and run normally.
# NOT SUPPORTED ON WINDOWS pidfile /var/run/redis.pid

# Specify the server verbosity level.
# This can be one of:
# debug (a lot of information, useful for development/testing)
# verbose (many rarely useful info, but not a mess like the debug level)
# notice (moderately verbose, what you want in production probably)
# warning (only very important / critical messages are logged)
loglevel notice
#Redis日志级别

# Specify the log file name. Also 'stdout' can be used to force
# Redis to log on the standard output.
logfile ""
#Redis日志存储地址

# To enable logging to the Windows EventLog, just set 'syslog-enabled' to
# yes, and optionally update the other syslog parameters to suit your needs.
# If Redis is installed and launched as a Windows Service, this will
# automatically be enabled.
# syslog-enabled no

# Specify the source name of the events in the Windows Application log.
# syslog-ident redis

# Set the number of databases. The default database is DB 0, you can select
# a different one on a per-connection basis using SELECT <dbid> where
# dbid is a number between 0 and 'databases'-1
databases 16
#Redis数据库数量，索引从0开始（0—15）

################################ SNAPSHOTTING  ################################
#
# Save the DB on disk:
#
#   save <seconds> <changes>
#
#   Will save the DB if both the given number of seconds and the given
#   number of write operations against the DB occurred.
#
#   In the example below the behaviour will be to save:
#   after 900 sec (15 min) if at least 1 key changed
#   after 300 sec (5 min) if at least 10 keys changed
#   after 60 sec if at least 10000 keys changed
#
#   Note: you can disable saving completely by commenting out all "save" lines.
#
#   It is also possible to remove all the previously configured save
#   points by adding a save directive with a single empty string argument
#   like in the following example:
#
#   save ""

save 900 1
save 300 10
save 60 10000
#数据同步到rdb文件策略。900秒内有一次操作/300秒有10次操作/60秒有10000次操作。如果注释save或者save为空字符串，则不对数据进行持久化到rdb文件。

# By default Redis will stop accepting writes if RDB snapshots are enabled
# (at least one save point) and the latest background save failed.
# This will make the user aware (in a hard way) that data is not persisting
# on disk properly, otherwise chances are that no one will notice and some
# disaster will happen.
#
# If the background saving process will start working again Redis will
# automatically allow writes again.
#
# However if you have setup your proper monitoring of the Redis server
# and persistence, you may want to disable this feature so that Redis will
# continue to work as usual even if there are problems with disk,
# permissions, and so forth.
stop-writes-on-bgsave-error yes

# Compress string objects using LZF when dump .rdb databases?
# For default that's set to 'yes' as it's almost always a win.
# If you want to save some CPU in the saving child set it to 'no' but
# the dataset will likely be bigger if you have compressible values or keys.
rdbcompression yes
#开启rdb文件压缩

# Since version 5 of RDB a CRC64 checksum is placed at the end of the file.
# This makes the format more resistant to corruption but there is a performance
# hit to pay (around 10%) when saving and loading RDB files, so you can disable it
# for maximum performances.
#
# RDB files created with checksum disabled have a checksum of zero that will
# tell the loading code to skip the check.
rdbchecksum yes

# The filename where to dump the DB
dbfilename dump.rdb
#rdb文件名称

# The working directory.
#
# The DB will be written inside this directory, with the filename specified
# above using the 'dbfilename' configuration directive.
#
# The Append Only File will also be created inside this directory.
#
# Note that you must specify a directory here, not a file name.
dir ./
#rdb文件存储路径

################################# REPLICATION #################################

# Master-Slave replication. Use slaveof to make a Redis instance a copy of
# another Redis server. A few things to understand ASAP about Redis replication.
#
# 1) Redis replication is asynchronous, but you can configure a master to
#    stop accepting writes if it appears to be not connected with at least
#    a given number of slaves.
# 2) Redis slaves are able to perform a partial resynchronization with the
#    master if the replication link is lost for a relatively small amount of
#    time. You may want to configure the replication backlog size (see the next
#    sections of this file) with a sensible value depending on your needs.
# 3) Replication is automatic and does not need user intervention. After a
#    network partition slaves automatically try to reconnect to masters
#    and resynchronize with them.
#
# slaveof <masterip> <masterport>
# 配置主Redis slaveof 127.0.0.1 6379

# If the master is password protected (using the "requirepass" configuration
# directive below) it is possible to tell the slave to authenticate before
# starting the replication synchronization process, otherwise the master will
# refuse the slave request.
#
# masterauth <master-password>
#配置主Redis密码 masterauth 123456

# When a slave loses its connection with the master, or when the replication
# is still in progress, the slave can act in two different ways:
#
# 1) if slave-serve-stale-data is set to 'yes' (the default) the slave will
#    still reply to client requests, possibly with out of date data, or the
#    data set may just be empty if this is the first synchronization.
#
# 2) if slave-serve-stale-data is set to 'no' the slave will reply with
#    an error "SYNC with master in progress" to all the kind of commands
#    but to INFO and SLAVEOF.
#
slave-serve-stale-data yes

# You can configure a slave instance to accept writes or not. Writing against
# a slave instance may be useful to store some ephemeral data (because data
# written on a slave will be easily deleted after resync with the master) but
# may also cause problems if clients are writing to it because of a
# misconfiguration.
#
# Since Redis 2.6 by default slaves are read-only.
#
# Note: read only slaves are not designed to be exposed to untrusted clients
# on the internet. It's just a protection layer against misuse of the instance.
# Still a read only slave exports by default all the administrative commands
# such as CONFIG, DEBUG, and so forth. To a limited extent you can improve
# security of read only slaves using 'rename-command' to shadow all the
# administrative / dangerous commands.
slave-read-only yes
# 设置从Redis为只读

# Replication SYNC strategy: disk or socket.
#
# -------------------------------------------------------
# WARNING: DISKLESS REPLICATION IS EXPERIMENTAL CURRENTLY
# -------------------------------------------------------
#
# New slaves and reconnecting slaves that are not able to continue the replication
# process just receiving differences, need to do what is called a "full
# synchronization". An RDB file is transmitted from the master to the slaves.
# The transmission can happen in two different ways:
#
# 1) Disk-backed: The Redis master creates a new process that writes the RDB
#                 file on disk. Later the file is transferred by the parent
#                 process to the slaves incrementally.
# 2) Diskless: The Redis master creates a new process that directly writes the
#              RDB file to slave sockets, without touching the disk at all.
#
# With disk-backed replication, while the RDB file is generated, more slaves
# can be queued and served with the RDB file as soon as the current child producing
# the RDB file finishes its work. With diskless replication instead once
# the transfer starts, new slaves arriving will be queued and a new transfer
# will start when the current one terminates.
#
# When diskless replication is used, the master waits a configurable amount of
# time (in seconds) before starting the transfer in the hope that multiple slaves
# will arrive and the transfer can be parallelized.
#
# With slow disks and fast (large bandwidth) networks, diskless replication
# works better.
repl-diskless-sync no

# When diskless replication is enabled, it is possible to configure the delay
# the server waits in order to spawn the child that transfers the RDB via socket
# to the slaves.
#
# This is important since once the transfer starts, it is not possible to serve
# new slaves arriving, that will be queued for the next RDB transfer, so the server
# waits a delay in order to let more slaves arrive.
#
# The delay is specified in seconds, and by default is 5 seconds. To disable
# it entirely just set it to 0 seconds and the transfer will start ASAP.
repl-diskless-sync-delay 5

# Slaves send PINGs to server in a predefined interval. It's possible to change
# this interval with the repl_ping_slave_period option. The default value is 10
# seconds.
#
# repl-ping-slave-period 10

# The following option sets the replication timeout for:
#
# 1) Bulk transfer I/O during SYNC, from the point of view of slave.
# 2) Master timeout from the point of view of slaves (data, pings).
# 3) Slave timeout from the point of view of masters (REPLCONF ACK pings).
#
# It is important to make sure that this value is greater than the value
# specified for repl-ping-slave-period otherwise a timeout will be detected
# every time there is low traffic between the master and the slave.
#
# repl-timeout 60

# Disable TCP_NODELAY on the slave socket after SYNC?
#
# If you select "yes" Redis will use a smaller number of TCP packets and
# less bandwidth to send data to slaves. But this can add a delay for
# the data to appear on the slave side, up to 40 milliseconds with
# Linux kernels using a default configuration.
#
# If you select "no" the delay for data to appear on the slave side will
# be reduced but more bandwidth will be used for replication.
#
# By default we optimize for low latency, but in very high traffic conditions
# or when the master and slaves are many hops away, turning this to "yes" may
# be a good idea.
repl-disable-tcp-nodelay no

# Set the replication backlog size. The backlog is a buffer that accumulates
# slave data when slaves are disconnected for some time, so that when a slave
# wants to reconnect again, often a full resync is not needed, but a partial
# resync is enough, just passing the portion of data the slave missed while
# disconnected.
#
# The bigger the replication backlog, the longer the time the slave can be
# disconnected and later be able to perform a partial resynchronization.
#
# The backlog is only allocated once there is at least a slave connected.
#
# repl-backlog-size 1mb

# After a master has no longer connected slaves for some time, the backlog
# will be freed. The following option configures the amount of seconds that
# need to elapse, starting from the time the last slave disconnected, for
# the backlog buffer to be freed.
#
# A value of 0 means to never release the backlog.
#
# repl-backlog-ttl 3600

# The slave priority is an integer number published by Redis in the INFO output.
# It is used by Redis Sentinel in order to select a slave to promote into a
# master if the master is no longer working correctly.
#
# A slave with a low priority number is considered better for promotion, so
# for instance if there are three slaves with priority 10, 100, 25 Sentinel will
# pick the one with priority 10, that is the lowest.
#
# However a special priority of 0 marks the slave as not able to perform the
# role of master, so a slave with priority of 0 will never be selected by
# Redis Sentinel for promotion.
#
# By default the priority is 100.
slave-priority 100

# It is possible for a master to stop accepting writes if there are less than
# N slaves connected, having a lag less or equal than M seconds.
#
# The N slaves need to be in "online" state.
#
# The lag in seconds, that must be <= the specified value, is calculated from
# the last ping received from the slave, that is usually sent every second.
#
# This option does not GUARANTEE that N replicas will accept the write, but
# will limit the window of exposure for lost writes in case not enough slaves
# are available, to the specified number of seconds.
#
# For example to require at least 3 slaves with a lag <= 10 seconds use:
#
# min-slaves-to-write 3
# min-slaves-max-lag 10
#
# Setting one or the other to 0 disables the feature.
#
# By default min-slaves-to-write is set to 0 (feature disabled) and
# min-slaves-max-lag is set to 10.

################################## SECURITY ###################################

# Require clients to issue AUTH <PASSWORD> before processing any other
# commands.  This might be useful in environments in which you do not trust
# others with access to the host running redis-server.
#
# This should stay commented out for backward compatibility and because most
# people do not need auth (e.g. they run their own servers).
#
# Warning: since Redis is pretty fast an outside user can try up to
# 150k passwords per second against a good box. This means that you should
# use a very strong password otherwise it will be very easy to break.
#
# requirepass foobared
#设置密码

# Command renaming.
#
# It is possible to change the name of dangerous commands in a shared
# environment. For instance the CONFIG command may be renamed into something
# hard to guess so that it will still be available for internal-use tools
# but not available for general clients.
#
# Example:
#
# rename-command CONFIG b840fc02d524045429941cc15f59e41cb7be6c52
#
# It is also possible to completely kill a command by renaming it into
# an empty string:
#
# rename-command CONFIG ""
#
# Please note that changing the name of commands that are logged into the
# AOF file or transmitted to slaves may cause problems.

################################### LIMITS ####################################

# Set the max number of connected clients at the same time. By default
# this limit is set to 10000 clients, however if the Redis server is not
# able to configure the process file limit to allow for the specified limit
# the max number of allowed clients is set to the current file limit
# minus 32 (as Redis reserves a few file descriptors for internal uses).
#
# Once the limit is reached Redis will close all the new connections sending
# an error 'max number of clients reached'.
#
# maxclients 10000

# If Redis is to be used as an in-memory-only cache without any kind of
# persistence, then the fork() mechanism used by the background AOF/RDB
# persistence is unnecessary. As an optimization, all persistence can be
# turned off in the Windows version of Redis. This will redirect heap
# allocations to the system heap allocator, and disable commands that would
# otherwise cause fork() operations: BGSAVE and BGREWRITEAOF.
# This flag may not be combined with any of the other flags that configure
# AOF and RDB operations.
# persistence-available [(yes)|no]

# Don't use more memory than the specified amount of bytes.
# When the memory limit is reached Redis will try to remove keys
# according to the eviction policy selected (see maxmemory-policy).
#
# If Redis can't remove keys according to the policy, or if the policy is
# set to 'noeviction', Redis will start to reply with errors to commands
# that would use more memory, like SET, LPUSH, and so on, and will continue
# to reply to read-only commands like GET.
#
# This option is usually useful when using Redis as an LRU cache, or to set
# a hard memory limit for an instance (using the 'noeviction' policy).
#
# WARNING: If you have slaves attached to an instance with maxmemory on,
# the size of the output buffers needed to feed the slaves are subtracted
# from the used memory count, so that network problems / resyncs will
# not trigger a loop where keys are evicted, and in turn the output
# buffer of slaves is full with DELs of keys evicted triggering the deletion
# of more keys, and so forth until the database is completely emptied.
#
# In short... if you have slaves attached it is suggested that you set a lower
# limit for maxmemory so that there is some free RAM on the system for slave
# output buffers (but this is not needed if the policy is 'noeviction').
#
# WARNING: not setting maxmemory will cause Redis to terminate with an
# out-of-memory exception if the heap limit is reached.
#
# NOTE: since Redis uses the system paging file to allocate the heap memory,
# the Working Set memory usage showed by the Windows Task Manager or by other
# tools such as ProcessExplorer will not always be accurate. For example, right
# after a background save of the RDB or the AOF files, the working set value
# may drop significantly. In order to check the correct amount of memory used
# by the redis-server to store the data, use the INFO client command. The INFO
# command shows only the memory used to store the redis data, not the extra
# memory used by the Windows process for its own requirements. Th3 extra amount
# of memory not reported by the INFO command can be calculated subtracting the
# Peak Working Set reported by the Windows Task Manager and the used_memory_peak
# reported by the INFO command.
#
# maxmemory <bytes>

# MAXMEMORY POLICY: how Redis will select what to remove when maxmemory
# is reached. You can select among five behaviors:
#
# volatile-lru -> remove the key with an expire set using an LRU algorithm
# allkeys-lru -> remove any key according to the LRU algorithm
# volatile-random -> remove a random key with an expire set
# allkeys-random -> remove a random key, any key
# volatile-ttl -> remove the key with the nearest expire time (minor TTL)
# noeviction -> don't expire at all, just return an error on write operations
#
# Note: with any of the above policies, Redis will return an error on write
#       operations, when there are no suitable keys for eviction.
#
#       At the date of writing these commands are: set setnx setex append
#       incr decr rpush lpush rpushx lpushx linsert lset rpoplpush sadd
#       sinter sinterstore sunion sunionstore sdiff sdiffstore zadd zincrby
#       zunionstore zinterstore hset hsetnx hmset hincrby incrby decrby
#       getset mset msetnx exec sort
#
# The default is:
#
# maxmemory-policy noeviction

# LRU and minimal TTL algorithms are not precise algorithms but approximated
# algorithms (in order to save memory), so you can tune it for speed or
# accuracy. For default Redis will check five keys and pick the one that was
# used less recently, you can change the sample size using the following
# configuration directive.
#
# The default of 5 produces good enough results. 10 Approximates very closely
# true LRU but costs a bit more CPU. 3 is very fast but not very accurate.
#
# maxmemory-samples 5

############################## APPEND ONLY MODE ###############################

# By default Redis asynchronously dumps the dataset on disk. This mode is
# good enough in many applications, but an issue with the Redis process or
# a power outage may result into a few minutes of writes lost (depending on
# the configured save points).
#
# The Append Only File is an alternative persistence mode that provides
# much better durability. For instance using the default data fsync policy
# (see later in the config file) Redis can lose just one second of writes in a
# dramatic event like a server power outage, or a single write if something
# wrong with the Redis process itself happens, but the operating system is
# still running correctly.
#
# AOF and RDB persistence can be enabled at the same time without problems.
# If the AOF is enabled on startup Redis will load the AOF, that is the file
# with the better durability guarantees.
#
# Please check http://redis.io/topics/persistence for more information.

appendonly no

# The name of the append only file (default: "appendonly.aof")
appendfilename "appendonly.aof"

# The fsync() call tells the Operating System to actually write data on disk
# instead of waiting for more data in the output buffer. Some OS will really flush
# data on disk, some other OS will just try to do it ASAP.
#
# Redis supports three different modes:
#
# no: don't fsync, just let the OS flush the data when it wants. Faster.
# always: fsync after every write to the append only log. Slow, Safest.
# everysec: fsync only one time every second. Compromise.
#
# The default is "everysec", as that's usually the right compromise between
# speed and data safety. It's up to you to understand if you can relax this to
# "no" that will let the operating system flush the output buffer when
# it wants, for better performances (but if you can live with the idea of
# some data loss consider the default persistence mode that's snapshotting),
# or on the contrary, use "always" that's very slow but a bit safer than
# everysec.
#
# More details please check the following article:
# http://antirez.com/post/redis-persistence-demystified.html
#
# If unsure, use "everysec".

# appendfsync always
appendfsync everysec
# appendfsync no

# When the AOF fsync policy is set to always or everysec, and a background
# saving process (a background save or AOF log background rewriting) is
# performing a lot of I/O against the disk, in some Linux configurations
# Redis may block too long on the fsync() call. Note that there is no fix for
# this currently, as even performing fsync in a different thread will block
# our synchronous write(2) call.
#
# In order to mitigate this problem it's possible to use the following option
# that will prevent fsync() from being called in the main process while a
# BGSAVE or BGREWRITEAOF is in progress.
#
# This means that while another child is saving, the durability of Redis is
# the same as "appendfsync none". In practical terms, this means that it is
# possible to lose up to 30 seconds of log in the worst scenario (with the
# default Linux settings).
#
# If you have latency problems turn this to "yes". Otherwise leave it as
# "no" that is the safest pick from the point of view of durability.
no-appendfsync-on-rewrite no

# Automatic rewrite of the append only file.
# Redis is able to automatically rewrite the log file implicitly calling
# BGREWRITEAOF when the AOF log size grows by the specified percentage.
#
# This is how it works: Redis remembers the size of the AOF file after the
# latest rewrite (if no rewrite has happened since the restart, the size of
# the AOF at startup is used).
#
# This base size is compared to the current size. If the current size is
# bigger than the specified percentage, the rewrite is triggered. Also
# you need to specify a minimal size for the AOF file to be rewritten, this
# is useful to avoid rewriting the AOF file even if the percentage increase
# is reached but it is still pretty small.
#
# Specify a percentage of zero in order to disable the automatic AOF
# rewrite feature.

auto-aof-rewrite-percentage 100
auto-aof-rewrite-min-size 64mb

# An AOF file may be found to be truncated at the end during the Redis
# startup process, when the AOF data gets loaded back into memory.
# This may happen when the system where Redis is running
# crashes, especially when an ext4 filesystem is mounted without the
# data=ordered option (however this can't happen when Redis itself
# crashes or aborts but the operating system still works correctly).
#
# Redis can either exit with an error when this happens, or load as much
# data as possible (the default now) and start if the AOF file is found
# to be truncated at the end. The following option controls this behavior.
#
# If aof-load-truncated is set to yes, a truncated AOF file is loaded and
# the Redis server starts emitting a log to inform the user of the event.
# Otherwise if the option is set to no, the server aborts with an error
# and refuses to start. When the option is set to no, the user requires
# to fix the AOF file using the "redis-check-aof" utility before to restart
# the server.
#
# Note that if the AOF file will be found to be corrupted in the middle
# the server will still exit with an error. This option only applies when
# Redis will try to read more data from the AOF file but not enough bytes
# will be found.
aof-load-truncated yes

################################ LUA SCRIPTING  ###############################

# Max execution time of a Lua script in milliseconds.
#
# If the maximum execution time is reached Redis will log that a script is
# still in execution after the maximum allowed time and will start to
# reply to queries with an error.
#
# When a long running script exceeds the maximum execution time only the
# SCRIPT KILL and SHUTDOWN NOSAVE commands are available. The first can be
# used to stop a script that did not yet called write commands. The second
# is the only way to shut down the server in the case a write command was
# already issued by the script but the user doesn't want to wait for the natural
# termination of the script.
#
# Set it to 0 or a negative value for unlimited execution without warnings.
lua-time-limit 5000

################################ REDIS CLUSTER  ###############################
#
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
# WARNING EXPERIMENTAL: Redis Cluster is considered to be stable code, however
# in order to mark it as "mature" we need to wait for a non trivial percentage
# of users to deploy it in production.
# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
#
# Normal Redis instances can't be part of a Redis Cluster; only nodes that are
# started as cluster nodes can. In order to start a Redis instance as a
# cluster node enable the cluster support uncommenting the following:
#
# cluster-enabled yes

# Every cluster node has a cluster configuration file. This file is not
# intended to be edited by hand. It is created and updated by Redis nodes.
# Every Redis Cluster node requires a different cluster configuration file.
# Make sure that instances running in the same system do not have
# overlapping cluster configuration file names.
#
# cluster-config-file nodes-6379.conf

# Cluster node timeout is the amount of milliseconds a node must be unreachable
# for it to be considered in failure state.
# Most other internal time limits are multiple of the node timeout.
#
# cluster-node-timeout 15000

# A slave of a failing master will avoid to start a failover if its data
# looks too old.
#
# There is no simple way for a slave to actually have a exact measure of
# its "data age", so the following two checks are performed:
#
# 1) If there are multiple slaves able to failover, they exchange messages
#    in order to try to give an advantage to the slave with the best
#    replication offset (more data from the master processed).
#    Slaves will try to get their rank by offset, and apply to the start
#    of the failover a delay proportional to their rank.
#
# 2) Every single slave computes the time of the last interaction with
#    its master. This can be the last ping or command received (if the master
#    is still in the "connected" state), or the time that elapsed since the
#    disconnection with the master (if the replication link is currently down).
#    If the last interaction is too old, the slave will not try to failover
#    at all.
#
# The point "2" can be tuned by user. Specifically a slave will not perform
# the failover if, since the last interaction with the master, the time
# elapsed is greater than:
#
#   (node-timeout * slave-validity-factor) + repl-ping-slave-period
#
# So for example if node-timeout is 30 seconds, and the slave-validity-factor
# is 10, and assuming a default repl-ping-slave-period of 10 seconds, the
# slave will not try to failover if it was not able to talk with the master
# for longer than 310 seconds.
#
# A large slave-validity-factor may allow slaves with too old data to failover
# a master, while a too small value may prevent the cluster from being able to
# elect a slave at all.
#
# For maximum availability, it is possible to set the slave-validity-factor
# to a value of 0, which means, that slaves will always try to failover the
# master regardless of the last time they interacted with the master.
# (However they'll always try to apply a delay proportional to their
# offset rank).
#
# Zero is the only value able to guarantee that when all the partitions heal
# the cluster will always be able to continue.
#
# cluster-slave-validity-factor 10

# Cluster slaves are able to migrate to orphaned masters, that are masters
# that are left without working slaves. This improves the cluster ability
# to resist to failures as otherwise an orphaned master can't be failed over
# in case of failure if it has no working slaves.
#
# Slaves migrate to orphaned masters only if there are still at least a
# given number of other working slaves for their old master. This number
# is the "migration barrier". A migration barrier of 1 means that a slave
# will migrate only if there is at least 1 other working slave for its master
# and so forth. It usually reflects the number of slaves you want for every
# master in your cluster.
#
# Default is 1 (slaves migrate only if their masters remain with at least
# one slave). To disable migration just set it to a very large value.
# A value of 0 can be set but is useful only for debugging and dangerous
# in production.
#
# cluster-migration-barrier 1

# By default Redis Cluster nodes stop accepting queries if they detect there
# is at least an hash slot uncovered (no available node is serving it).
# This way if the cluster is partially down (for example a range of hash slots
# are no longer covered) all the cluster becomes, eventually, unavailable.
# It automatically returns available as soon as all the slots are covered again.
#
# However sometimes you want the subset of the cluster which is working,
# to continue to accept queries for the part of the key space that is still
# covered. In order to do so, just set the cluster-require-full-coverage
# option to no.
#
# cluster-require-full-coverage yes

# In order to setup your cluster make sure to read the documentation
# available at http://redis.io web site.

################################## SLOW LOG ###################################

# The Redis Slow Log is a system to log queries that exceeded a specified
# execution time. The execution time does not include the I/O operations
# like talking with the client, sending the reply and so forth,
# but just the time needed to actually execute the command (this is the only
# stage of command execution where the thread is blocked and can not serve
# other requests in the meantime).
#
# You can configure the slow log with two parameters: one tells Redis
# what is the execution time, in microseconds, to exceed in order for the
# command to get logged, and the other parameter is the length of the
# slow log. When a new command is logged the oldest one is removed from the
# queue of logged commands.

# The following time is expressed in microseconds, so 1000000 is equivalent
# to one second. Note that a negative number disables the slow log, while
# a value of zero forces the logging of every command.
slowlog-log-slower-than 10000

# There is no limit to this length. Just be aware that it will consume memory.
# You can reclaim memory used by the slow log with SLOWLOG RESET.
slowlog-max-len 128

################################ LATENCY MONITOR ##############################

# The Redis latency monitoring subsystem samples different operations
# at runtime in order to collect data related to possible sources of
# latency of a Redis instance.
#
# Via the LATENCY command this information is available to the user that can
# print graphs and obtain reports.
#
# The system only logs operations that were performed in a time equal or
# greater than the amount of milliseconds specified via the
# latency-monitor-threshold configuration directive. When its value is set
# to zero, the latency monitor is turned off.
#
# By default latency monitoring is disabled since it is mostly not needed
# if you don't have latency issues, and collecting data has a performance
# impact, that while very small, can be measured under big load. Latency
# monitoring can easily be enabled at runtime using the command
# "CONFIG SET latency-monitor-threshold <milliseconds>" if needed.
latency-monitor-threshold 0

############################# EVENT NOTIFICATION ##############################

# Redis can notify Pub/Sub clients about events happening in the key space.
# This feature is documented at http://redis.io/topics/notifications
#
# For instance if keyspace events notification is enabled, and a client
# performs a DEL operation on key "foo" stored in the Database 0, two
# messages will be published via Pub/Sub:
#
# PUBLISH __keyspace@0__:foo del
# PUBLISH __keyevent@0__:del foo
#
# It is possible to select the events that Redis will notify among a set
# of classes. Every class is identified by a single character:
#
#  K     Keyspace events, published with __keyspace@<db>__ prefix.
#  E     Keyevent events, published with __keyevent@<db>__ prefix.
#  g     Generic commands (non-type specific) like DEL, EXPIRE, RENAME, ...
#  $     String commands
#  l     List commands
#  s     Set commands
#  h     Hash commands
#  z     Sorted set commands
#  x     Expired events (events generated every time a key expires)
#  e     Evicted events (events generated when a key is evicted for maxmemory)
#  A     Alias for g$lshzxe, so that the "AKE" string means all the events.
#
#  The "notify-keyspace-events" takes as argument a string that is composed
#  of zero or multiple characters. The empty string means that notifications
#  are disabled.
#
#  Example: to enable list and generic events, from the point of view of the
#           event name, use:
#
#  notify-keyspace-events Elg
#
#  Example 2: to get the stream of the expired keys subscribing to channel
#             name __keyevent@0__:expired use:
#
#  notify-keyspace-events Ex
#
#  By default all notifications are disabled because most users don't need
#  this feature and the feature has some overhead. Note that if you don't
#  specify at least one of K or E, no events will be delivered.
notify-keyspace-events ""

############################### ADVANCED CONFIG ###############################

# Hashes are encoded using a memory efficient data structure when they have a
# small number of entries, and the biggest entry does not exceed a given
# threshold. These thresholds can be configured using the following directives.
hash-max-ziplist-entries 512
hash-max-ziplist-value 64

# Lists are also encoded in a special way to save a lot of space.
# The number of entries allowed per internal list node can be specified
# as a fixed maximum size or a maximum number of elements.
# For a fixed maximum size, use -5 through -1, meaning:
# -5: max size: 64 Kb  <-- not recommended for normal workloads
# -4: max size: 32 Kb  <-- not recommended
# -3: max size: 16 Kb  <-- probably not recommended
# -2: max size: 8 Kb   <-- good
# -1: max size: 4 Kb   <-- good
# Positive numbers mean store up to _exactly_ that number of elements
# per list node.
# The highest performing option is usually -2 (8 Kb size) or -1 (4 Kb size),
# but if your use case is unique, adjust the settings as necessary.
list-max-ziplist-size -2

# Lists may also be compressed.
# Compress depth is the number of quicklist ziplist nodes from *each* side of
# the list to *exclude* from compression.  The head and tail of the list
# are always uncompressed for fast push/pop operations.  Settings are:
# 0: disable all list compression
# 1: depth 1 means "don't start compressing until after 1 node into the list,
#    going from either the head or tail"
#    So: [head]->node->node->...->node->[tail]
#    [head], [tail] will always be uncompressed; inner nodes will compress.
# 2: [head]->[next]->node->node->...->node->[prev]->[tail]
#    2 here means: don't compress head or head->next or tail->prev or tail,
#    but compress all nodes between them.
# 3: [head]->[next]->[next]->node->node->...->node->[prev]->[prev]->[tail]
# etc.
list-compress-depth 0

# Sets have a special encoding in just one case: when a set is composed
# of just strings that happen to be integers in radix 10 in the range
# of 64 bit signed integers.
# The following configuration setting sets the limit in the size of the
# set in order to use this special memory saving encoding.
set-max-intset-entries 512

# Similarly to hashes and lists, sorted sets are also specially encoded in
# order to save a lot of space. This encoding is only used when the length and
# elements of a sorted set are below the following limits:
zset-max-ziplist-entries 128
zset-max-ziplist-value 64

# HyperLogLog sparse representation bytes limit. The limit includes the
# 16 bytes header. When an HyperLogLog using the sparse representation crosses
# this limit, it is converted into the dense representation.
#
# A value greater than 16000 is totally useless, since at that point the
# dense representation is more memory efficient.
#
# The suggested value is ~ 3000 in order to have the benefits of
# the space efficient encoding without slowing down too much PFADD,
# which is O(N) with the sparse encoding. The value can be raised to
# ~ 10000 when CPU is not a concern, but space is, and the data set is
# composed of many HyperLogLogs with cardinality in the 0 - 15000 range.
hll-sparse-max-bytes 3000

# Active rehashing uses 1 millisecond every 100 milliseconds of CPU time in
# order to help rehashing the main Redis hash table (the one mapping top-level
# keys to values). The hash table implementation Redis uses (see dict.c)
# performs a lazy rehashing: the more operation you run into a hash table
# that is rehashing, the more rehashing "steps" are performed, so if the
# server is idle the rehashing is never complete and some more memory is used
# by the hash table.
#
# The default is to use this millisecond 10 times every second in order to
# actively rehash the main dictionaries, freeing memory when possible.
#
# If unsure:
# use "activerehashing no" if you have hard latency requirements and it is
# not a good thing in your environment that Redis can reply from time to time
# to queries with 2 milliseconds delay.
#
# use "activerehashing yes" if you don't have such hard requirements but
# want to free memory asap when possible.
activerehashing yes

# The client output buffer limits can be used to force disconnection of clients
# that are not reading data from the server fast enough for some reason (a
# common reason is that a Pub/Sub client can't consume messages as fast as the
# publisher can produce them).
#
# The limit can be set differently for the three different classes of clients:
#
# normal -> normal clients including MONITOR clients
# slave  -> slave clients
# pubsub -> clients subscribed to at least one pubsub channel or pattern
#
# The syntax of every client-output-buffer-limit directive is the following:
#
# client-output-buffer-limit <class> <hard limit> <soft limit> <soft seconds>
#
# A client is immediately disconnected once the hard limit is reached, or if
# the soft limit is reached and remains reached for the specified number of
# seconds (continuously).
# So for instance if the hard limit is 32 megabytes and the soft limit is
# 16 megabytes / 10 seconds, the client will get disconnected immediately
# if the size of the output buffers reach 32 megabytes, but will also get
# disconnected if the client reaches 16 megabytes and continuously overcomes
# the limit for 10 seconds.
#
# By default normal clients are not limited because they don't receive data
# without asking (in a push way), but just after a request, so only
# asynchronous clients may create a scenario where data is requested faster
# than it can read.
#
# Instead there is a default limit for pubsub and slave clients, since
# subscribers and slaves receive data in a push fashion.
#
# Both the hard or the soft limit can be disabled by setting them to zero.
client-output-buffer-limit normal 0 0 0
client-output-buffer-limit slave 256mb 64mb 60
client-output-buffer-limit pubsub 32mb 8mb 60

# Redis calls an internal function to perform many background tasks, like
# closing connections of clients in timeot, purging expired keys that are
# never requested, and so forth.
#
# Not all tasks are perforemd with the same frequency, but Redis checks for
# tasks to perform according to the specified "hz" value.
#
# By default "hz" is set to 10. Raising the value will use more CPU when
# Redis is idle, but at the same time will make Redis more responsive when
# there are many keys expiring at the same time, and timeouts may be
# handled with more precision.
#
# The range is between 1 and 500, however a value over 100 is usually not
# a good idea. Most users should use the default of 10 and raise this up to
# 100 only in environments where very low latency is required.
hz 10

# When a child rewrites the AOF file, if the following option is enabled
# the file will be fsync-ed every 32 MB of data generated. This is useful
# in order to commit the file to the disk more incrementally and avoid
# big latency spikes.
aof-rewrite-incremental-fsync yes

################################## INCLUDES ###################################

# Include one or more other config files here.  This is useful if you
# have a standard template that goes to all Redis server but also need
# to customize a few per-server settings.  Include files can include
# other files, so use this wisely.
#
# include /path/to/local.conf
# include /path/to/other.conf

```

### 持久化

Redis 提供了不同级别的持久化方式：

1. RDB持久化方式能够在指定的时间间隔能对你的数据进行快照存储。
2. AOF持久化方式记录每次对服务器写的操作，当服务器重启的时候会重新执行这些命令来恢复原始的数据，AOF命令以redis协议追加保存每次写的操作到文件末尾。Redis还能对AOF文件进行后台重写，使得AOF文件的体积不至于过大。
3. 如果你只希望你的数据在服务器运行的时候存在，你也可以不使用任何持久化方式。
4. 你也可以同时开启两种持久化方式， 在这种情况下， 当redis重启的时候会优先载入AOF文件来恢复原始的数据，因为在通常情况下AOF文件保存的数据集要比RDB文件保存的数据集要完整。

#### RDB

1. 配置

   save 900 1

   save 300 10

   save 60 10000

   以上配置代表：900内秒有一次改变或300内秒有10次改变或60秒内有10000次改变就会触发一次RDB备份

2. 工作方式

   Redis调用forks，同时拥有父进程和子进程。

   子进程将数据写入到一个临时的RDB文件中。

   当子进程完成对RDB文件写入时，Redis使用新的RDB文件替换原来的RDB文件，并删除旧的RDB文件。

3. 触发条件

   满足配置文件中save条件

   执行flushall命令

   退出redis

#### AOF

1. 配置

   appendonly yes

   \#appendfsync always 每次有新命令追加到 AOF 文件时就执行一次 fsync ：非常慢，也非常安全

   appendfsync everysec 每秒 fsync 一次：足够快（和使用 RDB 持久化差不多），并且在故障时只会丢失 1 秒钟的数据。

   \#appendfsync no 从不 fsync ：将数据交给操作系统来处理。更快，也更不安全的选择。

   以上配置代表：开启AOP，每秒 fsync 一次命令到磁盘。

2. 工作方式

   Redis调用fork，同时拥有父进程和子进程。

   子进程开始将新AOF文件的内容写入临时文件。

   对所有新执行的写入命令，父进程一边将他们累积到一个内存缓存中，一边将改动追加到现有AOF文件的末尾。

   当子进程完成重写工作时，它给父进程发送一个信号，父进程在接收到信号之后，将内存缓存中的所有数据追加到新 AOF 文件的末尾。

   现在 Redis 原子地用新文件替换旧文件，之后所有命令都会直接追加到新 AOF 文件的末尾。

3. 触发条件

   配置文件中配置的appendsync的值，always、everysec、no

#### RDB和AOF对比

RDB优点：

- RDB是一个非常紧凑的文件,它保存了某个时间点得数据集,非常适用于数据集的备份,比如你可以在每个小时报保存一下过去24小时内的数据,同时每天保存过去30天的数据,这样即使出了问题你也可以根据需求恢复到不同版本的数据集.
- RDB是一个紧凑的单一文件,很方便传送到另一个远端数据中心或者亚马逊的S3（可能加密），非常适用于灾难恢复.
- RDB在保存RDB文件时父进程唯一需要做的就是fork出一个子进程,接下来的工作全部由子进程来做，父进程不需要再做其他IO操作，所以RDB持久化方式可以最大化redis的性能.
- 与AOF相比,在恢复大的数据集的时候，RDB方式会更快一些.

RDB缺点：

- 如果你希望在redis意外停止工作（例如电源中断）的情况下丢失的数据最少的话，那么RDB不适合你.虽然你可以配置不同的save时间点(例如每隔5分钟并且对数据集有100个写的操作),是Redis要完整的保存整个数据集是一个比较繁重的工作,你通常会每隔5分钟或者更久做一次完整的保存,万一在Redis意外宕机,你可能会丢失几分钟的数据.
- RDB 需要经常fork子进程来保存数据集到硬盘上,当数据集比较大的时候,fork的过程是非常耗时的,可能会导致Redis在一些毫秒级内不能响应客户端的请求.如果数据集巨大并且CPU性能不是很好的情况下,这种情况会持续1秒,AOF也需要fork,但是你可以调节重写日志文件的频率来提高数据集的耐久度.

AOF优点：

- 使用AOF 会让你的Redis更加耐久: 你可以使用不同的fsync策略：无fsync,每秒fsync,每次写的时候fsync.使用默认的每秒fsync策略,Redis的性能依然很好(fsync是由后台线程进行处理的,主线程会尽力处理客户端请求),一旦出现故障，你最多丢失1秒的数据.
- AOF文件是一个只进行追加的日志文件,所以不需要写入seek,即使由于某些原因(磁盘空间已满，写的过程中宕机等等)未执行完整的写入命令,你也也可使用redis-check-aof工具修复这些问题.
- Redis 可以在 AOF 文件体积变得过大时，自动地在后台对 AOF 进行重写： 重写后的新 AOF 文件包含了恢复当前数据集所需的最小命令集合。 整个重写操作是绝对安全的，因为 Redis 在创建新 AOF 文件的过程中，会继续将命令追加到现有的 AOF 文件里面，即使重写过程中发生停机，现有的 AOF 文件也不会丢失。 而一旦新 AOF 文件创建完毕，Redis 就会从旧 AOF 文件切换到新 AOF 文件，并开始对新 AOF 文件进行追加操作。
- AOF 文件有序地保存了对数据库执行的所有写入操作， 这些写入操作以 Redis 协议的格式保存， 因此 AOF 文件的内容非常容易被人读懂， 对文件进行分析（parse）也很轻松。 导出（export） AOF 文件也非常简单： 举个例子， 如果你不小心执行了 FLUSHALL 命令， 但只要 AOF 文件未被重写， 那么只要停止服务器， 移除 AOF 文件末尾的 FLUSHALL 命令， 并重启 Redis ， 就可以将数据集恢复到 FLUSHALL 执行之前的状态。

AOF缺点：

- 对于相同的数据集来说，AOF 文件的体积通常要大于 RDB 文件的体积。
- 根据所使用的 fsync 策略，AOF 的速度可能会慢于 RDB 。 在一般情况下， 每秒 fsync 的性能依然非常高， 而关闭 fsync 可以让 AOF 的速度和 RDB 一样快， 即使在高负荷之下也是如此。 不过在处理巨大的写入载入时，RDB 可以提供更有保证的最大延迟时间（latency）。

#### 备份选择

可以同时使用RDB和AOF进行数据备份。如果可以忍受几分钟的数据丢失，可以单独使用RDB，不建议单独使用AOF。

### 键过期删除策略及内存淘汰

#### 定时删除

在设置键的过期时间的同时，创建一个定时器，让定时器在键的过期时间来临时，立即执行对键的删除操作。

定时删除的好处是可以尽快释放内存，但是由于过期键可能存在很多的时候删除键的操作占用大量CPU时间导致服务器的响应时间和吞吐量降低。

#### 惰性删除

放任过期键不管，但是每次从键空间获取键的时候，先检查键是否过期，如果过期则删除过期的键，如果没有过期返回键给客户端。

由于键只有在取出的时候才会进行过期时间检查，同时只检查当前键因此对于CPU是友好的。但是可能会出现过期键长时间保留在内存中导致内存不会被释放。

####  定期删除

每隔一段时间，Redis自动对键进行检查，删除已经过期的键。但是不会检查所有的键。

-----

--------

Redis实际上采用惰性删除和定期删除两种策略。通过这两种策略配合，Redis可以更好的利用CPU和内存


#### 内存淘汰机制

实际上采用惰性删除和定期删除并不能完全排除内存逐渐升高的问题，因此Redis提供6种内存淘汰策略。默认为不驱逐，返回报错信息。

配置详情：默认 maxmemory-policy noeviction

 volatile-lru -> remove the key with an expire set using an LRU algorithm 在过期时间的键中移除最少使用的键
 allkeys-lru -> remove any key according to the LRU algorithm 在所有键中移除最少使用的键
 volatile-random -> remove a random key with an expire set 在过期的键中随机移除
 allkeys-random -> remove a random key, any key 在所有的键中随机移除
 volatile-ttl -> remove the key with the nearest expire time (minor TTL)  在过期的键中移除最近过期的键
 noeviction -> don't expire at all, just return an error on write operations 不移除，返回写入错误







