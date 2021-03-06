package chapter27;

import java.util.Map;

import chapter19.Future;

public class FindOrderDetailMessage extends MethodMessage {

	public FindOrderDetailMessage(Map<String, Object> params, OrderService orderService) {
		super(params, orderService);
	}

	@Override
	public void execute() {
		// 执行orderService的findOrderDetails方法
		Future<String> realFuture = orderService.findOrderDetails((Long)params.get("orderId"));
		ActiveFuture<String> activeFuture = (ActiveFuture<String>) params.get("activeFuture");
		try {
			//调用orderServiceImpl返回的Future.get()，会阻塞直到findOrderDetails方法结束
			String result = realFuture.get();
			//通过finish传递给activefuture结果
			activeFuture.finish(result);
		} catch (InterruptedException e) {
			activeFuture.finish(null);
		}
		
	}

}
