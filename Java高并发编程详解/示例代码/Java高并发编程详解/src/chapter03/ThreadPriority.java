package chapter03;

public class ThreadPriority {
	
	
	public static void main(String[] args) {
//		//t2出现几率比较高
//		Thread t1 = new Thread( () -> {
//			while(true){
//				System.out.println("t1");
//			}
//		});
//		t1.setPriority(3);
//		
//		Thread t2 = new Thread( () -> {
//			while(true){
//				System.out.println("t2");
//			}
//		});
//		t2.setPriority(10);
//		t1.start();
//		t2.start();
		
		/*//不会超过group的最大优先级
		ThreadGroup group = new ThreadGroup("test");
		group.setMaxPriority(7);
		Thread thread = new Thread(group,"test-thread");
		thread.setPriority(10);
		System.out.println(thread.getPriority());*/
		
		//默认优先级和父类一致
		Thread t1 = new Thread();
		System.out.println("t1 优先级：" + t1.getPriority());
		
		Thread t2 = new Thread(() -> {
			Thread t3 = new Thread();
			System.out.println("t3 优先级:" + t3.getPriority());
		});
		
		t2.setPriority(6);
		t2.start();
		System.out.println("t2 优先级：" + t2.getPriority());
	}

}
