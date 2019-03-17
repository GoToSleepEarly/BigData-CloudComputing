package chapter19;

import java.util.concurrent.TimeUnit;

public class FutureTest {

	public static void main(String[] args) throws InterruptedException {
		//定义有返回值的FutureService
		FutureService<String, Integer> service = FutureService.newService();
		//submit方法会立即返回
		Future<Integer> future = service.submit(input -> {
			//匿名函数覆写get方法，输入参数为input
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return input.length();
		}, "Hello");
		//get方法会陷入阻塞
		System.out.println(future.get());
		
		//定义无返回值的FutureService
		FutureService<Void, Void> service1 = FutureService.newService();
		//submit立即返回
		Future<?> future1 = service1.submit(() -> {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("运行结束");
		});
		//get方法会阻塞
		future1.get();
		
		//定义有返回值的FutureService
		FutureService<String, Integer> service3 = FutureService.newService();
		//submit方法会立即返回
		Future<Integer> future3 = service.submit(input -> {
			//匿名函数覆写get方法，输入参数为input
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return input.length();
		}, "Hello-3",System.out::println);
	}
}
