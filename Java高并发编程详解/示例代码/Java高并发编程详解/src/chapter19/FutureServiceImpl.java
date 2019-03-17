package chapter19;
 
 
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

 
/**
 *
 * 当提交任务的时候，创建一个信息的线程来受理该任务，进而达到任务异步执行的效果
 * @param <IN>
 * @param <OUT>
 */
public class FutureServiceImpl<IN,OUT> implements FutureService<IN,OUT> {
 
    // 为执行的线程指定名字前缀
    private final static String FUTURE_THREAD_PREFIX = "FUTURE-" ;
 
    private final AtomicInteger nextCounter = new AtomicInteger(0) ;
 
    private String getNextName(){
        return FUTURE_THREAD_PREFIX+nextCounter.getAndIncrement() ;
    }
 
 
    @Override
    public Future<?> submit(Runnable runnable) {
    	//没有返回值
        final FutureTask<Void> future = new FutureTask<>();
        new Thread(()->{
            runnable.run();
            //结束之后将null传给future
            future.finish(null);
        },getNextName()).start();
        return future;
    }
 
    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN input) {
        final FutureTask<OUT> future = new FutureTask<>();
        new Thread(()->{
        	//Task其实就是线程，get理解为run
            OUT result = task.get(input) ;
            //结束后将设置返回结果
            future.finish(result);
        },getNextName()).start();
        return future;
    }
 
 
 
    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback) {
        final FutureTask<OUT> future = new FutureTask<>();
        new Thread(()->{
            OUT result = task.get(input) ;
            //  设置返回结果
            future.finish(result);
            if(null != callback){
                //  回调函数
                callback.call(result);
            }
 
        },getNextName()).start();
        return future;
    }

}
