import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Courier extends Thread{

	PostOffice postOffice;
	int id;
	
	public Courier(int id, PostOffice postOffice) {
		this.postOffice = postOffice;
		this.id = id;
	}
	
	@Override
	public void run() {	
		while(!postOffice.getEndCheckMail() || !postOffice.getBufferTrainingIsEmpty()) {
			try {
				try {
					postOffice.courierAction(id);
				} catch (AddressException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (MessagingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("----------------------- Courier enter: " + id + "-----------------------");
	}
}
