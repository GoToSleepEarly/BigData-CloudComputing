package chapter05;

import static java.util.concurrent.ThreadLocalRandom.current;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static java.lang.Thread.currentThread;
public class BooleanLockTest {
	
	//定义BooleanLock
	private final Lock lock = new BooleanLock();
		
	public void syncMethod() {
		try {
			lock.lock();
			int randomInt = current().nextInt(5);
			System.out.println(currentThread()+"得到了锁"+randomInt);
			TimeUnit.SECONDS.sleep(randomInt);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally{
			lock.unlock();
		}
	}
	public static void main(String[] args) throws InterruptedException {
		BooleanLockTest blt =new BooleanLockTest();
		/*IntStream.range(0, 10).mapToObj(i -> new Thread(() -> {
				blt.syncMethod();
		})).forEach(Thread::start);
		*/
		
		new Thread(blt::syncMethod,"T1").start();
		TimeUnit.MILLISECONDS.sleep(2);
		Thread t2 = new Thread(blt::syncMethod,"T2");
		t2.start();
		TimeUnit.MILLISECONDS.sleep(10);
		t2.interrupt();
	}

}
