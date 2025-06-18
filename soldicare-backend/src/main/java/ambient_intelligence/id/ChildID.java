package ambient_intelligence.id;

public class ChildID {
	private String objectId;
	private String systemID;
	
	public ChildID() {}
	
	public ChildID(String objectId , String systemID) {
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
		return "ChildID [objectId=" + objectId + ", systemID=" + systemID + "]";
	}
	
}
