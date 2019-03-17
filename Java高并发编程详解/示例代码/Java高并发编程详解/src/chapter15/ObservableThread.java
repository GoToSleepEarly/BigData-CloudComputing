package chapter15;

public class ObservableThread<T> extends Thread
							implements Observable{

	private final TaskLifeCycle<T> lifecycle;
	
	private final Task<T> task;
	
	private Cycle cycle;
	
	//ָ��Task��ʵ�֣�Ĭ�����ʹ��EmptyLifeCyc
	public ObservableThread(Task<T> task) {
		this(new TaskLifeCycle.EmptyLifeCycle<>(), task);
	}
	
	public ObservableThread(TaskLifeCycle<T> lifecycle, Task<T> task) {
		super();
		//task������Ϊnull
		if(task == null){
			throw new IllegalArgumentException("Task����Ϊ��");
		}
		this.lifecycle = lifecycle;
		this.task = task;
	}
	@Override
	public final void run(){
		//ִ���߼���Ԫʱ���ֱ𴥷���Ӧ�¼�
		this.update(Cycle.STARTED, null, null);
		try{
			this.update(Cycle.RUNNING, null, null);
			
			T result =  this.task.call();
			this.update(Cycle.DONE, result, null);
		}catch(Exception e){
			this.update(Cycle.ERROR, null, e);
		}
	}
	private void update(Cycle cycle, T result, Exception e) {
		this.cycle = cycle;
		if(lifecycle == null){
			return;
		}
		try{
			switch (cycle) {
			case STARTED:
				this.lifecycle.onStart(Thread.currentThread());
				break;
			case RUNNING:
				this.lifecycle.onRunning(Thread.currentThread());
				break;
			case DONE:
				this.lifecycle.onFinish(Thread.currentThread(), result);
				break;
			case ERROR:
				this.lifecycle.onError(Thread.currentThread(), e);
				break;
			}
		}catch(Exception ex){
				if(cycle == Cycle.ERROR)
					throw ex;
			}
	}

	@Override
	public Cycle getCycle() {
		return this.cycle;
	}
	

}
