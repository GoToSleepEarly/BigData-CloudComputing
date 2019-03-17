package chapter19;

public interface FutureService<IN, OUT> {
	//�ύ����Ҫ����ֵ������,Future.get������null
	Future<?> submit(Runnable runnable);
	 
	//�ύ��Ҫ����ֵ����������Task�ӿڴ�����Runnable�ӿ�
	Future<OUT> submit(Task<IN,OUT> task, IN input);
	
	//ʹ�þ�̬��������һ��FutureService��ʵ��
	static <T,R> FutureService<T,R> newService(){
		return new FutureServiceImpl<>();
	}

	Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback);
}
