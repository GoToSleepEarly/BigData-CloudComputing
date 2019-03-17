package chapter06;

import java.util.concurrent.TimeUnit;

public class ThreadGroupInterrupt {

	public static void main(String[] args) throws InterruptedException {
		ThreadGroup group = new ThreadGroup("TestGroup");
		new Thread(group, () ->{
			while(true){
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			System.out.println("t1 will exit");
		},"t1").start();
		
		new Thread(group, () ->{
			while(true){
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
					break;
				}
			}
			System.out.println("t2 will exit");
		},"t2").start();
	
		TimeUnit.MILLISECONDS.sleep(2);
		
		group.interrupt();
	}
	
}
