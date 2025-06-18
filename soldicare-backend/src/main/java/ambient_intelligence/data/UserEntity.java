package ambient_intelligence.data;

import org.springframework.data.mongodb.core.mapping.Document;

import org.springframework.data.annotation.Id;

@Document(collection = "USERS")
public class UserEntity {
	@Id
	private String systemIDEmail;
	private UserRole role;
	private String username;
	private String avatar;

	public UserEntity() {
	}

	public UserEntity(String email, String systemID, UserRole role, String username, String avatar) {
		this.systemIDEmail = systemID + '#' + email;
		this.role = role;
		this.username = username;
		this.avatar = avatar;
	}

	public String getSystemIdAndEmail() {
		return this.systemIDEmail;
	}

	public void setSystemIdAndEmail(String emailAndSystemId) {
		this.systemIDEmail = emailAndSystemId;
	}

	public String getEmail() {
		return this.systemIDEmail.split("#")[1];
	}

	public void setEmail(String email) {
		String systemID = this.systemIDEmail != null ? getSystemID() : "";
		this.systemIDEmail = systemID + "#" + email;
	}

	public String getSystemID() {
		return this.systemIDEmail.split("#")[0];
	}

	public void setSystemID(String systemID) {
		String email = this.systemIDEmail != null ? getEmail() : "";
		this.systemIDEmail = systemID + "#" + email;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
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
		return "UserEntity [email=" + getEmail() + ", systemID=" + getSystemID() + ", role=" + role + ", username="
				+ username + ", avatar=" + avatar + "]";
	}

}
