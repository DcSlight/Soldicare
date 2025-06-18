package ambient_intelligence.logic;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import ambient_intelligence.data.UserEntity;
import ambient_intelligence.data.UserRole;
import ambient_intelligence.domain.boundary.CommandBoundary;
import ambient_intelligence.exception.NotFoundException;
import ambient_intelligence.exception.UnauthorizedException;
import ambient_intelligence.id.CommandID;
import ambient_intelligence.id.ObjectID;
import ambient_intelligence.id.TargetObject;
import ambient_intelligence.repository.CommandRepository;
import ambient_intelligence.repository.UserRepository;
import ambient_intelligence.utils.CommandConverter;

@Service("commandsService")
public class CommandsServiceImpl implements CommandsService {

	private final CommandRepository commandRepository;
	private final UserRepository userRepository;
	private String springApplicationName;
	private final CommandConverter commandConverter;

	public CommandsServiceImpl(CommandRepository commandRepository, CommandConverter commandConverter,
			UserRepository userRepository) {
		this.commandRepository = commandRepository;
		this.commandConverter = commandConverter;
		this.userRepository = userRepository;
	}

	@Value("${spring.application.name:dummyApplication}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
	}

	@Override
	public List<Object> invokeCommand(CommandBoundary command) {
		String userSystemID = command.getInvokedBy().getUserId().getSystemID();
		String userEmail = command.getInvokedBy().getUserId().getEmail();
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!UserRole.END_USER.equals(role)) {
			throw new UnauthorizedException("Only END_USER can create an object");
		}

		// Generate a unique command ID (UUID-based)
		String commandId = UUID.randomUUID().toString();
		command.setId(new CommandID(commandId, springApplicationName));

		// Generate target object ID similarly
		String targetObjectId = UUID.randomUUID().toString();
		TargetObject targetObject = new TargetObject(new ObjectID(targetObjectId, springApplicationName));
		command.setTargetObject(targetObject);

		command.setInvocationTimestamp(new Date());

		commandRepository.save(commandConverter.toEntity(command));

		return List.of(Map.of("status", "Command received", "command", command.getCommand()));
	}

	@Override
	public List<CommandBoundary> getAllCommandsHistory(String userSystemID, String userEmail, int size, int page) {
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!"ADMIN".equals(role.toString())) {
			throw new UnauthorizedException("Only ADMIN can get all commands");
		}

		return StreamSupport.stream(commandRepository
				.findAll(PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "commandId")).spliterator(),
				false).map(CommandBoundary::new).collect(Collectors.toList());
	}

	@Override
	public void deleteAllCommands(String userSystemID, String userEmail) {
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!"ADMIN".equals(role.toString())) {
			throw new UnauthorizedException("Only ADMIN can delete all commands");
		}
		commandRepository.deleteAll();
	}

	@Override
	public List<CommandBoundary> getAllAlertCriticalCommands(String userSystemID, String userEmail, int size,
			int page) {
		return this.commandRepository
				.findByCommand("ALERT_CRITICAL_VALUES",
						PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "commandId"))
				.stream().map(CommandBoundary::new).collect(Collectors.toList());
	}
}
