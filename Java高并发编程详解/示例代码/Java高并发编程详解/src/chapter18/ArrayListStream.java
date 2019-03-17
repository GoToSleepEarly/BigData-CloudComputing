package chapter18;

import java.util.Arrays;
import java.util.List;

public class ArrayListStream {
	public static void main(String[] args){
		//����һ��list��ʹ��Arrays��ʼ��
		List<String> list = Arrays.asList("Java", "Thread", "Concurrency", "Scala", "Clojure");
	
		//��ȡ�Ĳ��е�stream��Ȼ��ͨ��map���мӹ�����������
		list.parallelStream().map(String::toUpperCase).forEach(System.out::println);
		list.forEach(System.out::println);
	}
}
