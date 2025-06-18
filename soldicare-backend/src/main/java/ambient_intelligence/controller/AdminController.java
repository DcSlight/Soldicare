package ambient_intelligence.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import ambient_intelligence.domain.boundary.CommandBoundary;
import ambient_intelligence.domain.boundary.UserBoundary;
import ambient_intelligence.logic.CommandsService;
import ambient_intelligence.logic.ObjectsService;
import ambient_intelligence.logic.UsersService;

@RestController
@RequestMapping(path = { "/ambient-intelligence/admin" })
public class AdminController {
	private final UsersService userService;
	private final ObjectsService objectService;
	private final CommandsService commandService;

	public AdminController(ObjectsService objectService, UsersService userService, CommandsService commandService) {
		this.userService = userService;
		this.commandService = commandService;
		this.objectService = objectService;
	}

	@DeleteMapping("/users")
	public void deleteAllUsers(@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail) {
		userService.deleteAllUsers(userSystemID, userEmail);
	}

	@DeleteMapping("/commands")
	public void deleteAllCommands(@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail) {
		commandService.deleteAllCommands(userSystemID, userEmail);
	}

	@DeleteMapping("/objects")
	public void deleteAllObjects(@RequestParam(name = "userSystemID") String userSystemID,
			@RequestParam(name = "userEmail") String userEmail) {
		objectService.deleteAllObjects(userSystemID, userEmail);
	}

	@GetMapping("/users")
	public List<UserBoundary> exportAllUsers(@RequestParam("userSystemID") String userSystemID,
			@RequestParam("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return userService.getAllUsers(userSystemID, userEmail, size, page);
	}

	@GetMapping("/commands")
	public List<CommandBoundary> exportAllCommands(@RequestParam("userSystemID") String userSystemID,
			@RequestParam("userEmail") String userEmail,
			@RequestParam(name = "size", required = false, defaultValue = "10") int size,
			@RequestParam(name = "page", required = false, defaultValue = "0") int page) {
		return commandService.getAllCommandsHistory(userSystemID, userEmail, size, page);
	}
}
