package chapter19;

public interface Task<IN,OUT> {

	//����һ���������������㷵�ؽ��
	OUT get(IN input);
}
