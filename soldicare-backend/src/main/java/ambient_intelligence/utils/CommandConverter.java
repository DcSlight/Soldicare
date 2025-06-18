package ambient_intelligence.utils;

import org.springframework.stereotype.Component;

import ambient_intelligence.data.CommandEntity;
import ambient_intelligence.domain.boundary.CommandBoundary;

@Component
public class CommandConverter {
	
	public CommandEntity toEntity(CommandBoundary commandBoundary) {
		CommandEntity ce = new CommandEntity();
		ce.setId(commandBoundary.getId());
		ce.setCommand(commandBoundary.getCommand());
		ce.setTargetObject(commandBoundary.getTargetObject());
		ce.setInvocationTimestamp(commandBoundary.getInvocationTimestamp());
		ce.setInvokedBy(commandBoundary.getInvokedBy());
		ce.setCommandAttributes(commandBoundary.getCommandAttributes());
		return ce;
	}
	
	public CommandBoundary toBoundary(CommandEntity commandEntity) {
		CommandBoundary cb = new CommandBoundary();
		cb.setId(commandEntity.getId());
		cb.setCommand(commandEntity.getCommand());
		cb.setTargetObject(commandEntity.getTargetObject());
		cb.setInvocationTimestamp(commandEntity.getInvocationTimestamp());
		cb.setInvokedBy(commandEntity.getInvokedBy());
		cb.setCommandAttributes(commandEntity.getCommandAttributes());
		return cb;
	}
	
}
