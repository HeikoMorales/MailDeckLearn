import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;

public class Main {

	PostOffice postOffice;

	List<UserLoader> userLoaderList;
	List<Courier> courierList;
	List<UserIdLoader> userIdLoaderList;

	int userLoaderTherads = 20;
	int courierTherads = 20;
	int userIdLoaderTherads = 1;
	long startTime;

	public Main() {
		startTime = System.nanoTime();
		postOffice = new PostOffice();
		userLoaderList = new ArrayList<UserLoader>();
		courierList = new ArrayList<Courier>();
		userIdLoaderList = new ArrayList<UserIdLoader>();

	}

	public void execute() throws InterruptedException {
		initTheads();
		runTherads();
		joinTheads();

		long endTime = System.nanoTime();
		long totalTime = endTime - startTime;
		System.out.println(totalTime);
	}

	private void joinTheads() throws InterruptedException {
		for (int i = 0; i < userLoaderTherads; i++) {
			userLoaderList.get(i).join();
		}

		for (int i = 0; i < userIdLoaderTherads; i++) {
			userIdLoaderList.get(i).join();
		}

		for (int i = 0; i < courierTherads; i++) {
			courierList.get(i).join();
		}
	}

	private void runTherads() {
		for (int i = 0; i < userLoaderTherads; i++) {
			userLoaderList.get(i).start();
		}

		for (int i = 0; i < userIdLoaderTherads; i++) {
			userIdLoaderList.get(i).start();
		}

		for (int i = 0; i < courierTherads; i++) {
			courierList.get(i).start();
		}
	}

	private void initTheads() {

		for (int i = 0; i < userLoaderTherads; i++) {
			userLoaderList.add(i, new UserLoader(i, postOffice));
		}

		for (int i = 0; i < userIdLoaderTherads; i++) {
			userIdLoaderList.add(i, new UserIdLoader(postOffice, i));
		}

		for (int i = 0; i < courierTherads; i++) {
			courierList.add(i, new Courier(i, postOffice));
		}
	}

	public static void main(String[] args)
			throws IOException, AddressException, MessagingException, InterruptedException {

		Main main = new Main();
		main.execute();

	}
}
