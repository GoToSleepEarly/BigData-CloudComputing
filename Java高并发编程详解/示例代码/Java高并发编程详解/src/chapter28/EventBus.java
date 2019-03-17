package chapter28;
 
import java.util.concurrent.Executor;
 
public class EventBus implements Bus {
    //����ά��Subscriber��ע���
    private final Registry registry = new Registry();
    //EventBus������
    private String busName;
    //Ĭ������
    private final static String DEFAULT_BUS_NAME = "default-bus";
    //Ĭ��topic����
    private final static String DEFAULT_TOPIC = "default-topic";
    //���ڷַ��㲥��Ϣ������Subscriber����
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
    //��ע��Subscriber�Ķ���ֱ��ί�и�Registry
    @Override
    public void register(Object subscriber) {
        this.registry.bind(subscriber);
    }
 
    @Override
    public void unregister(Object subscriber) {
        this.registry.unbind(subscriber);
    }
    //�ύĬ�ϵ�topic
    @Override
    public void post(Object event) {
        this.post(event,DEFAULT_TOPIC);
    }
    //�ύevent��ָ����topic
    @Override
    public void post(Object event, String topic) {
        this.dispatcher.dispatch(this,registry,event,topic);
    }
 
    //�ر�����bus
    @Override
    public void close() {
        this.dispatcher.close();
    }
 
    @Override
    public String getBusName() {
        return this.busName;
    }
}
