package chapter12;

public class ThreadCloseale extends Thread {

	//�ɼ��� �ر��߳�
	private volatile boolean started = true;
	
	@Override
	public void run(){
		while(started){
			//do something
		}
	};
	
	public void shutdown(){
		this.started = false;
	}

}
