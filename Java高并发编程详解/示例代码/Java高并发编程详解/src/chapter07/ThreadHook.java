package chapter07;

import java.util.concurrent.TimeUnit;

public class ThreadHook {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread( () -> {
			System.out.println("Hook线程启动");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Hook线程退出");
		}));
	
		//可以注册多个Hook线程
		Runtime.getRuntime().addShutdownHook(new Thread( () -> {
			System.out.println("Hook2线程启动");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Hook2线程退出");
		}));
		
		System.out.println("main线程结束");
	}

}
