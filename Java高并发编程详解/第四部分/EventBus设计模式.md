# Event Bus设计模式
EventBus是一个 发布/订阅 模式的消息总线库，它简化了应用程序内各组件间、组件与后台线程间的通信，解耦了事件的发送者和接收者，避免了复杂的、易于出错的依赖及生命周期问题，可以使我们的代码更加简洁、健壮。

消息中间件，如Apache ActiveMQ 和 Apache Kafka等，某subscriber在消息中间件上注册了某个topic，当有消息发送到topic上之后，注册在该topic的所有subscriber都将会收到信息。
![](https://i.postimg.cc/7Z2p2Q2Q/28-1.png)
![](https://i.postimg.cc/6QxFLLV6/28-1-1.png)
我们可以将事件监听者的管理，注册监听者、移除监听者，事件发布等方法等都交给EventBus来完成，而只定义事件类，实现事件处理方法即可。这是**解决进程之间消息异步处理的解决方案。**

## 28.1 Event Bus 设计
![](https://i.postimg.cc/C1LPmvR0/28-2.png)
- Bus接口对外提供了几种主要的使用方式，比如post方法用来发送到Event，register方法用来注册Event接受者（Subscriber）接受响应事件，EventBus采用同步的方式推送Event，AsyncEventBus采用异步的方式（Thread-Per-Message）推送Event
- Register注册表，主要用来记录对应的Subscriber以及受理消息的回调方法，回调方法我们用注解@Subscriber来标识
- Dispatcher主要用来将event广播给注册表中监听了topic的Subscriber

同步执行逻辑：
![](https://i.postimg.cc/76dRh3Ly/28-2-1.png)

异步执行逻辑：
![](https://i.postimg.cc/HxVKTZd2/28-2-2.png)

### 28.1.1 Bus接口详解
见*Bus.java*
- register(Object subscriber)：将某个对象实例注册到EventBus
- unregister(Object subscriber)：取消注册，会删除注册表Registry中的该实例
- post(Object event)：提交Event到EventBus中的默认topic
- post(Object event, String topic)：提高到指定Event
- close()：销毁该EventBus
- getBusName()：返回EventBus的名称

注册对象给EventBus的时候需要指定接受消息时的回调方法（Topic），我们采用注解解决，见*Subscribe.java*

@Subscribe要求标注类中的方法，注解时可指定topic，不指定的情况下为默认topic。

### 28.1.2 同步EventBus详解
同步EventBus是最核心的一个类，它实现了Bus的所有功能，但是该类对Event的广播推送采用的是同步的方式，如果想要使用异步的方式进行推送，可使用EventBus的子类AsyncEventBus，见*EventBus.java*
- EventBus的构造除了名称之外，还需要有ExceptionHandler和Executor，后两个主要给Dispatcher使用
- register和unregister都是通过Subscriber注册表完成的
- Event的提交post则是由Dispatcher完成的
- Executor并没有用线程池，而是用JDK的Executor接口。因为线程池无法做到同步处理，接口不统一。

### 28.1.3 异步EventBus详解
异步的EventBus比较简单，继承自同步Bus，然后用Thread-Per-Message用异步处理的Executor代替同步Executor，见*AsyncEventBus.java*

具体差别见代码，只是将Executor替换成了ThreadPoolExecutor。

#### 28.1.4 Subscriber注册表Registry详解
注册表维护了topic和subscriber之间的关系，当有Event被post之后，Dispatcher需要知道该消息应该发送给哪个Subscriber的实例和对应的方法，Subscriber对象没有任何特殊要求，见*Registry.java*

由于Registry是在Bus中使用，不能暴露给外部，因此Registry被设计成了包可见的类，我们所设计的EventBus对Subscriber没有任何限制，但是要接受Event的回调需要将方法用@Subscribe标记，同一个Subscriber的不同方法可以接受不同的Topic，由@Subscribe指定。

#### 分析：Registry里面包含了<Topic , SubscriberList>的map。当要bind时，先获取subscriber里所有@Subscribe的方法，然后根据topic存入map里。unbind则是遍历map将subscriber设置为disable。

当然，为了方便，里面定义了两个函数getSubscribeMethod，其逻辑是：获取当前subscriber的所有方法，当方法被@subscribe注解，且只有一个入参且是public，则加入一个list，最后返回；tierSubscriber逻辑为：获取参数subscriber的注解类subscribe，放入相应的topic对应的list。

### 28.1.5 Event广播Dispatcher详解
Dispatcher的作用就是将EventBus post的event推送给每一个注册到topic的subscriber上，具体的推送其实是执行被@Subscribe详解的方法，见*Dispatcher.java*

在Dispatcher中，除了从Registry中获取对应的Subscriber执行外，我们还定义了几个静态内部类，主要实现了JDK1.5以后的Executor接口和EventContent。

### 28.1.6 其他类接口设计
#### 1）Subscriber类
Subscriber类封装了对象实例和被@Subscribe标记的方法，也就是说，一个实例对象有可能被封装成若干个Subscriber。见*Subscriber.java*
#### 2）EventExceptionHandler接口
EventBus将方法的调用交给Runnable接口去执行，我们知道Runnable接口不能抛出checked异常信息，并且在每一个subscribe方法中，也不允许将异常抛出从而影响EventBus对后续Subscriber进行消息推送，但是异常信息又不能被忽略掉，因此注册一个异常回调接口就可以知道在进行消息推送时发生了什么。见*EventExceptionHandler.java*
#### 3）EventContext接口
Event接口提供了获取消息源，消息体，以及该消息由哪一个Subscriber的哪个subscribe方法接受，主要用于消息推送出错时，被回调接口EventExceptionHandler使用，见*EventContext.java*

### 理一下思路：我们的要求是，构造一个EventBus，每当post一个topic的event时，注解@subscribe相应topic的subscriber的方法将会被调用。
### EventBus有register和unregister方法，这两个方法和Registry的bind和unbind相关（参数都是Object，意味着是我们自己写的subscriber，下面称为subscriberObject）。当然，EventBus有post方法，这会用到Dispatcher的dispatch方法。
### Registry类会统计topic和Subscriber（注意，这里的Subscriber是封装了我们自己写的subscriberObject和method的一个类）。当bind时，会先搜索subscriberObject的注解了@subscribe的方法，然后把每个method和topic绑定，存入到之前的map中。unbind方法只是单纯的把subscribe setDisable而已。源代码用了较多的java 8流处理和lambda表达式，可以多研究
### Dispatcher参数主要有，Executor执行方法，EventExceptionHandler处理异常。dispatch方法首先获得map里相关的subscriber，通过反射方法调用即可。

## 28.3 本章总结
EventBus看着像GOF设计模式中的监听模式，但是其实更加的强大。Bus、Registry、Dispatcher是三大组件，可以着重看看如何实现。

