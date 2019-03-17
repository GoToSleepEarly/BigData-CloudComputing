package chapter15;

public interface TaskLifeCycle<T> {
	//启动触发
	void onStart(Thread t);
	
	//运行触发
	void onRunning(Thread t);
	
	//结束触发，result是结果
	void onFinish(Thread t, T result);
	
	//报错触发
	void onError(Thread t, Exception e);
	
	//生命周期
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
