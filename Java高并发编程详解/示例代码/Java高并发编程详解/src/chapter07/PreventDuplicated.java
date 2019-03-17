package chapter07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class PreventDuplicated {

	private final static String LOCK_PATH = "C:/Users/pcc/Documents/GitHub/BigData-CloudComputing/Java�߲���������/ʾ������/Java�߲���������/src/chapter07/";

	private final static String LOCK_FILE = ".lock";
	
	public static void main(String[] args) throws IOException {
		checkRunning();
		
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("The program received kill SIGNAL . ");
            getLockFile().toFile().delete();
        }));
		
		
		//ģ�⵱ǰ�߳���������
		for(;;){
			try {
				TimeUnit.MILLISECONDS.sleep(1);
				//System.out.println("������������");
				//System.exit(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static void checkRunning() throws IOException{
		Path path = getLockFile();
		if(path.toFile().exists())
			throw new RuntimeException("���������������𷴸�����");
		else
			Files.createFile(path);
	}
	private static Path getLockFile(){
		return Paths.get(LOCK_PATH,LOCK_FILE);
	}
}
