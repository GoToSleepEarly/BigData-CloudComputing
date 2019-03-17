package chapter01;

public class TemplateMethod {

	//��������ɸ�����ƣ�������final���ƣ���������д������ʵ�־����߼���
	public final void print(String message){
		System.out.println("#############");
		wrapPrint(message);
		System.out.println("#############");
	}
	protected void wrapPrint(String message) {
		
	}
	public static void main(String[] args) {
		TemplateMethod t1 = new TemplateMethod(){
			@Override
			protected void wrapPrint(String message){
				System.out.println("|"+message+"|");
			}
		};
		t1.print("Hello World");
		TemplateMethod t2 = new TemplateMethod(){
			@Override
			protected void wrapPrint(String message){
				System.out.println("*"+message+"*");
			}
		};
		t2.print("Hello World");
	}

}
