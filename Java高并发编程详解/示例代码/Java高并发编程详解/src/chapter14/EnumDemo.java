package chapter14;

//枚举类型，使用关键字enum
enum Day {
  MONDAY, TUESDAY, WEDNESDAY,
  THURSDAY, FRIDAY, SATURDAY, SUNDAY;
}

public class EnumDemo{
	public static void main(String[] args){
		//直接引用
		Day day = Day.MONDAY;
		//System.out.println(day.ordinal());
	}
}