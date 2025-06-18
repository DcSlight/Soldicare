package ambient_intelligence.domain.boundary;

import ambient_intelligence.data.UserEntity;
import ambient_intelligence.id.UserID;

public class UserBoundary{
	private UserID userId;
	private String role;
	private String username;
	private String avatar;
	
	public UserBoundary() {}
	
	public UserBoundary(UserEntity userEntity) {
		UserID userId = new UserID();
		userId.setEmail(userEntity.getEmail());
		userId.setSystemID(userEntity.getSystemID());
		this.userId=userId;
		this.role=userEntity.getRole().toString();
		this.username=userEntity.getUsername();
		this.avatar=userEntity.getAvatar();
	}

	public UserID getUserId() {
		return userId;
	}

	public void setUserId(UserID userId) {
		this.userId = userId;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "UserBoundary [userId=" + userId + ", role=" + role + ", username=" + username + ", avatar=" + avatar
				+ "]";
	}
}
