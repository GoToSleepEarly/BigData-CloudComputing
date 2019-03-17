package chapter04;

import java.util.concurrent.TimeUnit;

public class ClassMonitor {

	public static synchronized void method1(){
		System.out.println(Thread.currentThread().getName()+"进入到方法1");
		try {
			TimeUnit.MINUTES.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	/*public static synchronized void method2(){
		System.out.println(Thread.currentThread().getName()+"进入到方法2");
		try {
			TimeUnit.MINUTES.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}*/
	public static synchronized void method2(){
		synchronized (ClassMonitor.class) {
			System.out.println(Thread.currentThread().getName()+"进入到方法2");
			try {
				TimeUnit.MINUTES.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}	
		}
	}
	
	public static void main(String[] args) {
		new Thread(ClassMonitor::method1,"T1").start();
		new Thread(ClassMonitor::method2,"T2").start();
	}

}
