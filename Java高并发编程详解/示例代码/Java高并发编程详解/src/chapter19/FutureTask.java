package chapter19;

public class FutureTask<T> implements Future<T> {
	//������
	private T result;
	
	//�����Ƿ����
	private boolean isDone = false;
	
	//���������
	private final Object LOCK = new Object();

	
	@Override
	public T get() throws InterruptedException {
		synchronized (LOCK) {
			//������û���ʱ������get��������������
			while(!isDone){
				LOCK.wait();
			}
			//���ؽ��
			return result;
		}
	}

	//finish��������ΪFutureTask���ü�����
	protected void finish(T result){
		synchronized (LOCK) {
			//balking���ģʽ
			if(isDone){
				return;
			}
			//������ɣ�Ϊresultָ����������ҽ�isDone��Ϊtrue��ͬʱ���������е��߳�
			this.result = result;
			this.isDone = true;
			LOCK.notifyAll();
		}
	}
	
	//���ص�ǰ�����Ƿ��Ѿ����
	@Override
	public boolean done() {
		return isDone;
	}
	
}
