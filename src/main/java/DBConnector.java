import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class DBConnector {

	private static Connection generateConnnection() throws FileNotFoundException, IOException, SQLException {
		Properties properties = new Properties();
		properties.load(new FileInputStream(new File("properties/DBsettings.properties")));
		Connection connection = DriverManager.getConnection(properties.getProperty("URL"),
				properties.getProperty("USERNAME"), properties.getProperty("PASSWORD"));
		return connection;
	}

	public static Deck loadDeck(int deckId) {
		Deck deck = null;
		try {
			Connection connection = generateConnnection();

			String sql = "SELECT title FROM deck where deck_id = " + deckId;

			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				deck = new Deck(result.getString(1));
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return deck;
	}

	public static User loadUser(int userId) {
		User user = null;
		try {
			Connection connection = generateConnnection();

			String sql = "SELECT username, email FROM user where user_id = " + userId;

			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				user = new User(result.getString(1), result.getString(2));
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return user;
	}

	public static Integer loadMinBox(int training_session_id) {
		Integer boxNumber = -1;
		try {
			Connection connection = generateConnnection();

			String sql = "SELECT min(box_number) FROM results WHERE training_session_id = " + training_session_id;

			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				boxNumber = result.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return boxNumber;
	}

	public static Training_session loadTraining_session(int training_id) {
		Training_session training_session = null;
		try {
			Connection connection = generateConnnection();

			String sql = "SELECT * FROM training_session WHERE training_id = " + training_id + " order by training_session_date limit 1";
			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				training_session = new Training_session(result.getInt(1), result.getInt(2), result.getDate(3));
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return training_session;
	}

	public static int countUsers() {
		int userId = -1;
		try {
			Connection connection = generateConnnection();

			String sql = "SELECT count(user_id) FROM user";
			Statement statement = connection.createStatement();

			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				userId = result.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userId;
	}

	public static Integer loadUserID(int cuantity) {
		int userID = -1;
		try {
			Connection connection = generateConnnection();

			String sql = "SELECT user_id FROM user limit 1 offset " + cuantity;
			Statement statement = connection.createStatement();
			ResultSet result = statement.executeQuery(sql);

			while (result.next()) {
				userID = result.getInt(1);
			}
			connection.close();
		} catch (SQLException e) {

			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return userID;
	}

	public static List<Training> loadTrainings(int id) {
		List<Training> trainings = new ArrayList<Training>();
		try {
			Connection connection = generateConnnection();

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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return trainings;
	}

}
