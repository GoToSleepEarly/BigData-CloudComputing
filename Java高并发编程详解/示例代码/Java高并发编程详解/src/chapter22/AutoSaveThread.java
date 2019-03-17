package chapter22;

import java.util.concurrent.TimeUnit;

public class AutoSaveThread extends Thread{

	private final Document document ;
	 
    public AutoSaveThread(Document document){
        super("DocumnetAutoSaveThread .......");
        this.document = document ;
    }
 
    @Override
    public void run(){
        while (true) {
 
            try {
                document.save();
                TimeUnit.SECONDS.sleep(1);
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }
        }
    }



}
