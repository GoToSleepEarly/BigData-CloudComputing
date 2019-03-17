package chapter07;

import java.util.concurrent.TimeUnit;

public class ThreadHook {

	public static void main(String[] args) {
		Runtime.getRuntime().addShutdownHook(new Thread( () -> {
			System.out.println("Hook�߳�����");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Hook�߳��˳�");
		}));
	
		//����ע����Hook�߳�
		Runtime.getRuntime().addShutdownHook(new Thread( () -> {
			System.out.println("Hook2�߳�����");
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("Hook2�߳��˳�");
		}));
		
		System.out.println("main�߳̽���");
	}

}
