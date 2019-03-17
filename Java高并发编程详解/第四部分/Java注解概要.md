# Java注解概要——Annotation
Java Annotation是JDK5.0引入的一种注释机制。
注解(Annotation)很重要，未来的开发模式都是基于注解的，JPA是基于注解的，Spring2.5以上都是基于注解的，Hibernate3.x以后也是基于注解的，现在的Struts2有一部分也是基于注解的了，注解是一种趋势，现在已经有不少的人开始用注解了，注解是JDK1.5之后才有的新特性。

![](https://i.postimg.cc/HnPN3SPK/1.jpg)

从中，我们可以看出：

1. 1个Annotation 和 1个RetentionPolicy关联。  
可以理解为：每1个Annotation对象，都会有唯一的RetentionPolicy属性。

2. 1个Annotation 和 1~n个ElementType关联。  
可以理解为：对于每1个Annotation对象，可以有若干个ElementType属性。

3. Annotation 有许多实现类，包括：Deprecated, Documented, Inherited, Override等等。  
Annotation 的每一个实现类，都“和1个RetentionPolicy关联”并且“和1~n个ElementType关联”。

## Annotation组成部分
下面，我先介绍框架图的左半边(如下图)，即Annotation, RetentionPolicy, ElementType；然后在就Annotation的实现类进行举例说明。

![](https://i.postimg.cc/HWSFpxs2/2.jpg)

### 1. Annotation.java接口：
	
	package java.lang.annotation;
	public interface Annotation {

    	boolean equals(Object obj);

    	int hashCode();

    	String toString();

    	Class<? extends Annotation> annotationType();
	}

### 2.ElementType.java枚举类型
 
    package java.lang.annotation;

	public enum ElementType {
    	TYPE,               /* 类、接口（包括注释类型）或枚举声明  */

    	FIELD,              /* 字段声明（包括枚举常量）  */

    	METHOD,             /* 方法声明  */

    	PARAMETER,          /* 参数声明  */

    	CONSTRUCTOR,        /* 构造方法声明  */

    	LOCAL_VARIABLE,     /* 局部变量声明  */

    	ANNOTATION_TYPE,    /* 注释类型声明  */

    	PACKAGE             /* 包声明  */
	}

 “每1个Annotation” 都与 “1～n个ElementType”关联。当Annotation与某个ElementType关联时，就意味着：Annotation有了某种用途。例如，若一个Annotation对象是METHOD类型，则该Annotation只能用来修饰方法。

### 3.RetentionPolicy.java枚举类型
    package java.lang.annotation;
	public enum RetentionPolicy {
    	SOURCE,            /* Annotation信息仅存在于编译器处理期间，编译器处理完之后就没有该Annotation信息了  */

    	CLASS,             /* 编译器将Annotation存储于类对应的.class文件中。默认行为  */

    	RUNTIME            /* 编译器将Annotation存储于class文件中，并且可由JVM读入 */
	}

 “每1个Annotation” 都与 “1个RetentionPolicy”关联，并且与 “1～n个ElementType”关联。可以通俗的理解为：每1个Annotation对象，都会有唯一的RetentionPolicy属性。

**RetentionPolicy.SOURCE、RetentionPolicy.CLASS、RetentionPolicy.RUNTIME**  
分别对应：  
**Java源文件(.java文件)---->.class文件---->内存中的字节码**

- 若Annotation的类型为 SOURCE，则意味着：Annotation仅存在于编译器处理期间，编译器处理完之后，该Annotation就没用了。例如，“ @Override ”标志就是一个Annotation。当它修饰一个方法的时候，就意味着该方法覆盖父类的方法；并且在编译期间会进行语法检查！编译器处理完后，“@Override”就没有任何作用了。

- 若Annotation的类型为 CLASS，则意味着：编译器将Annotation存储于类对应的.class文件中，它是Annotation的默认行为。

- 若Annotation的类型为 RUNTIME，则意味着：编译器将Annotation存储于class文件中，并且可由JVM读入。

## Java自带的Annotation
### 1.Annotation通用定义
    @Documented
	@Target(ElementType.TYPE)
	@Retention(RetentionPolicy.RUNTIME)
	public @interface MyAnnotation1 {
	}
上面的作用是定义一个Annotation，它的名字是MyAnnotation1。定义了MyAnnotation1之后，我们可以在代码中通过“@MyAnnotation1”来使用它。
其它的，@Documented, @Target, @Retention, @interface都是来修饰MyAnnotation1的。
- @interface  
使用@interface定义注解时，意味着它实现了
java.lang.annotation.Annotation接口，即该注解就是一个Annotation。定义Annotation时，@interface是必须的。  
注意：它和我们通常的implemented实现接口的方法不同。Annotation接口的实现细节都由编译器完成。通过@interface定义注解后，该注解不能继承其他的注解或接口。

- @Documented  
类和方法的Annotation在缺省情况下是不出现在javadoc中的。如果使用@Documented修饰该Annotation，则表示它可以出现在javadoc中。  
定义Annotation时，@Documented可有可无；若没有定义，则Annotation不会出现在javadoc中。

- @Target(ElementType.TYPE)  
前面我们说过，ElementType 是Annotation的类型属性。而@Target的作用，就是来指定Annotation的类型属性。  
@Target(ElementType.TYPE) 的意思就是指定该Annotation的类型是ElementType.TYPE。这就意味着，MyAnnotation1是来修饰“类、接口（包括注释类型）或枚举声明”的注解。  
定义Annotation时，@Target可有可无。若有@Target，则该Annotation只能用于它所指定的地方；若没有@Target，则该Annotation可以用于任何地方。

- @Retention(RetentionPolicy.RUNTIME)  
前面我们说过，RetentionPolicy 是Annotation的策略属性，而@Retention的作用，就是指定Annotation的策略属性。  
@Retention(RetentionPolicy.RUNTIME) 的意思就是指定该Annotation的策略是RetentionPolicy.RUNTIME。这就意味着，编译器会将该Annotation信息保留在.class文件中，并且能被虚拟机读取。  
定义Annotation时，@Retention可有可无。若没有@Retention，则默认是RetentionPolicy.CLASS。

### 2.Java自带的Annotation
![](https://i.postimg.cc/nzNRDsjb/3.jpg)
![](https://i.postimg.cc/vHrq4ZGk/4.png)
由于“@Deprecated和@Override”类似，“@Documented, @Inherited, @Retention, @Target”类似；下面，我们只对@Deprecated, @Inherited, @SuppressWarnings 这3个Annotation进行说明。

#### 2.1 @Deprecated
	//它的作用是说明该注解能出现在javadoc中。
    @Documented
	//这就意味着，编译器会将Inherited的信息保留在.class文件中，并且能被虚拟机读取。
	@Retention(RetentionPolicy.RUNTIME)
	public @interface Deprecated {
	}
@Deprecated 所标注内容，不再被建议使用。
![](https://i.postimg.cc/8znh52fx/5.png)

#### 2.2 @Inherited
	@Documented
	@Retention(RetentionPolicy.RUNTIME)
	//这就意味着，@Inherited只能被用来标注“Annotation类型”。
	@Target(ElementType.ANNOTATION_TYPE)
	public @interface Inherited {
	}
假设，我们定义了某个Annotaion，它的名称是MyAnnotation，并且MyAnnotation被标注为@Inherited。现在，某个类Base使用了MyAnnotation，则Base具有了“具有了注解MyAnnotation”；现在，Sub继承了Base，由于MyAnnotation是@Inherited的(具有继承性)，所以，**Sub也“具有了注解MyAnnotation”**。

#### 2.3 @SuppressWarnings
	@Target({TYPE, FIELD, METHOD, PARAMETER, CONSTRUCTOR, LOCAL_VARIABLE})
	//SuppressWarnings信息仅存在于编译器处理期间，编译器处理完之后SuppressWarnings就没有作用了。
	@Retention(RetentionPolicy.SOURCE)
	public @interface SuppressWarnings {
		//SuppressWarnings能指定参数
    	String[] value();
	}

- deprecation  -- 使用了不赞成使用的类或方法时的警告
- unchecked    -- 执行了未检查的转换时的警告，例如当使用集合时没有用泛型 (Generics) 来指定集合保存的类型。
- fallthrough  -- 当 Switch 程序块直接通往下一种情况而没有 Break 时的警告。
- path         -- 在类路径、源文件路径等中有不存在的路径时的警告。
- serial       -- 当在可序列化的类上缺少 serialVersionUID 定义时的警告。
- finally      -- 任何 finally 子句不能正常完成时的警告。
- all          -- 关于以上所有情况的警告。

使用方法：

	//@SuppressWarnings(value={"deprecation"})
	public void method(){
	……
	}

## Annotation 的作用
### 1. 编译检查
Annotation具有“让编译器进行编译检查的作用”。例如，@SuppressWarnings, @Deprecated和@Override都具有编译检查作用。

### 2. 在反射中使用Annotation
![](https://i.postimg.cc/G2S0Sh4q/6.jpg)
在反射的Class, Method, Field等函数中，有许多于Annotation相关的接口。这也意味着，我们可以在反射中解析并使用Annotation。

	import java.lang.annotation.Annotation;
	import java.lang.annotation.Target;
	import java.lang.annotation.ElementType;
	import java.lang.annotation.Retention;
	import java.lang.annotation.RetentionPolicy;
	import java.lang.annotation.Inherited;
	import java.lang.reflect.Method;

	@Retention(RetentionPolicy.RUNTIME)
	@interface MyAnnotation {
	    String[] value() default "unknown";
	}
	
	/**
	 * Person类。它会使用MyAnnotation注解。
	 */
	class Person {
	    
	    /**
	     * empty()方法同时被 "@Deprecated" 和 “@MyAnnotation(value={"a","b"})”所标注 
	     * (01) @Deprecated，意味着empty()方法，不再被建议使用
	     * (02) @MyAnnotation, 意味着empty() 方法对应的MyAnnotation的value值是默认值"unknown"
	     */
	    @MyAnnotation
	    @Deprecated
	    public void empty(){
	        System.out.println("\nempty");
	    }
	    
	    /**
	     * sombody() 被 @MyAnnotation(value={"girl","boy"}) 所标注，
	     * @MyAnnotation(value={"girl","boy"}), 意味着MyAnnotation的value值是{"girl","boy"}
	     */
	    @MyAnnotation(value={"girl","boy"})
	    public void somebody(String name, int age){
	        System.out.println("\nsomebody: "+name+", "+age);
	    }
	}
	
	public class AnnotationTest {
	
	    public static void main(String[] args) throws Exception {
	        
	        // 新建Person
	        Person person = new Person();
	        // 获取Person的Class实例
	        Class<Person> c = Person.class;
	        // 获取 somebody() 方法的Method实例
	        Method mSomebody = c.getMethod("somebody", new Class[]{String.class, int.class});
	        // 执行该方法
	        mSomebody.invoke(person, new Object[]{"lily", 18});
	        iteratorAnnotations(mSomebody);
	        
	
	        // 获取 somebody() 方法的Method实例
	        Method mEmpty = c.getMethod("empty", new Class[]{});
	        // 执行该方法
	        mEmpty.invoke(person, new Object[]{});        
	        iteratorAnnotations(mEmpty);
	    }
	    
	    public static void iteratorAnnotations(Method method) {
	
	        // 判断 somebody() 方法是否包含MyAnnotation注解
	        if(method.isAnnotationPresent(MyAnnotation.class)){
	            // 获取该方法的MyAnnotation注解实例
	            MyAnnotation myAnnotation = method.getAnnotation(MyAnnotation.class);
	            // 获取 myAnnotation的值，并打印出来
	            String[] values = myAnnotation.value();
	            for (String str:values)
	                System.out.printf(str+", ");
	            System.out.println();
	        }
	        
	        // 获取方法上的所有注解，并打印出来
	        Annotation[] annotations = method.getAnnotations();
	        for(Annotation annotation : annotations){
	            System.out.println(annotation);
	        }
	    }
	}

### 3. 根据Annotation生成帮助文档
通过给Annotation注解加上@Documented标签，能使该Annotation标签出现在javadoc中。
### 4. 能够帮忙查看查看代码
通过@Override, @Deprecated等，我们能很方便的了解程序的大致结构。另外，我们也可以通过自定义Annotation来实现一些功能。

## 注解为特殊的接口
### 1.添加属性

	@Retention(RetentionPolicy.RUNTIME)
	@Target( { ElementType.METHOD, ElementType.TYPE })
	public @interface MyAnnotation {
    	String color();
	}

使用：

	@MyAnnotation(color="red")//应用MyAnnotation注解的color属性
	public class MyAnnotationTest {
	    public static void main(String[] args) {
	        /**
	         * 用反射方式获得注解对应的实例对象后，在通过该对象调用属性对应的方法
	         */
	        MyAnnotation annotation = (MyAnnotation) MyAnnotationTest.class.getAnnotation(MyAnnotation.class);
	        System.out.println(annotation.color());//输出red
	    }
	}

当只有一个属性时，可以
	
	@MyAnnotation("red")
	…………

### 2.为属性指定缺省值(默认值)
	@Retention(RetentionPolicy.RUNTIME)
	//Retention注解决定MyAnnotation注解的生命周期
	@Target( { ElementType.METHOD, ElementType.TYPE })
	public @interface MyAnnotation {
		String color() default "blue";//为属性指定缺省值
	}


### 3.为注解增加高级属性
数组类型的属性：
- 增加数组类型的属性：int[] arrayAttr() default {1,2,4};
- 应用数组类型的属性：@MyAnnotation(arrayAttr={2,4,5})
- 如果数组属性只有一个值，这时候属性值部分可以省略大括号，如：@MyAnnotation(arrayAttr=2)，这就表示数组属性只有一个值，值为2

枚举类型的属性：
- 增加枚举类型的属性：EumTrafficLamp lamp() default EumTrafficLamp.RED;
- 应用枚举类型的属性：@MyAnnotation(lamp=EumTrafficLamp.GREEN)

## class里注解的相关方法
从getAnnotation进去可以看到java.lang.class实现了AnnotatedElement方法

	public final class Class<T> implements java.io.Serializable,
					GenericDeclaration,
					Type,
					AnnotatedElement

java.lang.reflect.AnnotatedElement接口是所有程序元素（Class、Method和Constructor）的父接口，所以程序通过反射获取了某个类的AnnotatedElement对象之后，程序就可以调用该对象的如下四个个方法来访问Annotation信息：
- 方法1：<T extends Annotation> T getAnnotation  (Class<T> annotationClass):   
返回该程序元素上存在的、指定类型的注解，如果该类型注解不存在，则返回null。
- 方法2：Annotation[] getAnnotations():  
返回该程序元素上存在的所有注解。
- 方法3：boolean is AnnotationPresent(Class<?extends Annotation> annotationClass):  
判断该程序元素上是否包含指定类型的注解，存在则返回true，否则返回false.
- 方法4：Annotation[] getDeclaredAnnotations()：  
返回直接存在于此元素上的所有注释。与此接口中的其他方法不同，该方法将忽略继承的注释。（如果没有注释直接存在于此元素上，则返回长度为零的一个数组。）该方法的调用者可以随意修改返回的数组；这不会对其他调用者返回的数组产生任何影响

## 注解的本质
java.lang.annotation.Annotation」接口中有这么一句话，用来描述『注解』。

> The common interface extended by all annotation types  
> 所有的注解类型都继承自这个普通的接口（Annotation）

比如：

	@Target(ElementType.METHOD)
	@Retention(RetentionPolicy.SOURCE)
	public @interface Override {
	}
本质上就是：
	
	public interface Override extends Annotation{
    
	}
一个注解准确意义上来说，只不过是一种特殊的注释而已，如果没有解析它的代码，它可能连注释都不如。

而解析一个类或者方法的注解往往有两种形式，一种是编译器直接的扫描，一种是运行期反射。反射的事情我们待会说，而编译器的扫描指的是编译器在对 java 代码编译字节码的过程中会检测到某个类或者方法被一些注解修饰，这时它就会对于这些注解进行某些处理。

典型的就是注解 @Override，一旦编译器检测到某个方法被修饰了 @Override 注解，编译器就会检查当前方法的方法签名是否真正重写了父类的某个方法，也就是比较父类中是否具有一个同样的方法签名。

这一种情况只适用于那些编译器已经熟知的注解类，比如 JDK 内置的几个注解，而你自定义的注解，编译器是不知道你这个注解的作用的，当然也不知道该如何处理，往往只是会根据该注解的作用范围来选择是否编译进字节码文件，仅此而已。

#### 从虚拟机层面解释

	@Target(value = {ElementType.FIELD,ElementType.METHOD})
	@Retention(value = RetentionPolicy.RUNTIME)
	public @interface Hello{
		String value();
	}

这里我们指定了 Hello 这个注解只能修饰字段和方法，并且该注解永久存活，以便我们反射获取。

之前我们说过，虚拟机规范定义了一系列和注解相关的属性表，也就是说，无论是字段、方法或是类本身，如果被注解修饰了，就可以被写进字节码文件。属性表有以下几种：
1. RuntimeVisibleAnnotations：运行时可见的注解
2. RuntimeInVisibleAnnotations：运行时不可见的注解
3. RuntimeVisibleParameterAnnotations：运行时可见的方法参数注解
4. RuntimeInVisibleParameterAnnotations：运行时不可见的方法参数注解
5. AnnotationDefault：注解类元素的默认值

给大家看虚拟机的这几个注解相关的属性表的目的在于，让大家从整体上构建一个基本的印象，注解在字节码文件中是如何存储的。

所以，对于一个类或者接口来说，Class 类中提供了以下一些方法用于反射注解。
1. getAnnotation：返回指定的注解
2. isAnnotationPresent：判定当前元素是否被指定注解修饰
3. getAnnotations：返回所有的注解
4. getDeclaredAnnotation：返回本元素的指定注解
5. getDeclaredAnnotations：返回本元素的所有注解，不包含父类继承而来的

首先，设置一个虚拟机启动参数，用于捕获 JDK 动态代理类。
> -Dsun.misc.ProxyGenerator.saveGeneratedFiles=true

然后main函数

	public class Test {
		@Hello("hello")
		public static void main(String[] args) throws NoSuchMethodException{
			//System.getProperties().put("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
			Class cls = Test.class;
			Method method = cls.getMethod("main", String[].class);
			Hello hello = method.getAnnotation(Hello.class);
		}
	}

使用luyten-0.5.4反编译$Proxy0后如下（或使用JD-Eclipse）：

		package com.sun.proxy;
	
	import \u6ce8\u89e3\u539f\u7406.*;
	import java.lang.reflect.*;
	
	//继承了Hello
	public final class $Proxy1 extends Proxy implements Hello
	{
	    private static Method m1;
	    private static Method m2;
	    private static Method m4;
	    private static Method m0;
	    private static Method m3;
	    
	    public $Proxy1(final InvocationHandler invocationHandler) {
	        super(invocationHandler);
	    }
	    
	    public final boolean equals(final Object o) {
	        try {
	            return (boolean)super.h.invoke(this, $Proxy1.m1, new Object[] { o });
	        }
	        catch (Error | RuntimeException error) {
	            throw;
	        }
	        catch (Throwable t) {
	            throw new UndeclaredThrowableException(t);
	        }
	    }
	    
	    public final String toString() {
	        try {
	            return (String)super.h.invoke(this, $Proxy1.m2, null);
	        }
	        catch (Error | RuntimeException error) {
	            throw;
	        }
	        catch (Throwable t) {
	            throw new UndeclaredThrowableException(t);
	        }
	    }
	    
	    public final Class annotationType() {
	        try {
	            return (Class)super.h.invoke(this, $Proxy1.m4, null);
	        }
	        catch (Error | RuntimeException error) {
	            throw;
	        }
	        catch (Throwable t) {
	            throw new UndeclaredThrowableException(t);
	        }
	    }
	    
	    public final int hashCode() {
	        try {
	            return (int)super.h.invoke(this, $Proxy1.m0, null);
	        }
	        catch (Error | RuntimeException error) {
	            throw;
	        }
	        catch (Throwable t) {
	            throw new UndeclaredThrowableException(t);
	        }
	    }
	    
	    public final String value() {
	        try {
	            return (String)super.h.invoke(this, $Proxy1.m3, null);
	        }
	        catch (Error | RuntimeException error) {
	            throw;
	        }
	        catch (Throwable t) {
	            throw new UndeclaredThrowableException(t);
	        }
	    }
	    
	    static {
	        try {
	            $Proxy1.m1 = Class.forName("java.lang.Object").getMethod("equals", Class.forName("java.lang.Object"));
	            $Proxy1.m2 = Class.forName("java.lang.Object").getMethod("toString", (Class<?>[])new Class[0]);
	            $Proxy1.m4 = Class.forName("\u6ce8\u89e3\u539f\u7406.Hello").getMethod("annotationType", (Class<?>[])new Class[0]);
	            $Proxy1.m0 = Class.forName("java.lang.Object").getMethod("hashCode", (Class<?>[])new Class[0]);
	            $Proxy1.m3 = Class.forName("\u6ce8\u89e3\u539f\u7406.Hello").getMethod("value", (Class<?>[])new Class[0]);
	        }
	        catch (NoSuchMethodException ex) {
	            throw new NoSuchMethodError(ex.getMessage());
	        }
	        catch (ClassNotFoundException ex2) {
	            throw new NoClassDefFoundError(ex2.getMessage());
	        }
	    }
	}
看一下这个InvocationHandler所生成的方法：
![](https://i.postimg.cc/T1KdbJG2/6.png)
这里有一个 memberValues，它是一个 Map 键值对，键是我们注解属性名称，值就是该属性当初被赋上的值。
![](https://i.postimg.cc/9XkFhKtD/7.png)
![](https://i.postimg.cc/ZnXTxvK8/8.png)

而这个 invoke 方法就很有意思了，大家注意看，我们的代理类代理了 Hello 接口中所有的方法，所以对于代理类中任何方法的调用都会被转到这里来。

var2 指向被调用的方法实例，而这里首先用变量 var4 获取该方法的简明名称，接着 switch 结构判断当前的调用方法是谁，如果是 Annotation 中的四大方法，将 var7 赋上特定的值。

如果当前调用的方法是 toString，equals，hashCode，annotationType 的话，AnnotationInvocationHandler 实例中已经预定义好了这些方法的实现，直接调用即可。

那么假如 var7 没有匹配上这四种方法，说明当前的方法调用的是自定义注解字节声明的方法，例如我们 Hello 注解的 value 方法。这种情况下，将从我们的注解 map 中获取这个注解属性对应的值。

其实，JAVA 中的注解设计个人觉得有点反人类，**明明是属性的操作，非要用方法来实现。**

最后我们再总结一下整个反射注解的工作原理：

### 1. 首先，我们通过键值对的形式可以为注解属性赋值，像这样：@Hello（value = "hello"）。

### 2. 接着，你用注解修饰某个元素，编译器将在编译期扫描每个类或者方法上的注解，会做一个基本的检查，你的这个注解是否允许作用在当前位置，最后会将注解信息写入元素的属性表。

### 3. 然后，当你进行反射的时候，虚拟机将所有生命周期在 RUNTIME 的注解取出来放到一个 map 中，并创建一个 AnnotationInvocationHandler 实例，把这个 map 传递给它。

### 4. 最后，虚拟机将采用 JDK 动态代理机制生成一个目标注解的代理类，并初始化好处理器。

那么这样，一个注解的实例就创建出来了，它本质上就是一个代理类，你应当去理解好 AnnotationInvocationHandler 中 invoke 方法的实现逻辑，这是核心。一句话概括就是，**通过方法名返回注解属性值。**

### 和动态代理的区别是：注解生成Proxy代理的是不需要传入的Annotation（自己定义的），而动态代理生成Proxy代理的是Instance（是需要newProxyInstance传入相关东西的）。

所以如果在方法上@Annotation，实际上没什么区别，JVM会自动产生的是**注解**的Proxy。除非我们重写InvocationHandler，在invoke中处理我们关注的方法，其他的如toString等返回method.invoke(instance,args)不理会即可。