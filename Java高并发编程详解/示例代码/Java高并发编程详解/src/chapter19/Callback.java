package chapter19;

public interface Callback<T> {
	//任务完成后会调用此方法，其中T为任务执行后的结果
	void call(T t);
}
