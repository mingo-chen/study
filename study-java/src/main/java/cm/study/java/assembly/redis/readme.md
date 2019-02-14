## redis server简易实现

### RedisServer 
- 负责监听客户端的请求
- 2个线程池, connections:负责保持客户端的socket连接, work:负责执行客户端的请求
- work也是单线程, 同真实redis一样

### CacheStore
- 负责数据的存储, 
- 目前已经实现的数据类型有: String, List, Set, Zset, Map; 每种类型只实现了部分命令, 未完成的命令基本可按redis文档里命令进行实现

### CmdInvoker
- 负责解析客户端的请求命令, 调用相应的方法

### 本地运行及测试
- 启动RedisServer
- 打开终端/cmd, 输入`telnet 127.0.0.1 6379`
- 然后就可以在里面测试cmdInvoker里已经绑定的命令了
