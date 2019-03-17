package chapter27;

import java.util.LinkedList;

public class ActiveMessageQueue {
	
	//���ڴ���ύ��MethodMessage����
	private final LinkedList<MethodMessage> messages = new LinkedList<>();
	
	public ActiveMessageQueue(){
		//����worker�߳�
		new ActiveDaemonThread(this).start();
	}
	
	public void offer(MethodMessage methodMessage){
		synchronized (this) {
			messages.addLast(methodMessage);
			//��Ϊֻ��һ���̸߳���take���ݣ�����û��ҪnotifyAll
			this.notify();
		}
	}
	
	protected MethodMessage take(){
		synchronized (this) {
			//��û��Message��ʱ��ִ���߳̽�������
			while(messages.isEmpty()){
				try {
					this.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			//��ȡ����һ����ɾ��
			return messages.removeFirst();
			
		}
	}
}
