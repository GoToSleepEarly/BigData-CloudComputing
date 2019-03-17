package chapter08;

@FunctionalInterface
public interface DenyPolicy {
	
	void reject(Runnable runnable, ThreadPool threadPool);
	
	//ֱ�ӽ�������
	class DiscardDenyPolicy implements DenyPolicy{

		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			// do nothing
		}
	}
	
	//�׳��쳣
	class AbortDenyPolicy implements DenyPolicy{

		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			throw new RunnableDenyException("�����񽫻ᱻֱ������");
		}
	}	
	
	//���ύ�������߳���ִ������
	class RunnerDenyPolicy implements DenyPolicy{

		@Override
		public void reject(Runnable runnable, ThreadPool threadPool) {
			if( !threadPool.isShutdown()){
				runnable.run();
			}
		}
		
	}
}
