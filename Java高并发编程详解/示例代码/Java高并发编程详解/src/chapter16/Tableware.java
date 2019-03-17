package chapter16;


public class Tableware {
    //²Í¾ßÃû³Æ
    private final String toolName ;
 
    public Tableware(String toolName){
        this.toolName= toolName ;
    }
 
    @Override
    public String toString(){
        return "Tool: "+toolName ;
    }
 
 
}
