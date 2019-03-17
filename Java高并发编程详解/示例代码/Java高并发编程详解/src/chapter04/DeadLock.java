package chapter04;


public class DeadLock {
	private final Object MUTEX_READ = new Object();
	private final Object MUTEX_WRITE = new Object();

	public void read(){
		synchronized (MUTEX_READ) {
			System.out.println(Thread.currentThread().getName()+"获得读取锁");
			synchronized (MUTEX_WRITE) {
				System.out.println(Thread.currentThread().getName()+"获得写入锁");
			}
			System.out.println(Thread.currentThread().getName()+"释放写入锁");
		}
		System.out.println(Thread.currentThread().getName()+"释放读取锁");
	}
	public void write(){
		synchronized (MUTEX_WRITE) {
			System.out.println(Thread.currentThread().getName()+"获得写入锁");
			synchronized (MUTEX_READ) {
				System.out.println(Thread.currentThread().getName()+"获得读取锁");
			}
			System.out.println(Thread.currentThread().getName()+"释放读取锁");
		}
		System.out.println(Thread.currentThread().getName()+"释放写入锁");
	}
	
	public static void main(String[] args) {
		DeadLock deadLock = new DeadLock();
		//会直接执行
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
