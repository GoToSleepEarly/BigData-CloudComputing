package chapter14;

public class Singleton2 {
	/*
	 ����ʽ����ģʽ
	 */	
	//ʵ������
	private byte[] data = new byte[1024];
	
	//ʵ����ʱֱ�ӳ�ʼ��
	private static Singleton2 instance = null ;
	
	//˽�й��췽ʽ
	private Singleton2(){
	}
	
	public static Singleton2 getInstance(){
		if(null == instance){
			instance = new Singleton2();
		}
		return instance;
	}
}
