package chapter17;

public interface ReadWriteLock {
 
 
    //��������
    Lock readLock();
 
    //����д��
    Lock writeLock();
 
    // ��ȡ��ǰ�ж��ٸ��߳�����ִ��д���� ���1��
    int getWritingWriters();
 
    // ��ȡ��ǰ�ж����߳����ڵȴ���ȡд���� ����д����������
    int getWaitingWriters();
 
    // �ȴ���ǰ�ж����߳����ڵȴ���ȡ����
    int getReadingReaders();
 
    //  ������������ReadWriteLock 
    static ReadWriteLock readWriteLock() {
        return new ReadWriteLockImpl();
    }
 
    // ��������������ReadWriteLock ��������preferWriter
    static ReadWriteLock readWriteLock(boolean preferWriter) {
        return new ReadWriteLockImpl(preferWriter);
    }
 
 
 
}
