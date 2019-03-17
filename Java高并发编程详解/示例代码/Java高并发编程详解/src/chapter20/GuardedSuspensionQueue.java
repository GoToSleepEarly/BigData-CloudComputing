package chapter20;

import java.util.LinkedList;

public class GuardedSuspensionQueue {

	
	//������Integer���͵�queue
	private final  LinkedList<Integer> queue  = new LinkedList<>();
	
	//����queue���������Ϊ100
	private final int LIMIT = 100;
	
	//��queue�в�������
	public void offer(Integer data) throws InterruptedException{
		synchronized (this) {
			//�ж�queue�ĵ�ǰԪ���Ƿ񳬹���limit
			while(queue.size() >= LIMIT){
				//�����߳�
				this.wait();
			}
			//����Ԫ�ز�����take
			queue.add(data);
			this.notifyAll();
		}
	}
	
	//��queue��ȡ����
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
