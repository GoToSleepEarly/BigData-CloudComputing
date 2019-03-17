package chapter08;

public class InternalTask implements Runnable{
	
	//�̳߳�ÿ��������Ҫִ��queue�����񣬴˴���InternalTask��ʾ
	
	private final RunnableQueue runnableQueue;
	
	private volatile boolean running = true;
	
	public InternalTask(RunnableQueue runnableQueue){
		this.runnableQueue = runnableQueue;
	}
	
	@Override
	public void run(){
		//�����ǰ����Ϊrunning����û���жϣ����佫���ϵش�queue�л�ȡrunnable��ִ��run
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
