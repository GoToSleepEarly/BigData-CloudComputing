package chapter16;



public class TableWarePair {
 
 
 
    //���ֱߵĲ;�
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
