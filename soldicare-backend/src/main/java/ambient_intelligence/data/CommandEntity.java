package ambient_intelligence.data;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import ambient_intelligence.id.CommandID;
import ambient_intelligence.id.InvokedBy;
import ambient_intelligence.id.TargetObject;

@Document(collection = "COMMAND")
public class CommandEntity {
	@Id
	private CommandID id;
	private String command;
	private TargetObject targetObject;
	private Date invocationTimestamp;
	private InvokedBy invokedBy;

	private Map<String, Object> commandAttributes;

	public CommandEntity() {
	}

	public CommandEntity(CommandID id, String command, TargetObject targetObject, Date invocationTimestamp,
			InvokedBy invokedBy, Map<String, Object> commandAttributes) {
		this.id = id;
		this.command = command;
		this.targetObject = targetObject;
		this.invocationTimestamp = invocationTimestamp;
		this.invokedBy = invokedBy;
		this.commandAttributes = commandAttributes;
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
		return "CommandEntity [ ID=" + id + ", command=" + command + ", targetObject=" + targetObject
				+ ", invocationTimestamp=" + invocationTimestamp + ", invokedBy=" + invokedBy + ", commandAttributes="
				+ commandAttributes + "]";
	}

}
