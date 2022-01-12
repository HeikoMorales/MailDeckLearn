
public class UserIdLoader extends Thread{

	PostOffice postOffice;
	int id;
	
	public UserIdLoader(PostOffice postOffice, int id) {
		this.id = id;
		this.postOffice = postOffice;
	}
	
	@Override
	public void run() {
		//while (!this.isInterrupted()) {
			try {
				postOffice.UserIdLoaderAction(id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
}
