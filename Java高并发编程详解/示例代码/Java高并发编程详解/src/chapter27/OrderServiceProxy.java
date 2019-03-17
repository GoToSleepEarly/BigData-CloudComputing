package chapter27;

import java.util.HashMap;
import java.util.Map;

import chapter19.Future;

public class OrderServiceProxy implements OrderService{
	
	private final OrderService orderService;
	private final ActiveMessageQueue activeMessageQueue;
	
	public OrderServiceProxy(OrderService orderService, ActiveMessageQueue activeMessageQueue){
		this.orderService = orderService;
		this.activeMessageQueue = activeMessageQueue;
	}
	
	@Override
	public Future<String> findOrderDetails(long orderId) {
		//����һ��ActiveFuture������֧����������
		final ActiveFuture<String> activeFuture = new ActiveFuture<>();
		//�ռ�������κͷ��ص�ActiveFuture��װ��MethodMessage
		Map<String, Object> params = new HashMap<>();
		params.put("orderId",  orderId);
		params.put("activeFuture", activeFuture);
		MethodMessage message = new FindOrderDetailMessage(params, orderService);
		//��MethodMessage������activeMessageQueue
		activeMessageQueue.offer(message);
		return activeFuture;
	}

	@Override
	public void order(String account, long orderId) {
		Map<String, Object> params = new HashMap<>();
		params.put("account", account);
		params.put("orderId",  orderId);
		MethodMessage message = new OrderMessage(params, orderService);
		//��MethodMessage������activeMessageQueue
		activeMessageQueue.offer(message);
	}
	
}
