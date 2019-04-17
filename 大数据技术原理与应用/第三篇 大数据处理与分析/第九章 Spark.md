# 第九章 Spark

## 9.1 概述
### 9.1.1 Spark简介
基于内存计算的大数据并行计算框架，构建大型的、低延迟的数据分析应用程序。如今是Apache三大分布式计算系统开源项目之一（Hadoop，Spark，Storm）
- 运行速度快：使用DAG执行引擎以支持循环数据流与内存计算
- 容易使用：支持使用Scala，Java，Python和R语言进行编程，可以通过Spark Shell进行交互式编程
- 通用性：Spark提供完整而强大的技术栈，包括SQL查询、流式计算、机器学习和图算法组件
- 运行模式多样：可运行于独立集群，可以运行于Hadoop，也可以Amazon EC2等云环境，并且可以访问HDFS、Cassandra、HBase、Hive等多种数据源

![](https://i.postimg.cc/XJGJhbFD/10-1.png)

### 9.1.2 Scala简介
Scala是一门现代的多范式编程语言，运行于Java平台(JVM)，并兼容现有的Java程序

Scala特性：
- 具备强大的并发性，支持函数式编程，可以更好地支持分布式系统
- 语法简洁，提供优雅的API
- 兼容Java，运行速度快，且能融合到Hadoop生态
- 是Spark的主要编程语言，但还支持Java，Python，R作为编程语言
- 提供了REPL(Read-Eval-Print Loop,交互式解释器)，提高编程开发效率

### 9.1.3 Spark和Hadoop对比
Hadoop存在的缺点：
- 表达能力有限：MapReduce并不适用于所有情况
- 磁盘I/O开销大：每次执行都需要磁盘读写，中间结果也要
- 延迟高：一系列MapReduce任务都涉及I/O开销，较高延迟，而且在前一个任务完成前，其他任务无法开始，难以胜任多阶段复杂的任务


Spark具有以下优点：
- 计算模式也属于MapReduce，但是不局限于Map和Reduce，提供了多种数据集操作
- 提供了内存计算，可将中间结果放在内存中，对于迭代效率更高
- 基于DAG的任务调度执行机制，优于Hadoop MapReduce的迭代执行机制

![](https://i.postimg.cc/15H9vt23/10-2.png)

## 9.2 Spark生态系统
实际应用中，大数据处理包括以下三种类型：
- 复杂的批量数据处理：通常时间跨度在数十分钟到数小时之间
- 基于历史数据的交互式查询：通常时间跨度在数十秒到数分钟之间
- 基于实时数据流的数据处理：通常时间跨度在数百毫秒到数秒之间

当同时存在以上三种场景时，就需要部署三种不同的软件：
比如：MapReduce/Impala/Storm

这样会带来问题：
- 不同场景之间输入输出数据无法做到无缝共享，通常需要进行数据格式的转换
- 不同的软件需要不同的开发和维护团队，成本高
- 比较难以对同一个集群中的各个系统进行统一的资源协调和分配

**Spark生态系统**：
- Spark的设计遵循“一个软件栈满足不同应用场景”的理念，构成完整的生态系统
- 既能提供内存计算框架，也可以支持SQL即席查询，实时流式计算、机器学习和图计算等
- Spark可以部署在资源管理器YARN之上，提供一站式大数据解决方案
- 所以Spark支持：批处理、交互式查询、流处理三大工恩个

![](https://i.postimg.cc/c1cySzmD/10-4.png)
![](https://i.postimg.cc/L66d6VQm/1.png)

## 9.3 Spark运行架构
### 9.3.1 基本概念
- RDD：Resilent Distributed Dataset，弹性分布式数据集，是分布式内存的一个抽象概念，提供了一种高度受限的共享内存模型
- DAG：Directed Acyclic Graph，有向无环图，反应RDD之间的依赖关系
- Executor：运行在工作节点Worker Node的一个进程，负责运行Task
- Application：用户编写的Spark应用
- Task：运行在Executor上的工作单元
- Job：一个Job包含多个RDD以及作用于相应RDD上的各种操作
- Stage：是Job的基本调度单位，一个Job会分为多组Task，每组Task被称为Stage，或者成为TaskSet，代表了一组关联的，相互之间没有Shuffle依赖关系的任务组成的任务集

### 9.3.2 架构设计
![](https://i.postimg.cc/C1T2kCFr/10-5.png)
Spark运行架构包括集群资源管理器（Cluster Manager）、运行作业任务的工作节点（Worker Node）、每个应用的任务控制节点（Driver）和每个工作节点上负责具体任务的执行进程（Executor）
- 资源管理器可以用自带的Mesos或YARN

与HadoopMapReduce计算框架相比，Spark采用的Executor有两个优点：
- 利用多线程来执行具体的任务（Hadoop MapReduce采用进程），减少任务的启动开销
- Executor有一个BlockManager存储模块，会将内存和磁盘共同作为存储设备，有效减少I/O开销

![](https://i.postimg.cc/SK0dvywJ/10-6.png)
- 一个Application由一个Driver和若干个Job构成，一个Job由多个Stage构成，一个Stage由多个没有Shuffle关系的Task组成
- 当执行一个Application时，Driver会向集群管理器申请资源，启动Executor，并向Executor发送应用程序代码和文件，然后在Executor上执行Task，运行结束后，执行结果会返回给Driver，或者写到HDFS或者其他数据库中

### 9.3.3 Spark运行基本流程
![](https://i.postimg.cc/j2WJTffK/10-7.png)
1. 当一个Spark应用被提交时，首先为这个应用构建基本的运行环境，即由任务控制节点（Driver）创建一个SparkContext，由SparkContext负责和资源管理器Cluster Manager的通信以及资源申请、任务的分配和监控等。SparkContext会向资源管理器注册并申请运行Executor的资源
2. 资源管理器为Executor分配资源，启动Executor进程，Executor运行情况会随着“心跳”发送给资源管理器
3. SparkContext根据RDD的依赖关系构建DAG图，DAG图提交给DAG调度器（DAG Scheduler）进行解析，将DAG图分解成多个Stage（任务集），并且计算出各个阶段之间的依赖关系，然后把一个一个TaskSet提交给底层的任务调度器（Task Scheduler）处理，Executor向SparkContext申请Task，任务调度器将Task发放给Executor并提供应用程序代码
4. Task在Executor上运行，把结果反馈给TaskScheduler，然后反馈给DAGScheduler，运行完毕后写入数据并释放资源

总而言之，Spark运行架构有以下特点：
- 每个Application都有自己专属的Executor进程，并且该进程在Application运行期间一直驻留。Executor进程以多线程的方式运行Task
- Spark运行过程与资源管理器无关，只要能够获取Executor进程并保持通信即可
- Task采用数据本地性和推测执行等优化机制

### 9.3.4 RDD的设计与运行原理
#### 9.3.4.1 RDD的设计背景
- 许多迭代算法和交互式数据挖掘，不同计算阶段之间会重用中间结果
- 目前MapReduce框架都是中间结果写入HDFS，带来大量的数据复制、磁盘I/O和序列化开销
- RDD提供一个抽象的数据框架，不必担心底层数据的分布式特性，只需将具体的应用逻辑表达为一系列转换处理，不同RDD之间的转换形成依赖关系，可以实现管道化，避免中间数据存储

#### 9.3.4.2 RDD概念
- 一个RDD就是一个分布式对象集合，本质上是一个只读的分区记录集合，每个RDD可分成多个分区，每个分区就是一个数据集片段，并且一个RDD的不同分区可以被保存在集群中的不同的节点上，从而可以在集群中的不同节点上进行并行计算
- RDD提供了一种高度受限（只读，不可修改除非从父RDD到子RDD）的共享内存模型，即RDD是只读的记录分区的集合，不能直接修改，只能基于稳定的物理存储中的数据集创建RDD，或者通过其他RDD上执行确定的转换操作（map、join、groupby）而创建
- RDD提供风负责运算操作，分为“动作Action”和“转换Transformation”两种，前者返回值，后者RDD
- RDD的转换接口都非常简单，类似map、filter、groupBy、join等粗粒度的数据转换操作，而不是针对某个数据项的细粒度修改（如网页爬虫，Web应用等）

**典型的执行过程如下**
- RDD读入外部数据源（或内存中的集合）进行创建
- RDD经过一系列“转换”操作，产生不同的RDD
- 最后的RDD经过“动作”操作，输出到外部数据源

![](https://i.postimg.cc/KcNMXZgR/10-8.png)
这一系列称为Lineage血缘关系，即DAG拓扑排序的结果

优点：惰性调用、管道化、避免同步等待、不需要保存中间结果

### 注意：惰性调用！ ###
对于“行动”前的“转换”操作，都是逻辑上的概念，Spark只记录下生成轨迹，直到上图F，即动作操作时，才真正开始计算。

#### 9.3.4.3 RDD特性
1. 高效的容错性
- 现有的容错机制：数据复制或者记录日志
- RDD：血缘关系、重新计算丢失分区、无需回滚系统、重算过程在不同节点并行、只计算粗粒度操作
2. 中间结果持久化到内存，数据在内存中的多个RDD操作之间进行传递，避免了不必要的读写磁盘开销
3. 存放的数据可以使Java对象，避免了不必要的对象序列化和反序列化

#### 9.3.4.4 RDD之间的依赖关系
RDD中的依赖关系分为窄依赖（Narrow Dependency）与宽依赖（Wide Dependency），两种依赖之间的区别如图所示：
![](https://i.postimg.cc/66vBRzBw/10-9.png)

主要差别就在于：父RDD**分区**会被拆分成几个子RDD分区，多个就是宽依赖

对于窄依赖，可以采用流水线形式处理所有分区，没有网络的数据混合；  
对于宽依赖，通常伴随Shuffle操作，首先需要计算好所有的父分区，然后进行Shuffle

Spark具备天生的容错性，因为这个转换链，如果数据丢失，可以直接重新计算。同时Spark提供数据检查点和记录日志，用于持久化RDD，再一次提高效率

#### 9.3.4.5 Stage的划分
Spark通过分析RDD的依赖关系生成DAG，在分析各个RDD中分区之间的依赖关系决定Stage：
- 在DAG中反向解析，遇到宽依赖就断开
- 遇到窄依赖就把当前RDD加入到Stage中
- 将窄依赖尽量划分在一个Stage中，实现流水线计算

![](https://i.postimg.cc/MTrXTG0D/10-10.png)
由图可知，每个阶段代表一组关联的、相互之间没有Shuffle依赖关系的任务组成的任务集合

Stage类型包括两种：ShuffleMapStage和ResultStage：
- ShuffleMapStage：不是最终的Stage，它的输出经过Shuffle过程，并作为后续Stage的输入。
- ResultStage：最终的Stage，没有输出，而是直接产生结果或存储。在一个Job里必定包含该类型Stage。

#### 9.3.4.6 RDD运行过程
1. 创建RDD对象
2. SparkContext负责计算RDD之间的依赖关系，构建DAG
3. DAGScheduler负责把图分解成多个Stage，每个Stage包含多个Task，每个Task被TaskScheduler分发给各个WorkerNode上的Executor执行

![](https://i.postimg.cc/j5TB76cq/10-11.png)

