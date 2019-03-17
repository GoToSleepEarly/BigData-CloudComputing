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
				//�ݴ浱ǰ�߳�
				final Thread tempThread = currentThread();
				try{
					if(!blockedList.contains(tempThread))
						blockedList.add(tempThread);
						this.wait();
				}catch(InterruptedException e){
					//�����wait�����б��жϣ���remove
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
			//mills���Ϸ�����Ĭ�ϵ���lock
			if(mills <= 0){
				this.lock();
			}else{
				long remainingMills = mills;
				long endMills = System.currentTimeMillis() + remainingMills;
				while(locked){
					//���remainingMills��=0����Ȼ��ǰ�߳��ѱ����ѣ��򳬹�waitʱ�䣬��ʱ�׳��쳣
					if(remainingMills <=0){
						throw new TimeoutException("�޷���"+ mills +"ʱ���ڻ����");
					}
					if(!blockedList.contains(currentThread())){
						blockedList.add(currentThread());
					}
					//
					//��lockһ������Ҫtrycatchɾ���쳣ʱ��thread
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
			//ֻ��������currentThread���ܽ���
			if(currentThread == currentThread()){
				this.locked = false;
				System.out.println("�ͷ�����");
				this.notifyAll();
			}
		}
	}

	@Override
	public List<Thread> getBlockedThread() {
		return Collections.unmodifiableList(blockedList);
	}

}
