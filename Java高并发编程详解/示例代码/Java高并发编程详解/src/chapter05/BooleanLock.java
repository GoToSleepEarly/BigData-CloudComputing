package chapter05;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

import static java.lang.Thread.currentThread;;
public class BooleanLock implements Lock {

	private Thread currentThread;
	
	private boolean locked = false;
	
	private final List<Thread> blockedList = new ArrayList<>();
	
	@Override
	public void lock() throws InterruptedException {
		synchronized (this) {
			while(locked){
				//暂存当前线程
				final Thread tempThread = currentThread();
				try{
					if(!blockedList.contains(tempThread))
						blockedList.add(tempThread);
						this.wait();
				}catch(InterruptedException e){
					//如果在wait过程中被中断，则remove
					blockedList.remove(tempThread);
					throw e;
				}
			}
			blockedList.remove(currentThread());
			this.locked = true;
			this.currentThread = currentThread();
		}
	}

	@Override
	public void lock(long mills) throws InterruptedException, TimeoutException {
		synchronized (this) {
			//mills不合法，就默认调用lock
			if(mills <= 0){
				this.lock();
			}else{
				long remainingMills = mills;
				long endMills = System.currentTimeMillis() + remainingMills;
				while(locked){
					//如果remainingMills《=0，显然当前线程已被唤醒，或超过wait时间，此时抛出异常
					if(remainingMills <=0){
						throw new TimeoutException("无法在"+ mills +"时间内获得锁");
					}
					if(!blockedList.contains(currentThread())){
						blockedList.add(currentThread());
					}
					//
					//和lock一样，需要trycatch删除异常时的thread
					this.wait(remainingMills);
					remainingMills = endMills - System.currentTimeMillis();
				}
				blockedList.remove(currentThread());
				this.locked = true;
				this.currentThread = currentThread();
			}
		}
	}

	@Override
	public void unlock() {
		synchronized (this) {
			//只有上锁的currentThread才能解锁
			if(currentThread == currentThread()){
				this.locked = false;
				System.out.println("释放了锁");
				this.notifyAll();
			}
		}
	}

	@Override
	public List<Thread> getBlockedThread() {
		return Collections.unmodifiableList(blockedList);
	}

}
