package chapter19;

//Future就是屏障
public interface Future<T> {
	//获取计算结果，该方法会进入阻塞
	T get() throws InterruptedException;
	
	//判断任务是否已经被执行完成
	boolean done();
	
}
