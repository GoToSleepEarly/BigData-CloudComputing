package chapter27;

public class ActiveDaemonThread2 extends Thread {

		private final ActiveMessageQueue2 queue;
		
		public ActiveDaemonThread2(ActiveMessageQueue2 queue){
			super("ActiveDaemonThread2");
			this.queue = queue;
			//设置为守护线程
			setDaemon(true);
		}
		
		@Override
		public void run(){
			for(;;){
				//取出MethodMessage并执行execute方法
				ActiveMessage activeMessage = this.queue.take();
				activeMessage.execute();
			}
		}
}
