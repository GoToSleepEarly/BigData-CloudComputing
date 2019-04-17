# 大数据与云计算
## ❤谢某人的笔记❤
![](https://i.postimg.cc/5NKWpS4m/timg.jpg)
## 1.Java
### 多线程基础：《Java高并发详解》—— 第一部分
- Thread、Runnable线程详解及相关API
- synchronized、monitor锁详解及数据安全相关
- wait、notify多线程通信详解及自定义Lock锁
- ThreadGroup详解
- Hook以及捕获线程异常
- 线程池原理以及自定义线程池

### Java ClassLoader：《Java高并发详解》 —— 第二部分
- 类的加载过程(加载，连接[验证，准备，解析]，初始化)
- JVM三大类加载器、自定义类加载器、双亲委托机制及其破坏方法1重写之loadClass
- 线程上下文类加载器，双亲破坏方法2之Class.forName
- **[附]Static本质、JVM概要**

### 深入理解volatile： 《Java高并发详解》 —— 第三部分
- volatile介绍，CPU Cache模型，缓存一致性，JMM
- 原子性，可见性，有序性，JMM如何保证三大特性，volatile原理和实现机制，volatile与synchronized的对比

### 多线程设计架构模式： 《Java高并发详解》 —— 第四部分  
- 7种单例模式的设计，volatile+Doubl-Check，Holder模式等
- 线程监听者模式，监控任务的生命周期，允许获取返回值
- Single Thread Execution，同步锁保护资源，避免交叉死锁
- 读写锁分离设计模式，提高读>>写场景时的并发性能
- 不可变对象设计模式，典型如java.lang.String，每次运算返回新对象，保证线程安全
- Future设计模式，运行后返回"凭据"，不阻塞调用线程，异步执行任务，最后通过“凭据”得到结果
- Guarded Suspension 和 Balking设计模式，不满足处理请求时，前者挂起，后者放弃，从而保证数据完整性
- ThreadLocal设计模式，线程上下文彼此独立，互相不受影响，**ThreadLocal原理概要**
- Latch设计模式，“门阀”设计，只有当前条件满足时才能往下继续执行，否则阻塞
- Thread-Per-Message设计模式，每个任务新开一个线程，可用线程池改进
- Two-Phase-Termination设计模式，最大程度保证回收系统资源，**Strong-Soft-Weak-Phantom Reference的详细说明**
- Worker-Thread 和 消费者生产者模式，提供任务提交和自动流水化处理，详解两者的区别
- Active Object设计模式，将一个接口的方法转换成可接受异步消息的主动对象。运用到异步代理注解等知识，需重点掌握
- EventBus和EventDriven，降低模块耦合度，实现消息自动分发等功能，需重点掌握
- **[附]Java注解概要**
- **[附]Java枚举概要**
- **[附]Java静、动态绑定**
- **[附]Java动态代理**



## 2.大数据基础架构
### 大数据基础：《大数据技术原理与应用》 —— 第一篇
- 大数据概念，关键技术，技术模式， 云计算物联网
- Hadoop简介，生态系统组件介绍

### 第二篇 大数据存储与管理：《大数据技术原理与应用》 —— 第二篇
- HDFS体系结构，存储原理，功能组件，读写详解
- 列族数据库HBase数据模型，实现原理、运行机制、功能组件、性能优化
- NoSQL数据库、四大类型、CAP、BASE、最终一致性

### 第三篇 大数据处理与分析：《大数据技术原理与应用》 —— 第三篇
- MapReduce基本思想，Map、Shuffle、Reduce过程详解
- Hadoop再探讨，HDFS1.0缺陷，HDFS2.0的HA，Federation和YARN，Hadoop生态系统
- Spark生态系统，运行架构，执行过程（RDD，DAG等）
- 流计算处理模型，Storm设计思想，基本原理与构造，运行过程
