package chapter17;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
 
/**
 * 对数据的书写操作
 */
public class ShareData {
 
    // 定义共享数据（资源）
    private final List<Character> container = new ArrayList<>() ;
 
    // 构造ReadWriteLock
    private final ReadWriteLock readWriteLock = ReadWriteLock.readWriteLock();
 
    // 创建读取锁
    private final Lock readLock = readWriteLock.readLock();
 
    // 创建写入锁
    private final  Lock writeLock = readWriteLock.writeLock();
 
    private final int length ;
 
    public ShareData(int length){
        this.length = length ;
        for (int i = 0 ;i < length ; i++) {
            container.add(i,'c') ;
        }
    }
    public char[] read() throws InterruptedException{
        try{
            // 首先使用读锁进行lock
            readLock.lock();
            char[] newBuffer = new char[length] ;
            for (int i= 0 ; i < length ; i++) {
                newBuffer[i] = container.get(i) ;
            }
            slowly();
            return newBuffer ;
        }finally {
            // 当操作结束之后，将锁释放
            readLock.unlock();
        }
    }
    public void write(char c ) throws InterruptedException{
        try{
            // 使用锁进行lock
            writeLock.lock();
            for (int i = 0 ; i<length ; i++) {
                this.container.add(i,c) ;
            }
            slowly();
        }finally {
            // 当所有操作结束之后，将锁释放
            writeLock.unlock();
        }
    }
    private void slowly() {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
