package chapter14;

//ö�����ͱ������final�����ܼ̳�
public enum Singleton7 {
	INSTANCE;
	//ʵ������
	private byte[] data = new byte[1024];
	
	private Singleton7() {
		System.out.println("INSTANCE���ᱻ��ʼ��");
	}
	
	public static void method(){
		//���ø÷�����������ʹ��Singleton��INSTANCE�ᱻʵ����
	}
	
	public static Singleton7 getInstance(){
		return INSTANCE;
	}
}
