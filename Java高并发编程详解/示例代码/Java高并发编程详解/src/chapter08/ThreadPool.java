package chapter08;

public interface ThreadPool {
	
	//�ύ�����߳�
	void execute(Runnable runnable);

	//�ر��̳߳�
	void shutdown();
	
	//��ȡ�̳߳صĳ�ʼ����С
	int getInitSize();
	
	//��ȡ�̳߳������߳���
	int getMaxSize();
	
	//��ȡ�̳߳صĺ����߳�����
	int getCoreSize();
	
	//��ȡ�̳߳������ڻ���������еĴ�С
	int getQueueSize();
	
	//��ȡ�̳߳��л�Ծ�̵߳�����
	int getActiveCount();
	
	//�鿴�̳߳��Ƿ��ѱ�shutdown
	boolean isShutdown();




}
