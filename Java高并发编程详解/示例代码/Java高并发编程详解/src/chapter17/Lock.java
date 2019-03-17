package chapter17;

public interface Lock {
	//¼ÓËø
	void lock() throws InterruptedException;
	//½âËø
	void unlock();
}
