package chapter23;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
 
public class ProgrammerTravel extends  Thread {
 
 
    //  门阀
    private final Latch latch ;
 
    // 程序员
    private  final  String programmer ;
 
    // 交通工具
    private final String transportation ;
 
 
    public ProgrammerTravel (Latch latch , String programmer , String transportation ){
        this.latch = latch ;
        this.programmer  = programmer ;
        this.transportation = transportation ;
    }
 
 
    @Override
    public void run(){
        System.out.println(programmer+" start take the transportation [ " + transportation +"  ] ");
 
        try {
            TimeUnit.SECONDS.sleep(ThreadLocalRandom.current().nextInt(10));
 
 
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
 
 
        System.out.println(programmer + " arrived by " + transportation  );
 
        // 完成任务，计数器减一
        latch.countDown();
    }
    
    public static void main(String[] args) throws InterruptedException{
    	//定义Latch
    	Latch latch = new CountDownLatch(4);
    	new ProgrammerTravel(latch, "王", "拖鞋").start();
    	new ProgrammerTravel(latch, "石", "自行车").start();
    	new ProgrammerTravel(latch, "谢", "电动车").start();
    	new ProgrammerTravel(latch, "徐", "汽车").start();
    	//latch.await();
    	try {
			latch.await(TimeUnit.SECONDS, 5);
		} catch (WatiTimeoutException e) {
			e.printStackTrace();
		}
    	System.out.println("超时，活动开始");
    }
 
 
 
 
}
