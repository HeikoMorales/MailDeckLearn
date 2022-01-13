import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import static java.time.temporal.ChronoUnit.DAYS;

public class PostOffice {

	MailSender mail;

	Buffer bufferUserId;
	BufferTraining bufferTraining;

	Lock mutex;
	Lock mutexCourier;

	int bufferCapacity = 20;
	int endUserLoaders = 0, endCheckMail = 0;
	int countUsers = 0;

	public PostOffice() {
		bufferUserId = new Buffer(bufferCapacity);
		bufferTraining = new BufferTraining(bufferCapacity);
		mutexCourier = new ReentrantLock();
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
			List<Training> trainingMail = new ArrayList<Training>();

			for (Training training : trainings) {
				if (checkMail(training)) {
					//System.out.println("necesario mandar correo");
					trainingMail.add(training);
				}
			}

			if (trainingMail.size() != 0) {
					System.out.println(trainingMail.size());
					bufferTraining.put(trainingMail);
			}
			endUserLoaders++;
		}
		mutex.unlock();
		System.out.println("----------------------- UserLoader number: " + id + " -----------------------");
	}

	private boolean checkMail(Training training) {
		boolean needMail = false;

		Training_session training_session = DBConnector.loadTraining_session(training.getTrainingId());
		if (training_session != null) {

			int box_number = DBConnector.loadMinBox(training_session.training_session_id);
			if (box_number != 0) {
				//System.out.println(training.toString() + " " + training_session.toString() + " box Number: " + box_number);

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

	public void courierAction(int id) throws InterruptedException {
		//System.out.println("----------------------- Courier number: " + id + " -----------------------");
		mutexCourier.lock();
		while (endCheckMail < countUsers) {
			List<Training> trainingsMail = bufferTraining.get();
			for (Training training : trainingsMail) {
				System.out.println(training.getTrainingId());
			}
			
			endCheckMail++;
		}
		mutexCourier.unlock();
		System.out.println("----------------------- Courier number: " + id + " -----------------------");
	}
	//Se boklea por el bucle del while crear una variable ended que este protegida en un metodo fuera por el mutex 1
}
