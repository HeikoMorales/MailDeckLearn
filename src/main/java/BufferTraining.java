
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class BufferTraining {
    int capacity;
	List<List<Training>> buffer;
	Lock mutex;
	Condition isFull, isEmpty;
	
	public BufferTraining (int capacity) {
		buffer = new ArrayList<List<Training>>();
		this.capacity = capacity;
		mutex = new ReentrantLock();
		isFull = mutex.newCondition();
		isEmpty = mutex.newCondition();
	}
	public void put(List<Training> value) throws InterruptedException {
		mutex.lock();
		while (buffer.size() == capacity) {
			isFull.await();
		}
		buffer.add(value);
		isEmpty.signalAll();
		mutex.unlock();
	}
	public List<Training> get() throws InterruptedException {
		List<Training> value = null;
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
