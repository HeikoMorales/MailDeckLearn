
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;

public class Buffer {
	
	int capacity;
	List<Integer> buffer;
	Semaphore empty, full;
	Semaphore mutex;
	
	public Buffer (int capacity) {
		buffer = new ArrayList<Integer>();
		this.capacity = capacity;
		full = new Semaphore(capacity);
		empty = new Semaphore (0);
		mutex = new Semaphore (1);
	}
	public void put(Integer action) throws InterruptedException {
		
		
		full.acquire();
		mutex.acquire();
		buffer.add(action);
		mutex.release();
		empty.release();
		
	}
	public Integer get() throws InterruptedException {
		Integer action = null;
		
		empty.acquire();
		mutex.acquire();
		action = buffer.remove(0);
		mutex.release();
		full.release();
		
		
		return action;
	}
	public boolean isEmpty() {
		
		return (buffer.size() == 0);
	}
}
