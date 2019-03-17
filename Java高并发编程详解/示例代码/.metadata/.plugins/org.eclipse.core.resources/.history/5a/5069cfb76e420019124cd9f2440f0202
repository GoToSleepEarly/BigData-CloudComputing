package chapter21;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThreadLocalExample {

	public static void main(String[] args) {
		//创建ThreadLocal实例
		ThreadLocal<Integer> tlocal = new ThreadLocal<>();
		//创建十个线程，使用tlocal
		IntStream.range(0, 10).forEach(i -> new Thread(() ->{
			//每个线程都会设置tlocal，但是是线程隔离的
			tlocal.set(i);
			System.out.println(Thread.currentThread()+" set i "+ tlocal.get());
			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println(Thread.currentThread()+" get i "+ tlocal.get());
		}).start()
		);
		
		//ThreadLocal<Object> threadLocal = ThreadLocal.withInitial(Object::new);
		ThreadLocal<Object> threadLocal = new ThreadLocal<Object>(){
			@Override
			protected Object initialValue(){
				return new Object();
			}
		};
		
		new Thread(() -> {
			System.out.println(threadLocal.get());
		}).start();
		System.out.println(threadLocal.get());
		
	}

}
