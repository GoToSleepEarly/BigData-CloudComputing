package chapter29;

public interface Message {
	//����Message����
	Class<? extends Message> getType();
}
