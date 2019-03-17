package chapter28;
 
import java.util.concurrent.TimeUnit;
 
public class Subscriber1 {
    @Subscribe
    public void method1(String message){
        try {
            TimeUnit.SECONDS.sleep(2);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("this is subscriber1's method1");
    }
    @Subscribe(topic = "test")
    public void method2(String message){
        System.out.println("this is method2 for subscriber1, and my topic is not a default topic");
    }
}
