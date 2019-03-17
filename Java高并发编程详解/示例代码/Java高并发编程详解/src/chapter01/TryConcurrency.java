package chapter01;

import java.util.concurrent.TimeUnit;

public class TryConcurrency {

	public static void main(String[] args) {
		/*
		 	browseNews();
			enjoyMusic();
			���������ύ�����,ֻ��һֱbrowseNews.
		 */
		
		//java 8 Lambda
		//new Thread(TryConcurrency::enjoyMusic).start();
		new Thread(){
			@Override
			public void run(){
				enjoyMusic();
			}
		}.start();
		browseNews();
		}

	/***
	 *  �����µ�����~
	 */
	private static void enjoyMusic() {
		for(;;){
			System.out.println("Uh-huh, �������ţ�");
			sleep(1);
		}
		
	}
	
	/***
	 *  �����˵�����~
	 */
	private static void browseNews() {
		for(;;){
			System.out.println("Uh-huh, �������֣�");
			sleep(1);
		}
	}
	
	private static void sleep(int seconds) {
		try {
			TimeUnit.SECONDS.sleep(seconds);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
