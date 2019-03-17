package chapter14;

import java.net.Socket;
import java.sql.Connection;

public class Singleton4 {
	/*
	Double-Check����ģʽ
	 */
	
	private byte[] data = new byte[1024];
	
	private static Singleton4 instance = null;
	
	//�����
	Connection con;
	Socket socket;
	
	public Singleton4(){
		//��ʼ��
		this.con = null;
		this.socket = null;
	}
	
	public static Singleton4 getInstance(){
		//��instance����ʱ��ͬ������飬ֻ���ü�ֻ�ж�null���߳�����
		if(null == instance){
			synchronized(Singleton4.class){
				//double-check�ڴˣ������߳������ָ��󣬷��ֲ���null���˳���
				if(null == instance){
					instance = new Singleton4();
				}
			}
		}
		return instance;
	}
	
	
}