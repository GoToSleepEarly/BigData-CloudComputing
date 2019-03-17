# Java方法区、栈及堆

## 一 方法区（Method Area）
### 1. 什么是方法区（Method Area）？
《深入理解JVM》书中对方法区（Method Area）描述如下：

> 方法区（Method Area）与Java堆一样，是各个线程共享的内存区域。

### 2. 方法区（Method Area）存储什么？
《深入理解JVM》书中对方法区（Method Area）存储内容描述如下：

> 它存储已被Java虚拟机加载的类信息、常量、静态变量、即时编译器编译后的代码等

![](https://i.postimg.cc/MKhr6LXF/image.png)
#### 2.1 方法区（Method Area）存储的类信息
对每个加载的类型（类class、接口interface、枚举enum、注解annotation），JVM必须在方法区中存储以下类型信息：
![](https://i.postimg.cc/prRDTDz4/image.png)
- 这个类型的完整有效名称（全名=包名.类名） 
- 这个类型直接父类的完整有效名称( java.lang.Object除外，其他类型若没有声明父类，默认父类是Object)
- 这个类型的修饰符(public、abstract、final的某个子集)
- 这个类型直接接口的一个有序列表 

除此之外还方法区（Method Area）存储类信息还有
- 类型的常量池( constant pool) 
- 域(Field)信息(类型，修饰符等)
- 方法(Method)信息
- 除了常量外的所有静态(static)变量

**注意**：这是类型的常量池，不是运行时常量池
![](https://i.postimg.cc/fb4LvMyf/3.png)
![](https://i.postimg.cc/htNJ39yx/1.png)
↑类型常量池
#### 2.2 方法区（Method Area）存储的常量
static final修饰的成员变量都存储于 方法区（Method Area）中
#### 2.3 方法区（Method Area）存储的静态变量
- 静态变量又称为类变量，类中被static修饰的成员变量都是静态变量（类变量）
- 静态变量之所以又称为类变量，是因为静态变量和类关联在一起，随着类的加载而存在于方法区（而不是堆中）
- 八种基本数据类型（byte、short、int、long、float、double、char、boolean）的静态变量会在方法区开辟空间，并将对应的值存储在方法方法区，对于引用类型的静态变量如果未用new关键字为引用类型的静态变量分配对象（如：static Object obj;）那么对象的引用obj会存储在方法区中，并为其指定默认值null;若，对于引用类型的静态变量如果用new关键字为引用类型的静态变量分配对象（如：static Person person = new Person();）,那么对象的引用person 会存储在方法区中，并且该对象在堆中的地址也会存储在方法区中（注意此时静态变量只存储了对象的堆地址，而对象本身仍在堆内存中）

#### 2.4 方法区（Method Area）存储的方法（Method）
- 程序运行时会加载类编译生成的字节码，这个过程中静态变量（类变量）和静态方法及普通方法对应的字节码加载到方法区。
- 但是！！！方法区中没有实例变量，这是因为，类加载先于对应类对象的产生，而实例变量是和对象关联在一起的，没有对象就不存在实例变量，类加载时没有对象，所以方法区中没有实例变量
- 静态变量（类变量）和静态方法及普通方法在方法区（Method Area）存储方式是有区别的

> 方法区里存储着class文件的信息和动态常量池,class文件的信息包括类信息和静态常量池。可以将类的信息是对class文件内容的一个框架，里面具体的内容通过常量池来存储。
>
> CLass文件中除了有类的版本、字段、方法、接口等描述信息外，还有一项信息是常量池，用于存放编译期生成的各种字面量和符号引用，这部分内容将在类加载后进入方法区的运行时常量池中存放。
> 
> 动态常量池里的内容除了是静态常量池里的内容外，还将静态常量池里的符号引用转变为直接引用，而且动态常量池里的内容是能动态添加的。例如调用String的intern方法就能将string的值添加到String常量池中，这里String常量池是包含在动态常量池里的，但在jdk1.8后，将String常量池放到了堆中。



## 二 栈（Stack）
> 栈（Stack）：线程私有的内存区域

- 每个方法（Method）执行时，都会创建一个栈帧，用于存储局部变量表、操作数栈、动态链接、方法出口信息等

- 栈中所存储的变量和引用都是局部的（即：定义在方法体中的变量或者引用），局部变量和引用都在栈中（包括final的局部变量）

- 八种基本数据类型（byte、short、int、long、float、double、char、boolean）的局部变量（定义在方法体中的基本数据类型的变量）在栈中存储的是它们对应的值
 
- 栈中还存储局部的对象的引用（定义在方法体中的引用类型的变量），对象的引用并不是对象本身，而是对象在堆中的地址，换句话说，局部的对象的引用所指对象在堆中的地址在存储在了栈中。当然，如果对象的引用没有指向具体的对象，对象的引用则是null

## 三 Java堆（Java Heap）
> Java堆（Java Heap） ：被所有线程共享的一块内存区域，在虚拟机启动时创建。Java堆（Java Heap）唯一目的就是存放对象实例。所有的对象实例及数组都要在Java堆（Java Heap）上分配内存空间。

- 由关键字new产生的所有对象都存储于Java堆（Java Heap）
- ！！！ 实例变量（非static修饰的成员变量）和对象关联在一起，所以实例变量也在堆中
- java数组也在堆中开辟内存空间

## 四 实例
具体见注释：
	
	public class  PersonDemo
	{
	    public static void main(String[] args) 
	    {   //局部变量p和形参args都在main方法的栈帧中
	        //new Person()对象在堆中分配空间
	        PersonDemo p = new PersonDemo();
	        //sum在栈中，new int[10]在堆中分配空间
	        int[] sum = new int[10];
	    }
	}
	
	
	class Person
	{   //实例变量name和age在堆(Heap)中分配空间
	    private String name;
	    private int age;
	    //类变量(引用类型)name1和"cn"都在方法区(Method Area)
	    private static String name1 = "cn";
	    //类变量(引用类型)name2在方法区(Method Area)
	    //new String("cn")对象在堆(Heap)中分配空间
	    private static String name2 = new String("cn");
	    //num在堆中，new int[10]也在堆中
	    private int[] num = new int[10];
	
	
	    Person(String name,int age)
	    {   
	        //this及形参name、age在构造方法被调用时
	        //会在构造方法的栈帧中开辟空间
	        this.name = name;
	        this.age = age;
	    }
	
	    //setName()方法在方法区中
	    public void setName(String name)
	    {
	        this.name = name;
	    }
	
	    //speak()方法在方法区中
	    public void speak()
	    {
	        System.out.println(this.name+"..."+this.age);
	    }
	
	    //showCountry()方法在方法区中
	    public static void  showCountry()
	    {
	        System.out.println("country="+name1);
	    }
	}	