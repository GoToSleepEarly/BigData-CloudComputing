package chapter01;

public class TicketWindow extends Thread{
	
	//��̨����
	private final String name;
	
	//�������50��ҵ��
	private static final int MAX = 50;
	
	//����ִ��󣬶�ȡindex��Ψһ
	//private int index = 1;
	private static int index = 1;
	
	public TicketWindow(String name){
		this.name = name;
	}
	
	@Override
	public void run(){
		while(index <= MAX){
			System.out.println("��̨��" + name + "��ǰ�ĺ����ǣ�" + (index++));
		}
	}
	
	public static void main(String[] args) {
		TicketWindow ticketWindow1 = new TicketWindow("1�Ź�̨��");
		ticketWindow1.start();
		TicketWindow ticketWindow2 = new TicketWindow("2�Ź�̨��");
		ticketWindow2.start();
		TicketWindow ticketWindow3 = new TicketWindow("3�Ź�̨��");
		ticketWindow3.start();
		TicketWindow ticketWindow4 = new TicketWindow("4�Ź�̨��");
		ticketWindow4.start();
		TicketWindow ticketWindow5 = new TicketWindow("5�Ź�̨��");
		ticketWindow5.start();
	}

}
