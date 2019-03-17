package chapter08;

public interface RunnableQueue {

	//当有新任务来首先会offer到队列中
	void offer(Runnable runnbale);
	
	//工作线程通过take方法获取Runnable
	Runnable take() throws InterruptedException;
	
	//获取任务队列中任务的数量
	int size();
}
