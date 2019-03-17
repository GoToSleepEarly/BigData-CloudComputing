package chapter04;


public class DeadLock {
	private final Object MUTEX_READ = new Object();
	private final Object MUTEX_WRITE = new Object();

	public void read(){
		synchronized (MUTEX_READ) {
			System.out.println(Thread.currentThread().getName()+"��ö�ȡ��");
			synchronized (MUTEX_WRITE) {
				System.out.println(Thread.currentThread().getName()+"���д����");
			}
			System.out.println(Thread.currentThread().getName()+"�ͷ�д����");
		}
		System.out.println(Thread.currentThread().getName()+"�ͷŶ�ȡ��");
	}
	public void write(){
		synchronized (MUTEX_WRITE) {
			System.out.println(Thread.currentThread().getName()+"���д����");
			synchronized (MUTEX_READ) {
				System.out.println(Thread.currentThread().getName()+"��ö�ȡ��");
			}
			System.out.println(Thread.currentThread().getName()+"�ͷŶ�ȡ��");
		}
		System.out.println(Thread.currentThread().getName()+"�ͷ�д����");
	}
	
	public static void main(String[] args) {
		DeadLock deadLock = new DeadLock();
		//��ֱ��ִ��
		//new Thread(deadLock::read).start();
		//new Thread(deadLock::write).start();
		new Thread(() ->{
			while(true){
				deadLock.read();
			}
		},"READ-THREAD").start();
		new Thread(() ->{
			while(true){
				deadLock.write();
			}
		},"WRITE-THREAD").start();
	}
	

}
