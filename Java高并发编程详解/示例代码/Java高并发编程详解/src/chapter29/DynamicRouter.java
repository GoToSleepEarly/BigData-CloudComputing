package chapter29;

public interface DynamicRouter<E extends Message>{
	//���ÿ��Message����ע����ص�Channel��ֻ���ҵ����ʵ�Channel���ܱ�����
	void registerChannel(Class<? extends E> messageType, Channel<? extends E> channel);
	
	//Ϊ��Ӧ��Channel����Message
	void dispatch(E message);
}
