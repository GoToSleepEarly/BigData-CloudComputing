package chapter28;


import java.util.concurrent.ThreadPoolExecutor;
 
public class AsyncEventBus extends EventBus {
    AsyncEventBus(String busName, EventExceptionHandler eventExceptionHandler, ThreadPoolExecutor executor){
        super(busName,eventExceptionHandler,executor);
    }
    AsyncEventBus(String busName,ThreadPoolExecutor executor){
        this(busName,null,executor);
    }
    AsyncEventBus(ThreadPoolExecutor executor){
        this("default-async",null,executor);
    }
    AsyncEventBus(EventExceptionHandler eventExceptionHandler,ThreadPoolExecutor executor){
        this("default-async",eventExceptionHandler,executor);
    }
}
 
