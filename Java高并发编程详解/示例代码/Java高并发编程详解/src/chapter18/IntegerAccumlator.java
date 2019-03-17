package chapter18;

import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class IntegerAccumlator {
	private int init;
	
	//����ʱ�����ʼֵ
	public IntegerAccumlator(int init){
		this.init = init;
	}
	
	//�Գ�ʼ��ֵ��һ
	public int add(int i){
		this.init += i;
		return this.init;
	}
	
	//���ص�ǰ�ĳ�ʼֵ
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
		//����һ����ʼ��Ϊ0
		IntegerAccumlator accumulator = new IntegerAccumlator(0);
		//���������߳�
		/*IntStream.range(0, 3).forEach(i -> new Thread(() ->{
			int inc = 0;
			while(true){
				//���Ȼ��oldvalue
				int oldValue = accumulator.getValue();
				//Ȼ�����add����
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
