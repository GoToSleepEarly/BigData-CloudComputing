package chapter17;

public interface ReadWriteLock {
 
 
    //创建读锁
    Lock readLock();
 
    //创建写锁
    Lock writeLock();
 
    // 获取当前有多少个线程正在执行写操作 最多1个
    int getWritingWriters();
 
    // 获取当前有多少线程正在等待获取写入锁 由于写锁导致阻塞
    int getWaitingWriters();
 
    // 等待当前有多少线程正在等待获取读锁
    int getReadingReaders();
 
    //  工厂方法创建ReadWriteLock 
    static ReadWriteLock readWriteLock() {
        return new ReadWriteLockImpl();
    }
 
    // 工厂方法，创建ReadWriteLock ，并传入preferWriter
    static ReadWriteLock readWriteLock(boolean preferWriter) {
        return new ReadWriteLockImpl(preferWriter);
    }
 
 
 
}
