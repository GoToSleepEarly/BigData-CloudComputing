# 第27章 Active Object 设计模式
## 27.1 接受异步消息的主动对象
Active Object 模式是一种异步编程模式。它通过对方法的调用与方法的执行进行解耦来提高并发性。若以任务的概念来说，Active Object 模式的核心则是它允许任务的提交（相当于对异步方法的调用）和任务的执行（相当于异步方法的真正执行）分离。这有点类似于 System.gc() 这个方法：客户端代码调用完 gc() 后，一个进行垃圾回收的任务被提交，但此时 JVM 并不一定进行了垃圾回收，而可能是在 gc() 方法调用返回后的某段时间才开始执行任务——回收垃圾。我们知道，System.gc() 的调用方代码是运行在自己的线程上（通常是 main 线程派生的子线程），而 JVM 的垃圾回收这个动作则由专门的线程（垃圾回收线程）来执行的。换言之，System.gc() 这个方法所代表的动作（其所定义的功能）的调用方和执行方是运行在不同的线程中的，从而提高了并发性。
![](https://i.postimg.cc/4yZv5sJy/27-1.png)

再进一步介绍 Active Object 模式，我们可先简单地将其核心理解为一个名为 ActiveObject 的类，该类对外暴露了一些异步方法，如图 1 所示。
![](https://i.postimg.cc/28RMsZ5L/27-1.png)

doSomething 方法的调用方和执行方运行在各自的线程上。在并发的环境下，doSomething 方法会被多个线程调用。这时所需的线程安全控制封装在 doSomething 方法背后，使得调用方代码无需关心这点，从而简化了调用方代码：从调用方代码来看，调用一个 Active Object 对象的方法与调用普通 Java 对象的方法并无太大差别。如清单 1 所示。

    ActiveObject ao=...;
    Future future = ao.doSomething("data");
    // 执行其它操作 
    String result = future.get();
    System.out.println(result);

## 27.2 标准Active Objects模式设计
![](https://i.postimg.cc/Wb5mMBxp/27-2.png)

### 执行流程：
当某个线程调用OrderService接口的findOrderDetails方法时，实际上是发送一个包含findOrderDetails方法参数以及OrderService具体实现的Message至Message队列，执行队列线程通过从队列中获取Message来调用具体的实现，接口方法的调用和接口方法的执行分别处于不同的线程中。

![](https://i.postimg.cc/FKLjX7z1/27-3.png)

### 先捋一下思路
我们有OrderService，负责提交订单(order方法)和查询订单详情(findOrderDetails方法)，现在需要调用orderService(OrderService的实例化对象)的方法，但是却是后台异步执行。

所以我们现在有一个OrderServiceProxy继承OrderService，通过多态，我们可以保留orderService的方法，但实际上，比如OrderService.order(XX)是调用OrderServiceProxy的方法。他会将任务封装成MethodMessage，放到Queue里，由后台ActiveDaemonThread提取执行。

也是因为这个道理，OrderServiceProxy构造函数需要传入的参数是OrderService类型(实际是OrderServiceImpl，因为该类有具体的方法)和ActiveMessageQueue。

因为是异步执行，所以肯定会用到前面所说的Future接口，所以ActiveFuture其实就是"凭据"，用来获取结果的。

Queue里面存放的是MethodMessage，对应于OrderService提供的两个方法，所以MethodMessage有两个子类(FindDetail和Order)。这里用到了WorkerThread的思想，MethodMessage封装了函数所需的参数和execute方法。

最后以FindOrderDetailsMessage为例，他的构造函数肯定需要2个，一个是OrderService的findDetailsMessage方法(OrderServiceImpl写的，多态获得)，另一个是参数Map。在execute方法中，首先调用findOrderDetails方法，得到realFuture凭据（OrderServiceImpl定义的返回类型，可以得到异步的结果），再通过Map得到activeFuture(调用者实际得到的是这个)。realFuture.get方法会使方法阻塞直到运行结束，最后activeFuture再通知任务执行结束。

这里为什么不能返回Future而是自定义ActiveFuture呢？因为OrderServiceProxy并没有调用逻辑代码，所以没有Future，所以我们没有"凭据"返回给调用者。

![](https://i.postimg.cc/4NKX01Kv/Order-Service.png)
### 27.2.1 OrderService接口设计
OrderService是一个简单的接口，见*OrderService.java*
- findOrderDetails( long orderId)：通过订单编号获取订单详情，有返回值的方法必须是Future类型的，因为方法的执行是在其他线程中进行的。
- Order(String account, long orderId)：提交用户的订单信息，是一种无返回值的方法。

### 27.2.2 OrderServiceImpl详解
OrderService类是OrderService的一个具体实现，该类是在执行线程中将要被使用的类，其中findOrderDetails方法通过Future立即返回，order方法则通过休眠来模拟该方法的执行比较耗时，见*OrderServiceImpl.java*

### 27.2.3 OrderServiceProxy详解
OrderServiceProxy是OrderService的子类，他的作用是将OrderService的每一个方法都封装成MethodMessage，然后提交给ActiveMessage队列，在使用OrderService接口方法时，实际上是在调用OrderServiceProxy中的方法。  

OrderServiceProxy主要作用是将OrderService接口定义的方法封装成MethodMessage，然后offer给ActiveMessageQueue。见*OrderServiceProxy.java*

*ActiveFuture.java*的作用是立即返回，当调用结果时会陷入阻塞，它是FutureTask的直接子类，重写finish，把protected改成public，使得执行线程完成后传递最终结果。

### 27.2.4 MethodMessage
MethodMessage的主要作用是收集每个接口方法参数，并且提供execute方法供ActiveDaemonThread直接调用，这是典型的**WorkerThread**中的Product（附有说明书的半成品，等待流水线工人的加工），execute方法则是加工说明书。见*MethodMessage.java*。

其中params主要用来收集方法参数，OrderService是具体的实现接口，每一个方法都被拆分成不同的Message，在OrderService中，我们定义了两个方法，所以需要实现两个MethodMessage。

#### 1）FindOrderDetailsMessage
对应于findOrderDetails方法，见*FindOrderDetailsMessage.java*
#### 2）OrderMessage
对应于order方法，从params中获取参数，然后执行真正的OrderService的order方法。

### 27.2.5 ActiveMessageQueue
-  创建ActiveMessageQueue同时启动一个ActiveDaemonThread线程，主要用来异步执行方法
-  offer没有限制，允许提交无限个MethodMessage，并且有新的Message加入会通知ActiveDaemonThread线程
-  take主要是给ActiveDaemonThread使用，当messages队列为空，将会被挂起（GuardedSuspension）

见*ActiveMessageQueue.java*和*ActiveDaemonThread.java*

### 27.2.6 OrderServiceFactory及测试
接口方法的每一次调用实际上都是向Queue中提交了一个对应的Message信息，这个工作由Proxy完成，但是为了让Proxy的构造透明化，我们需要设计一个Factory工具类。见*OrderServiceFactory.java*

## 27.3 通用Active Objects框架设计
标准的Active Objects要将每一个方法都封装成Message，然后提交至Message队列中，这样的做法有点类似于RPC（Remote Process Call）。如果某个接口方法很多，那么需要封装很多Message类，同样如果很多接口需要成为Active Objects，则需要封装非常多的Message类，这样很不友好。下面开始设计一个更加通用的框架，可以把任意接口转换成Active Object。

我们将采用JDK动态代理的方式。首先定义IllegalActiveMethod异常，见*IllegalActiveMethod.java*

通用的Active Objects设计消除了为每一个接口定义MethodMessage的过程，同时以抛弃了为每一个接口创建定义Proxy的实现，所有的操作会被支持动态代理的工厂类ActiveServiceFactory替代，框架如下。
![](https://i.postimg.cc/KzJwksZw/27-4.png)
## 捋一下思路：
我们通过使用Annotation注解来解决这个问题。首先，我们需要一个实例对象(就是写明具体逻辑的类的实例化对象，将需要的方法@注解)，利用active方法返回一个Proxy（Java提供的类，我具体见代码，需要重写Handler），然后调用该proxy的方法即可做到。

Proxy做了什么处理呢？动态代理Proxy，调用方法时会转向invoke方法，所以我在invoke里会检查标签，再进一步处理，这是我们需要写的逻辑（比如放入queue中，判断方法参数合理等）。

为了方便ActiveMessage的统一（上一节说的复杂化问题），我们采用Builder设计模式。

### 27.3.1 ActiveMessage详解
相比较于MethodMessage，ActiveMessage更加通用，其可以满足所有Active Objects接口方法的要求，与MethodMessage类似，ActiveMessage也是用于收集接口方法信息和具体方法调用。见*ActiveMessage.java*

## 重点理解ActiveMessage.java如何实现的！
构造ActiveMessage必须通过Builder方式进行，其中包含了调用某个方法必须的入参（objects），代表该方法的java.lang.reflect.Method实例，将要执行的ActiveService实例（service），以及如果有返回值，需要返回的Future实例（future）。

### 27.3.2 @ActiveMethod
通用的Active Objects更加灵活，它允许将某个接口的任意方法转换成ActiveMethod，如果不需要转换，则按照普通方法执行，而不会被线程单独执行。我们通过@ActiveMethod注解来标记。见*ActiveMethod.java*

我们就可以这么用：

	@ActiveMethod
	@Override
	public Future<String> findOrderDetails(long orderId) {
	}

### 27.3.3 ActiveServiceFactory详解
ActiveServiceFactory是通用Active Objects的核心类，其负责生成Service的代理以及构建ActiveMessage，见*ActiveServiceFactory.java*

- 静态方法active()会根据Active Service实例生成一个动态代理实例，其中会用到ActiveInvocationHandler作为newProxyInstance的InvocationHandler。
- 在invoke方法中，首先会判断是否被@ActiveMethod标记，如果没有则被当做正常方法使用。
- 如果被标记，则判断方法是否符合规范，有返回类型就必须是Future类型。
- 定义ActiveMessage.Builder分别使用method，方法参数数组以及ActiveService实例，如果该方法是Future的返回类型，则还需要定义ActiveFuture。
- 最后将ActiveMessage插入ActiveMessageQueue中，并且返回method方法invoke结果。

### 27.3.4 ActiveMessageQueue及其他
修改ActiveMessageQueue，见*ActiveMessageQueue2.java*
修改ActiveDaemonThread，见*ActiveDaemonThread.java*
测试代码如下：

	OrderService orderService = ActiveServiceFactory.active(new OrderServiceImpl());
	Future<String> future = orderService.findOrderDetails(2343);
	System.out.println("立马返回！");
	System.out.println(future.get());

## 27.4 本章总结
Active Objects模式技能完整的保留接口方法的调用模式，又能让方法的执行异步化，这也是其他接口异步调用模式无法做到的。

关于@注解和动态代理Proxy，见同目录
## 注解概要.md
## 动态代理概要.md