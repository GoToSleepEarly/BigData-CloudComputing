package chapter27;

import java.util.Map;

public abstract class MethodMessage {
	
	//�ռ������������������Future������һ���ռ�
	protected final Map<String, Object> params;
	
	protected final OrderService orderService;
	
	public MethodMessage(Map<String, Object> params, OrderService orderService){
		this.params = params;
		this.orderService = orderService;
	}
	
	//���󷽷�������worker Thread ��˵����
	public abstract void execute();
}
