package chapter24;

//import java.util.concurrent.ThreadPoolExecutor;

import chapter08.BasicThreadPool;

public class Operator {
	private final BasicThreadPool threadPool = new BasicThreadPool(2, 6, 4, 1000);
	
	public void call(String business){
		TaskHandler th = new TaskHandler(new Request(business));
		//new Thread(th).start();
		threadPool.execute(th);
	}
	
	public static void main(String[] args) {
        Operator operator = new Operator();
 
        operator.call("1");
        operator.call("2");
        operator.call("3");
        operator.call("4");
        operator.call("5");
    }

}
