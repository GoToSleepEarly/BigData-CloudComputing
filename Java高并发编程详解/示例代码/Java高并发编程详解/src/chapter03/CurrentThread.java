package chapter03;

public class CurrentThread {

	public static void main(String[] args) {
		//���������� this�ᱨ�� cannot use this in a static context
		/*Thread thread = new Thread(() ->{
				System.out.println(Thread.currentThread() == this);
			}
		);*/
		
		Thread thread1 = new Thread(){
			@Override
			public void run(){
				System.out.println(Thread.currentThread() == this);
			}
		};
		
		thread1.start();
		
		String name = Thread.currentThread().getName();
		System.out.println(name);
	}

}
