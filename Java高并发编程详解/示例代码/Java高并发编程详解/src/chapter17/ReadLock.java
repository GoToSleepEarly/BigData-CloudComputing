package chapter17;
 
// ����Ϊ���ɼ�
class ReadLock implements Lock {
 
    private final ReadWriteLockImpl readWriteLock ;
 
    ReadLock(ReadWriteLockImpl readWriteLock ){
        this.readWriteLock = readWriteLock ;
    }
 
 
 
    @Override
    public void lock() throws InterruptedException {
        // ʹ��MUTEX��Ϊ��
        synchronized (readWriteLock.getMUTEX()){
            // ����ʱ���߳����ڽ���д������������д�߳��ڵȴ�����ƫ��д���ı�ʶΪture�����޷���ȡ������ֻ�ܹ���
            while(readWriteLock.getWritingWriters() > 0 || (readWriteLock.getPreferWriter() && readWriteLock.getWaitingWriters() >0 )){
 
                readWriteLock.getMUTEX().wait();
            }
            // �ɹ���ȡ����������ʹreadingReader����������
            readWriteLock.increamentReadingReaders();
        }
    }
 
    @Override
    public void unlock() {
        //ʹ��Mutex��Ϊ�������ҽ���ͬ��
        synchronized (readWriteLock.getMUTEX()){
            // �ͷ����Ĺ��̾���ʹ�õ�ǰreading��������һ
            // ��perferWriter����Ϊtrue �� ����ʹ��writer�̻߳�ø���Ļ���
            // ֪ͨ������Mutex����monitor waitset�е��߳�
 
            readWriteLock.decrementReadingReaders();
            readWriteLock.changePrefer(true);
            readWriteLock.getMUTEX().notifyAll();
        }
    }
}
