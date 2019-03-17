package chapter08;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;

import chapter02.ThreadTest;

public class BasicThreadPool extends Thread implements ThreadPool{

	//��ʼ���߳�����
	private final int initSize;
	//��ʼ������߳���
	private final int maxSize;
	//�̳߳غ����߳���
	private final int coreSize;
	//��ǰ��Ծ���߳�����
	private int activeCount;
	//�����߳�����Ĺ���
	private final ThreadFactory threadFactory;
	//�������
	private final RunnableQueue runnableQueue;
	//�̳߳��Ƿ��ѱ�shutdown
	private volatile boolean isShutdown = false;
	//�����̶߳���
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
	
	//��ʼ��ʱ���ȴ���initSize���߳�
	private void init() {
		this.start();
		for(int i =0; i< initSize; i++){
			newThread();
		}
	}
	
	@Override
	public void execute(Runnable runnable) {
		if(this.isShutdown){
			throw new IllegalStateException("�̳߳������");
		}
		this.runnableQueue.offer(runnable);
	}
	
	private void newThread(){
		//���������̣߳���������
		InternalTask internalTask = new InternalTask(runnableQueue);
		Thread thread = this.threadFactory.createThread(internalTask);
		ThreadTask threadTask = new ThreadTask(thread, internalTask);
		threadQueue.offer(threadTask);
		this.activeCount++;
		thread.start();
	}
	
	public void removeThread() {
		//���̳߳����Ƴ�ĳ���߳�
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
				//��ǰ��������������δ��������activeCount <coreSize���������
				if(runnableQueue.size() > 0 && activeCount < coreSize){
					for(int i = initSize; i< coreSize;i++){
						newThread();
					}
					//continue��Ŀ�����ڲ������߳�����ֱ�Ӵﵽmaxsize
					continue;
				}
				//��ǰ������������δ��������activeCount<maxSize���������
				if(runnableQueue.size() > 0 && activeCount < maxSize)
					for(int i = coreSize;i<maxSize;i++){
						newThread();
					}
				}
				//��ǰ�������û����������Ҫ������coreSize
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
        // ��ȡ��Ծ�߳�������Ҫ����ͬ������
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
