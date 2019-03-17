package chapter10;

public class NameSpace {

	public static void main(String[] args) throws ClassNotFoundException {
		ClassLoader classLoader  = NameSpace.class.getClassLoader();
		MyClassLoader classLoader1 = new MyClassLoader();
		MyClassLoader classLoader2 = new MyClassLoader();
		
		Class<?> aClass = classLoader1.loadClass("chapter10.NameSpace");
		Class<?> bClass = classLoader2.loadClass("chapter10.NameSpace");
		System.out.println(aClass.getClassLoader());
		System.out.println(bClass.getClassLoader());
		System.out.println(aClass == bClass);
	}

}
