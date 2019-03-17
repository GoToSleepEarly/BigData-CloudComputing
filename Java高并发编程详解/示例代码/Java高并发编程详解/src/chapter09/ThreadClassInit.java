package chapter09;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class ThreadClassInit {

	static {
		System.out.println("¾²Ì¬´úÂë¿é¼¤»î");
		try {
			TimeUnit.SECONDS.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args) {
		IntStream.range(0, 5).forEach(i -> {
			new Thread(ThreadClassInit::new);
		});
	}

}
