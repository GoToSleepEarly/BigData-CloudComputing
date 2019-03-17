package chapter05;

import java.util.List;
import java.util.concurrent.TimeoutException;

public interface Lock {
	/*
	- lock()方法永远阻塞，除非获得锁，和synchronized相似，除了可以中断并抛出InterruptedException异常
	- lock(long mills)除了被中断，还增加超时功能
	- unlock() 释放锁
	- getBlockedThreads()用于获取当前被阻塞的线程。
	 */
	void lock() throws InterruptedException;
	
	void lock(long mills) throws InterruptedException, TimeoutException; 
	
	void unlock();
	
	List<Thread> getBlockedThread();
}
