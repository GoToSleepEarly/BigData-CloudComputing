package chapter12;

import java.util.concurrent.TimeUnit;

public class VolatileFoo {
	
	final static int MAX = 5;
	
	//volatile 
	static int init_value = 0;
	
	public static void main(String[] args) {
		new Thread(() -> {
			int localValue = init_value;
			while(localValue < MAX){
				if(init_value != localValue){
					System.out.println("init_value�Ը�����"+init_value);
					localValue = init_value;
				}
			}
		},"Reader").start();
		
		new Thread(() -> {
			int localValue = init_value;
			while(localValue < MAX){
				System.out.println("�޸�init_value��"+ ++localValue);
				init_value = localValue;
				try {
					TimeUnit.SECONDS.sleep(2);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		},"Updater").start();
	}

}
