package ambient_intelligence.id;

public class CreatedBy {
	private UserID userId;

	public CreatedBy() {
	}

	public CreatedBy(UserID userId) {
		this.userId = userId;
	}

	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		this.userId = userId;
	}

	@Override
	public String toString() {
		return "CreatedBy [userId=" + userId + "]";
	}
}
