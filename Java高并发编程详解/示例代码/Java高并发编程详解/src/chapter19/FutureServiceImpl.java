package chapter19;
 
 
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.text.html.HTMLDocument.HTMLReader.IsindexAction;

 
/**
 *
 * ���ύ�����ʱ�򣬴���һ����Ϣ���߳�����������񣬽����ﵽ�����첽ִ�е�Ч��
 * @param <IN>
 * @param <OUT>
 */
public class FutureServiceImpl<IN,OUT> implements FutureService<IN,OUT> {
 
    // Ϊִ�е��߳�ָ������ǰ׺
    private final static String FUTURE_THREAD_PREFIX = "FUTURE-" ;
 
    private final AtomicInteger nextCounter = new AtomicInteger(0) ;
 
    private String getNextName(){
        return FUTURE_THREAD_PREFIX+nextCounter.getAndIncrement() ;
    }
 
 
    @Override
    public Future<?> submit(Runnable runnable) {
    	//û�з���ֵ
        final FutureTask<Void> future = new FutureTask<>();
        new Thread(()->{
            runnable.run();
            //����֮��null����future
            future.finish(null);
        },getNextName()).start();
        return future;
    }
 
    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN input) {
        final FutureTask<OUT> future = new FutureTask<>();
        new Thread(()->{
        	//Task��ʵ�����̣߳�get���Ϊrun
            OUT result = task.get(input) ;
            //���������÷��ؽ��
            future.finish(result);
        },getNextName()).start();
        return future;
    }
 
 
 
    @Override
    public Future<OUT> submit(Task<IN, OUT> task, IN input, Callback<OUT> callback) {
        final FutureTask<OUT> future = new FutureTask<>();
        new Thread(()->{
            OUT result = task.get(input) ;
            //  ���÷��ؽ��
            future.finish(result);
            if(null != callback){
                //  �ص�����
                callback.call(result);
            }
 
        },getNextName()).start();
        return future;
    }

}
