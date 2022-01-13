import java.sql.Date;

public class Training_session {
    int training_session_id, training_id;
	Date training_session_date;

	public Training_session(int training_session_id, int training_id, Date training_session_date) {
		this.training_session_id = training_session_id;
		this.training_id = training_id;
        this.training_session_date = training_session_date;
	}

    public int getTraining_session_Id() {
		return training_session_id;
	}

    public int getTrainingId() {
		return training_id;
	}

    public Date getTraining_session_date() {
		return training_session_date;
	}

	@Override
	public String toString() {
		return "Training_session [training_session_id=" + training_session_id +
         ", training_id=" + training_id + ", training_session_date=" + training_session_date + "]";
	} 
}

