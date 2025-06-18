package ambient_intelligence.domain.boundary;

import java.util.Date;
import java.util.Map;

import ambient_intelligence.data.CommandEntity;
import ambient_intelligence.id.CommandID;
import ambient_intelligence.id.InvokedBy;
import ambient_intelligence.id.TargetObject;

public class CommandBoundary {
	private CommandID id;
	private String command;
	private TargetObject targetObject;
	private Date invocationTimestamp;
	private InvokedBy invokedBy;
	private Map<String, Object> commandAttributes;
	
	public CommandBoundary() {}

	public CommandBoundary(CommandEntity command) {
		this.id = command.getId();
		this.command = command.getCommand();
		this.targetObject = command.getTargetObject();
		this.invocationTimestamp = command.getInvocationTimestamp();
		this.invokedBy = command.getInvokedBy();
		this.commandAttributes = command.getCommandAttributes();
	}

	public CommandID getId() {
		return id;
	}

	public void setId(CommandID id) {
		this.id = id;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public TargetObject getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(TargetObject targetObject) {
		this.targetObject = targetObject;
	}

	public Date getInvocationTimestamp() {
		return invocationTimestamp;
	}

	public void setInvocationTimestamp(Date invocationTimestamp) {
		this.invocationTimestamp = invocationTimestamp;
	}

	public InvokedBy getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(InvokedBy invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}

	@Override
	public String toString() {
		return "CommandBoundary [id=" + id + ", command=" + command + ", targetObject=" + targetObject
				+ ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy + ", commandAttributes="
				+ commandAttributes + "]";
	}

}
