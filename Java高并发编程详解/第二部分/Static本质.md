# static的本质
为啥需要静态初始化块？
> 静态初始化块主要用来初始化类变量

类变量可以在构造函数中初始化为啥还要定义静态初始化块呢？
> 因为，类变量可以通过类名.类变量的形式调用，有的时候我们不想为了初始化类变量而创建对象，所以就有了静态初始化块。 
> 静态初始化块可以在类加载的时候执行一次。避免了了初始化类变量而创建对象

执行顺序？
> 执行顺序：类变量及引用（静态变量及静态引用）的初始化（默认初始化或“=”显示赋值）->静态代码块(静态初始化块，对默认初始化的类变量根据需要重新初始化)->main()->实例变量及引用的初始化->构造代码块->构造方法
例子：
	
	public class B
	{
    	public static B t1 = new B();
    	public static B t2 = new B();
    	{
    	    System.out.println("构造块");
    	}
    	static
    	{
    	    System.out.println("静态块");
    	}
    	public static void main(String[] args)
    	{
    	    B t = new B();
    	}
	}
输出如下：	构造块 构造块 静态块 构造块 


	static {  
        _i = 20;  
    }  
    public static int _i = 10;  

    public static void main(String[] args) {  
        System.out.println(_i);  
    }  
上述代码输出 10 。

## 1. 静态变量如何初始化
	public static int _i;  
    static {  
        _i = 10;  
    } 
和

	public static int _i = 10;  
有区别吗？通过 javap -c 查看字节码

    public Test();
      Code:
       0: aload_0
       1: invokespecial #1; //Method java/lang/Object."<init>":()V
       4: return
    
    static {};
      Code:
       0: bipush 10
       2: putstatic #2; //Field _i:I
       5: return
分析上面，当定义不含static块时，编译器会提供默认的static块(有静态变量初始化的前提下)。

## 2. JDK如何处理static块？
	public static int _i;  
    static {  
        _i = 10;  
    }  

    public static void main(String[] args) {  
    }  

    static {  
        _i = 20;  
    }  
同样观察字节码，发现和如下的代码效果一致
 	
	public static int _i;  

    public static void main(String[] args) {  
    }  

    static {  
        _i = 10;  
        _i = 20;  
    }  
不仅类定义中可以有多个static块，而且在编译时编译器会将多个static块按照代码的前后位置重新组合成一个static块。
    
	public Test();
      Code:
       0: aload_0
       1: invokespecial #1; //Method java/lang/Object."<init>":()V
       4: return
    
    public static void main(java.lang.String[]);
      Code:
       0: return
    
    static {};
      Code:
       0: bipush 10
       2: putstatic #2; //Field _i:I
       5: bipush 20
       7: putstatic #2; //Field _i:I
       10: return

**注意：**静态变量的声明和初始化是在不同阶段(连接的准备、初始化),所以会有上述差别。
