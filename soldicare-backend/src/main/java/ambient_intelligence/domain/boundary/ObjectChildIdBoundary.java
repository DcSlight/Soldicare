package ambient_intelligence.domain.boundary;

import ambient_intelligence.id.ChildID;

public class ObjectChildIdBoundary {
	private ChildID childId;
	
	public ObjectChildIdBoundary() {}
	
	public ObjectChildIdBoundary(ChildID childId) {
		this.childId = childId;
	}

	public ChildID getChildId() {
		return childId;
	}

	public void setChildId(ChildID childId) {
		this.childId = childId;
	}

	@Override
	public String toString() {
		return "ObjectChildIdBoundary [childId=" + childId + "]";
	}
	
}
