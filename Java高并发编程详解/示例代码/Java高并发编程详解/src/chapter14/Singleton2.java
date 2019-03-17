package chapter14;

public class Singleton2 {
	/*
	 懒汉式单例模式
	 */	
	//实例变量
	private byte[] data = new byte[1024];
	
	//实例化时直接初始化
	private static Singleton2 instance = null ;
	
	//私有构造方式
	private Singleton2(){
	}
	
	public static Singleton2 getInstance(){
		if(null == instance){
			instance = new Singleton2();
		}
		return instance;
	}
}
