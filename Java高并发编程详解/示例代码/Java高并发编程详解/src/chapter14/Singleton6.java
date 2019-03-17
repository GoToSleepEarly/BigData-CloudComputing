package chapter14;

//不允许被继承
public final class Singleton6 {
	/*
	 Holder方式
	 */
	//实例变量
	private byte[] data = new byte[1024];
	
	private Singleton6(){
		
	}
	
	//在静态内部类中持有Singleton的实例，并且可以被直接初始化
	private static class Holder{
		private static Singleton6 instance = new Singleton6();
	}
	
	//获得Holder的静态属性
	public static Singleton6 getInstance(){
		return Holder.instance;
	}
	
}
