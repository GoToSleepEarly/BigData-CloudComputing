package chapter28;
 
import java.util.concurrent.Executor;
 
public class EventBus implements Bus {
    //用于维护Subscriber的注册表
    private final Registry registry = new Registry();
    //EventBus的名字
    private String busName;
    //默认名字
    private final static String DEFAULT_BUS_NAME = "default-bus";
    //默认topic名字
    private final static String DEFAULT_TOPIC = "default-topic";
    //用于分发广播消息到各个Subscriber的类
    private final Dispatcher dispatcher;
    public EventBus(){
        this(DEFAULT_BUS_NAME,null,Dispatcher.SEQ_EXECUTOR_SERVICE);
    }
    EventBus(String name){
        this(name,null,Dispatcher.SEQ_EXECUTOR_SERVICE);
    }
    EventBus(String busName, EventExceptionHandler eventExceptionHandler, Executor executor){
        this.busName = busName;
        this.dispatcher = Dispatcher.newDispatcher(eventExceptionHandler,executor);
    }
    EventBus(EventExceptionHandler eventExceptionHandler){
        this(DEFAULT_BUS_NAME,eventExceptionHandler,Dispatcher.SEQ_EXECUTOR_SERVICE);
    }
    //将注册Subscriber的动作直接委托给Registry
    @Override
    public void register(Object subscriber) {
        this.registry.bind(subscriber);
    }
 
    @Override
    public void unregister(Object subscriber) {
        this.registry.unbind(subscriber);
    }
    //提交默认的topic
    @Override
    public void post(Object event) {
        this.post(event,DEFAULT_TOPIC);
    }
    //提交event到指定的topic
    @Override
    public void post(Object event, String topic) {
        this.dispatcher.dispatch(this,registry,event,topic);
    }
 
    //关闭销毁bus
    @Override
    public void close() {
        this.dispatcher.close();
    }
 
    @Override
    public String getBusName() {
        return this.busName;
    }
}
