package chapter10;

public class BootStrapClassLoader {

	public static void main(String[] args) {
		//���������ǻ�ȡ�������õģ�����Ϊnull
		System.out.println("BootStrap:" + String.class.getClassLoader());
		System.out.println(System.getProperty("sun.boot.class.path"));
	}

}
