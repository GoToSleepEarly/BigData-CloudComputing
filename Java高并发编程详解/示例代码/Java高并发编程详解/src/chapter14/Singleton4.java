package chapter14;

import java.net.Socket;
import java.sql.Connection;

public class Singleton4 {
	/*
	Double-Check单例模式
	 */
	
	private byte[] data = new byte[1024];
	
	private static Singleton4 instance = null;
	
	//类变量
	Connection con;
	Socket socket;
	
	public Singleton4(){
		//初始化
		this.con = null;
		this.socket = null;
	}
	
	public static Singleton4 getInstance(){
		//当instance进入时，同步代码块，只会让几只判断null的线程阻塞
		if(null == instance){
			synchronized(Singleton4.class){
				//double-check在此，其他线程阻塞恢复后，发现不是null就退出了
				if(null == instance){
					instance = new Singleton4();
				}
			}
		}
		return instance;
	}
	
	
}