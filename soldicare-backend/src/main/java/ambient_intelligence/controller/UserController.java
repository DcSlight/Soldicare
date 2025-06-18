package ambient_intelligence.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import ambient_intelligence.data.UserRole;
import ambient_intelligence.domain.boundary.NewUserBoundary;
import ambient_intelligence.domain.boundary.UserBoundary;
import ambient_intelligence.exception.NotFoundException;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.UsersService;

@RestController
@RequestMapping(path = { "/ambient-intelligence/users" })
public class UserController {

	private final UsersService userService;

	public UserController(UsersService userService) {
		this.userService = userService;
	}

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary createNewUser(@RequestBody NewUserBoundary newUser) {
		UserBoundary userBoundary = new UserBoundary();
		userBoundary.setRole(newUser.getRole() != null ? UserRole.valueOf(newUser.getRole()).toString() : UserRole.END_USER.toString());
		userBoundary.setUsername(newUser.getUsername());
		userBoundary.setAvatar(newUser.getAvatar());
		//userBoundary.setUsername(newUser.getUsername() != null ? newUser.getUsername() : "Anonymous");
		//userBoundary.setAvatar(newUser.getAvatar() != null ? newUser.getAvatar() : ":-)");
		UserID userId = new UserID();
		userId.setEmail(newUser.getEmail());
		userBoundary.setUserId(userId);
		return userService.createUser(userBoundary);
	}

	@GetMapping(path = "/login/{systemID}/{userEmail}", produces = MediaType.APPLICATION_JSON_VALUE)
	public UserBoundary getById(@PathVariable("userEmail") String userEmail,
			@PathVariable("systemID") String systemID) {
		return userService.login(systemID, userEmail)
				.orElseThrow(() -> new NotFoundException("user with email: " + userEmail + " and systemId: " + systemID + " not found!"));
	}

	@PutMapping(path = "/{systemID}/{userEmail}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public void updateUser(@PathVariable("userEmail") String userEmail, @PathVariable("systemID") String systemID,
			@RequestBody UserBoundary updatedUser) {
		userService.updateUser(systemID, userEmail, updatedUser);
	}
}
