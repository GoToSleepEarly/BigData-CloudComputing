package chapter08;
 
import java.util.concurrent.TimeUnit;
 
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        // �����̳߳أ���ʼ���߳���2�� �����߳�4�� ����߳�6�� ����������ֵ1000
        final ThreadPool threadPool = new BasicThreadPool(2,6,4,1000) ;
 
        // ����20�������ύ���̳߳�
        for (int i= 0 ; i< 20 ; i++) {
            threadPool.execute(()->{
 
                try {
                    TimeUnit.SECONDS.sleep(10);
                    System.out.println(Thread.currentThread().getName()+" is running and done .");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
 
        }

         //���������Ϣ�����ڲ鿴�̳߳ص�״��
//        for (;;){
//            System.out.println("��Ծ�߳��� ActiveCount:"+threadPool.getActiveCount());
//            System.out.println("�̶߳������� QueueSize:"+threadPool.getQueueSize());
//            System.out.println("�����߳��� CoreSize:"+threadPool.getCoreSize());
//            System.out.println("�߳����ֵ MaxSize:"+threadPool.getMaxSize());
//            System.out.println("=========================================");
//            TimeUnit.SECONDS.sleep(5);
//        }
        //12s��ע��
        
        TimeUnit.SECONDS.sleep(12);
        
        threadPool.shutdown();
        Thread.currentThread().join();
 
 
    }
}
