package chapter08;

public interface RunnableQueue {

	//���������������Ȼ�offer��������
	void offer(Runnable runnbale);
	
	//�����߳�ͨ��take������ȡRunnable
	Runnable take() throws InterruptedException;
	
	//��ȡ������������������
	int size();
}
