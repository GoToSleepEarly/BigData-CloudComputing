package chapter17;


import static java.lang.Thread.currentThread;
 
public class ReadWriteLockTest {
 
    private final static String text = "Disconnected from the target VM, address: '127.0.0.1:60936', transport: 'socket'" ;
 
    public static void main(String[] args) {
        final  ShareData shareData = new ShareData(50) ;
 
        for (int i = 0 ; i< 2 ; i++) {
            new Thread(()->{
                for (int index =0 ; index < text.length() ; index++) {
                    try {
                        char c = text.charAt(index) ;
                        shareData.write(c);
                        System.out.println(currentThread()+"  write  " + c );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
        for (int i = 0 ; i< 5 ; i++) {
            new Thread(()->{
                for (int index =0 ; index < text.length() ; index++) {
                    while (true) {
                        try {
                            String data = new String(shareData.read()) ;
                            System.out.println(currentThread()+"  read   " + data );
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    }
}
