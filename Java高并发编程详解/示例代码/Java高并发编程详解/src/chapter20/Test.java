package chapter20;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		GuardedSuspensionQueue q = new GuardedSuspensionQueue();
		q.take();
		//q.offer(1);
		System.out.println("之前就被阻塞");
	}

}
