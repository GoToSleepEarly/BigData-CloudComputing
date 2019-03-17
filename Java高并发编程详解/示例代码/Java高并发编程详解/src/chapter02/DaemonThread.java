package chapter02;

public class DaemonThread {

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(() ->
		{
			while(true){
				try {
					Thread.sleep(1);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});
		
		//如果注释掉，则会print，但jvm一直在工作。
		//thread.setDaemon(true);
		
		thread.start();
		Thread.sleep(2_000L);
		System.out.println("Main线程结束");
	}

}
