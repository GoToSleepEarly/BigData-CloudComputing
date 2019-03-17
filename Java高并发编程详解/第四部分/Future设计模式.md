# Future设计模式
## 19.1 先给你一张凭据
假设某个任务执行需要很长时间，通常需要等待任务执行结束或者任务异常出错才能返回，在此期间只能陷入阻塞等待。Future设计模式提供了一种凭据式的方案，提交任务时会给一个凭据，此凭据就是Future，在之后可以凭借Future获得结果。  
自JDK1.5起，Java提供了强大的Future接口，JDK1.8引入了CompletableFuture，结合函数式编程可实现更强大的功能。
## 19.2 Future设计模式实现
Future设计模式的UML图。
![](https://i.postimg.cc/v8hnmW7V/19-1.png)
![](https://i.postimg.cc/TYPD5pQH/19-2.png)

### 19.2.1 接口定义
1. Future接口设计
Future，也就是凭据，提供了获取计算结果和判断任务是否完成的两个方法的接口。其中获取结果将会导致阻塞（如何还未完成），见*Future.java*
2. FutureService接口设计  
FutureService主要用于提交任务，一种是需要返回结果的，另一种是不需要的。接口中提供了对FutureServiceImpl构建的工厂方法，JDK8不仅支持default方法还支持静态方法，JDK9甚至支持接口私有方法。见*FutureService.java*
3. Task接口设计  
Task主要给调用者实现计算逻辑使用，可以接受一个参数并返回计算结果，类似JDK1.5的Callable接口，见*Task.java*

### 19.2.2 程序实现
1. FutureTask  
FutureTask是Future的一个实现，除了Future的get和done方法外，还有protected方法finish，主要用于接收任务被完成的通知。见*FutureTask.java*  
充分利用线程间的wait和notifyAll,当任务没有完成前使用get将会导致调用者进入阻塞状态，直到被唤醒(finish收到任务完成的通知，唤醒get阻塞的线程)
2. FutureServiceImpl  
submit方法中，会启动新的线程，起到了异步的作用，任务结束后会通知future。见*FutureServiceImpl.java*
## 19.3 Future的使用以及技巧总结
见*FutureTest.java*  
Future将一些耗时工作交给另一个线程去执行，从而达到异步的目的，提交线程在提交任务和获得计算结果之间可以进行其他的任务操作。  
但是，虽然提交时不阻塞，但是当调用者使用get获取结果时，还有可能陷入阻塞直到任务完成，这个问题在JDK1.5时就存在，在JDK1.8时引入CompletableFuture才得到增强。
## 19.4 增强FutureService使其支持回调
见*FutureTest.java*  
使用任务完成时回调机制可以让调用者不再显式使用get方法，而是将调用接口一并注入。修改后的submit方法，增加了一个CallBack参数，当执行完成后会将结果交给CallBack接口进行下一步操作，从而避免显式调用get进入阻塞。
## 19.5 本章总结
本章实现的Future设计模式存在以下缺陷：
- 将提交的任务交给线程池
- Get方法没有超时功能
- Future未提供cancel功能
- 任务出错未提供回调功能

### 需求：调用者提交一个线程，得到一个凭据，通过凭据获得结果
### 实现：构造一个service，可以submit任务(我们的线程)，得到future凭据，submit里面新开线程运行任务，任务结束时将结果传回future，调用者用future.get得到结果
### 具体：future接口有get和done方法，在具体实现是需要有个finish方法，通知future完成了，从而唤醒get阻塞的线程。Service里面需要多个submit方法，submit内部新建future，将提交的任务交给新线程，并返回future，执行结束调用finish通知future，




