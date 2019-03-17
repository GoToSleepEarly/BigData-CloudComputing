package chapter17;
 
 
// 对包可见的类
class ReadWriteLockImpl implements ReadWriteLock {
 
    // 定义对象锁
    private final Object MUTEX = new Object();
 
    //  当前有多少个线程正在执行写操作 最多1个
    private int  writingWriters = 0 ;
 
    //  当前有多少线程正在等待获取写入锁 由于写锁导致阻塞
    private int waitingWriters = 0 ;
 
    // 等待当前有多少线程正在等待获取读锁
    private int readingReaders = 0 ;
 
    //read 和 writer 的偏好设置
    private boolean preferWriter ;
 
    public ReadWriteLockImpl(){
        this(true);
    }
 
    public ReadWriteLockImpl(boolean preferWriter) {
        this.preferWriter = preferWriter ;
    }
 
 
    @Override
    public Lock readLock() {
        return new ReadLock(this);
    }
 
    @Override
    public Lock writeLock() {
        return new WriteLock(this);
    }
 
    // 写线程增加
    void incrementWritingWriters() {
        this.writingWriters++ ;
    }
 
    // 等待写入线程增加
    void increamentWaitingWriters(){
        this.waitingWriters++ ;
    }
    // 读线程的数量增加
    void increamentReadingReaders(){
        this.readingReaders++ ;
    }
 
 
    // 写入线程数量减少
    void decrementWritingWriters() {
        this.writingWriters-- ;
    }
    // 写入等待线程减少
    void decrementWaitingWriters(){
        this.waitingWriters-- ;
    }
    // 读取线程减少
    void decrementReadingReaders(){
        this.readingReaders-- ;
    }
 
    // 获取当前有多少线程正在进行写操作
    @Override
    public int getWritingWriters() {
        return this.writingWriters;
    }
 
    // 获取当前有多少个线程正在等待获取写入锁
    @Override
    public int getWaitingWriters() {
        return this.waitingWriters ;
    }
 
    // 获取当前有多少读取线程
    @Override
    public int getReadingReaders() {
        return this.readingReaders;
    }
    // 获取对象锁
    Object getMUTEX(){
        return  this.MUTEX ;
    }
 
    // 获取当前是否偏向写锁
    boolean getPreferWriter(){
        return this.preferWriter ;
    }
 
    //  设置写锁偏好
    void changePrefer(boolean preferWriter){
        this.preferWriter = preferWriter ;
    }
 
 
}
