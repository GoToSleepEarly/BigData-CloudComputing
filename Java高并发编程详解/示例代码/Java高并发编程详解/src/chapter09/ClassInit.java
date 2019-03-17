package chapter09;

public class ClassInit {
	
	//Java语言规定不能如此使用，不能通过编译，因为先声明才能访问。
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
	
	//父类
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
