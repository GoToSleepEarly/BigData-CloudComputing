package chapter15;

public interface TaskLifeCycle<T> {
	//��������
	void onStart(Thread t);
	
	//���д���
	void onRunning(Thread t);
	
	//����������result�ǽ��
	void onFinish(Thread t, T result);
	
	//������
	void onError(Thread t, Exception e);
	
	//��������
	class EmptyLifeCycle<T> implements TaskLifeCycle<T>{
		
		@Override
		public void onStart(Thread t) {
			//do nothing	
		}

		@Override
		public void onRunning(Thread t) {
			//do nothing
			
		}

		@Override
		public void onFinish(Thread t, T result) {
			//do nothing	
		}

		@Override
		public void onError(Thread t, Exception e) {
			//do nothing
		}
	}
	
}
