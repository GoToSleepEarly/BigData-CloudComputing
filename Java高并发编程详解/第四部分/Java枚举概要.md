# Java枚举概要
原文地址：[https://blog.csdn.net/javazejian/article/details/71333103](https://blog.csdn.net/javazejian/article/details/71333103 "原文地址")  
此处只摘抄基本方法，原理见链接。
## 一、枚举的定义
在没有枚举之前，如果定义常量，常见方法如下：
	
	public class DayDemo {
		//普通方法定义日期常量
	    public static final int MONDAY =1;
	
	    public static final int TUESDAY=2;
	
	    public static final int WEDNESDAY=3;
	
	    public static final int THURSDAY=4;
	
	    public static final int FRIDAY=5;
	
	    public static final int SATURDAY=6;
	
	    public static final int SUNDAY=7;
	
	}
它存在许多不足，如在类型安全和使用方便性上并没有多少好处，如果存在定义int值相同的变量，混淆的几率还是很大的，编译器也不会提出任何警告，因此这种方式在枚举出现后并不提倡，现在我们利用枚举类型来重新定义上述的常量。

	//枚举类型，使用关键字enum
	enum Day {
	  MONDAY, TUESDAY, WEDNESDAY,
	  THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}
枚举常量在类型安全性和便捷性都很有保证，如果出现类型问题编译器也会提示我们改进，但务必记住枚举表示的类型其取值是必须有限的，也就是说每个值都是可以枚举出来的。使用方法如下：
	
	//枚举类型，使用关键字enum
	enum Day {
	  MONDAY, TUESDAY, WEDNESDAY,
	  THURSDAY, FRIDAY, SATURDAY, SUNDAY
	}
	
	public class EnumDemo{
		public static void main(String[] args){
			//直接引用
			Day day = Day.MONDAY;
		}
	}

## 二、枚举实现原理
实际上在使用关键字enum创建枚举类型并编译后，编译器会为我们生成一个相关的类，这个类继承了Java API中的java.lang.Enum类。

	//反编译Day.class
	final class Day extends Enum
	{
	    //编译器为我们添加的静态的values()方法
	    public static Day[] values()
	    {
	        return (Day[])$VALUES.clone();
	    }
	    //编译器为我们添加的静态的valueOf()方法，注意间接调用了Enum也类的valueOf方法
	    public static Day valueOf(String s)
	    {
	        return (Day)Enum.valueOf(com/zejian/enumdemo/Day, s);
	    }
	    //私有构造函数
	    private Day(String s, int i)
	    {
	        super(s, i);
	    }
	     //前面定义的7种枚举实例
	    public static final Day MONDAY;
	    public static final Day TUESDAY;
	    public static final Day WEDNESDAY;
	    public static final Day THURSDAY;
	    public static final Day FRIDAY;
	    public static final Day SATURDAY;
	    public static final Day SUNDAY;
	    private static final Day $VALUES[];
	
	    static 
	    {    
	        //实例化枚举实例
	        MONDAY = new Day("MONDAY", 0);
	        TUESDAY = new Day("TUESDAY", 1);
	        WEDNESDAY = new Day("WEDNESDAY", 2);
	        THURSDAY = new Day("THURSDAY", 3);
	        FRIDAY = new Day("FRIDAY", 4);
	        SATURDAY = new Day("SATURDAY", 5);
	        SUNDAY = new Day("SUNDAY", 6);
	        $VALUES = (new Day[] {
	            MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY
	        });
	    }
	}
从反编译的代码可以看出编译器确实帮助我们生成了一个Day类(注意该类是final类型的，将无法被继承)而且该类继承自java.lang.Enum类，该类是一个抽象类(稍后我们会分析该类中的主要方法)，除此之外，编译器还帮助我们生成了7个Day类型的实例对象分别对应枚举中定义的7个日期，这也充分说明了我们前面使用关键字enum定义的Day类型中的每种日期枚举常量也是实实在在的Day实例对象，只不过代表的内容不一样而已。注意编译器还为我们生成了两个静态方法，分别是values()和 valueOf()，稍后会分析它们的用法，到此我们也就明白了，使用关键字enum定义的枚举类型，在编译期后，也将转换成为一个实实在在的类，而在该类中，会存在每个在枚举类型中定义好变量的对应实例对象，如上述的MONDAY枚举类型对应

    public static final Day MONDAY;

同时编译器会为该类创建两个方法，分别是values()和valueOf()。到此相信我们对枚举的实现原理也比较清晰，下面我们深入了解一下java.lang.Enum类以及values()和valueOf()的用途。

## 三、枚举的常见方法
### Enum抽象类常见方法
![](https://i.postimg.cc/hP4GXVbB/enum.png)
- ordinal()方法，该方法获取的是枚举变量在枚举类中声明的顺序。
- compareTo(E o)方法则是比较枚举的大小，注意其内部实现是根据每个枚举的ordinal值大小进行比较的。
- name()方法与toString()几乎是等同的，都是输出变量的字符串形式。
- valueOf(Class<T> enumType, String name)方法则是根据枚举类的Class对象和枚举名称获取枚举常量，注意该方法是静态的。Day d=Enum.valueOf(Day.class,"MONDAY")返回MONDAY;


	//实现了Comparable
	public abstract class Enum<E extends Enum<E>>
	        implements Comparable<E>, Serializable {
	
	    private final String name; //枚举字符串名称
	
	    public final String name() {
	        return name;
	    }
	
	    private final int ordinal;//枚举顺序值
	
	    public final int ordinal() {
	        return ordinal;
	    }
	
	    //枚举的构造方法，只能由编译器调用
	    protected Enum(String name, int ordinal) {
	        this.name = name;
	        this.ordinal = ordinal;
	    }
	
	    public String toString() {
	        return name;
	    }
	
	    public final boolean equals(Object other) {
	        return this==other;
	    }
	
	    //比较的是ordinal值
	    public final int compareTo(E o) {
	        Enum<?> other = (Enum<?>)o;
	        Enum<E> self = this;
	        if (self.getClass() != other.getClass() && // optimization
	            self.getDeclaringClass() != other.getDeclaringClass())
	            throw new ClassCastException();
	        return self.ordinal - other.ordinal;//根据ordinal值比较大小
	    }
	
	    @SuppressWarnings("unchecked")
	    public final Class<E> getDeclaringClass() {
	        //获取class对象引用，getClass()是Object的方法
	        Class<?> clazz = getClass();
	        //获取父类Class对象引用
	        Class<?> zuper = clazz.getSuperclass();
	        return (zuper == Enum.class) ? (Class<E>)clazz : (Class<E>)zuper;
	    }
	
	
	    public static <T extends Enum<T>> T valueOf(Class<T> enumType,
	                                                String name) {
	        //enumType.enumConstantDirectory()获取到的是一个map集合，key值就是name值，value则是枚举变量值   
	        //enumConstantDirectory是class对象内部的方法，根据class对象获取一个map集合的值       
	        T result = enumType.enumConstantDirectory().get(name);
	        if (result != null)
	            return result;
	        if (name == null)
	            throw new NullPointerException("Name is null");
	        throw new IllegalArgumentException(
	            "No enum constant " + enumType.getCanonicalName() + "." + name);
	    }
	
	    //.....省略其他没用的方法
	}

### 编译器生成的Values方法与ValueOf方法

    Day[] days2 = Day.values();
    System.out.println("day2:"+Arrays.toString(days2));
    Day day = Day.valueOf("MONDAY");
    System.out.println("day:"+day);
    
    /**
     输出结果:
     day2:[MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY, SUNDAY]
     day:MONDAY
     */
values()方法的作用就是获取枚举类中的所有变量，并作为数组返回，而valueOf(String name)方法与Enum类中的valueOf方法的作用类似根据名称获取枚举变量，只不过编译器生成的valueOf方法更简洁些只需传递一个参数。

## 三、枚举的进阶用法 自定义函数
	public enum Day2 {
	    MONDAY("星期一"),
	    TUESDAY("星期二"),
	    WEDNESDAY("星期三"),
	    THURSDAY("星期四"),
	    FRIDAY("星期五"),
	    SATURDAY("星期六"),
	    SUNDAY("星期日");//记住要用分号结束
	
	    private String desc;//中文描述
	
	    /**
	     * 私有构造,防止被外部调用
	     * @param desc
	     */
	    private Day2(String desc){
	        this.desc=desc;
	    }
	
	    /**
	     * 定义方法,返回描述,跟常规类的定义没区别
	     * @return
	     */
	    public String getDesc(){
	        return desc;
	    }
	
	    public static void main(String[] args){
	        for (Day2 day:Day2.values()) {
	            System.out.println("name:"+day.name()+
	                    ",desc:"+day.getDesc());
	        }
	        System.out.println(Day2.FRIDAY.ordinal());
	    }
	
	    /**
	     输出结果:
	     name:MONDAY,desc:星期一
	     name:TUESDAY,desc:星期二
	     name:WEDNESDAY,desc:星期三
	     name:THURSDAY,desc:星期四
	     name:FRIDAY,desc:星期五
	     name:SATURDAY,desc:星期六
	     name:SUNDAY,desc:星期日
	     4
	     */
	}
在enum类中定义方法，务必在声明完枚举实例后使用分号分开，倘若在枚举实例前定义任何方法，编译器都将会报错，无法编译通过。

## EnumMap
现在我们有一堆size大小相同而颜色不同的数据，需要统计出每种颜色的数量是多少以便将数据录入仓库，定义如下枚举用于表示颜色Color:

	enum Color {
    	GREEN,RED,BLUE,YELLOW
	}
我们有如下解决方案，使用Map集合来统计，key值作为颜色名称，value代表衣服数量，如下：
 	
	Map<Color,Integer> enumMap=new EnumMap<>(Color.class);

    for (Clothes clothes:list){
		Color color=clothes.getColor();
        Integer count = enumMap.get(color);
        if(count!=null){
			enumMap.put(color,count+1);
        }else {
			enumMap.put(color,1);
        }
     }
	System.out.println(enumMap.toString());
也可以用HashMap实现，但是太麻烦了。EnumMap会更加高效，它只能接收同一枚举类型的实例作为键值且不能为null，由于枚举类型实例的数量**相对固定并且有限**，所以EnumMap使用数组来存放与枚举类型对应的值，毕竟数组是一段连续的内存空间，根据程序局部性原理，效率会相当高。

## EnumSet(线程不安全)

	enum Color {
	    GREEN , RED , BLUE , BLACK , YELLOW
	}
	
	
	public class EnumSetDemo {
	
	    public static void main(String[] args){
	
	        //空集合
	        EnumSet<Color> enumSet= EnumSet.noneOf(Color.class);
	        System.out.println("添加前："+enumSet.toString());
	        enumSet.add(Color.GREEN);
	        enumSet.add(Color.RED);
	        enumSet.add(Color.BLACK);
	        enumSet.add(Color.BLUE);
	        enumSet.add(Color.YELLOW);
	        System.out.println("添加后："+enumSet.toString());
	
	        System.out.println("-----------------------------------");
	
	        //使用allOf创建包含所有枚举类型的enumSet，其内部根据Class对象初始化了所有枚举实例
	        EnumSet<Color> enumSet1= EnumSet.allOf(Color.class);
	        System.out.println("allOf直接填充："+enumSet1.toString());
	
	        System.out.println("-----------------------------------");
	
	        //初始集合包括枚举值中指定范围的元素
	        EnumSet<Color> enumSet2= EnumSet.range(Color.BLACK,Color.YELLOW);
	        System.out.println("指定初始化范围："+enumSet2.toString());
	
	        System.out.println("-----------------------------------");
	
	        //指定补集，也就是从全部枚举类型中去除参数集合中的元素，如下去掉上述enumSet2的元素
	        EnumSet<Color> enumSet3= EnumSet.complementOf(enumSet2);
	        System.out.println("指定补集："+enumSet3.toString());
	
	        System.out.println("-----------------------------------");
	
	        //初始化时直接指定元素
	        EnumSet<Color> enumSet4= EnumSet.of(Color.BLACK);
	        System.out.println("指定Color.BLACK元素："+enumSet4.toString());
	        EnumSet<Color> enumSet5= EnumSet.of(Color.BLACK,Color.GREEN);
	        System.out.println("指定Color.BLACK和Color.GREEN元素："+enumSet5.toString());
	
	        System.out.println("-----------------------------------");
	
	        //复制enumSet5容器的数据作为初始化数据
	        EnumSet<Color> enumSet6= EnumSet.copyOf(enumSet5);
	        System.out.println("enumSet6："+enumSet6.toString());
	
	        System.out.println("-----------------------------------");
	
	        List<Color> list = new ArrayList<Color>();
	        list.add(Color.BLACK);
	        list.add(Color.BLACK);//重复元素
	        list.add(Color.RED);
	        list.add(Color.BLUE);
	        System.out.println("list:"+list.toString());
	
	        //使用copyOf(Collection<E> c)
	        EnumSet enumSet7=EnumSet.copyOf(list);
	        System.out.println("enumSet7:"+enumSet7.toString());
	
	        /**
	         输出结果：
	         添加前：[]
	         添加后：[GREEN, RED, BLUE, BLACK, YELLOW]
	         -----------------------------------
	         allOf直接填充：[GREEN, RED, BLUE, BLACK, YELLOW]
	         -----------------------------------
	         指定初始化范围：[BLACK, YELLOW]
	         -----------------------------------
	         指定补集：[GREEN, RED, BLUE]
	         -----------------------------------
	         指定Color.BLACK元素：[BLACK]
	         指定Color.BLACK和Color.GREEN元素：[GREEN, BLACK]
	         -----------------------------------
	         enumSet6：[GREEN, BLACK]
	         -----------------------------------
	         list:[BLACK, BLACK, RED, BLUE]
	         enumSet7:[RED, BLUE, BLACK]
	         */
	    }
	
	}

