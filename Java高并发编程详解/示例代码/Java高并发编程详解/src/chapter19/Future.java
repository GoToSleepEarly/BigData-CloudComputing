package chapter19;

//Future��������
public interface Future<T> {
	//��ȡ���������÷������������
	T get() throws InterruptedException;
	
	//�ж������Ƿ��Ѿ���ִ�����
	boolean done();
	
}
