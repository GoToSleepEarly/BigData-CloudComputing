package chapter14;

import java.net.Socket;
import java.sql.Connection;

public class Singleton5 {
	/*
	Double-Check单例模式+volatile
	 */
	
	private byte[] data = new byte[1024];
	
	private volatile static Singleton5 instance = null;
	
	//类变量
	Connection con;
	Socket socket;
	
	public Singleton5(){
		//初始化
		this.con = null;
		this.socket = null;
	}
	
	public static Singleton5 getInstance(){
		//当instance进入时，同步代码块，只会让几只判断null的线程阻塞
		if(null == instance){
			synchronized(Singleton5.class){
				//double-check在此，其他线程阻塞恢复后，发现不是null就退出了
				if(null == instance){
					instance = new Singleton5();
				}
			}
		}
		return instance;
	}
	
	
}