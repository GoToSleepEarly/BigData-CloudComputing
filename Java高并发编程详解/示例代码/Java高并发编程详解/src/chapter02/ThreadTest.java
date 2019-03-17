package chapter02;

/**
 * @author xzt
 * @date 2019/02/12 
 */
//class 的一些信息也是放在方法区
public class ThreadTest {
 
    //存放在方法区
    private static int i = 0;
 
    //引用地址会放到方法区，具体数据会放到堆
    private byte[] bytes = new byte[1024];
 
    //JVM 会创建 main 线程
    public void main(String[] args) {
        //会为 main 线程开辟一个虚拟机栈
 
        //告诉 CPU下一步要执行什么，需要程序计数器
 
       // m 直接放到局部变量表
       int m = 1;
 
       //局部变量表中会有 arr 的地址，数据还是放到堆中
       int[] arr = new int[1024];
    }
 
}

