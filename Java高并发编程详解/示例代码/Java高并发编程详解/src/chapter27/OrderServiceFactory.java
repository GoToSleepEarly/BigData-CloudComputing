package chapter27;

public class OrderServiceFactory {

	//��ActiveMessageQueue����Ϊstatic��Ŀ���ǣ�������������JVM��������Ψһ�ģ�����ActiveDaemonThread�ʹ�����
	private final static ActiveMessageQueue activeMessageQueue = new ActiveMessageQueue();
	
	//������ͨ���ⲿnew��������
	private OrderServiceFactory(){
		
	}
	//����OrderServiceProxy
	public static OrderService toActiveObject(OrderService orderService){
		return new OrderServiceProxy(orderService, activeMessageQueue);
	}
	public static void main(String[] args) throws InterruptedException {
		//�ڴ���OrderServiceʱ��Ҫ����OrderService�ӿڵľ���ʵ��
		OrderService orderService = OrderServiceFactory.
				toActiveObject(new OrderServiceImpl());
		orderService.order("Hello", 453453);
		System.out.println("������");
		Thread.currentThread().join();
	}

}
