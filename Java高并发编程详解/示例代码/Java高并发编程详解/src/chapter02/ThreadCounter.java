package chapter02;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadCounter extends Thread {
	
	final static AtomicInteger counter = new AtomicInteger(0);
	
	//别尝试了，会死机。
	public static void main(String[] args) {
		
		while(true){
			new ThreadCounter().start();
		}
		
	}
	
	@Override
	public void run(){
		System.out.println("The" + counter.getAndIncrement() + "Thread");
		try {
			TimeUnit.MINUTES.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
