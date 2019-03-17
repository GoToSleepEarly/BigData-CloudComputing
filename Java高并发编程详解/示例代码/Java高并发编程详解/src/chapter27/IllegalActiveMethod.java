package chapter27;

//若方法不符合被转换成Active方法将会抛出该异常
public class IllegalActiveMethod extends Exception{
	
	public IllegalActiveMethod(String message){
		super(message);
	}
}
