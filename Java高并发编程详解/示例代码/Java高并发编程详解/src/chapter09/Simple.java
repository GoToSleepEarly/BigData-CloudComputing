package chapter09;

import java.util.Random;

//���SimpleTest���
public class Simple {
	static {
		  //System.out.println(x);
	      System.out.println("�һᱻ��ʼ��");    
	      x=100;
	  }    
	public static int x = 10;
	
	public static void test(){
		//do nothing
	}
	
	//��������������MAX���ᵼ�³�ʼ������̬���벻�����
	public static final int MAX = 10;
	
	//���ڼ��㸴�ӣ�ֻ�г�ʼ������ܵõ���������Ի������̬�����
	public static final int RANDOM = new Random().nextInt();
	
	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("chapter09.Simple");
	}

}
