package chapter03;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class ThreadJoin {

	public static void main(String[] args) throws InterruptedException {
		List<Thread> threads = IntStream.range(1, 3).mapToObj(ThreadJoin::create).collect(Collectors.toList());
		threads.forEach(Thread::start);
		
		//joinǰ�Ѿ�ִ���꣬��������
		TimeUnit.SECONDS.sleep(10);
		
		//���߳�join������1��2�߳�ִ����main�ſ�ʼ����ִ�С�
		//join��һ���̵߳�ʱ��main���������Եڶ���join����ִ��
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
