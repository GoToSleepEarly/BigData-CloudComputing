package chapter04;

import java.util.concurrent.TimeUnit;

public class TicketWindowRunnable implements Runnable{

	private int index =1;
	
	private final static int MAX = 10;
	
	private final static Object MUTEX = new Object();
	@Override
	public void run(){
		//synchronized (MUTEX) {
			while(index <= MAX){
				System.out.println(Thread.currentThread()+""+index);
				if(Thread.currentThread().getName().equals("1�Ź�̨��"))
					index++;
				else
					try {
						TimeUnit.NANOSECONDS.sleep(10);
						index++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				System.out.println(Thread.currentThread()+"�ĺ���"+(index));
				
				/*try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				//System.out.println(Thread.currentThread()+"�ĺ�����:"+(++index));
			}
		}
		
	//}
	public static void main(String[] args) {
		final TicketWindowRunnable task = new TicketWindowRunnable();
		
		Thread windowThread1 = new Thread(task,"1�Ź�̨��");
		Thread windowThread2 = new Thread(task,"2�Ź�̨��");
		Thread windowThread3 = new Thread(task,"3�Ź�̨��");
		//Thread windowThread4 = new Thread(task,"4�Ź�̨��");
		
		windowThread1.start();
		windowThread2.start();
		windowThread3.start();
		//windowThread4.start();
	}

}
