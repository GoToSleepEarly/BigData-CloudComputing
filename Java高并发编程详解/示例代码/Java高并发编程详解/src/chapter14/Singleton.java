package chapter14;

public class Singleton {
	/*
	 ����ʽ����ģʽ
	 */	
	//ʵ������
	private byte[] data = new byte[1024];
	
	//ʵ����ʱֱ�ӳ�ʼ��
	private static Singleton instance= new Singleton();
	
	//˽�й��췽ʽ
	private Singleton(){
		
	}
	
	public static Singleton getInstance(){
		return instance;
	}
}
