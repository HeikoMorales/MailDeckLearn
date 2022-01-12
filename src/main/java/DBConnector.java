import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBConnector {

	static String url = "jdbc:mysql://localhost:3306/deck_learn";
	static String username = "root";
	static String password = "Ikasle12345";

	public static int countUsers() {
		int userId = -1;
		try {
			Connection connection = DriverManager.getConnection(url, username, password);

			String sql = "SELECT count(user_id) FROM user";
			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				userId = result.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return userId;
	}

	public static Integer loadUserID(int cuantity) {
		int userID = -1;
		try {
			Connection connection = DriverManager.getConnection(url, username, password);

			String sql = "SELECT user_id FROM user limit 1 offset " + cuantity;
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				userID = result.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return userID;
	}

	public static List<Training> loadTrainings(int id) {
		List<Training> trainings = new ArrayList<Training>();
		try {
			Connection connection = DriverManager.getConnection(url, username, password);

			String sql = "SELECT * FROM training WHERE user_id = " + id;
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				Training training = new Training(result.getInt(1), result.getInt(2), result.getInt(3),
						result.getDate(4));

				trainings.add(training);
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		}
		return trainings;
	}

}
