package chapter15;

public interface Observable {
	//任务生命周期的枚举类型
	enum Cycle{
		STARTED, RUNNING, DONE, ERROR
	}
	
	//获取当前任务的生命周期状态
	Cycle getCycle();
	
	//定义线程的启动方法，主要为了屏蔽Thread的其他方法
	void start();
	
	//定义打断方法，作用与start一样， 为了屏蔽
	void interrupt();
}
