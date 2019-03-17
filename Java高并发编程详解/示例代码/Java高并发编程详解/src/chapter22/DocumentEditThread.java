package chapter22;

import java.io.IOException;
import java.util.Scanner;
 
/**
 * ģ���Զ��༭����̣߳� �޸ĳ���5���Զ�����
 */
public class DocumentEditThread extends Thread {
 
    private final  String documnetPath ;
 
    private final  String getDocumnetName ;
 
    private final Scanner scanner = new Scanner(System.in) ;
 
    public DocumentEditThread(String documnetPath , String documnetName){
        super("DocumnetEditThread .... ");
        this.documnetPath = documnetPath ;
        this.getDocumnetName = documnetName;
 
    }
 
    @Override
    public void run(){
        int times = 0 ;
 
        try{
            Document document = Document.create(documnetPath,getDocumnetName);
 
            while (true) {
                String text = scanner.next();
                if("quit".equals(text)){
                    document.close();
                    break;
                }
 
                document.edit(text);
 
                if(times == 5 ){
                    document.save();
                    times = 0 ;
                }
                times ++ ;
            }
 
 
        } catch ( IOException e){
            throw new RuntimeException() ;
        }
 
    }
 
}
