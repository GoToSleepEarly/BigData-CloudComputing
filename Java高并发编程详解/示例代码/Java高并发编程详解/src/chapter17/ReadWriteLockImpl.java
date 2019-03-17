package chapter17;
 
 
// �԰��ɼ�����
class ReadWriteLockImpl implements ReadWriteLock {
 
    // ���������
    private final Object MUTEX = new Object();
 
    //  ��ǰ�ж��ٸ��߳�����ִ��д���� ���1��
    private int  writingWriters = 0 ;
 
    //  ��ǰ�ж����߳����ڵȴ���ȡд���� ����д����������
    private int waitingWriters = 0 ;
 
    // �ȴ���ǰ�ж����߳����ڵȴ���ȡ����
    private int readingReaders = 0 ;
 
    //read �� writer ��ƫ������
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
 
    // д�߳�����
    void incrementWritingWriters() {
        this.writingWriters++ ;
    }
 
    // �ȴ�д���߳�����
    void increamentWaitingWriters(){
        this.waitingWriters++ ;
    }
    // ���̵߳���������
    void increamentReadingReaders(){
        this.readingReaders++ ;
    }
 
 
    // д���߳���������
    void decrementWritingWriters() {
        this.writingWriters-- ;
    }
    // д��ȴ��̼߳���
    void decrementWaitingWriters(){
        this.waitingWriters-- ;
    }
    // ��ȡ�̼߳���
    void decrementReadingReaders(){
        this.readingReaders-- ;
    }
 
    // ��ȡ��ǰ�ж����߳����ڽ���д����
    @Override
    public int getWritingWriters() {
        return this.writingWriters;
    }
 
    // ��ȡ��ǰ�ж��ٸ��߳����ڵȴ���ȡд����
    @Override
    public int getWaitingWriters() {
        return this.waitingWriters ;
    }
 
    // ��ȡ��ǰ�ж��ٶ�ȡ�߳�
    @Override
    public int getReadingReaders() {
        return this.readingReaders;
    }
    // ��ȡ������
    Object getMUTEX(){
        return  this.MUTEX ;
    }
 
    // ��ȡ��ǰ�Ƿ�ƫ��д��
    boolean getPreferWriter(){
        return this.preferWriter ;
    }
 
    //  ����д��ƫ��
    void changePrefer(boolean preferWriter){
        this.preferWriter = preferWriter ;
    }
 
 
}
