package chapter15;

@FunctionalInterface
public interface Task<T> {
	//����ִ�нӿ� �ýӿ������з���ֵ
	T call();
}
