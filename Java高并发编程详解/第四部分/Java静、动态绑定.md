# Java方法的静态绑定与动态绑定讲解（向上转型的运行机制详解）
原文地址：[http://www.cnblogs.com/ygj0930/p/6554103.html](原文地址：http://www.cnblogs.com/ygj0930/p/6554103.html)

**静态(static binding)** 和**动态绑定机制(auto binding)**
## 一、 静态绑定机制
    //被调用的类  
    class Father{  
      public static void f1(){  
      	System.out.println("Father— f1()");  
      }  
    }  
    //调用静态方法  
    public class StaticCall{  
       public static void main(){  
		Father.f1(); //调用静态方法  
       }  
    }  
上面的源代码中执行方法调用的语句(Father.f1())被编译器编译成了一条指令：invokestatic #13。我们看看JVM是如何处理这条指令的
1. 指令中的#13指的是StaticCall类的常量池中第13个常量表的索引项(关于常量池详见[《Class文件内容及常量池 》](https://hxraid.iteye.com/blog/687660))。这个常量表(CONSTATN_Methodref_info) 记录的是方法f1信息的**符号引用**(包括f1所在的类名，方法名和返回类型)。JVM会首先根据这个符号引用找到方法f1所在的类的全限定名: XX.XXX.Father。
2. 紧接着JVM会加载、链接和初始化Father类。（见之前的ClassLoader）
3. 然后在Father类所在的方法区中找到f1()方法的直接地址，并将这个直接地址记录到StaticCall类的常量池索引为13的常量表中。这个过程叫常量池解析 ，以后再次调用Father.f1()时，将直接找到f1方法的字节码。
4. 完成了StaticCall类常量池索引项13的常量表的解析之后，JVM就可以调用f1()方法，并开始解释执行f1()方法中的指令了。

通过上面的过程，我们发现经过常量池解析之后，JVM就能够确定要调用的f1()方法具体在内存的什么位置上了。实际上，这个信息在编译阶段就已经在StaticCall类的常量池中记录了下来。这种在编译阶段就能够确定调用哪个方法的方式，我们叫做 静态绑定机制 。同样的还有**private**和**finial**，因为无法被继承。另外所有类的初始化方法< init>和< clinit>会被编译成invokespecial指令。JVM会采用静态绑定机制来顺利的调用这些方法。

TIPS：Clinit是类初始化，init是类实例化。
## 二、静态绑定机制
    package hr.test;  
    //被调用的父类  
    class Father{  
    	public void f1(){  
    		System.out.println("father-f1()");  
    	}  
		public void f1(int i){  
    		System.out.println("father-f1()  para-int "+i);  
		}  
    }  
    //被调用的子类  
    class Son extends Father{  
    	public void f1(){ //覆盖父类的方法  
    		System.out.println("Son-f1()");  
    	}  
    	public void f1(char c){  
    		System.out.println("Son-s1() para-char "+c);  
    	}  
    }  
      
    //调用方法  
    import hr.test.*;  
    public class AutoCall{  
    	public static void main(String[] args){  
    		Father father=new Son(); //多态  
    		father.f1(); //打印结果： Son-f1()  
    	}  
    }  
上面的源代码中有三个重要的概念：**多态(polymorphism) 、方法覆盖 、方法重载** 。打印的结果大家也都比较清楚，但是JVM是如何知道f.f1()调用的是子类Sun中方法而不是Father中的方法呢？在解释这个问题之前，我们首先简单的讲下JVM管理的一个非常重要的数据结构——方法表 。  
在JVM加载类的同时，会在方法区中为这个类存放很多信息(详见[《Java 虚拟机体系结构》](https://hxraid.iteye.com/blog/676235))。其中就有一个数据结构叫**方法表**。**它以数组的形式记录了当前类及其所有超类的可见方法字节码在内存中的直接地址** 。下图是上面源代码中Father和Sun类在方法区中的方法表：
![](https://i.postimg.cc/VL1JjdnY/image.jpg)
上图中的方法表有两个特点：(1) 子类方法表中继承了父类的方法，比如Father extends Object。 (2) 相同的方法(相同的方法签名：方法名和参数列表)在所有类的方法表中的**索引相同**。比如Father方法表中的f1()和Son方法表中的f1()都位于各自方法表的第11项中。   
对于上面的源代码，编译器首先会把main方法编译成下面的字节码指令：

    0  new hr.test.Son [13] //在堆中开辟一个Son对象的内存空间，并将对象引用压入操作数栈  
    3  dup
    4  invokespecial #7 [15] // 调用初始化方法来初始化堆中的Son对象   
    7  astore_1 //弹出操作数栈的Son对象引用压入局部变量1中  
    8  aload_1 //取出局部变量1中的对象引用压入操作数栈  
    9  invokevirtual #15 //调用f1()方法  
    12  return  

其中中invokevirtual指令的详细调用过程是这样的：
1. invokevirtual指令中的#15指的是AutoCall类的常量池中第15个常量表的索引项。这个常量表(**CONSTATN_Methodref_info** ) 记录的是方法f1信息的符号引用(包括f1所在的类名，方法名和返回类型)。JVM会首先根据这个符号引用找到调用方法f1的类的全限定名: hr.test.Father。这是因为调用方法f1的类的对象father声明为Father类型。
2. 在Father类型的方法表中查找方法f1，如果找到，则将方法f1在方法表中的索引项11(如上图)记录到AutoCall类的常量池中第15个常量表中(**常量池解析** )。这里有一点要注意：**如果Father类型方法表中没有方法f1，那么即使Son类型中方法表有，编译的时候也通过不了。因为调用方法f1的类的对象father的声明为Father类型。**
3. 在调用invokevirtual指令前有一个aload_1指令，它会将开始创建在堆中的Son对象的引用压入操作数栈。然后invokevirtual指令会**根据这个Son对象的引用**首先找到堆中的Son对象，然后进一步找到Son对象所属类型的方法表。过程如下图所示：
![](https://i.postimg.cc/W3wvjb3n/image.jpg)
4. 这时通过第(2)步中解析完成的#15常量表中的方法表的索引项11，可以定位到Son类型方法表中的方法f1()，然后通过直接地址找到该方法字节码所在的内存空间。
  
很明显，根据对象(father)的声明类型(Father)还不能够确定调用方法f1的位置，**必须根据father在堆中实际创建的对象类型Son来确定f1方法所在的位置**。这种在程序运行过程中，通过动态创建的对象的方法表来定位方法的方式，我们叫做 **动态绑定机制** 。

上面基本解释了动态绑定的原理，下面这种情况呢？
    
	public class AutoCall{  
       public static void main(String[] args){  
             Father father=new Son();  
             char c='a';  
             father.f1(c); //打印结果：father-f1()  para-int 97  
       }  
	}  
问题是Fahter类型中并没有方法签名为f1(char)的方法呀。但打印结果显示JVM调用了Father类型中的f1(int)方法，并没有调用到Son类型中的f1(char)方法。  
首先可以明确的是：JVM首先是根据对象father声明的类型Father来解析常量池的(也就是用Father方法表中的索引项来代替常量池中的符号引用)。如果Father中没有匹配到"合适" 的方法，就无法进行常量池解析，这在编译阶段就通过不了。
那么什么叫"合适"的方法呢？当然，方法签名完全一样的方法自然是合适的。但是如果方法中的参数类型在声明的类型中并不能找到呢？比如上面的代码中调用father.f1(char)，Father类型并没有f1(char)的方法签名。实际上，JVM会找到一种“凑合”的办法，就是通过 **参数的自动转型** 来找 到“合适”的 方法。
## 三、总结
- 所有私有方法、静态方法、构造器及初始化方法< clinit>都是采用静态绑定机制。在编译器阶段就已经指明了调用方法在常量池中的符号引用，JVM运行的时候只需要进行一次常量池解析即可。
- 类对象方法的调用必须在运行过程中采用动态绑定机制。
- 首先，根据对象的声明类型(对象引用的类型)找到“合适”的方法。具体步骤如下：
	1. 如果能在声明类型中匹配到方法签名完全一样(参数类型一致)的方法，那么这个方法是最合适的。
	2. 在第1条不能满足的情况下，寻找可以“凑合”的方法。标准就是通过将参数类型进行自动转型之后再进行匹配。如果匹配到多个自动转型后的方法签名f(A)和f(B)，则用下面的标准来确定合适的方法：传递给f(A)方法的参数都可以传递给f(B)，则f(A)最合适。反之f(B)最合适 。
	3. 如果仍然在声明类型中找不到“合适”的方法，则编译阶段就无法通过。
	4. 然后，根据在堆中创建对象的实际类型找到对应的方法表，从中确定具体的方法在内存中的位置。
- 如果是接口的动态绑定，是通过**搜素**(代替了偏移量)来确定的。

另外几张图：
![](https://i.postimg.cc/zGK2423w/image.jpg)
![](https://i.postimg.cc/NMfN9gDb/2.jpg)
![](https://i.postimg.cc/L4Wy5F1B/2.png)
![](https://i.postimg.cc/tJs2k2Lx/3.png)
## 四、其他
### 覆写（Override）
一个实例方法可以覆写（override）在其超类中可访问到的具有相同签名的所有实例方法，从而使能了动态分派（dynamic dispatch）；换句话说，VM将基于实例的运行期类型来选择要调用的覆写方法。覆写是面向对象编程技术的基础，并且是唯一没有被普遍劝阻的名字重用形式：

    class Base{  
      public void f(){}  
	}  
	class Derived extends Base{  
      public void f(){}  
	}  

### 重载（Overload）
在某个类中的方法可以重载（overload）另一个方法，只要它们具有相同的名字和不同的签名。由调用所指定的重载方法是在编译期选定的。

    class CircuitBreaker{  
      public void f (int i){}    //int overloading  
      public void f(String s){}   //String overloading  
	}  
