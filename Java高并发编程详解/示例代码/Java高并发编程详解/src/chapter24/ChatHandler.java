package chapter24;
 
import java.io.*;
import java.net.Socket;
 
public class ChatHandler implements Runnable {
 
    private final Socket socket ;
 
    private final  String clientIdentify ;
 
    public ChatHandler(final Socket socket){
        this.socket = socket ;
        this.clientIdentify = socket.getInetAddress().getHostAddress() + ":" + socket.getPort();
    }
 
 
 
    @Override
    public void run() {
        try {
            this.chat();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
 
    private void chat() throws IOException {
        BufferedReader bufferedReader = wrap2Reader(this.socket.getInputStream());
 
        PrintStream printStream = wrap2Print(this.socket.getOutputStream());
 
        String received ;
 
        while((received = bufferedReader.readLine() )!= null){
            System.out.printf("client: %s-message:%s\n", clientIdentify , received );
            if("quit".equals(received)){
                write2Client(printStream,"client will close ....");
               socket.close();
               break;
            }
            write2Client(printStream,"Server: "+ received);
        }
 
    }
    private void release(){
    	try{
    		if(socket != null){
    			socket.close();
    		}
    	}catch(Throwable e){
    		if(socket != null){
    			//将socket实例加入Tracker中
    			SocketCleaningTracker.tracker(socket);
    		}
    	}

    }
    private void write2Client(PrintStream printStream, String message) {
        printStream.println(message);
        printStream.flush();
    }
 
 
    private PrintStream wrap2Print(OutputStream outputStream) {
        return new PrintStream(outputStream) ;
    }
 
    private BufferedReader wrap2Reader(InputStream inputStream) {
        return new BufferedReader(new InputStreamReader(inputStream));
    }
 
 
 
 
 
}
