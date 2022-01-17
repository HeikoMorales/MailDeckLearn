import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

import static java.time.temporal.ChronoUnit.DAYS;

import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;

public class PostOffice {

	Buffer bufferUserId;
	BufferTraining bufferTraining;

	Lock mutex;
	Lock mutexCourier;

	int bufferCapacity = 20;
	int endUserLoaders = 0;
	boolean endUserId = false, endCheckMail = false;
	boolean getTraining = false;
	int DBUsers, contUser = 0;

	public PostOffice() {
		bufferUserId = new Buffer(bufferCapacity);
		bufferTraining = new BufferTraining(bufferCapacity);
		mutexCourier = new ReentrantLock();
		mutex = new ReentrantLock();

		DBUsers = DBConnector.countUsers();
	}

	public void UserIdLoaderAction(int id) throws InterruptedException {
		int userId;

		while (contUser < DBUsers) {
			userId = DBConnector.loadUserID(contUser);
			bufferUserId.put(userId);
			contUser++;
		}
		endUserId = true;
		System.out.println("----------------------- UserIdLoaderAction number: " + id + " -----------------------");

	}

	public void userLoaderAction(int id) throws InterruptedException {

		// System.out.println("----------------------- UserLoader enter: " + id +
		// "-----------------------");

		List<Training> trainings = DBConnector.loadTrainings(bufferUserId.get());
		List<Training> trainingMail = new ArrayList<Training>();

		for (Training training : trainings) {
			if (checkMail(training)) {
				// System.out.println("necesario mandar correo");
				trainingMail.add(training);
			}
		}
		if (trainingMail.size() != 0) {
			bufferTraining.put(trainingMail);
		}
	}

	private boolean checkMail(Training training) {
		boolean needMail = false;

		Training_session training_session = DBConnector.loadTraining_session(training.getTrainingId());
		if (training_session != null) {

			int box_number = DBConnector.loadMinBox(training_session.training_session_id);
			if (box_number != 0) {
				// System.out.println(training.toString() + " " + training_session.toString() +
				// " box Number: " + box_number);

				LocalDate training_session_date = training_session.getTraining_session_date().toLocalDate();
				Long daysBetween = DAYS.between(training_session_date, LocalDate.now());
				for (int i = 0; i < 10; i++) {
					if (Math.pow(2, i) > daysBetween) {
						if (i >= box_number) {
							needMail = true;
							break;
						}
					}
				}

			}
		}
		return needMail;

	}

	public void courierAction(int id) throws InterruptedException, AddressException, MessagingException, UnsupportedEncodingException, FileNotFoundException {

		List<Training> trainingsMail = bufferTraining.get();
		List<Deck> decks = new ArrayList<Deck>();
		for (Training training : trainingsMail) {
			decks.add(DBConnector.loadDeck(training.getDeck_id()));
		}
		Thread.sleep(5000);
		 MailSender mail = new MailSender();
		 mail.sendEmail(DBConnector.loadUser(trainingsMail.get(0).getUser_id()),decks);
		 mail.closeTransport();

	}

	public void setEndCheckMail(boolean bool) {
		endCheckMail = bool;
	}

	public boolean getEndUserId() {
		return endUserId;
	}

	public boolean getBufferUserIdIsEmpty() {
		return bufferUserId.isEmpty();
	}

	public boolean getEndCheckMail() {
		return endCheckMail;
	}

	public boolean getBufferTrainingIsEmpty() {
		return bufferTraining.isEmpty();
	}
}
