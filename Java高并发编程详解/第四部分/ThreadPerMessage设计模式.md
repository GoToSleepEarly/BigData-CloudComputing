# 第24章 Thread-Per-Message设计模式
## 24.1 什么是Thread-Per-Message模式
意思是为每一个消息的处理开辟一个线程使得消息能够以并发的方式进行处理，从而提高系统整体的吞吐能力。好比电话接线员，收到每一个业务电话都会提交对应的工单。
## 24.2 每个任务一个线程
此处模拟上面所述的接线员业务。 
 
**开发中不建议采用这种形式**。  
- 客户提交的任务处理请求都会分装成Request对象，见*Request.java*  
- TaskHandler代表每一个工作人员接收到任务后的处理逻辑，见*TaskHandler.java*  
- Operator代表接线员，会将客户请求封装成一个Request，然后交给工作人员（线程）处理，见*Operator.java*

根据第二章所学，**JVM中可创建的线程数量是有限的**，如果每个任务都创建一个新的线程，并且每一个线程执行时间较长，则会导致栈内存溢出；再比如每一个执行时间短，那么线程的创建销毁也将是不小的影响。  
此时，我们可以**用线程池优化Operator的call方法**，见*Operator.java*

## 24.3 多用户的网络聊天
Thread-Per-Message模式在网络通信中的使用也是非常广泛的，比如下面要写的网络聊天程序，在服务端每一个连接到服务端的连接都将创建一个独立的线程处理，当客户端的连接数超过服务端的最大受理能力时，客户端将被存放至排队队列中。
### 24.3.1 服务端程序
ChatServer用于服务端接受客户端连接，并建立TCP通信交互，当服务端收到连接将会给线程池提交一个任务用于交互，进而提高并发响应能力，见*ChatServer.java*
### 24.3.2 相应客户端连接的Handler
待服务端接收到客户端的连接后，便会创建一个新的ChatHandler对象提交给线程，ChatHandler任务是Runnable接口的实现，主要负责和客户端进行你来我往的简单通信，见*ChatHandler.java*
### 24.3.3 测试
需要用到telnet命令，故放弃测试。

    public static void main(String[] args) throws IOException{
    	new ChatServer().startServer();
    }

## 24.4 本章总结
Thread-Per-Message设计模式日常中非常常见，但是需要灵活运用。
