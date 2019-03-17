package chapter02;

public class stackSizeTest {
	public static void main(String[] args) {
		Runnable runnable = new Runnable(){
			final int MAX = Integer.MAX_VALUE;
			
			@Override
			public void run(){
				int i =0;
				recurse(i);
			}
			
			private void recurse(int i){
				System.out.println(i);
				if(i<MAX){
					recurse(i+1);
				}
			}
		};
		Thread t = new Thread(Thread.currentThread().getThreadGroup(),runnable,"xzt",10000);
		t.start();
	}
}
