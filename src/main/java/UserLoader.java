
public class UserLoader extends Thread {

	int id;
	PostOffice postOffice;

	public UserLoader(Integer id, PostOffice postOffice) {
		this.postOffice = postOffice;
		this.id = id;
	}

	@Override
	public void run() {
		while (!postOffice.getEndUserId()|| !postOffice.getBufferUserIdIsEmpty()) {
			try {
				postOffice.userLoaderAction(id);
			} catch (InterruptedException e) {
				System.out.println(id);
				//e.printStackTrace();
			}
		}
		postOffice.setEndCheckMail(true);
		System.out.println("----------------------- UserLoader exit: " + id + "-----------------------");

	}
}
