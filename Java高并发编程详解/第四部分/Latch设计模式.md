# 第23章 Latch设计模式
## 23.1 什么是Latch(门阀)
应用场景：  
确保某个计算在其需要的所有资源**都被初始化**之后才继续执行。二元闭锁（包括两个状态）可以用来表示“资源R已经被初始化”，而所有需要R的操作都必须先在这个闭锁上等待。确保某个服务在其依赖的所有其他服务都已经启动之后才启动。等待直到某个操作的所有参与者都就绪在继续执行。（例如：多人游戏中需要所有玩家准备才能开始）  
CountDownLatch是JDK 5+里面闭锁的一个实现，允许一个或者多个线程等待某个事件的发生。CountDownLatch有一个正数计数器，countDown方法对计数器做减操作，await方法等待计数器达到0。所有await的线程都会阻塞直到计数器为0或者等待线程中断或者超时。

## 23.2 CountDownLatch程序实现
### 23.2.1 无线等待的Latch
首先定义一个无线等待的抽象类Latch，定义await、countDown和getUnarrived方法，其中limit属性非常重要，当limit降到0时，门阀会自动打开，见*Latch.java*
- 子任务达到limit门阀才能打开
- await等待所有子任务完成，如果未达到将无线等待
- 任务完成时countDown使计数器减少
- getUnarrived查询当前有多少子任务
1. 无线等待CountDownLatch实现  
见*CountDownLatch.java*，较容易，注意，getUnarrived返回的是一个评估值，因为没有加锁。
2. 程序测试齐心协力打开门阀  
模拟一下程序员集会的案例，当程序员通过不同交通工具一起到达时，则开始活动，见*ProgrammerTravel.java*。
	> 石 start take the transportation [ 自行车  ]   
	> 徐 start take the transportation [ 汽车  ]   
	> 王 start take the transportation [ 拖鞋  ]   
	> 谢 start take the transportation [ 电动车  ]   
	> 谢 arrived by 电动车  
	> 石 arrived by 自行车  
	> 徐 arrived by 汽车  
	> 王 arrived by 拖鞋  
	> 全都到达，活动开始  
3. 有超时设置的Latch  
上面的Latch有一个问题，假设其中有一个任务无法完成，如中断，取消等，那么Latch就会无线等待，所以此处加上超时功能。
见*WatiTimeoutException.java*和*CountDownLatch.java*
4. 收到超时通知
	> 王 arrived by 拖鞋  
	> 石 arrived by 自行车  
	> 谢 arrived by 电动车  
	> 超时，活动开始  
	> chapter23.WatiTimeoutException: 等待超时  
	> 徐 arrived by 汽车

## 23.3 本章总结
通过上面的例子，我们发现就算await超时，并不影响未完成的线程，也就是说，**Latch模式并不负责控制线程**，只负责门阀的功能。  
最后，我们拓展Latch的功能，**使其增加回调功能**，见*CountDownLatch.java*.