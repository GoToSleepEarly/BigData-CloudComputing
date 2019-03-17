package chapter14;

public class Singleton3 {
	/*
	 ����ʽ����ģʽ+synchronized
	 */	
	//ʵ������
	private byte[] data = new byte[1024];
	
	//ʵ����ʱֱ�ӳ�ʼ��
	private static Singleton3 instance = null ;
	
	//˽�й��췽ʽ
	private Singleton3(){
	}
	
	//����synchronized
	public static synchronized Singleton3 getInstance(){
		if(null == instance){
			instance = new Singleton3();
		}
		return instance;
	}
}
