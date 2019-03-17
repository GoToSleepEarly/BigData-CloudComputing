package chapter09;

public class SimpleTest {

	public static void main(String[] args) throws ClassNotFoundException {
		//访问类的静态变量会导致类的初始化
		System.out.println(Simple.x);
		//访问类的静态方法也会初始化。和上句一起只会初始一次。
		Simple.test();
		//对类进行反射操作，会导致类的初始化
		Class.forName("chapter09.Simple");
		//子类调用父类的静态变量，只会初始化父类，子类不会被初始化
		System.out.println(SimpleChild.x);
		
		Simple[] simples = new Simple[10];
		System.out.println(simples.length);
		
		System.out.println(Simple.MAX);
		System.out.println(Simple.RANDOM);
	}

}
