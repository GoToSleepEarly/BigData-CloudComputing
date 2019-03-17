package chapter29;

public interface DynamicRouter<E extends Message>{
	//针对每种Message类型注册相关的Channel，只有找到合适的Channel才能被处理
	void registerChannel(Class<? extends E> messageType, Channel<? extends E> channel);
	
	//为响应的Channel分配Message
	void dispatch(E message);
}
