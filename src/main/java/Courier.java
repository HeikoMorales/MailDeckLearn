
public class Courier extends Thread{

	PostOffice postOffice;
	int id;
	
	public Courier(int id, PostOffice postOffice) {
		this.postOffice = postOffice;
		this.id = id;
	}
	
	@Override
	public void run() {
		//while (!this.isInterrupted()) {
			try {
				postOffice.courierAction(id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
}
