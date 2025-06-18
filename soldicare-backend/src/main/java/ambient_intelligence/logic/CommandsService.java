package ambient_intelligence.logic;

import java.util.List;

import ambient_intelligence.domain.boundary.CommandBoundary;

public interface CommandsService {
	List<Object> invokeCommand(CommandBoundary command);
	List<CommandBoundary> getAllCommandsHistory(String systemID, String email, int size, int page);
	void deleteAllCommands(String systemID, String userEmail);
	public List<CommandBoundary> getAllAlertCriticalCommands(String userSystemID, String userEmail, int size, int page);
}
