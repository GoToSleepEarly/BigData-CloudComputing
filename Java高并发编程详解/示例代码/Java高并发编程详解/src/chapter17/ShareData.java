package chapter17;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
 
/**
 * �����ݵ���д����
 */
public class ShareData {
 
    // ���干�����ݣ���Դ��
    private final List<Character> container = new ArrayList<>() ;
 
    // ����ReadWriteLock
    private final ReadWriteLock readWriteLock = ReadWriteLock.readWriteLock();
 
    // ������ȡ��
    private final Lock readLock = readWriteLock.readLock();
 
    // ����д����
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
            // ����ʹ�ö�������lock
            readLock.lock();
            char[] newBuffer = new char[length] ;
            for (int i= 0 ; i < length ; i++) {
                newBuffer[i] = container.get(i) ;
            }
            slowly();
            return newBuffer ;
        }finally {
            // ����������֮�󣬽����ͷ�
            readLock.unlock();
        }
    }
    public void write(char c ) throws InterruptedException{
        try{
            // ʹ��������lock
            writeLock.lock();
            for (int i = 0 ; i<length ; i++) {
                this.container.add(i,c) ;
            }
            slowly();
        }finally {
            // �����в�������֮�󣬽����ͷ�
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
