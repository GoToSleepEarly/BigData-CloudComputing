package chapter08;
 
import java.util.concurrent.TimeUnit;
 
public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        // 定义线程池，初始化线程数2， 核心线程4， 最大线程6， 任务队列最大值1000
        final ThreadPool threadPool = new BasicThreadPool(2,6,4,1000) ;
 
        // 定义20个任务提交给线程池
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

         //不断输出信息，用于查看线程池的状况
//        for (;;){
//            System.out.println("活跃线程数 ActiveCount:"+threadPool.getActiveCount());
//            System.out.println("线程队列数量 QueueSize:"+threadPool.getQueueSize());
//            System.out.println("核心线程数 CoreSize:"+threadPool.getCoreSize());
//            System.out.println("线程最大值 MaxSize:"+threadPool.getMaxSize());
//            System.out.println("=========================================");
//            TimeUnit.SECONDS.sleep(5);
//        }
        //12s后注销
        
        TimeUnit.SECONDS.sleep(12);
        
        threadPool.shutdown();
        Thread.currentThread().join();
 
 
    }
}
