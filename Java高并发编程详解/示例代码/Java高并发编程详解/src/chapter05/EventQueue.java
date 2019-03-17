package chapter05;

import java.util.LinkedList;

public class EventQueue {
	private final int max;
	
	static class Event{
		
	}
	
	private final LinkedList<Event> eventQueue = new LinkedList<>();
	
	private final static int DEFAULT_MAX_EVENT = 10;
	
	public EventQueue(){
		this(DEFAULT_MAX_EVENT);
	}
	
	public EventQueue(int max){
		this.max = max;		
	}
	
	public void offer(Event event){
		synchronized (eventQueue) {
			//if改成while
			while(eventQueue.size() >= max){
				console("事务队列已满");
				try {
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			console("已提交新事务"+eventQueue.size());
			eventQueue.addLast(event);
			eventQueue.notify();
		}
	}
		
	public Event take(){
		synchronized (eventQueue) {
			//if改成while
			while(eventQueue.isEmpty()){
				console("事务队列为空");
				try {
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Event event = eventQueue.removeFirst();
			//notify可以改成notifyAll()
			this.eventQueue.notify();
			console("事务："+event+"已经处理");
			return event;
		}
	}

	private void console(String message) {
		System.out.println(Thread.currentThread().getName()+" "+ message);
	}
}
