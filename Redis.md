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

ZRANGE：

ZCARD：

ZCOUNT：

ZRANK：

ZREM：

ZREVRANK：

ZSCORE：

----

----

##### 示例

```bash

```

#### **Geo**



-------

----------

##### 常用命令



---------

--------

##### 示例

```bash

```

#### **HyperLogLog**



-------------

-----------

##### 常用命令



--------

--------

##### 示例

```bash

```



#### **Bitmaps**

##### 常用命令



--------

--------

##### 示例

```bash

```

### 事务



