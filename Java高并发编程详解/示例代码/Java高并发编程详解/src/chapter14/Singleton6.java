package chapter14;

//�������̳�
public final class Singleton6 {
	/*
	 Holder��ʽ
	 */
	//ʵ������
	private byte[] data = new byte[1024];
	
	private Singleton6(){
		
	}
	
	//�ھ�̬�ڲ����г���Singleton��ʵ�������ҿ��Ա�ֱ�ӳ�ʼ��
	private static class Holder{
		private static Singleton6 instance = new Singleton6();
	}
	
	//���Holder�ľ�̬����
	public static Singleton6 getInstance(){
		return Holder.instance;
	}
	
}
