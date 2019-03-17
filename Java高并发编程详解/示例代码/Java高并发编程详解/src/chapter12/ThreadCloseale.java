package chapter12;

public class ThreadCloseale extends Thread {

	//可见性 关闭线程
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
