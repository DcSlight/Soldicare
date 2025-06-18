package ambient_intelligence.utils;

import org.springframework.stereotype.Component;

import ambient_intelligence.data.UserEntity;
import ambient_intelligence.data.UserRole;
import ambient_intelligence.domain.boundary.UserBoundary;
import ambient_intelligence.id.UserID;

@Component
public class UserConverter {
	
	public UserEntity toEntity(UserBoundary userBoundary) {
		UserEntity entity = new UserEntity();
		entity.setEmail(userBoundary.getUserId().getEmail());
		entity.setSystemID(userBoundary.getUserId().getSystemID());
		entity.setRole(UserRole.fromString(userBoundary.getRole().toString()));
		entity.setUsername(userBoundary.getUsername());
		entity.setAvatar(userBoundary.getAvatar());
		return entity;
	}
	
	public UserBoundary toBoundary(UserEntity userEntity) {
		UserBoundary boundary = new UserBoundary();
		UserID userId = new UserID(userEntity.getEmail(), userEntity.getSystemID());
		boundary.setUserId(userId);
		boundary.setRole(userEntity.getRole().toString());
		boundary.setUsername(userEntity.getUsername());
		boundary.setAvatar(userEntity.getAvatar());
		return boundary;
	}
}
