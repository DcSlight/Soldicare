package ambient_intelligence.data;

import ambient_intelligence.exception.BadRequestException;

public enum UserRole {
	ADMIN , OPERATOR , END_USER;
	
	public static UserRole fromString(String type) {
		try {
			UserRole userRole = UserRole.valueOf(type);
			return userRole;
		} catch (IllegalArgumentException e) {
			throw new BadRequestException("Unknown user type: " + type);
		}
	}
}
