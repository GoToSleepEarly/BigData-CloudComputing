package chapter01;

public class TicketWindowRunnable implements Runnable{

	private int index =1;
	
	private final static int MAX = 50;
	
	@Override
	public void run(){
		
		while(index <= MAX){
			System.out.println(Thread.currentThread()+"�ĺ�����:"+(index++));
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	public static void main(String[] args) {
		final TicketWindowRunnable task = new TicketWindowRunnable();
		
		Thread windowThread1 = new Thread(task,"1�Ź�̨��");
		Thread windowThread2 = new Thread(task,"2�Ź�̨��");
		Thread windowThread3 = new Thread(task,"3�Ź�̨��");
		Thread windowThread4 = new Thread(task,"4�Ź�̨��");
		
		windowThread1.start();
		windowThread2.start();
		windowThread3.start();
		windowThread4.start();
	}

}
