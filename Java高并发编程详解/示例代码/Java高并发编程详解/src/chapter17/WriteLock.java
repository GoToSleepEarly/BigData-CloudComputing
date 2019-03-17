package chapter17;

//���Ϊ���ɼ�
class WriteLock implements Lock {

 private final ReadWriteLockImpl readWriteLock ;

 WriteLock(ReadWriteLockImpl readWriteLock ){
     this.readWriteLock = readWriteLock ;
 }



 @Override
 public void lock() throws InterruptedException {
     synchronized (readWriteLock.getMUTEX()){

         try{
             // �ȴ���ȡд���������ּ�һ
             readWriteLock.increamentWaitingWriters();

             // �����ʱ�������߳����ڽ��ж�����������д��������ô��ǰ�߳̽�������
             while (readWriteLock.getReadingReaders() > 0 || readWriteLock.getWritingWriters()>0 ){
                 readWriteLock.getMUTEX().wait();
             }

         }finally {
             // �ɹ�ȡ�õ�д������ʹ�õȴ���ȡд�����ļ�������һ
             this.readWriteLock.decrementWaitingWriters();
         }
         readWriteLock.incrementWritingWriters();
     }
 }

 @Override
 public void unlock() {
     synchronized (readWriteLock.getMUTEX()){
         // ��������д�������̼߳�����
         readWriteLock.decrementWritingWriters();

         // ��ƫ��״̬�޸�Ϊfalse ������ʹ�ö��������Ļ��
         readWriteLock.changePrefer(false);

         //���������߳�
         readWriteLock.getMUTEX().notifyAll();
     }
 }
}
