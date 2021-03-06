package chapter26;

import java.util.Random;
import java.util.concurrent.TimeUnit;
 
public class Worker extends Thread {
 
    private final ProductionChannel channel ;
 
    private final static Random random = new Random(System.currentTimeMillis()) ;
 
 
    public Worker(String workName , ProductionChannel channel) {
        super(workName);
        this.channel = channel ;
    }
 
   @Override
   public void run(){
        while (true){
            // 从队列中获取产品，并消费
            Production production  = channel.takeProduction();
            System.out.println(getName()+"  process the  "+production);
            production.create();
            try {
                TimeUnit.SECONDS.sleep(random.nextInt(10));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
   }
 
 
}
