package chapter20;

import java.util.LinkedList;

public class GuardedSuspensionQueue {

	
	//定义存放Integer类型的queue
	private final  LinkedList<Integer> queue  = new LinkedList<>();
	
	//定义queue的最大容量为100
	private final int LIMIT = 100;
	
	//往queue中插入数据
	public void offer(Integer data) throws InterruptedException{
		synchronized (this) {
			//判断queue的当前元素是否超过了limit
			while(queue.size() >= LIMIT){
				//阻塞线程
				this.wait();
			}
			//插入元素并唤醒take
			queue.add(data);
			this.notifyAll();
		}
	}
	
	//往queue中取数据
	public Integer take() throws InterruptedException{
		synchronized (this) {
			while(queue.isEmpty()){
				this.wait();
			}
			this.notifyAll();
			return queue.removeFirst();
		}
	}
		
	
}
