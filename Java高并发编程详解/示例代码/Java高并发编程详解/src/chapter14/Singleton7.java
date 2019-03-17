package chapter14;

//枚举类型本身就是final，不能继承
public enum Singleton7 {
	INSTANCE;
	//实例变量
	private byte[] data = new byte[1024];
	
	private Singleton7() {
		System.out.println("INSTANCE将会被初始化");
	}
	
	public static void method(){
		//调用该方法将会主动使用Singleton，INSTANCE会被实例化
	}
	
	public static Singleton7 getInstance(){
		return INSTANCE;
	}
}
