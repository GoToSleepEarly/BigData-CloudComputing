package chapter03;

import java.util.concurrent.TimeUnit;

public class ThreadisInterrupted {

	public static void main(String[] args) throws InterruptedException {
		Thread thread = new Thread(() -> {
			while(true){
				//��ѭ��
				//�������try-catch����ôinterrupt���ᴥ���쳣���жϡ�
				try {
					TimeUnit.MINUTES.sleep(1);
				} catch (InterruptedException e) {
					//����catch�쳣�����false����Ϊ�ж��ͺź󣬻���Ϊfalse
					//System.out.println("I am interrupted:" + isInteruptted());
				}
			}
			/*���������ڲ���
			 * while (!isInterrupted()) {
		        // ִ������...
		    }*/
		});
		thread.start();
		//ֻ��sleep��ǰ�߳�
		//TimeUnit.MILLISECONDS.sleep(2);
		System.out.println("Thread is interrupted?��"+ thread.isInterrupted());
		thread.interrupt();
		System.out.println("Thread is interrupted?:"+ thread.isInterrupted());
		
	}

}
