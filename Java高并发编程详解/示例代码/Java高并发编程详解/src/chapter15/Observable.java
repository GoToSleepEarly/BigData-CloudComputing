package chapter15;

public interface Observable {
	//�����������ڵ�ö������
	enum Cycle{
		STARTED, RUNNING, DONE, ERROR
	}
	
	//��ȡ��ǰ�������������״̬
	Cycle getCycle();
	
	//�����̵߳�������������ҪΪ������Thread����������
	void start();
	
	//�����Ϸ�����������startһ���� Ϊ������
	void interrupt();
}
