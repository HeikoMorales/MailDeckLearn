
public class UserLoader extends Thread{

	int id;
	PostOffice postOffice;
	
	public UserLoader(Integer id, PostOffice postOffice) {
		this .postOffice = postOffice;
		this.id = id;
	}
	
	@Override
	public void run() {
		//while (!this.isInterrupted()) {
			try {
				postOffice.userLoaderAction(id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		//}
	}
}
