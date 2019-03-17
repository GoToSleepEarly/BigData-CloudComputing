package chapter23;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
 
public class ProgrammerTravel extends  Thread {
 
 
    //  �ŷ�
    private final Latch latch ;
 
    // ����Ա
    private  final  String programmer ;
 
    // ��ͨ����
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
 
        // ������񣬼�������һ
        latch.countDown();
    }
    
    public static void main(String[] args) throws InterruptedException{
    	//����Latch
    	Latch latch = new CountDownLatch(4);
    	new ProgrammerTravel(latch, "��", "��Ь").start();
    	new ProgrammerTravel(latch, "ʯ", "���г�").start();
    	new ProgrammerTravel(latch, "л", "�綯��").start();
    	new ProgrammerTravel(latch, "��", "����").start();
    	//latch.await();
    	try {
			latch.await(TimeUnit.SECONDS, 5);
		} catch (WatiTimeoutException e) {
			e.printStackTrace();
		}
    	System.out.println("��ʱ�����ʼ");
    }
 
 
 
 
}
