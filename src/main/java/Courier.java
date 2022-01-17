
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Courier extends Thread {

	PostOffice postOffice;
	int id;

	public Courier(int id, PostOffice postOffice) {
		this.postOffice = postOffice;
		this.id = id;
	}

	@Override
	public void run() {
		
		while (!postOffice.getEndCheckMail() || !postOffice.getBufferTrainingIsEmpty()) {
			
			try {
				// System.out.println("----------------------- Courier enter: " + id
				// +"-----------------------");
				postOffice.courierAction(id);
			} catch (AddressException e) {
				e.printStackTrace();
			} catch (MessagingException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		}
		System.out.println("----------------------- Courier Exit: " + id +"-----------------------");
	}
}
