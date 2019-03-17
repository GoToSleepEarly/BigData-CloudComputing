package chapter08;

@FunctionalInterface
public interface ThreadFactory {
	
	//创建线程
	Thread createThread(Runnable runnable);
}
