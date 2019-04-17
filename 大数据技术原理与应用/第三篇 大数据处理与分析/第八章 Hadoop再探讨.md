# 第八章 Hadoop在探讨
## 8.1 Hadoop的优化与发展
### 8.1.1 Hadoop的局限与不足
仅指MapReduce和HDFS的不足：
- 抽象层次低，需要人工编码
- 表达能力有限
- 开发者自己管理作业（Job）之间的依赖关系
- 难以看到程序整体逻辑
- 执行迭代操作效率低
- 资源浪费
- 实时性差

### 8.1.2 针对Hadoop的改进与提升
- 改进Hadoop自身框架
![](https://i.postimg.cc/jSdcSmdv/8-1.png)
- 完善Hadoop生态系统
![](https://i.postimg.cc/R0WTcttH/8-2.png)


## 8.2 HDFS2.0的新特性
### 8.2.1 HDFS HA
HDFS HA(High Availability)是为了解决单点故障问题
- HA集群设置了两个名称节点，“活跃（Active）”和“待命（Standb）”
- 两个名称节点状态同步，借助共享存储系统实现
- 一旦活跃名称节点出现故障，就立即切换到待命名称节点
- Zookeeper确保一个名称节点对外服务
- 名称节点维护映射信息，数据节点同时向两个名称节点汇报信息
![](https://i.postimg.cc/VLzBKpM1/8-1.png)

### 8.2.2 HDFS Federation
#### 8.2.2.1 HDFS1.0出现的问题
- 单点故障问题
- 不可以水平扩展，因为单节点；不可以纵向扩展，因为启动时间太长
- 系统性能取决于单个节点的吞吐量
- 单个名称节点难以提供不同程序之间的隔离
- HDFS HA是热备份，提供高可用，但是无法解决可拓展性、系统性能和隔离性

#### 8.2.2.2 HDFS Federation的设计
- 设计多个相互独立的名称节点，使得HDFS命名服务可以水平拓展，这些名称节点分别进行各自命名空间和块的管理，相互之间是联盟（Federation）关系，不需要彼此协调，并且向后兼容
- HDFS Federation中，所有名称节点会共享底层的数据节点存储资源，数据节点向所有名称节点汇报
- 属于同一个命名空间的块构成一个“块池”

![](https://i.postimg.cc/G2YVxVDG/8-2.png)

### 8.2.2.3 HDFS Federation的访问方式
- 对于Federation中的多个命名空间，可以采用客户挂载表（Client Side Mount Table）方式进行数据共享和访问
- 客户可以访问不同的挂载点来访问不同的子命名空间
- 把各个命名空间挂载到全局“挂载表”（mount-table）中，实现数据全局共享
- 同样的命名空间挂载到个人的挂载表中，就成为应用程序可见的命名空间
![](https://i.postimg.cc/sxmnnrgQ/8-3.png)
每个三角是一个独立的命名空间

### 8.2.2.4 HDFS Federation的优势
1. HDFS集群拓展性
2. 性能更高效
3. 良好的隔离性

## 8.3 新一代资源管理调度框架YARN
### 8.3.1 MapReduce1.0的缺陷
1. 存在单点故障
2. JobTracker“大包大揽”导致任务过重（任务多时内存开销大）
3. 容易出现内存溢出（分配资源只考虑MapReduce任务数，不考虑CPU内存等）
4. 资源划分不合理（强制分为slot）

### 8.3.2 YARN设计思路
JobTracker三大功能（资源调度，任务调度，任务监控）拆分
![](https://i.postimg.cc/DwXr0h3r/8-5.png)
将资源管理功能抽离出来形成了YARN，MapReduce成了YARN上的一个纯粹的技算框架，而YARN是纯粹的资源管理调度框架。

### 8.3.3 YARN体系结构
![](https://i.postimg.cc/vmXcH2T7/8-6.png)

#### ResourceManager
- ResourceManager是一个全局的资源管理器，负责整个系统的资源管理和分配，包括调度器（Scheduler）和应用程序管理器（Applications Manager）
- 调度器接收来自ApplicationMaster（YARN中负责任务调度和任务监控）的应用程序资源请求，把集群中的资源以“容器”的形式分配给提出申请的应用程序，容器的选择基于“计算向数据靠拢”
- 容器（Container）作为动态分配资源，封装了一定数量的CPU、内存、磁盘等资源，从而限定每个应用程序的资源量
- 调度器被设计成可插拔组件，YARN不仅自己提供了，也允许自定义
- 应用程序管理器（ApplicationManager）负责系统中所有应用程序的管理工作，包括应用程序提交，与调度器协商资源启动ApplicationMaster，监控ApplicationMaster的状态

### 区分：ApplicationMaster负责单个应用程序，ApplicationManager负责所有应用程序

#### ApplicationMaster
ResourceManager接收用户提交的作业，根据作业上下文信息以及从NodeManager收集的容器状态，启动调度过程，为用户启动ApplicationMaster
- 当用户提交作业时，ApplicationMaster与ResourceManager与ResourceManager协商获取资源，ResourceManager以容器的形式为ApplicationMaster分配资源
- 把获得的资源“二次分配”给内部的各个任务（Map或Reduce任务）
- 与NodeManager保持交互通信以进行应用程序的启动、运行、监控和停止，监控资源使用情况，对所有任务执行状态进行监控，失败时执行失败恢复
- 定时向ResourceManager发送“心跳”消息，报告资源的使用情况和应用进度
- 作业完成时，ApplicationMaster和ResourceManager注销容器

#### NodeManager
- 容器生命周期管理
- 监控每个容器的资源（CPU、内存等）使用情况
- 跟踪节点健康状况
- 以“心跳”与ResourceManager保持通信
- 向ResourceManager汇报作业的资源使用情况和容器的运行状态
- 接收ApplicationMaster的启动/停止容器的请求
- NodeManager不负责每个任务（Map和Reduce）自身管理，因为这是ApplicationMaster完成的
- 
![](https://i.postimg.cc/T15VbvWv/8-7.png)

### 8.3.4 YARN工作流程
![](https://i.postimg.cc/bwh5q3Kw/8-8.png)
（步骤2比较关键，它是先为应用程序分配一个容器，与该容器所在的NodeManager通信，然后在这容器中启动一个ApplicationMaster）

### 8.3.5 YARN与MapReduce1.0对比
- 大大减少了承担中心服务功能的ResourceManager的资源消耗：ApplicationMaster完成需要消耗大量资源消耗的任务调度和监控；多个作业对应多个ApplicationMaster，实现了监控分布化
- YARN成了资源调度管理框架，而MapReduce成了计算框架
- YARN的资源管理高效：以容器为单位，而不是slot

### 8.3.6 YARN的发展目标
- “一个集群多个框架”：即在一个集群上部署一个统一的资源调度框架YARN，在YARN上部署其他各种计算框架。
- 由YARN为这些计算框架提供统一资源调度管理服务，根据不同框架的负载需求调整资源，实现资源共享和弹性收缩
- 实现一个集群不同应用负载混搭，有效提高集群的利用率
- 不同计算框架共享底层存储，避免了数据集跨集群移动

![](https://i.postimg.cc/1RWRnN9S/8-9.png)

## 8.4 Hadoop生态系统中具有代表性的功能组件
### 8.4.1 Pig
提供类似SQL的PigLatin语言，自动转换成MapReduce，一般用于ETL，数据源采用Pig加工后加载到Hive中。

由于面对批处理，所以当需要查询大数据集的一小部分时，性能不好，因为需要整表扫描。
![](https://i.postimg.cc/SQLTDWX0/8-10.png)

### 8.4.2 Tez
DAG作业的计算框架，直接源于MapReduce，核心思想是将Map和Reduce拆分，Map成为Input，Processer，Sort，Merge和Output；Reduce成为Input，Shuffle，Sort，Merge，Processer和Output等。

将分解后的操作任何组合，形成一个大的DAG作业，通过这种方式提高效率。

下图配合Hive，省去写入HDFS和多余的Map，效率飞速提高。
![](https://i.postimg.cc/vmRfJNj0/8-12.png)

当然，Spark已经这么做了，这是如今最热门的大数据框架， 基本可以超越Hadoop MapReduce。

### 8.4.3 Kafka
![](https://i.postimg.cc/tTB14SSQ/8-14.png)
- Kafka是一种高吞吐量的分布式发布订阅消息系统，用户通过Kafka系统可以发布大量的消息，也能实现订阅消费消息
- Kafka可以满足在线实时处理和批量离线处理
- Kafka可以作为数据交换枢纽，不同类型的分布式数据库统一接入到Kafka中，实现Hadoop各组件间不同类型数据的实时高效交换。

