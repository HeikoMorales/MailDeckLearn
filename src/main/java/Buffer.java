

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Buffer {
	
	int capacity;
	List<Integer> buffer;
	Lock mutex;
	Condition isFull, isEmpty;
	
	public Buffer (int capacity) {
		buffer = new ArrayList<Integer>();
		this.capacity = capacity;
		mutex = new ReentrantLock();
		isFull = mutex.newCondition();
		isEmpty = mutex.newCondition();
	}
	public void put(Integer value) throws InterruptedException {
		mutex.lock();
		while (buffer.size() == capacity) {
			isFull.await();
		}
		buffer.add(value);
		isEmpty.signalAll();
		mutex.unlock();
	}
	public Integer get() throws InterruptedException {
		Integer value = null;
		mutex.lock();
		while (buffer.size() == 0) {
			isEmpty.await();
		}
		value = buffer.remove(0);
		isFull.signalAll();
		mutex.unlock();
		return value;
	}
	
	public boolean isEmpty() {
		return (buffer.size() == 0);
	}
}
