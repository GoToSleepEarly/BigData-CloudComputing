package chapter01;

public class TemplateMethod {

	//程序控制由父类控制，并且是final控制，不允许被重写。子类实现具体逻辑。
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
