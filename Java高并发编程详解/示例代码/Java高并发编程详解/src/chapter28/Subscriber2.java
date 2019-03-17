package chapter28;
 
public class Subscriber2 {
    @Subscribe
    public void method1(String message){
        System.out.println("this is subscriber2's method1");
    }
    @Subscribe(topic = "test")
    public void method2(String message){
        System.out.println("this is method2 for subscriber2, and my topic is not a default topic");
    }
    public static void main(String[] args) {
        Bus bus = new EventBus();
        bus.register(new Subscriber1());
        bus.register(new Subscriber2());
        bus.post("Hello");
        System.out.println("---------------------------");
        bus.post("Hello","test");
        bus.close();
    }

}
