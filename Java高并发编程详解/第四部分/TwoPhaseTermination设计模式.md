# 第25章 Two-phase Termination模式
## 25.1 什么是Two Phase Termination模式
我们将线程的正常处理状态称为“作业中”，当希望结束这个线程时，则送出“终止请求”。接着，这个线程并不会立刻结束，而是进入“终止处理中”状态，此时线程还是运行着的，可能处理一些释放资源等操作。直到终止处理完毕，才会真正结束。
![](https://i.postimg.cc/L85cj2YT/25-1.png)

Two-phase Termination主要考虑以下问题：
- 安全地结束（安全性）；
- 一定会进行终止处理（生命性）；
- 收到“终止请求”后，要尽快进行终止处理（响应性）；

## 25.2 Two Phase Termination实例
### 25.2.1 线程停止的TwoPhaseTermination
修改前：
![](https://i.postimg.cc/Qtd1d4nH/25-2-1.png)
如果发生异常，chat方法抛出错误，则该线程的任务执行也结束，我们需要确保任务结束也能尽快释放socket资源。
修改后：
![](https://i.postimg.cc/3wB2751s/25-2-2.png)
加入finally语句块，用于执行socket的主动关闭(两次判断确保关闭)。

### 25.2.1 进程关闭的TwoPhaseTermination
对于不论进程的主动关闭还是被动关闭(出现异常)都需要对持有资源释放时，可以采用Hook，见第7章。
	
	Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
			@Override
			public void run(){
				System.out.println("Execute Hook.....");
			}
	}));


## 25.3 知识拓展
无论是File还是Socket等重量级资源(严重依赖操作系统)，在进行释放时并不能保证100%成功（可能是操作系统原因），如25.2.1节中第二阶段释放可能失败，socket实例被垃圾回收器回收，但是socket实例对应的底层系统资源并未被释放。 
 
JDK提供了对象被垃圾回收时的可跟踪机制——PhantomReference。借助于它可以在对象被垃圾回收器清除前尝试一次资源回收，尽最大努力回收重量级资源。

