# 第七章 MapReduce

### Tez告诉我们:Map有Input，Processer，Sort，Merge和Output；Reduce有Input，Shuffle，Sort，Merge，Processer和Output等。
## 7.1 概述
在MapReduce出现之前，已经有像MPI这样成熟的并行计算框架了，那么MapReduce和其的去呗在哪？
![](https://i.postimg.cc/PrjrHq71/7-1-1.png)

## 7.1.2 MapReduce模型简介
- MapReduce将复杂的并行计算抽象为Map和Reduce函数
- MapReduce采用“分而治之”策略，一个大规模数据集会被切分成许多分片
- MapReduce的设计理念是“计算向数据靠拢”，因为移动数据需要大量网络开销
- MapReduce采用Master/Slave架构，包括一个Master和若干Slave，Master运行JobTracker，Slave运行TaskTracker
- Hadoop采用Java，但MapReduce不一定

### 7.1.3 Map和Reduce函数
![](https://i.postimg.cc/vBYLCMsy/7-1.png)

## 7.2 MapReduce的体系结构
MapReduce主要由以下4部分组成
![](https://i.postimg.cc/N0gHj4P7/7-2-1.png)
1. Client
- 用户编写的MapReduce程序通过Client提交到JobTracker
- 用户可通过Client提供的接口查看运行状态

2. JobTracker
- JobTracker负责资源监控和作业调度
- JobTracker负责所有TaskTracker与Job的健康状况，一旦发现失败，就将相应的任务转移到其他节点
- JobTracker会跟踪任务的执行进度、资源使用量等信息，并告诉任务调度器（TaskScheduler），而调度器会在资源空闲时，选择合适的任务去使用这些资源。

3. TaskTracker
- TaskTracker会周期性的通过“心跳”将本节点上资源情况和任务进度汇报给JobTracker，同时接受JobTracker发送过来的命令并执行相应的操作（如启动新任务、杀死任务）
- TaskTracker会使用“slot”等量划分本节点上的资源量（CPU、内存等）。一个Task获取到一个slot后才有机会运行，而Hadoop调度器的作用就是将各个TaskTracker上的空闲slot分配给Task使用。slot分为MapSlot和ReduceSlot两种

4. Task
- Task分为Map Task 和 Reduce Task，均由Task Tracker启动

## 7.3 MapReduce工作流程
### 7.3.1 工作流程概述
![](https://i.postimg.cc/QCWBynHs/7-1.png)
计算向数据移动  ->  Map任务通常运行在数据存储的节点上   
只有当全部Map结束后，Reduce才开始，中间结果存储在本地磁盘

- 不同Map任务不会进行通信
- 不同的Reduce任务也不会信息交换
- 用户不能显式从一台机器向一台机器发送消息
- 所有数据交换都是通过MapReduce框架自身实现的

![](https://i.postimg.cc/5yRtb5Qg/7-2.png)

1. MapReduce使用InputFormat模块做Map前的预处理，如验证输入格式等；然后将输入文件切分为逻辑上的多个InputSplit，是一个逻辑概念的文件输入单位，没有实际切割，而是记录了要处理的数据的位置和长度
2. 因为InputSplit不是物理切分，所以还需要RecordReader（RR）根据InputSplit中的信息来处理InputSplit中的具体记录，加载数据并转换为适合Map的键值对
3. Map任务会根据用户规则输出<Key,Value>作为中间结果
4. 为了让Reduce并行处理，需要对Map的输出进行分区（Partition）、排序（Sort）、合并（Combine）、归并（Merge）等操作，得到<Key,Value-list>的中间结果，再交给对应的Reduce，这个过程称为**Shuffle**，从无序的<Key-Value>到<Key,Value-list>
5. Reduce以<Key,Value-list>为输入执行用户逻辑，并输出给OutputFormat模块
6. OutputFormat会验证输出目录是否存在以及满足配置。

### 7.2.3 Shuffle过程详解
![](https://i.postimg.cc/mDrRkzs5/7-3.png)
Map的输出结果首先写入缓存，缓存满就启动溢写，写入磁盘并清空缓存。当启动溢写操作时，需先把缓存中的数据进行分区，然后对每个分区进行排序和合并，之后再写入磁盘。随着Map的执行，多个一些文件会被Merge成一个大的磁盘文件，然后通知Reduce来领取。

Reduce任务从Map端的不同Map机器领回自己的部分，然后进行归并，再交给Reduce处理

#### 7.2.3.1 Map端的Shuffle过程
![](https://i.postimg.cc/GtF1SkJ9/7-4.png)
1. **输入数据和执行Map任务**  
Map任务的输入数据一般保存在分布式文件系统中。接受<Key,Value>作为输入后，按规则生成一批<Key,Value>

2. **写入缓存**  
Map的输出结果先写入缓存，再一次性批量写入磁盘。（如果每次都直接写入磁盘，寻址开销很大）。在写入缓存前，key和value都会被序列化成字节数组。

3. **溢写（分区、排序和合并）**  
MapReduce默认100MB缓存，缓存满时，就会写入磁盘。为了保证不影响缓存写入，溢写比例设置一般为0.8。  
但是在写入磁盘前，缓存中的数据首先会被分区（Partition）。缓存中的数据是<Key,Value>形式的键值对，为了交给不同的Reduce任务，MapReduce通过Partitioner接口进行分区，默认采用Hash函数对key进行哈希后再用Reduce任务的数量取模，hash(key) mod R，这样可以平均分配。  
对于分区内的键值对，后台根据key对他们进行内存排序(Sort)，是默认操作。排序结束后有一个可选的合并（Combine）操作。  
所谓合并，就是具有相同key的<key,value>的value加起来，比如<"a",1>和<"a",1>，合并后为<"a",2>。**合并不能影响Reduce的结果**，一般累加，最大值等场景采用。而归并(Merge)是最后一步，的结果是<"a",<1,1>>，这是Map输出的<key,value-list>。

4. **文件归并**  
在Map任务全部结束前，系统对所有溢写文件进行归并(Merge)，生成一个大溢写文件，这个文件也是经过分区排序的。  
归并会将<Key,Value>整合成<key,value-list>。文件归并时，如果溢写文件数量大于预定值（默认为3），则会再次启动Combiner，减少写入磁盘的数据量。  
JobTracker会一直检测Map任务的执行，并通知Reduce任务来领取数据。

#### 7.2.3.2 Reduce端的Shuffle过程
![](https://i.postimg.cc/T2C0nyXc/5.png)

1. **“领取”数据**  
Reduce任务不断地通过RPC向JobTracker询问Map任务是否已经完成；JobTracker检测到一个Map任务完成后，就会通知Reduce任务来“领取数据”。一般系统存在多个Map机器，因此Reduce任务会多线程同时领回。

2. **归并数据**  
Reduce领取数据先放入缓存，来自不同机器，来自同一分区，所以先归并再合并，写入磁盘。多个溢写文件归并成一个或多个大文件，键值对是排序的。

3. **数据输入给Reduce任务**  
Reduce进行操作，并写入分布式文件系统。

![](https://i.postimg.cc/HnqVb4Wx/7-3-4.png)

## 7.4 WordCount实例
![](https://i.postimg.cc/c1rXWQj8/7-8.png)
![](https://i.postimg.cc/T3X9nBPr/7-9.png)

## 7.5 MapReduce具体应用
1. 关系代数运算（选择、投影、并、交、差）：都比较简单，合理设计Map和Reduce即可。
2. 关系的自然连接：![](https://i.postimg.cc/Z57WJyXC/image.png)
3. 分组和聚合：典型的Map-Reduce
4. 矩阵乘法：两个MapReduce，具体百度。