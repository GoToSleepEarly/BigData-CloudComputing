package chapter19;

import java.util.concurrent.TimeUnit;

public class FutureTest {

	public static void main(String[] args) throws InterruptedException {
		//�����з���ֵ��FutureService
		FutureService<String, Integer> service = FutureService.newService();
		//submit��������������
		Future<Integer> future = service.submit(input -> {
			//����������дget�������������Ϊinput
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return input.length();
		}, "Hello");
		//get��������������
		System.out.println(future.get());
		
		//�����޷���ֵ��FutureService
		FutureService<Void, Void> service1 = FutureService.newService();
		//submit��������
		Future<?> future1 = service1.submit(() -> {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("���н���");
		});
		//get����������
		future1.get();
		
		//�����з���ֵ��FutureService
		FutureService<String, Integer> service3 = FutureService.newService();
		//submit��������������
		Future<Integer> future3 = service.submit(input -> {
			//����������дget�������������Ϊinput
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return input.length();
		}, "Hello-3",System.out::println);
	}
}
