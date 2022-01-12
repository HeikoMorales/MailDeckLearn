import java.sql.Date;
import java.time.LocalDate;

public class Training {

	int training_id, user_id, deck_id;
	LocalDate date;

	public Training(int training_id, int user_id, int deck_id, Date date) {
		this.training_id = training_id;
		this.user_id = user_id;
		this.deck_id = deck_id;
		this.date = date.toLocalDate();
		
	}

	@Override
	public String toString() {
		return "Training [training_id=" + training_id + ", user_id=" + user_id + ", deck_id=" + deck_id + ", date="
				+ date + "]";
	}

}
