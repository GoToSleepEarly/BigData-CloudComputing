package chapter03;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FlightQueryExample {

	private static List<String> flightCompany = Arrays.asList("�Ϻ�����","��������","ɽ������");

	
	//�߼��Ƚ�������յ�search����󣬽��������Ӳ�ѯ�̷ֱ߳��������ÿ���̵߳�����ͳһ��չʾ��
	public static void main(String[] args) {
		List<String> results = search("SH","BJ");
		System.out.println("================");
		results.forEach(System.out::println);
	}

	private static List<String> search(String original, String dest) {
		final List<String> result = new ArrayList<String>();
		List<FlightQueryTask> tasks =flightCompany.stream().map(f -> createSerachTask(f,original,dest))
				.collect(Collectors.toList());
		tasks.forEach(Thread::start);
		tasks.forEach(t -> {
			try {
				t.join();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		});
		
		tasks.stream().map(FlightQuery::get).forEach(result::addAll);
		return result;
	}

	private static FlightQueryTask createSerachTask(String flight, String original, String dest) {
		return new FlightQueryTask(flight,original,dest);
	}

}
