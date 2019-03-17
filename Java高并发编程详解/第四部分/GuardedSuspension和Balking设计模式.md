# 第二十章 GuardedSuspension设计模式
## 20.1 什么是Guarded Suspension设计模式
Suspension是“挂起”，“暂停”的意思，而Guarded则是“担保”的意思，连在一起就是确保挂起。当线程在访问某个对象时，发现条件不满足，就暂时挂起等待条件满足时再次访问，这一点和Balking设计模式正好相反（条件不满足直接放弃）。  
Guarded Suspension设计模式是很多设计模式的基础，比如消费者生产者模式，Worker Thread设计模式等等，同样在Java并发包的BlockingQueue中也大量使用到了。

## 20.2 Guarded Suspension的示例
见*GuardedSuspensionQueue.java*
    package chapter20;

	import java.util.LinkedList;

	public class GuardedSuspensionQueue {

		//定义存放Integer类型的queue
		private final  LinkedList<Integer> queue  = new LinkedList<>();
	
		//定义queue的最大容量为100
		private final int LIMIT = 100;
	
		//往queue中插入数据
		public void offer(Integer data) throws InterruptedException{
			synchronized (this) {
				//判断queue的当前元素是否超过了limit
				while(queue.size() >= LIMIT){
				//阻塞线程
				this.wait();
			}
			//插入元素并唤醒take
			queue.add(data);
			this.notifyAll();
			}
		}
	
		//往queue中取数据
		public Integer take() throws InterruptedException{
			synchronized (this) {
				while(queue.isEmpty()){
					this.wait();
			}
			this.notifyAll();
			return queue.removeFirst();
			}
		}
	}
Guarded Suspension模式是一个非常基础的设计模式，它主要关注的是当某个条件（临界值）不满足时将操作线程正确的挂起，以防止出现数据不一致或操作超过临界值的控制范围。
wait会释放锁，进入waitset，等待notify。

# 第22章 Balking设计模式
## 22.1 什么是Balking设计
多个线程监控监控某个共享变量，A线程监控到共享变量发生变化后即刻触发某个动作，但是此时发现另一个线程B已经开始，**则A放弃准备**。**所谓Balking（犹豫），就是停止并返回的意思**。

Balking模式与Guarded Suspension模式一样，也存在守护条件，在Balking模式中，如果守护条件不成立，则立即中断处理。这与Guarded Suspension模式不同，因为Guarded Suspension模式是一直等待至可以运行。

## 22.2 Balking模式之文档编辑
### 22.2.1 Document
下面设计了Document类代表文档本身，在Document中有两个主要方法save和edit分别用于保存文档和编辑文档，见*Document.java*
- edit和save用synchronized同步，防止资源不一致
- changed默认为false，如果有修改则为true
- 保存时先看changed是否为true，是则保存，否则放弃，Balking主要观察的就是changed
- Document创建时自动开始AutoSaveThread
### 22.2.2 AutoSaveThread
自动保存的线程，见*AutoSaveThread.java*，内容比较简单，就是每隔一秒自动调用Document的save方法
### 22.2.3 DocumentEditThread
DocumentEditThread类似于主动编辑文档的作者，除此之外，每隔5次还会按下save，见*DocumentEditThread.java*  
测试案例见*BalkingTest.java*
结果：
![](https://i.postimg.cc/hGJ1R129/balking.png)

## 22.3 本章总结
系统资源初始化加载时只加载一次，可以采用balking模式：
    
	public synchronized Map<String, Resource> load(){
		//balking
		if(loaded)
			return
		else{
			//loading
			loaded = true;		
		}
	}