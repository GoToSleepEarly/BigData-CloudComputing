package chapter08;

public class InternalTask implements Runnable{
	
	//线程池每个任务都需要执行queue的任务，此处用InternalTask表示
	
	private final RunnableQueue runnableQueue;
	
	private volatile boolean running = true;
	
	public InternalTask(RunnableQueue runnableQueue){
		this.runnableQueue = runnableQueue;
	}
	
	@Override
	public void run(){
		//如果当前任务为running并且没被中断，则其将不断地从queue中获取runnable，执行run
		while(running && !Thread.currentThread().isInterrupted()){
			try{
				Runnable task = runnableQueue.take();
				task.run();
			}catch(Exception e){
				running = false;
				break;
			}
		}
	}
	
	public void stop(){
		this.running = false;
	}
}
