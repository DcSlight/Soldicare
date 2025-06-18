package ambient_intelligence.id;

import java.io.Serializable;
import java.util.Objects;

public class ObjectID implements Serializable {
	private static final long serialVersionUID = 1L;

	private String objectId;
	private String systemID;

	public ObjectID() {
	}

	public ObjectID(String objectId, String systemID) {
		this.objectId = objectId;
		this.systemID = systemID;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
	}

	public String getSystemID() {
		return systemID;
	}

	public void setSystemID(String systemID) {
		this.systemID = systemID;
	}

	@Override
	public String toString() {
		return "ObjectID [objectId=" + objectId + ", systemID=" + systemID + "]";
	}

	@Override
	public int hashCode() {
		return Objects.hash(objectId, systemID);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null || getClass() != obj.getClass())
			return false;
		ObjectID other = (ObjectID) obj;
		return Objects.equals(objectId, other.objectId) && Objects.equals(systemID, other.systemID);
	}
}
