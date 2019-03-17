# 第29章 Event Driven设计模式
## 29.1 Event-Driven Architecture基础
EDA是一种以事件为媒介，实现组件或服务之间最大松耦合的方式。传统面向接口编程是以接口为媒介，实现调用接口者和接口实现者之间的解耦，但是这种解耦程度不是很高，如果接口发生变化，双方代码都需要变动，而事件驱动则是调用者和被调用者互相不知道对方，两者只和中间消息队列耦合。

EDA（Event-Driven Architecutre）一种实现组件之间松耦合、易扩展的架构方式.

- Event ： 需要被处理的数据

- Event Handlers ： 处理Events的方式方法

- Event Loop ： 维护Events 和Event Handlers 之间的交互流程。

![](https://i.postimg.cc/htzMBShX/29-1.png)

### 29.1.1 Events
Events是EDA中的重要角色，一个Event至少需要包含两个属性：**类型和数据**，Event的类型决定了他被哪个Handler处理，数据是在Handler中代加工的材料。

### 29.2.2 Event Handlers 
Event Handlers 主要用于处理Event，比如一些filtering或者transforming数据的操作等

### 29.2.3 Event Loop
Event Loop处理接收到的所有Event，并且将它们分配给合适的Handler去处理。

在Event Loop中，一个Event都将从Queue中移除出去，通过类型匹配交给合适的Handler去处理

	package chapter29;
		
	import java.util.LinkedList;
	import java.util.Queue;
	  
	class Event {
	 
	    private final String type ;
	    private final String data ;
	 
	    public Event(String type , String data){
	        this.data = data ;
	        this.type = type ;
	    }
	 
	    public String getType() {
	        return type;
	    }
	 
	    public String getData() {
	        return data;
	    }
	}
	 
	public class FooEventDrivenExample {
	 
	    public static void  handleEventA(Event e){
	        System.out.println(e.getData() .toLowerCase() );
	    }
	 
	    public static void  handleEventB(Event e){
	        System.out.println(e.getData().toUpperCase() );
	    }
	 
	    public static void main(String[] args) {
	        Queue<Event> events = new LinkedList<>();
	        events.add(new Event("A","Hello")) ;
	        events.add(new Event("A","I am Event A")) ;
	 
	        events.add(new Event("B","I am Event B")) ;
	        events.add(new Event("B","World")) ;
	 
	        Event e ;
	 
	        while( !events.isEmpty()){
	            e = events.remove() ;
	            switch (e.getType()){
	                case "A" :
	                    handleEventA(e);
	                    break;
	                case "B" :
	                    handleEventB(e);
	                    break;
	            }
	        }
	    }
	}

## 29.2 开发一个Event-Driven框架
设计几个重要的组件：Event时间消息，Handler处理器，queue消息管道，EventLoop分配消息

## 29.2.1 同步EDA框架设计
### 1）Message
Message是对Event更高层的抽象，用于与对应的Handler做对应。见 *Message.java*

### 2）Channel
Channel主要用于接受来自EventLoop分配的消息，每一个Channel负责处理一种类型的消息，见*Channel.java*

### 3）Dynamic Router
Router的作用类似EventLoop，主要是帮助Event找到合适的Channel并且传送给他，见*Dynamic Router.java*。

Router需要了解到Channel的存在，因此registerChannel方法的作用是将相应的Channel注册给Router，dispatch方法则是根据Message的类型进行匹配。

### 4）Event
Event是对Message的一个简单实现，之后可以直接作为其他Message的基类，见*Event.java*

### 5）EventDispatcher
EventDispatcher是对DynamicRouter的一个最基本的实现，适合单线程的情况下运行，因此不考虑线程安全，见*EventDispatcher.java*

如果没有与Message对应的Channel，则会抛出无法匹配的异常，见*MessageMatcherExce.java*

### 6）测试
见*EventDispatcherExample.java*。

InputMessage是一个Message，包含了两个Int属性，而InputEventHandler是对InputEvent消息的处理，接收到InputEvent消息后，分别对X和Y进行相加操作，然后封装成ResultEvent提交给EventDispatcher，ResultEventHandler将结果打印在输出控制台上。

EDA的设计出了松耦合外，扩展性也很强，比如Channel非常容易扩展和替换，另外由于Dispatcher统一负责Event的调配，因此在消息通过Channel之前可以进行很多过滤，数据验证，权限控制，数据增强（Enhance）等工作。

![](https://i.postimg.cc/YC2ctLYd/29-2.png)

### 29.2.2 异步EDA框架设计
上面的EDA框架存在两个问题：
- EventDispatcher不是线程安全的类，在多线程的情况下，registerChannel方法会引起数据不一致的问题
- 我们实现的Channel无法并发消费Message，比如InputEventHandler只能逐个处理Message，低延迟的消息处理还会导致Dispatcher出现积压

#### 1）AsyncChannel
AsyncChannel提供了Message的并发处理能力，见AsyncChannel.java

为了防止子类在继承AsyncChannel基类的时候重写dispatch方法，用final关键字进行修饰，handle方法用于子类对Message进行具体的处理，stop方法停止ExecutorService。

#### 2）AsyncEventDispatcher
其次，提供新的EventDispatcher类AsyncEventDispatcher负责以并发的方式dispatch Message，其中Channel只能对应AsyncChannel类型。

在其中，routeTable使用concurrentHashMap代替，注册channel时，如果不是AsyncChannel类型，就会抛出异常。

![](https://i.postimg.cc/T3nZjqG2/29-3.png)

#### 3）测试代码
见*AsyncEventDispatcherExample.java*


## 29.4 本章总结
Message无论在同步还是异步EDA中，都没有使用同步方法，根本原因在于Event被设计成了不可变对象，因为Event经过每一个Handlr之后都会创建一个新的Event，不会出现资源竞争。

同理，生产者消费者模式如果采用EDA，生产者有消息就传入EDA，消费者Handler自然触发Message处理。
