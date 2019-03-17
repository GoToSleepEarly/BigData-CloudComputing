package chapter18;

import java.util.Arrays;
import java.util.List;

public class ArrayListStream {
	public static void main(String[] args){
		//定义一个list并使用Arrays初始化
		List<String> list = Arrays.asList("Java", "Thread", "Concurrency", "Scala", "Clojure");
	
		//获取的并行的stream，然后通过map进行加工，最后输出。
		list.parallelStream().map(String::toUpperCase).forEach(System.out::println);
		list.forEach(System.out::println);
	}
}
