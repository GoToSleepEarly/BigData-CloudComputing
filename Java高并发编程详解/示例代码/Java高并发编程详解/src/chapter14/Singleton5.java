package chapter14;

import java.net.Socket;
import java.sql.Connection;

public class Singleton5 {
	/*
	Double-Check����ģʽ+volatile
	 */
	
	private byte[] data = new byte[1024];
	
	private volatile static Singleton5 instance = null;
	
	//�����
	Connection con;
	Socket socket;
	
	public Singleton5(){
		//��ʼ��
		this.con = null;
		this.socket = null;
	}
	
	public static Singleton5 getInstance(){
		//��instance����ʱ��ͬ������飬ֻ���ü�ֻ�ж�null���߳�����
		if(null == instance){
			synchronized(Singleton5.class){
				//double-check�ڴˣ������߳������ָ��󣬷��ֲ���null���˳���
				if(null == instance){
					instance = new Singleton5();
				}
			}
		}
		return instance;
	}
	
	
}