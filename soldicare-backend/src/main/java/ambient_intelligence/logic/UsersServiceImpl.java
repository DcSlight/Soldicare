package ambient_intelligence.logic;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import ambient_intelligence.data.UserEntity;
import ambient_intelligence.data.UserRole;
import ambient_intelligence.domain.boundary.UserBoundary;
import ambient_intelligence.exception.BadRequestException;
import ambient_intelligence.exception.NotFoundException;
import ambient_intelligence.exception.UnauthorizedException;
import ambient_intelligence.id.UserID;
import ambient_intelligence.repository.UserRepository;
import ambient_intelligence.utils.UserConverter;

@Service("usersService")
public class UsersServiceImpl implements UsersService {
	private final UserRepository repository;
	private String springApplicationName;
	private final UserConverter userConverter;

	public UsersServiceImpl(UserRepository repository, UserConverter userConverter) {
		this.repository = repository;
		this.userConverter = userConverter;
	}

	@Value("${spring.application.name:dummyApplication}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
	}

	@Override
	public UserBoundary createUser(UserBoundary user) {
		String email = user.getUserId().getEmail();
		if (email == null) {
			throw new BadRequestException("Email must not be null");
		}

		// Basic email validation regex
		String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$";
		if (!email.matches(emailRegex)) {
			throw new BadRequestException("Invalid email format: " + email);
		}

		// Username validation
		if (user.getUsername() == null || user.getUsername().trim().isEmpty()) {
			throw new BadRequestException("Username must not be null or empty");
		}

		// Avatar validation
		if (user.getAvatar() == null || user.getAvatar().trim().isEmpty()) {
			throw new BadRequestException("Avatar must not be null or empty");
		}

		user.setUserId(new UserID(user.getUserId().getEmail(), springApplicationName));
		UserEntity userEntity = this.userConverter.toEntity(user);
		UserEntity savedEntity = repository.save(userEntity);
		return this.userConverter.toBoundary(savedEntity);
	}

	@Override
	public Optional<UserBoundary> login(String systemID, String userEmail) {
		// Fix: construct emailSystemId in the correct order to match how it's stored
		String systemIdEmail = systemID + '#' + userEmail;
		UserEntity user = repository.findBySystemIDEmail(systemIdEmail).orElseThrow(
				() -> new NotFoundException("User not found with SystemId: " + systemID + " and email: " + userEmail));
		return Optional.of(new UserBoundary(user));
	}

	@Override
	@Transactional(readOnly = true)
	public List<UserBoundary> getAllUsers(String userSystemID, String userEmail, int size, int page) {
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.repository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("Only ADMIN can get all users");
		}
		
		return this.repository.findAll(PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "userId"))
				.map(entity -> userConverter.toBoundary(entity)).toList();
	}

	@Override
	public void updateUser(String systemID, String userEmail, UserBoundary update) {
		if (update == null) {
			throw new BadRequestException("User data must not be null");
		}

		Optional<UserBoundary> userBoundary = login(systemID, userEmail);
		if (userBoundary == null) {
			throw new NotFoundException("User not found");
		}
		// Fetch existing user entity
		UserEntity existingUser = userConverter.toEntity(userBoundary.get());

		// Update fields if not null
		if (update.getUsername() != null) {
			if (update.getUsername().trim().isEmpty()) {
				throw new BadRequestException("Username must not be empty");
			}
			existingUser.setUsername(update.getUsername());
		}

		if (update.getAvatar() != null) {
			if (update.getAvatar().trim().isEmpty()) {
				throw new BadRequestException("Avatar must not be empty");
			}
			existingUser.setAvatar(update.getAvatar());
		}

		if (update.getRole() != null)
			existingUser.setRole(UserRole.valueOf(update.getRole()));

		// Save and return updated boundary
		repository.save(existingUser);
	}

	@Override
	public void deleteAllUsers(String userSystemID, String userEmail) {
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.repository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("Only ADMIN can delete all users");
		}
		
		repository.deleteAll();
	}
}
