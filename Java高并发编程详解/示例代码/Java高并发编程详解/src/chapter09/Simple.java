package chapter09;

import java.util.Random;

//配合SimpleTest理解
public class Simple {
	static {
		  //System.out.println(x);
	      System.out.println("我会被初始化");    
	      x=100;
	  }    
	public static int x = 10;
	
	public static void test(){
		//do nothing
	}
	
	//在其他类中引用MAX不会导致初始化，静态代码不会输出
	public static final int MAX = 10;
	
	//由于计算复杂，只有初始化后才能得到结果，所以会输出静态代码块
	public static final int RANDOM = new Random().nextInt();
	
	public static void main(String[] args) throws ClassNotFoundException {
		Class.forName("chapter09.Simple");
	}

}
