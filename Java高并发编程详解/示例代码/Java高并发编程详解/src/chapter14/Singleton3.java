package chapter14;

public class Singleton3 {
	/*
	 懒汉式单例模式+synchronized
	 */	
	//实例变量
	private byte[] data = new byte[1024];
	
	//实例化时直接初始化
	private static Singleton3 instance = null ;
	
	//私有构造方式
	private Singleton3(){
	}
	
	//加入synchronized
	public static synchronized Singleton3 getInstance(){
		if(null == instance){
			instance = new Singleton3();
		}
		return instance;
	}
}
