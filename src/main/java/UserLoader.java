
public class UserLoader extends Thread {

	int id;
	PostOffice postOffice;

	public UserLoader(Integer id, PostOffice postOffice) {
		this.postOffice = postOffice;
		this.id = id;
	}

	@Override
	public void run() {
		while (postOffice.getEndUserLoaders() < postOffice.getDBUsers()) {
			System.out.println("hilo: " + id + " postOffice.getEndUserLoaders(); " + postOffice.getEndUserLoaders());
			try {

				postOffice.userLoaderAction(id);

			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		postOffice.setEndCheckMail(true);
		System.out.println("hilo: " + id + " sale del bucle");
	}
}
