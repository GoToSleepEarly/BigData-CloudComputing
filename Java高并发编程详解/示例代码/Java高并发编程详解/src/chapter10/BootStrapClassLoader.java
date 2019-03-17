package chapter10;

public class BootStrapClassLoader {

	public static void main(String[] args) {
		//根加载器是获取不到引用的，所以为null
		System.out.println("BootStrap:" + String.class.getClassLoader());
		System.out.println(System.getProperty("sun.boot.class.path"));
	}

}
