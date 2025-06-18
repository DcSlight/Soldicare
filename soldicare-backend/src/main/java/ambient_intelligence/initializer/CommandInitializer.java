package ambient_intelligence.initializer;

import java.util.Date;

import org.springframework.boot.CommandLineRunner;

import org.springframework.stereotype.Component;

import ambient_intelligence.domain.boundary.CommandBoundary;
import ambient_intelligence.id.InvokedBy;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.CommandsService;

@Component
public class CommandInitializer implements CommandLineRunner {

	private final CommandsService commandService;

	public CommandInitializer(CommandsService commandService) {
		this.commandService = commandService;
	}

	@Override
	public void run(String... args) throws Exception {
		for (int i = 1; i <= 3; i++) {
			CommandBoundary command = new CommandBoundary();
			command.setCommand("INIT_COMMAND_" + i);
			command.setInvocationTimestamp(new Date());
			UserID userId = new UserID();
			userId.setEmail("jack@demo.org");
			userId.setSystemID("2025b.Itay.Chabra");
			InvokedBy invokedBy = new InvokedBy(userId);
			command.setInvokedBy(invokedBy);
			
			// Execute the command using the service
			Object response = commandService.invokeCommand(command);
			System.out.println("Initialized Command " + i + ": " + response);
		}
	}
}
