# Redis

## Redis基础

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

 ## Redis 进阶

### 数据库操作命令

选择数据库：select index

清空当前数据库：flushdb

清空所有数据库：flushall

### 键操作命令

当前数据库key的个数：dbsize

显示所有key：keys *  返回值所有的key集合

显示key过期时间：ttl key 返回值秒 ； pttl key 返回值毫秒

设置key过期时间：expire key seconds；pexpire key milliseconds

备注：key的设置不要太长，尽量不要超过1024个字节。数据过大不仅消耗内存，同时降低查询效率。key的设置不要太短，识别度不高。例：user:10000:passwd

### 数据结构

```bash
字符串（string）
```

字符串是一种最基本的Redis值类型。Redis字符串是二进制安全的，这意味着一个Redis字符串能包含任意类型的数据，例如： 一张JPEG格式的图片或者一个序列化的Ruby对象。一个字符串类型的值最多能存储512M字节的内容。



