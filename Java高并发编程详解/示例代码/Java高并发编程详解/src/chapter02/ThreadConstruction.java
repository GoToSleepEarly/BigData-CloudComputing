package chapter02;

import java.util.stream.IntStream;

public class ThreadConstruction {
	
	private final static String PREFIX="XZT-";
	
	public static void main(String[] args) {
		//Java 8 流操作+匿名函数  比如Thread(Runnable r)只有一个run方法是，就可用匿名函数代替。
		IntStream.range(0, 5).boxed().map(i -> new Thread(
				() -> System.out.println(Thread.currentThread().getName()))
				).forEach(Thread::start);
		
		//：：引用类只能是static方法。
		//IntStream.range(0, 5).boxed().map( i -> i*i).forEach(System.out::println);;
		
		IntStream.range(0, 5).mapToObj(ThreadConstruction::createThread).forEach(Thread::start);
		
		new Thread().setName("NewName");
		
		Thread t1 = new Thread("t1"); 
		ThreadGroup group = new ThreadGroup("TestGroup");
		Thread t2 = new Thread(group,"t2");
		ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
		System.out.println("main线程组："+ mainGroup.getName());
		System.out.println("t1的线程组:" + t1.getThreadGroup().getName());
		System.out.println("t2的线程组:"+ t2.getThreadGroup().getName());
	}
	
	public static Thread createThread(final int intName){
		//run方法无参，所以匿名函数也无参，但可以直接使用传入的参数。
		return new Thread(() -> System.out.println(Thread.currentThread().getName())
				,PREFIX + intName);
	}

}
