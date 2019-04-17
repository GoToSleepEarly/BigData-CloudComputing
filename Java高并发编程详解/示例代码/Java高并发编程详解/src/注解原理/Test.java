package ×¢½âÔ­Àí;

import java.lang.reflect.Method;
public class Test {

	@Hello("hello")
	public static void main(String[] args) throws NoSuchMethodException{
		System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
		Class cls = Test.class;
		Method method = cls.getMethod("main", String[].class);
		Hello hello = method.getAnnotation(Hello.class);
		//foo();
	}
}