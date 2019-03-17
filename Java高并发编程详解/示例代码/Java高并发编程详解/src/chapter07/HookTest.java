package chapter07;

public class HookTest {

	public static void main(String[] args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("会触发");
        }));
		
		//抛出异常也会触发Hook
		throw new Exception("抛出异常");
	}

}
