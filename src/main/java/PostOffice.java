import java.sql.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PostOffice {

	MailSender mail;

	Buffer bufferUserId;

	Lock mutex;

	int bufferCapacity = 20;
	int endUserLoaders = 0;
	int countUsers = 0;

	public PostOffice() {
		bufferUserId = new Buffer(bufferCapacity);
		mutex = new ReentrantLock();
		mail = new MailSender();
		countUsers = DBConnector.countUsers();
	}

	public void UserIdLoaderAction(int id) throws InterruptedException {
		int userId;

		for (int i = 0; i < Math.ceil((float) countUsers); i++) {

			userId = DBConnector.loadUserID(i);

			// System.out.println(userId);
			bufferUserId.put(userId);

			// System.out.println("----------------------- UserIdLoaderAction number: " + id
			// + " Vuelta" + i + " -----------------------");
		}
		System.out.println("----------------------- UserIdLoaderAction number: " + id + " -----------------------");

	}

	public void userLoaderAction(int id) throws InterruptedException {

		mutex.lock();
		while (endUserLoaders < countUsers) {
			List<Training> trainings = DBConnector.loadTrainings(bufferUserId.get());

			for (Training training : trainings) {

				Date training_session_date = DBConnector.loadTraining_session_date(training.getTrainingId());
				if (training_session_date != null) {
					System.out
							.println(training.toString() + " training_Session_date: "
									+ training_session_date);
				}

			}
			endUserLoaders++;
			// System.out.println(endUserLoaders);
		}
		mutex.unlock();
		System.out.println("----------------------- UserLoader number: " + id + " -----------------------");
	}

	public void courierAction(int id) throws InterruptedException {
		// System.out.println("Courier number: " + id);

	}

}
