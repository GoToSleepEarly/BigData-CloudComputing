package chapter07;

public class HookTest {

	public static void main(String[] args) throws Exception {
		Runtime.getRuntime().addShutdownHook(new Thread(()->{
            System.out.println("�ᴥ��");
        }));
		
		//�׳��쳣Ҳ�ᴥ��Hook
		throw new Exception("�׳��쳣");
	}

}
