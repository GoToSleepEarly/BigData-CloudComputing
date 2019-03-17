package chapter18;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class IntegerAccumlator {
	private int init;
	
	//构造时传入初始值
	public IntegerAccumlator(int init){
		this.init = init;
	}
	
	//对初始化值加一
	public int add(int i){
		this.init += i;
		return this.init;
	}
	
	//返回当前的初始值
	public int getValue(){
		return this.init;
	}
	
	private static void slowly(){
		try {
			TimeUnit.MILLISECONDS.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	public static void main(String[] args){
		//定义一个初始化为0
		IntegerAccumlator accumulator = new IntegerAccumlator(0);
		//定义三个线程
		/*IntStream.range(0, 3).forEach(i -> new Thread(() ->{
			int inc = 0;
			while(true){
				//首先获得oldvalue
				int oldValue = accumulator.getValue();
				//然后调用add方法
				int result = accumulator.add(inc);
				System.out.println(oldValue + "+" + inc + "=" + result);
				if(inc + oldValue != result){
					System.out.println("Error:" +oldValue + "+" + inc + "=" + result);
				}
				inc++;
				slowly();
			}
		}).start());*/
		
		IntStream.range(0, 3).forEach(i -> new Thread(() ->{
			int inc = 0;
			while(true){
				int oldValue;
				int result;
				synchronized (IntegerAccumlator.class) {
					oldValue = accumulator.getValue();
					result = accumulator.add(inc);
					//System.out.println(oldValue+" "+result);
				}
				System.out.println(oldValue + "+" + inc + "=" + result);
				if(inc + oldValue != result){
					System.out.println("Error:" +oldValue + "+" + inc + "=" + result);
				}
				inc++;
				slowly();
			}
		}).start());
	}
}
