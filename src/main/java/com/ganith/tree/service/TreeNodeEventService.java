package com.ganith.tree.service;

import java.util.Comparator;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import javax.inject.Named;

import lombok.Getter;

import com.ganith.tree.entity.Event;

@Named
public class TreeNodeEventService {
	
//	@Getter
//	private BlockingQueue<String> messageQueue = new ArrayBlockingQueue<String>(
//			1000, true);
	@Getter
	private BlockingQueue<Event> eventQueue = new PriorityBlockingQueue<Event>(100, new EventComparator());

//	@Getter
//	private ConcurrentHashMap<String, Integer> eventMap = new ConcurrentHashMap<String, Integer>();
	
	//@Inject
	//private UpdateCount updatesCount;
	
//	public void persistEventCount(UpdateCount updatesCount) {
//		int createCount = eventMap.get(Constants.EVENT_COUNT);
//		AtomicInteger atomicInteger = new AtomicInteger(createCount);
//		updatesCount.setEventCount(atomicInteger.incrementAndGet());
//		eventMap.put(Constants.EVENT_COUNT, updatesCount.getEventCount());
//	}
	
//	public int getUpdateCount(UpdateCount updatesCount) {
//		return (eventMap.get(Constants.EVENT_COUNT) - updatesCount.getEventCount());
//	}
	
//	public void refresh(UpdateCount updatesCount) {
//		updatesCount.setEventCount(eventMap.get(Constants.EVENT_COUNT));
//	}
	
//	public void logEventMessages(String message, UpdateCount updatesCount) {
//		messageQueue.offer(message);
//		persistEventCount(updatesCount);
//	}
	
//	public void logEventMessages(String message) {
//		messageQueue.offer(message);
//	}
	
	public void logEventMessages(Event event) {
		eventQueue.offer(event);
	}
	
	private static class EventComparator implements Comparator<Event> {

		public int compare(Event o1, Event o2) {
			return new Long(o1.getTimestamp() - o2.getTimestamp()).intValue();
		}

	}
	
//	public void persistEventCount() {
//		int createCount = eventMap.get(Constants.EVENT_COUNT);
//		AtomicInteger atomicInteger = new AtomicInteger(createCount);
//		updatesCount.setEventCount(atomicInteger.incrementAndGet());
//		eventMap.put(Constants.EVENT_COUNT, updatesCount.getEventCount());
//	}
	

//	@PostConstruct
//	public void initialiseEventMap() {
//		eventMap.put(Constants.EVENT_COUNT, 0);
//	}
}
