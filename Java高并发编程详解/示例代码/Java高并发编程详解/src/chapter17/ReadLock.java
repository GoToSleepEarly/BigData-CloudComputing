package chapter17;
 
// 设置为包可见
class ReadLock implements Lock {
 
    private final ReadWriteLockImpl readWriteLock ;
 
    ReadLock(ReadWriteLockImpl readWriteLock ){
        this.readWriteLock = readWriteLock ;
    }
 
 
 
    @Override
    public void lock() throws InterruptedException {
        // 使用MUTEX作为锁
        synchronized (readWriteLock.getMUTEX()){
            // 若此时有线程正在进行写操作，或者有写线程在等待并且偏向写锁的标识为ture，就无法获取读锁，只能挂起
            while(readWriteLock.getWritingWriters() > 0 || (readWriteLock.getPreferWriter() && readWriteLock.getWaitingWriters() >0 )){
 
                readWriteLock.getMUTEX().wait();
            }
            // 成功获取读锁，并且使readingReader的数量增加
            readWriteLock.increamentReadingReaders();
        }
    }
 
    @Override
    public void unlock() {
        //使用Mutex作为锁，并且进行同步
        synchronized (readWriteLock.getMUTEX()){
            // 释放锁的过程就是使得当前reading的数量减一
            // 将perferWriter设置为true ， 可以使得writer线程获得更多的机会
            // 通知唤醒与Mutex唤醒monitor waitset中的线程
 
            readWriteLock.decrementReadingReaders();
            readWriteLock.changePrefer(true);
            readWriteLock.getMUTEX().notifyAll();
        }
    }
}
