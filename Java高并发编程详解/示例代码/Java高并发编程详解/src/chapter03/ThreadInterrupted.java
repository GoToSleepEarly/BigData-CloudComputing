package chapter03;

import java.util.concurrent.TimeUnit;

public class ThreadInterrupted {

	public static void main(String[] args) throws InterruptedException {
		/*Thread thread = new Thread(){
			@Override
			public void run(){
				while(true){
					System.out.println(Thread.interrupted());
				}
			}
		};
		thread.setDaemon(true);
		thread.start();
		
		TimeUnit.MILLISECONDS.sleep(2);
		thread.interrupt();*/
		
		System.out.println("Main Thread:" + Thread.interrupted());
		
		Thread.currentThread().interrupt();
		
		System.out.println("Main Thread：" + Thread.currentThread().isInterrupted());
		
		System.out.println("Main Thread：" + Thread.interrupted());
		//会擦除flag
		System.out.println("Main Thread：" + Thread.interrupted());
		
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (Exception e) {
			System.out.println("直接被中断了");
		}
		
	}

}
