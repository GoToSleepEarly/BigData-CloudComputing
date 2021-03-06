package chapter27;

import java.util.LinkedList;

public class ActiveMessageQueue2 {
	
	//用于存放提交的MethodMessage方法
	private final LinkedList<ActiveMessage> messages = new LinkedList<>();
	
	public ActiveMessageQueue2(){
		//启动worker线程
		new ActiveDaemonThread2(this).start();
	}
	
	public void offer(ActiveMessage activeMessage){
		synchronized (this) {
			messages.addLast(activeMessage);
			//因为只有一个线程负责take数据，所以没必要notifyAll
			this.notify();
		}
	}
	
	protected ActiveMessage take(){
		synchronized (this) {
			//当没有Message的时候，执行线程进入阻塞
			while(messages.isEmpty()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//获取其中一个并删除
			return messages.removeFirst();
			
		}
	}
}
