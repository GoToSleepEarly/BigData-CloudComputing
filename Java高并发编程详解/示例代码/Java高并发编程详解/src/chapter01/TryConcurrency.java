package chapter01;

import java.util.concurrent.TimeUnit;

public class TryConcurrency {

	public static void main(String[] args) {
		/*
		 	browseNews();
			enjoyMusic();
			这样并不会交替输出,只会一直browseNews.
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
	 *  看最新的新闻~
	 */
	private static void enjoyMusic() {
		for(;;){
			System.out.println("Uh-huh, 最新新闻！");
			sleep(1);
		}
		
	}
	
	/***
	 *  听最嗨的音乐~
	 */
	private static void browseNews() {
		for(;;){
			System.out.println("Uh-huh, 最嗨音乐！");
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
