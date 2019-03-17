package chapter08;

@FunctionalInterface
public interface DenyPolicy {
	
	void reject(Runnable runnable, ThreadPool threadPool);
	
	//直接将任务丢弃
	class DiscardDenyPolicy implements DenyPolicy{

		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			// do nothing
		}
	}
	
	//抛出异常
	class AbortDenyPolicy implements DenyPolicy{

		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			throw new RunnableDenyException("该任务将会被直接抛弃");
		}
	}	
	
	//在提交者所在线程中执行任务
	class RunnerDenyPolicy implements DenyPolicy{

		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			if( !threadPool.isShutdown()){
				runnable.run();
			}
		}
		
	}
}
