package chapter27;

import chapter19.Future;

public interface OrderService {
	
	//���ݶ�����Ų�ѯ������ϸ�������Ҳ�з���ֵ�����Ƿ������ͱ�����Future
	Future<String> findOrderDetails(long orderId);
	
	//�ύ������û�з���ֵ
	void order(String account, long orderId);
	
}
