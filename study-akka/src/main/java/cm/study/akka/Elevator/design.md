## 人 ##
 - 随机产生一个人, 此人来到电梯调度系统前
 - 发出, 我要在Y楼到Z楼去的消息
 - 等待电梯到来
 - 进入电梯
 - 走出电梯
 
## 调度系统 ##
 - 接收乘坐请求
 - 从所有电梯目前的状态里, 挑选出一台过来接人
 - 接收电梯运行请求
 
## 电梯 ##
 - 有状态数据, 当前运行方向, 楼层; 当前里面的人
 - 有收到乘坐消息, 有X人在Y楼要去Z楼
 - 运行, 每到一楼, 把当前方向 人数, 重量发给调度中心
 