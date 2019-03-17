package chapter08;

import java.util.LinkedList;

public class LinkedRunnableQueue implements RunnableQueue{
	
	//������е�����������ڹ���ʱ����
	private final int limit;
	
	//�����������������Ҫִ�оܾ�����
	private final DenyPolicy denyPolicy;
	
	//�������Ķ���
	private final LinkedList<Runnable> runnableList = new LinkedList<>();
	
	private final ThreadPool threadPool;
	
	//���캯����һ��������Ҫlimit���ܾ����ԣ��̳߳�
	public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, 
			ThreadPool  threadPool) {
		this.limit = limit;
		this.denyPolicy = denyPolicy;
		this.threadPool = threadPool;
	}
	
	@Override
	public void offer(Runnable runnable) {
		synchronized (runnableList) {
			//��Ϊofferû��wait�����Բ���Ҫwhile�жϡ�
			if(runnableList.size() >= limit){
				//�޷�����ʱִ�оܾ�����
				denyPolicy.reject(runnable, threadPool);
			}else{
				//������ӵ���β�����һ����������߳�
				runnableList.addLast(runnable);
				runnableList.notifyAll();
			}
		}
	}

	@Override
	public Runnable take() throws InterruptedException {
		synchronized (runnableList) {
			while(runnableList.isEmpty()){
				try {
					//���û�п�ִ�����������𡣵ȴ�offer��notifyAll()
					runnableList.wait();
				} catch (InterruptedException e) {
					//���ж�ʱ��Ҫ�׳��쳣
					e.printStackTrace();
					throw e;
				}
			}
			return runnableList.removeFirst();
		}
	}

	@Override
	public int size() {
		//��Ҫ��������Ȼ���ܳ��쳣
		synchronized (runnableList) {
			return runnableList.size();
		}
	}
	
}
