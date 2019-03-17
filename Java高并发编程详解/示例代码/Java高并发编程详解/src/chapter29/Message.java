package chapter29;

public interface Message {
	//·µ»ØMessageÀàĞÍ
	Class<? extends Message> getType();
}
