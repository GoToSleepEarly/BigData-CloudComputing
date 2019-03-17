package chapter28;
 
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
 
public class Dispatcher {
    private final Executor executorService;
    private final EventExceptionHandler eventExceptionHandler;
    public static final Executor SEQ_EXECUTOR_SERVICE = SeqExecutorService.INSTANCE;
    static final Executor PRE_THREAD_EXECUTOR_SERVICE = PreThreadExecutorService.INSTANCE;
 
    public Dispatcher(Executor executorService, EventExceptionHandler eventExceptionHandler) {
        this.executorService = executorService;
        this.eventExceptionHandler = eventExceptionHandler;
    }
    public void dispatch(Bus bus,Registry registry,Object event,String topic){
        //����topic��ȡ���е�Subscriber �б�
        ConcurrentLinkedQueue<Subscriber> subscribers = registry.scanSubscriber(topic);
        if(null == subscribers){
            if(eventExceptionHandler!=null){
                eventExceptionHandler.handle(new IllegalArgumentException("the topic"+topic+"not bind yet"),
                        new BaseEventContext(bus.getBusName(),null,event));
            }
            return;
        }
        //�������еķ���������ͨ������ķ�ʽ���з�������
        subscribers.stream()
                .filter(subscriber -> !subscriber.isDisable())
                .filter(subscriber -> {
                    Method subscriberMethod = subscriber.getSubscrebeMethod();
                    Class<?> aClass = subscriberMethod.getParameterTypes()[0];
                    return (aClass.isAssignableFrom(event.getClass()));
                }).forEach(subscriber -> realInvokeSubscribe(subscriber,event,bus));
    }
    private void realInvokeSubscribe(Subscriber subscriber,Object event, Bus bus){
        Method subscribeMethod = subscriber.getSubscrebeMethod();
        Object subscribeObject = subscriber.getSubscribeObject();
        executorService.execute(()->{
            try {
                subscribeMethod.invoke(subscribeObject,event);
            } catch (Exception e) {
                if(null != eventExceptionHandler){
                    eventExceptionHandler.handle(e,new BaseEventContext(bus.getBusName(),subscriber,event));
                }
            }
        });
    }
 
    public void close(){
        if(executorService instanceof ExecutorService)
            ((ExecutorService) executorService).shutdown();
    }
 
    static Dispatcher newDispatcher(EventExceptionHandler eventExceptionHandler,Executor executor){
        return new Dispatcher(executor,eventExceptionHandler);
    }
 
    static Dispatcher seqDispatcher(EventExceptionHandler eventExceptionHandler){
        return new Dispatcher(SEQ_EXECUTOR_SERVICE,eventExceptionHandler);
    }
 
    static Dispatcher preThreadDispatcher(EventExceptionHandler eventExceptionHandler){
        return new Dispatcher(PRE_THREAD_EXECUTOR_SERVICE,eventExceptionHandler);
    }
    //˳��ִ�е�ExecutorService
    private static class SeqExecutorService implements Executor{
        private final static SeqExecutorService INSTANCE = new SeqExecutorService();
 
        @Override
        public void execute(Runnable command) {
            command.run();
        }
    }
    //ÿ���̸߳���һ����Ϣ����
    private static class PreThreadExecutorService implements Executor{
        private final static PreThreadExecutorService INSTANCE = new PreThreadExecutorService();
 
        @Override
        public void execute(Runnable command) {
            new Thread(command).start();
        }
    }
    private static class BaseEventContext implements EventContext{
        private final String eventBusName;
 
        private final Subscriber subscriber;
 
        private final Object event;
 
        private BaseEventContext(String eventBusName, Subscriber subscriber, Object event) {
            this.eventBusName = eventBusName;
            this.subscriber = subscriber;
            this.event = event;
        }
 
        @Override
        public String getSource() {
            return this.eventBusName;
        }
 
        @Override
        public Object getSubscriber() {
            return subscriber !=null?subscriber.getSubscribeObject():null;
        }
 
        @Override
        public Method getSubscribe() {
            return subscriber!=null?subscriber.getSubscrebeMethod():null;
        }
 
        @Override
        public Object getEvent() {
            return this.event;
        }
    }
}
