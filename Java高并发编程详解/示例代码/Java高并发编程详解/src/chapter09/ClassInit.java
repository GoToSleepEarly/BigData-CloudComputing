package chapter09;

public class ClassInit {
	
	//Java���Թ涨�������ʹ�ã�����ͨ�����룬��Ϊ���������ܷ��ʡ�
	static {
		//x=2;
		//System.out.println(x);      
	 }    
	public static int x = 10 ;
		
	public static void main(String[] args) throws ClassNotFoundException {
		//System.out.println(ClassInit.x);
		//System.out.println(Child.i);
		System.out.println(x);
	}
	
	//����
	static class Parent{
		static int value = 10;
		static {
			value = 20;
		}
	}
	
	static class Child extends Parent{
		static int i = value;
	}
}
