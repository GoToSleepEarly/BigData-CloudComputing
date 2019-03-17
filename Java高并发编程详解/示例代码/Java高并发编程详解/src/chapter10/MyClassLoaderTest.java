package chapter10;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import chapter10.classloader1.HelloWorld;

public class MyClassLoaderTest {

	public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchMethodException, SecurityException, IllegalArgumentException, InvocationTargetException {
		//ÉùÃ÷
		MyClassLoader classLoader = new MyClassLoader();
		System.out.println(classLoader.getClass());
		
		/*//Ê¹ÓÃclassLoader
		Class<?> aClass = classLoader.loadClass("chapter10.classloader1.HelloWorld");
		
		System.out.println(aClass.getClassLoader());
		
		Object helloWorld = aClass.newInstance();
		System.out.println(helloWorld);
		
		Method welcomeMethod = aClass.getMethod("welcome");
		String result = (String) welcomeMethod.invoke(helloWorld);
		System.out.println(result);*/
		
		System.out.println("___________");
		
		//elloWorld x = new HelloWorld();
		Class<?> bClass = Class.forName("HelloWorld",true,classLoader);
		System.out.println(bClass.getClassLoader().getClass());
		}

}
