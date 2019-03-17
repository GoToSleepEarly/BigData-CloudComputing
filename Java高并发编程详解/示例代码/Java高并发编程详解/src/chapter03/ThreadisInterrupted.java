package chapter03;

import java.util.concurrent.TimeUnit;

public class ThreadisInterrupted {

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(() -> {
			while(true){
				//死循环
				//如果加上try-catch，那么interrupt将会触发异常，中断。
				try {
					TimeUnit.MINUTES.sleep(1);
				} catch (InterruptedException e) {
					//将会catch异常并输出false，因为中断型号后，会置为false
					//System.out.println("I am interrupted:" + isInteruptted());
				}
			}
			/*得用匿名内部类
			 * while (!isInterrupted()) {
		        // 执行任务...
		    }*/
		});
		thread.start();
		//只能sleep当前线程
		//TimeUnit.MILLISECONDS.sleep(2);
		System.out.println("Thread is interrupted?："+ thread.isInterrupted());
		thread.interrupt();
		System.out.println("Thread is interrupted?:"+ thread.isInterrupted());
		
	}

}
