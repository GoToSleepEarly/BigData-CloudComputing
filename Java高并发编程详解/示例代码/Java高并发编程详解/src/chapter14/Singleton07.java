package chapter14;
public class Singleton07 {
 
    private Singleton07(){}
    
    private static Singleton07 getInstance(){
        return SingletonHolder.INSTANCE.getInstance();
    }
    
    public enum SingletonHolder{
        INSTANCE;
 
        private Singleton07 instance;
 
        SingletonHolder(){
            this.instance = new Singleton07();
        }
 
        public Singleton07 getInstance(){
            return instance;
        }
    }    
}
