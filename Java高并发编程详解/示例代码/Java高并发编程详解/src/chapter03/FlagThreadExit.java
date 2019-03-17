package chapter03;

import java.util.concurrent.TimeUnit;

public class FlagThreadExit {

	static class MyTask extends Thread{
		private volatile boolean closed = false;
		
		@Override
		public void run(){
			System.out.println("I will start to work");
			while(!closed && !isInterrupted()){
				//
			}
			System.out.println("I will be closed");
		}
		
		public void close(){
			this.closed= true;
			this.interrupt();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		MyTask t = new MyTask();
		t.start();
		TimeUnit.SECONDS.sleep(1);
		System.out.println("t will be shutdown");
		t.close();
		
	}

}
