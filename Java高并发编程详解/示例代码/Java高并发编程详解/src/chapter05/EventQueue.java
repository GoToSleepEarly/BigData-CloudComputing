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
			//if�ĳ�while
			while(eventQueue.size() >= max){
				console("�����������");
				try {
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			console("���ύ������"+eventQueue.size());
			eventQueue.addLast(event);
			eventQueue.notify();
		}
	}
		
	public Event take(){
		synchronized (eventQueue) {
			//if�ĳ�while
			while(eventQueue.isEmpty()){
				console("�������Ϊ��");
				try {
					eventQueue.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			Event event = eventQueue.removeFirst();
			//notify���Ըĳ�notifyAll()
			this.eventQueue.notify();
			console("����"+event+"�Ѿ�����");
			return event;
		}
	}

	private void console(String message) {
		System.out.println(Thread.currentThread().getName()+" "+ message);
	}
}
