package chapter09;

public class SimpleTest {

	public static void main(String[] args) throws ClassNotFoundException {
		//������ľ�̬�����ᵼ����ĳ�ʼ��
		System.out.println(Simple.x);
		//������ľ�̬����Ҳ���ʼ�������Ͼ�һ��ֻ���ʼһ�Ρ�
		Simple.test();
		//������з���������ᵼ����ĳ�ʼ��
		Class.forName("chapter09.Simple");
		//������ø���ľ�̬������ֻ���ʼ�����࣬���಻�ᱻ��ʼ��
		System.out.println(SimpleChild.x);
		
		Simple[] simples = new Simple[10];
		System.out.println(simples.length);
		
		System.out.println(Simple.MAX);
		System.out.println(Simple.RANDOM);
	}

}
