package chapter03;

import java.util.concurrent.TimeUnit;

public class ThreadInterrupt {

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(() -> {
			try {
				TimeUnit.MINUTES.sleep(1);
			} catch (InterruptedException e) {
				System.out.println("±ªinterrupt¡À");
			}
		});
		
		thread.start();
		
		TimeUnit.MILLISECONDS.sleep(2);
		thread.interrupt();
	}

}
