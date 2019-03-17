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
				if(Thread.currentThread().getName().equals("1号柜台机"))
					index++;
				else
					try {
						TimeUnit.NANOSECONDS.sleep(10);
						index++;
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				System.out.println(Thread.currentThread()+"的号码"+(index));
				
				/*try {
					TimeUnit.MILLISECONDS.sleep(100);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}*/
				//System.out.println(Thread.currentThread()+"的号码是:"+(++index));
			}
		}
		
	//}
	public static void main(String[] args) {
		final TicketWindowRunnable task = new TicketWindowRunnable();
		
		Thread windowThread1 = new Thread(task,"1号柜台机");
		Thread windowThread2 = new Thread(task,"2号柜台机");
		Thread windowThread3 = new Thread(task,"3号柜台机");
		//Thread windowThread4 = new Thread(task,"4号柜台机");
		
		windowThread1.start();
		windowThread2.start();
		windowThread3.start();
		//windowThread4.start();
	}

}
