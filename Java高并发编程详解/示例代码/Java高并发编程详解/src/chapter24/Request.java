package chapter24;

public class Request {
	private final String business;
	
	public Request(String busniess){
		this.business = busniess;
	}
	
	@Override
	public String toString(){
		return business;
	}
}
