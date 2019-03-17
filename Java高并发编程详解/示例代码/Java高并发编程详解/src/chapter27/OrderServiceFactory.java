package chapter27;

public class OrderServiceFactory {

	//将ActiveMessageQueue定义为static的目的是，保持其在整个JVM进程中是唯一的，并且ActiveDaemonThread就此启动
	private final static ActiveMessageQueue activeMessageQueue = new ActiveMessageQueue();
	
	//不允许通过外部new方法构建
	private OrderServiceFactory(){
		
	}
	//返回OrderServiceProxy
	public static OrderService toActiveObject(OrderService orderService){
		return new OrderServiceProxy(orderService, activeMessageQueue);
	}
	public static void main(String[] args) throws InterruptedException {
		//在创建OrderService时需要传递OrderService接口的具体实现
		OrderService orderService = OrderServiceFactory.
				toActiveObject(new OrderServiceImpl());
		orderService.order("Hello", 453453);
		System.out.println("立马返回");
		Thread.currentThread().join();
	}

}
