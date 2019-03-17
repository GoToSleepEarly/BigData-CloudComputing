package chapter18;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

//���ɱ���������̳�
public final class IntegerAccumulator2 {
	private final int init;
	
	//����ʱ�����ʼֵ
	public IntegerAccumulator2(int init){
		this.init = init;
	}
	
	//�����µ��ۼ�������Ҫ�õ���һ��accumulator�ͳ�ʼֵ
	public IntegerAccumulator2(IntegerAccumulator2 accumulator, int init){
		this.init = accumulator.getValue() + init;
	}
	
	//ÿ����Ӳ���һ���µ�IntegerAccumulator2
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
				//���Ȼ��oldvalue
				int oldValue = accumulator.getValue();
				//Ȼ�����add����S 
				
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
