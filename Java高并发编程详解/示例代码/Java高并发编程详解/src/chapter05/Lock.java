package chapter05;

import java.util.List;
import java.util.concurrent.TimeoutException;

public interface Lock {
	/*
	- lock()������Զ���������ǻ��������synchronized���ƣ����˿����жϲ��׳�InterruptedException�쳣
	- lock(long mills)���˱��жϣ������ӳ�ʱ����
	- unlock() �ͷ���
	- getBlockedThreads()���ڻ�ȡ��ǰ���������̡߳�
	 */
	void lock() throws InterruptedException;
	
	void lock(long mills) throws InterruptedException, TimeoutException; 
	
	void unlock();
	
	List<Thread> getBlockedThread();
}
