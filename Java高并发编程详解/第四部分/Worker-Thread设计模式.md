# 第26章 Worker-Thread设计模式
## 26.1 什么是Worker-Thread模式
Worker-Thread模式也称流水线设计模式，Worker的意思是工作的人，在Worker-Thread模式中，工人线程Worker thread会逐个取回工作并进行处理，当所有工作全部完成后，工人线程会等待新的工作到来。

Worker Thread模式也被成为Background Thread（背景线程）模式，另外，如果从保存多个工人线程的场所这一点看，我们也可以称这种模式为Thread Pool模式。线程初始化创建的线程类似于在流水线等待工作的工人，提交给线程池Runnable接口类似于需要加工的产品，而Runnable的run方法相当于组装该产品的说明书。

## 26.2 Worker-Thread模式实现
- 流水线工人：对传送带的产品加工
- 流水线传送带：用于传送来自上有的产品
- 产品组装说明书：用来说明该产品如何组装

### 26.2.1 产品及组装说明书
抽象类InstructionBook，代表组装产品的说明书，其中经过流水线传送带的产品将通过create方法加工，而firstProcess和secondProcess代表加工步骤，这就是说明书的作用，见*InstructionBook.java*

传送带的产品除了说明书还需要产品自身，所以产品继承了说明书，每个产品有产品编号，见*Production.java*

### 26.2.2 流水线传送带
流水线的传送带主要用于传送代加工产品，上游的工人将完成的半成品放到传送带上，工作人员从传送带上取下产品再次加工，见*ProductionChannel.java*，和ThreadPool一样理解即可。创建初始数量的工人线程拿取产品加工，上游拿和下游取都需要有数量比较，不满足则阻塞。

### 26.2.3 流水线工人
流水线工人是Thread的子类，不断提取产再次加工，即调用create方法，见*Worker.java*

### 26.3.1 产品流水线测试
测试案例中，我们假设有8个工作人员往流水线上放产品，5个工人进行再加工，见*Test.java*
结果如下：
> Worker-4  process the  PROD:0  
> Worker-3  process the  PROD:1  
> execute the 0 first process ...   
> execute the 1 first process ...   
> execute the 0 second process ...   
> execute the 1 second process ...  

### 26.3.2 Worker-Thread和Producer-Consumer
Worker-Thread和Producer-Consumer很容易记混，下面我们来区分一下。
#### 1）Producer-Consumer
![](https://i.postimg.cc/cCd7cXFS/producerconsumer.png)
Producer和Consumer对Queue都是**依赖关系**，Queue既不知道Producer的存在，也不知道Consumer的存在。另外，**Consumer对Queue的消费并不依赖于数据本身的方法。**

#### 2）Worker-Thread
![](https://i.postimg.cc/j2vH3P1J/workerthread.png)
Worker对于Channel是**聚合关系**(Channel创建Worker)，所以Channel必须知道Worker的存在。另外，**Worker对数据的消费依赖于Production自身的create方法**。

