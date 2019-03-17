package chapter03;

import java.sql.Time;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThreadYield {
	public static void main(String[] args){
		IntStream.range(0, 2).boxed().map(ThreadYield::create).forEach(Thread::start);
	}
	
	private static Thread create(int index){
		return new Thread(() ->{
			try {
				TimeUnit.SECONDS.sleep(2);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			if(index == 0)
				Thread.yield();
			System.out.println(index);
		});
	}
}
