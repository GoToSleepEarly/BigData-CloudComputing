package chapter08;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import chapter02.ThreadTest;

public class BasicThreadPool extends Thread implements ThreadPool{

	//初始化线程数量
	private final int initSize;
	//初始化最大线程数
	private final int maxSize;
	//线程池核心线程数
	private final int coreSize;
	//当前活跃的线程数量
	private int activeCount;
	//创建线程所需的工厂
	private final ThreadFactory threadFactory;
	//任务队列
	private final RunnableQueue runnableQueue;
	//线程池是否已被shutdown
	private volatile boolean isShutdown = false;
	//工作线程队列
	private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();
	
	private final static DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.DiscardDenyPolicy();
	
	private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();
	
	private final long keepAliveTime;
	
	private final TimeUnit timeUnit;
	
	public BasicThreadPool(int initSize, int maxSize, int coreSize,
			int queueSize) {
		this(initSize, maxSize, coreSize, DEFAULT_THREAD_FACTORY,
				queueSize, DEFAULT_DENY_POLICY, 10, TimeUnit.SECONDS);
	}
	public BasicThreadPool(int initSize, int maxSize, int coreSize, ThreadFactory threadFactory,
			int queueSize, DenyPolicy denyPolicy, long keepAliveTime, TimeUnit timeUnit) {
		this.initSize = initSize;
		this.maxSize = maxSize;
		this.coreSize = coreSize;
		this.threadFactory = threadFactory;
		this.runnableQueue = new LinkedRunnableQueue(queueSize, denyPolicy, this);
		this.keepAliveTime = keepAliveTime;
		this.timeUnit = timeUnit;
		this.init();
	}
	
	//初始化时，先创建initSize个线程
	private void init() {
		this.start();
		for(int i =0; i< initSize; i++){
			newThread();
		}
	}
	
	@Override
	public void execute(Runnable runnable) {
		if(this.isShutdown){
			throw new IllegalStateException("线程池已损毁");
		}
		this.runnableQueue.offer(runnable);
	}
	
	private void newThread(){
		//创建任务线程，并且启动
		InternalTask internalTask = new InternalTask(runnableQueue);
		Thread thread = this.threadFactory.createThread(internalTask);
		ThreadTask threadTask = new ThreadTask(thread, internalTask);
		threadQueue.offer(threadTask);
		this.activeCount++;
		thread.start();
	}
	
	public void removeThread() {
		//从线程池中移除某个线程
		ThreadTask threadTask = threadQueue.remove();
		threadTask.internalTask.stop();
		this.activeCount--;
	}

	@Override
	public void run(){
		while(!isShutdown && !isInterrupted()){
			try{
				timeUnit.sleep(keepAliveTime);
			}catch(Exception e){
				isShutdown = true;
				break;
			}
			synchronized (this) {
				if(isShutdown)
					break;
				//当前队列中有任务尚未处理，并且activeCount <coreSize则继续扩容
				if(runnableQueue.size() > 0 && activeCount < coreSize){
					for(int i = initSize; i< coreSize;i++){
						newThread();
					}
					//continue的目的在于不想让线程扩容直接达到maxsize
					continue;
				}
				//当前队列有任务尚未处理，并且activeCount<maxSize则继续扩容
				if(runnableQueue.size() > 0 && activeCount < maxSize)
					for(int i = coreSize;i<maxSize;i++){
						newThread();
					}
				}
				//当前任务队列没有任务，则需要回收至coreSize
				if(runnableQueue.size()==0 && activeCount > coreSize){
					for(int i= coreSize; i< activeCount;i++){
						removeThread();
					}
				}
		}
	}
	@Override
    public int getInitSize() {
        if(isShutdown){
            throw new IllegalStateException(" The thread pool is destory . ") ;
        }
        return this.initSize;
    }
 
    @Override
    public int getMaxSize() {
        if(isShutdown){
            throw new IllegalStateException(" The thread pool is destory . ") ;
        }
        return this.maxSize;
    }
 
    @Override
    public int getCoreSize() {
        if(isShutdown){
            throw new IllegalStateException(" The thread pool is destory . ") ;
        }
        return this.coreSize;
    }
 
    @Override
    public int getQueueSize() {
        if(isShutdown){
            throw new IllegalStateException(" The thread pool is destory . ") ;
        }
        return runnableQueue.size();
    }
 
 
    @Override
    public int getActiveCount() {
        // 获取活跃线程数，需要进行同步操作
        synchronized (this) {
            return this.activeCount;
        }
    }
 
    @Override
    public boolean isShutdown() {
 
        return this.isShutdown;
    }

	@Override
	public void shutdown() {
		synchronized (this) {
			if(isShutdown)
				return;
			isShutdown = true;
			threadQueue.forEach(threadTask ->{
				threadTask.internalTask.stop();
				threadTask.thread.interrupt();
			});
			this.interrupt();
		}
	}
	
}
