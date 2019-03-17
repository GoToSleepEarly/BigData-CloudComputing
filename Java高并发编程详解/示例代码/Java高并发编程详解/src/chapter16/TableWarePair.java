package chapter16;



public class TableWarePair {
 
 
 
    //×óÊÖ±ßµÄ²Í¾ß
    private final Tableware leftTool ;
 
 
    private final Tableware rightTool ;
 
    public  TableWarePair(  Tableware leftTool , Tableware rightTool ){
 
        this.leftTool = leftTool ;
        this.rightTool = rightTool ;
 
    }
 
 
    public Tableware getLeftTool() {
        return leftTool;
    }
 
    public Tableware getRightTool() {
        return rightTool;
    }
}
