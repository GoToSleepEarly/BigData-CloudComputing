package chapter26;

public class Production extends InstructionBook {
 
    // ��ƷID
    private final int prodId ;
 
    public  Production(int prodId){
        this.prodId = prodId ;
    }
 
    @Override
    protected void firstProcess() {
        System.out.println("execute the " + prodId + " first process ... ");
    }
 
    @Override
    protected void secondProcess() {
        System.out.println("execute the " + prodId + " second process ... ");
    }
    
    public String toString(){
    	return "PROD:"+prodId;
    }
}

