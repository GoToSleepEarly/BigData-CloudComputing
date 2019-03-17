package chapter09;

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
