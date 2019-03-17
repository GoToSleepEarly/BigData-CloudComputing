package chapter02;

import java.util.stream.IntStream;

public class ThreadConstruction {
	
	private final static String PREFIX="XZT-";
	
	public static void main(String[] args) {
		//Java 8 ������+��������  ����Thread(Runnable r)ֻ��һ��run�����ǣ��Ϳ��������������档
		IntStream.range(0, 5).boxed().map(i -> new Thread(
				() -> System.out.println(Thread.currentThread().getName()))
				).forEach(Thread::start);
		
		//����������ֻ����static������
		//IntStream.range(0, 5).boxed().map( i -> i*i).forEach(System.out::println);;
		
		IntStream.range(0, 5).mapToObj(ThreadConstruction::createThread).forEach(Thread::start);
		
		new Thread().setName("NewName");
		
		Thread t1 = new Thread("t1"); 
		ThreadGroup group = new ThreadGroup("TestGroup");
		Thread t2 = new Thread(group,"t2");
		ThreadGroup mainGroup = Thread.currentThread().getThreadGroup();
		System.out.println("main�߳��飺"+ mainGroup.getName());
		System.out.println("t1���߳���:" + t1.getThreadGroup().getName());
		System.out.println("t2���߳���:"+ t2.getThreadGroup().getName());
	}
	
	public static Thread createThread(final int intName){
		//run�����޲Σ�������������Ҳ�޲Σ�������ֱ��ʹ�ô���Ĳ�����
		return new Thread(() -> System.out.println(Thread.currentThread().getName())
				,PREFIX + intName);
	}

}
