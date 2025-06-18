package ambient_intelligence.logic;

import java.util.List;
import java.util.Optional;

import ambient_intelligence.domain.boundary.UserBoundary;

public interface UsersService {
	UserBoundary createUser(UserBoundary user);

	Optional<UserBoundary> login(String systemID, String userEmail);

	void updateUser(String systemID, String userEmail, UserBoundary update);

	List<UserBoundary> getAllUsers(String systemID, String email, int size, int page);

	void deleteAllUsers(String systemID, String userEmail);
}
