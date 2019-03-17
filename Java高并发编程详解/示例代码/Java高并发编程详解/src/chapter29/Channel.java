package chapter29;

public interface Channel<E extends Message> {
	//dispatch方法用于负责Message调度
	void dispatch(E message);
}
