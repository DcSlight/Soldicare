package ambient_intelligence.id;

import java.io.Serializable;
import java.util.Objects;

public class CommandID implements Serializable {
	private static final long serialVersionUID = 1L;
	private String commandId;
	private String systemID;

	public CommandID() {
	}

	public CommandID(String commandId, String systemID) {
		this.commandId = commandId;
		this.systemID = systemID;
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (!(o instanceof CommandID))
			return false;
		CommandID that = (CommandID) o;
		return Objects.equals(commandId, that.commandId) && Objects.equals(systemID, that.systemID);
	}

	@Override
	public int hashCode() {
		return Objects.hash(commandId, systemID);
	}

	@Override
	public String toString() {
		return "CommandID{" + "commandId='" + commandId + '\'' + ", systemID='" + systemID + '\'' + '}';
	}
}
