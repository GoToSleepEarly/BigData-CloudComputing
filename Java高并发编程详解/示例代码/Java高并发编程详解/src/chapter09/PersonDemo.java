package chapter09;

public class  PersonDemo
{
    public static void main(String[] args) 
    {   //�ֲ�����p���β�args����main������ջ֡��
        //new Person()�����ڶ��з���ռ�
        PersonDemo p = new PersonDemo();
        //sum��ջ�У�new int[10]�ڶ��з���ռ�
        int[] sum = new int[10];
    }
}


class Person
{   //ʵ������name��age�ڶ�(Heap)�з���ռ�
    private String name;
    private int age;
    //�����(��������)name1��"cn"���ڷ�����(Method Area)
    private static String name1 = "cn";
    //�����(��������)name2�ڷ�����(Method Area)
    //new String("cn")�����ڶ�(Heap)�з���ռ�
    private static String name2 = new String("cn");
    //num�ڶ��У�new int[10]Ҳ�ڶ���
    private int[] num = new int[10];


    Person(String name,int age)
    {   
        //this���β�name��age�ڹ��췽��������ʱ
        //���ڹ��췽����ջ֡�п��ٿռ�
        this.name = name;
        this.age = age;
    }

    //setName()�����ڷ�������
    public void setName(String name)
    {
        this.name = name;
    }

    //speak()�����ڷ�������
    public void speak()
    {
        System.out.println(this.name+"..."+this.age);
    }

    //showCountry()�����ڷ�������
    public static void  showCountry()
    {
        System.out.println("country="+name1);
    }
}
