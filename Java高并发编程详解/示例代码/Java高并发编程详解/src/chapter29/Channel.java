package chapter29;

public interface Channel<E extends Message> {
	//dispatch�������ڸ���Message����
	void dispatch(E message);
}