![](https://i.postimg.cc/YC3qXpbm/25-2.png)
### 25.3.1 Strong Reference及LRUCache
强引用（Strong Reference），当一个对象被new实例化时，他就是强引用，JVM会在堆内存开辟内存区域存放相应数据结构。JVM垃圾收集器线程会在达到GC条件时尝试回收（Full GC，Young GC），强引用的特点是**只要引用到Root根的路径可达，无论怎样的GC都不会将其释放**，而宁可JVM内存溢出。  

Cache是一种用于提高系统性能，提高数据检索效率的机制，而LRU（Least Recently Used，最近最少使用）算法和Cache结合是最常见的一种策略。

   
	public class Reference {
	
		private final byte[] data = new byte[2 << 19];
	
		@Override
		protected void finalize() throws Throwable{
			System.out.println("该引用将被GC");
		}
	}
finalize方法会在垃圾回收的标记阶段被调用。**调用finalize后并不表示已被回收**，只是被标记可以，对象在finalize方法中是可以“自我救赎”的。  

LRUCache实现方式有很多种，我们此处使用双向链表+Hash表的方式来实现，见*LRUCache.java*和*Cacheloader.java*。
- keyList对key进行升序排列，从旧到新，Cache存储真正的数据。
- CacheLoader用于数据存储
- put和get用于存取数据。

我们在前面定义Reference类，创建一个Reference实例会产生1MB的内存开销，当不断往Cache存放数据或固定数量大小（capacity）的数据时，由于是Strong Reference的缘故，会引起内存溢出。

	//溢出
	LRUCache<Integer, Reference> cache2 = new LRUCache<>(200, key -> new Reference());		
	for(int i = 0;i< Integer.MIN_VALUE;i++){
		cache2.get(i);
		TimeUnit.SECONDS.sleep(1);
		System.out.println("第"+i+"个reference已被缓存");		
	}
- -Xmx128M：最大堆大小
- -Xms64M：初始化堆大小
- -XX:+PrintGCDetails：在控制台输出GC的详细信息

程序运行到大约98个Reference时，JVM出现了堆溢出。因为cache的容量为200，又是强引用无法GC，最终超出最大堆大小。

### 25.3.2 Soft Reference及SoftLRUCache
当JVM探测到内存即将溢出，他将GC soft类型的reference，见*SoftLRUCache.java*，用SoftReference<>(value)即可包装成弱引用。

	SoftLRUCache<Integer, Reference> cache = new SoftLRUCache<>(1000, key -> new Reference());
	System.out.println(cache);
	for(int i = 0; i< Integer.MAX_VALUE; i++{
		cache.get(i);
		TimeUnit.SECONDS.sleep(1);
		System.out.println("The" + i + "Reference 存储在缓存");
	}
和上一节不一样，无论运行多久都不会出现JVM溢出的问题**（当cache插入速度太快，GC来不及收集，则还是可能溢出）**
![](https://i.postimg.cc/cJ23WVbh/25-3.png)
### 25.3.3 Weak Reference
无论young GC 还是 full GC，Weak Reference的引用都会被垃圾回收器回收，所以弱引用很少用作cache。

#### 1）任何类型的GC都可导致Weak Reference对象被回收
	Reference ref = new Reference();
	WeakReference<Reference> reference = new WeakReference<>(ref);
	ref = null;
	System.gc();
#### 2）获取被垃圾收集器回收的对象
	ReferenceQueue<Reference> queue = new ReferenceQueue<>();
	Reference ref2 = new Reference();
	WeakReference<Reference> reference2 = new WeakReference<Reference>(ref2,queue);
	ref = null;
	System.out.println(reference2.get());
	System.gc();
	TimeUnit.SECONDS.sleep(1);
	java.lang.ref.Reference<? extends Reference> gcedRef = queue.remove();
	System.out.println(gcedRef);
无论是WeakReference还是SoftReference引用，被垃圾收集器回收后，都会被存放到与之相关联的ReferenceQueue中。

### 25.3.4 Phantom Reference
“虚引用”顾名思义，就是形同虚设，与其他几种引用都不同，虚引用并不会决定对象的生命周期。如果一个对象仅持有虚引用，那么它就和没有任何引用一样，在任何时候都可能被垃圾回收器回收。

虚引用主要用来跟踪对象被垃圾回收器回收的活动。虚引用与软引用和弱引用的一个区别在于：虚引用必须和引用队列 （ReferenceQueue）联合使用。当垃圾回收器准备回收一个对象时，如果发现它还有虚引用，就会在回收对象的内存之前，把这个虚引用加入到与之 关联的引用队列中。

- Phantom Reference必须和Reference配合使用
- Phantom Reference的get方法返回的始终是null
- 当垃圾收集器决定回收Phantom Reference对象时，会将其插入关联的ReferenceQueue中
- **使用Phantom Reference进行清理动作要比Object的finalize方法更灵活**


	//Phantom Reference
	ReferenceQueue<Reference> q = new ReferenceQueue<>();
	PhantomReference<Reference> pf = new PhantomReference<Reference>(new Reference(),q);
	//一定是Null
	System.out.println(reference2.get());
	System.gc();
	java.lang.ref.Reference<? extends Reference> gcedref = q.remove();
	System.out.println(gcedref);
![](https://i.postimg.cc/RVTXmLx8/image.png)

### 重构ChatHandler的release方法
release方法中，当socket.close遇到异常时，会抛出Throwable异常，我们重构此处。

	private void release(){
    	try{
    		if(socket != null){
    			socket.close();
    		}
    	}catch(Throwable e){
    		if(socket != null){
    			//将socket实例加入Tracker中
    			SocketCleaningTracker.tracker(socket);
    		}
    	}
    }
SocketCleaningTracker见*SocketCleaningTracker.java*。

调用时会启动Cleaner线程(设置为守护线程)，不断的从ReferenceQueue中remove Tracker实例(Tracker是PhantomReference的子类)，然后尝试最后的清理动作。