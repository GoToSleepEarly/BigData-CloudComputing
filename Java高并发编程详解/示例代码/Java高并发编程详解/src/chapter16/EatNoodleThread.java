package chapter16;


public class EatNoodleThread extends Thread {
    private final String name ;
    
    /*
    //×óÊÖ±ßµÄ²Í¾ß
    private final Tableware leftTool ;
 
    private final Tableware rightTool ;
 
    public EatNoodleThread(String name ,Tableware leftTool , Tableware rightTool ){
        this.name = name ;
        this.leftTool = leftTool ;
        this.rightTool = rightTool ;
    }*/
 
    private final TableWarePair tableWarePair ;
    
    public EatNoodleThread(String name , TableWarePair tableWarePair ){
        this.name = name ;
        this.tableWarePair = tableWarePair ;
 
    }

 
    @Override
    public void run(){
        while (true){
            this.eat();
        }
    }
 
   /* private void eat() {
        synchronized (leftTool){
            System.out.println(name + "take up "+ leftTool +" (left)" );
            synchronized (rightTool){
                System.out.println(name + "take up "+ rightTool +" (right)" );
 
                System.out.println(name + "is eating now." );
 
                System.out.println(name + "put down "+rightTool +" (right)" );
            }
 
            System.out.println(name + "put down "+rightTool +" (left)" );
 
        }*/
    private void eat() {
        synchronized (tableWarePair){
            System.out.println(currentThread().getName() + " : " + name + " take up "+ tableWarePair.getLeftTool() +" (left)" );
 
            System.out.println(currentThread().getName() + " : " + name + " take up "+ tableWarePair.getRightTool() +" (right)" );
 
            System.out.println(currentThread().getName() + " : " + name + " is eating now." );
 
            System.out.println(currentThread().getName() + " : " + name + " put down "+tableWarePair.getRightTool() +" (right)" );
 
 
            System.out.println(currentThread().getName() + " : " + name + " put down "+tableWarePair.getLeftTool() +" (left)" );
 
        }
    }
}
