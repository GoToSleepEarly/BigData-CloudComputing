package chapter14;

public class Singleton {
	/*
	 饿汉式单例模式
	 */	
	//实例变量
	private byte[] data = new byte[1024];
	
	//实例化时直接初始化
	private static Singleton instance= new Singleton();
	
	//私有构造方式
	private Singleton(){
		
	}
	
	public static Singleton getInstance(){
		return instance;
	}
}
