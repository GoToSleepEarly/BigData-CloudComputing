package chapter25;

public class Reference {
	
	private final byte[] data = new byte[2 << 19];
	
	@Override
	protected void finalize() throws Throwable{
		System.out.println("�����ý���GC");
	}
}
