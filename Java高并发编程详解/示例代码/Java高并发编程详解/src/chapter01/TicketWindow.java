package chapter01;

public class TicketWindow extends Thread{
	
	//柜台名称
	private final String name;
	
	//最多受理50笔业务
	private static final int MAX = 50;
	
	//会出现错误，读取index不唯一
	//private int index = 1;
	private static int index = 1;
	
	public TicketWindow(String name){
		this.name = name;
	}
	
	@Override
	public void run(){
		while(index <= MAX){
			System.out.println("柜台：" + name + "当前的号码是：" + (index++));
		}
	}
	
	public static void main(String[] args) {
		TicketWindow ticketWindow1 = new TicketWindow("1号柜台机");
		ticketWindow1.start();
		TicketWindow ticketWindow2 = new TicketWindow("2号柜台机");
		ticketWindow2.start();
		TicketWindow ticketWindow3 = new TicketWindow("3号柜台机");
		ticketWindow3.start();
		TicketWindow ticketWindow4 = new TicketWindow("4号柜台机");
		ticketWindow4.start();
		TicketWindow ticketWindow5 = new TicketWindow("5号柜台机");
		ticketWindow5.start();
	}

}
