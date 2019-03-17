package chapter17;

//设计为包可见
class WriteLock implements Lock {

 private final ReadWriteLockImpl readWriteLock ;

 WriteLock(ReadWriteLockImpl readWriteLock ){
     this.readWriteLock = readWriteLock ;
 }



 @Override
 public void lock() throws InterruptedException {
     synchronized (readWriteLock.getMUTEX()){

         try{
             // 等待获取写入锁的数字加一
             readWriteLock.increamentWaitingWriters();

             // 如果此时有其他线程正在进行读操作，或者写操作，那么当前线程将被挂起
             while (readWriteLock.getReadingReaders() > 0 || readWriteLock.getWritingWriters()>0 ){
                 readWriteLock.getMUTEX().wait();
             }

         }finally {
             // 成功取得到写入锁，使得等待获取写入锁的计数器减一
             this.readWriteLock.decrementWaitingWriters();
         }
         readWriteLock.incrementWritingWriters();
     }
 }

 @Override
 public void unlock() {
     synchronized (readWriteLock.getMUTEX()){
         // 减少正在写入锁的线程计数器
         readWriteLock.decrementWritingWriters();

         // 将偏好状态修改为false ，可以使得读锁会更快的获得
         readWriteLock.changePrefer(false);

         //唤醒其他线程
         readWriteLock.getMUTEX().notifyAll();
     }
 }
}
