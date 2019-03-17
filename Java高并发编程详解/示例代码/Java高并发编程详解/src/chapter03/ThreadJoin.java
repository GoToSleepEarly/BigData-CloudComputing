package chapter03;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThreadJoin {

	public static void main(String[] args) throws InterruptedException {
		List<Thread> threads = IntStream.range(1, 3).mapToObj(ThreadJoin::create).collect(Collectors.toList());
		threads.forEach(Thread::start);
		
		//join前已经执行完，不会阻塞
		TimeUnit.SECONDS.sleep(10);
		
		//主线程join，所以1和2线程执行完main才开始继续执行。
		//join第一个线程的时候main阻塞，所以第二个join不会执行
		for(Thread thread: threads){
			thread.join();
		}
		
		
		for(int i=0; i< 10;i++){
			System.out.println(Thread.currentThread().getName()+"#"+i);
			shortSleep();
		}
	}
	
	
	public static Thread create(int seq){
		return new Thread(() -> {
			for(int i=0; i<10;i++){
				System.out.println(Thread.currentThread().getName()+"#"+i);
			}
		},String.valueOf(seq));
	}
	public static void shortSleep(){
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
