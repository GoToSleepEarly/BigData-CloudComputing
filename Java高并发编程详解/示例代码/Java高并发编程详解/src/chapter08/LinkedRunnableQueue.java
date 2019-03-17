package chapter08;

import java.util.LinkedList;

public class LinkedRunnableQueue implements RunnableQueue{
	
	//任务队列的最大容量，在构造时传入
	private final int limit;
	
	//若任务队列已满，需要执行拒绝策略
	private final DenyPolicy denyPolicy;
	
	//存放任务的队列
	private final LinkedList<Runnable> runnableList = new LinkedList<>();
	
	private final ThreadPool threadPool;
	
	//构造函数，一个队列需要limit，拒绝策略，线程池
	public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, 
			ThreadPool  threadPool) {
		this.limit = limit;
		this.denyPolicy = denyPolicy;
		this.threadPool = threadPool;
	}
	
	@Override
	public void offer(Runnable runnable) {
		synchronized (runnableList) {
			//因为offer没有wait，所以不需要while判断。
			if(runnableList.size() >= limit){
				//无法容纳时执行拒绝策略
				denyPolicy.reject(runnable, threadPool);
			}else{
				//将任务加到队尾，并且唤醒阻塞的线程
				runnableList.addLast(runnable);
				runnableList.notifyAll();
			}
		}
	}

	@Override
	public Runnable take() throws InterruptedException {
		synchronized (runnableList) {
			while(runnableList.isEmpty()){
				try {
					//如果没有可执行任务，则会挂起。等待offer的notifyAll()
					runnableList.wait();
				} catch (InterruptedException e) {
					//被中断时需要抛出异常
					e.printStackTrace();
					throw e;
				}
			}
			return runnableList.removeFirst();
		}
	}

	@Override
	public int size() {
		//需要上锁，不然可能出异常
		synchronized (runnableList) {
			return runnableList.size();
		}
	}
	
}
