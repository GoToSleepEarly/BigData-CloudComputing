package chapter18;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

//不可变对象不允许被继承
public final class IntegerAccumulator2 {
	private final int init;
	
	//构造时传入初始值
	public IntegerAccumulator2(int init){
		this.init = init;
	}
	
	//构造新的累加器，需要用到另一个accumulator和初始值
	public IntegerAccumulator2(IntegerAccumulator2 accumulator, int init){
		this.init = accumulator.getValue() + init;
	}
	
	//每次相加产生一个新的IntegerAccumulator2
	public IntegerAccumulator2 add(int i){
		return new IntegerAccumulator2(this,i);
	}
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
	
	public static void main(String[] args) {
		IntegerAccumulator2 accumulator = new IntegerAccumulator2(0);
		
		IntStream.range(0, 3).forEach(i -> new Thread(() ->{
			int inc = 0;
			while(true){
				//首先获得oldvalue
				int oldValue = accumulator.getValue();
				//然后调用add方法S 
				
				int result = accumulator.add(inc).getValue();
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
