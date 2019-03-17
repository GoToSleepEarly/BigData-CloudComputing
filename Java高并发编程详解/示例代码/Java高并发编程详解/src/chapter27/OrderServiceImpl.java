package chapter27;

import java.util.concurrent.TimeUnit;

import chapter19.Future;
import chapter19.FutureService;

public class OrderServiceImpl implements OrderService{

	@Override
	public Future<String> findOrderDetails(long orderId) {
		//使用Future来返回结果
		return FutureService.<Long, String>newService().submit(input -> {
			
			//通过休眠方法来模拟该方法执行耗时
			try {
				TimeUnit.SECONDS.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.out.println("处理orderID "+ orderId);
			return "The order Details Information";
		},orderId, null);
	}

	@Override
	public void order(String account, long orderId) {
		try {
			TimeUnit.SECONDS.sleep(10);
			System.out.println("处理"+account+"的order"+orderId);
		} catch (InterruptedException e) {
			e.printStackTrace();
		};
	}
}
