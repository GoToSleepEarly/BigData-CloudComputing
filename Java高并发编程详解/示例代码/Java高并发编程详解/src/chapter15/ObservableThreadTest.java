package chapter15;

import java.util.concurrent.TimeUnit;

public class ObservableThreadTest {

	public static void main(String[] args) {
		Observable observableThread = new ObservableThread<>(()-> {
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("运行结束");
			return null;
		});
		observableThread.start();
		
		final TaskLifeCycle<String> lifecycle = new TaskLifeCycle.EmptyLifeCycle<String>() {
			
			@Override
			public void onFinish(Thread t, String result) {
				System.out.println("结果是:" + result);	
			}
			
		};
		
		Observable myThread = new ObservableThread<>(lifecycle, () ->{
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("运行结束");
			return "这是结果";
		});
		myThread.start();
	}

}
