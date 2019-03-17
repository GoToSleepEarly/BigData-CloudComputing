# 第十六章 Single Thread Execution设计模式
这里有一座独木桥。因为桥身非常的细，**一次只能允许一个人通过**。当这个人没有下桥，另一个人就不能过桥。如果桥上同时又两个人，桥就会因为无法承重而破碎而掉落河里。 这就是Single Threaded Execution。有时也称为Critical section(临界区)。

## 16.1 机场过安检
### 16.1.1 非线程安全
模拟一个非线程安全的安检口类，检查登机牌和身份证。见*FlightSecurity.java*。  
FlightSecurity很简单，pass接受登机牌和身份证，check用来检查。  
测试代码见*FlightSecurityTest.java*，运行发现出错，主要有两种
### 16.1.2 问题分析
1. 首字母相同却未通过检查
![](https://i.postimg.cc/bNgnbL2Y/16-2.png)
2. 首字母不相同
![](https://i.postimg.cc/6pQvhRdr/16-3.png)

### 16.1.3 线程安全
pass方法加个synchronized即可。

### 什么时候适合Single Thread Execution设计模式?
1. 多线程访问资源的时候，被synchronized同步的方法总是排他性的。

2. 多个线程对某个类的状态发生改变的时候

## 16.2 吃面问题
### 16.2.1 吃面引起的死锁
synchronized交叉锁导致的死锁情况。A手持刀等待B放下叉，右边相反。  
餐具类*Tableware.java*、吃面条线程*EatNoodleThread.java*见代码。  
我们在*EatNoodleThreadTest.java*主线程中，一个持刀等待叉，一个持叉等待刀，结果如下 
> Atake up Tool: fork (left)  
> Btake up Tool: knife (left)

### 16.2.2 解决吃面引起的死锁问题
为了解决此问题，**我们将刀叉进行封装，使其属于同一个类**中，见 *TableWarePair.java*，同时EatNoodleThread中用TableWarePair代替leftTool和rightTool，这样就不会出现死锁问题，**因为刀叉同一时间都只有一个线程获得**。

## 16.3 本章总结
synchronized是以性能为代价的，所以尽量减少作用域。同时子类继承可能破坏SingleThreadExecution，出现继承异常。
