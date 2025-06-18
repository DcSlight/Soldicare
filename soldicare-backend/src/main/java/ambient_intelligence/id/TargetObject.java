package ambient_intelligence.id;

public class TargetObject {
	private ObjectID id;

	public TargetObject() {
	}

	public TargetObject(ObjectID id) {
		this.id = id;
	}

	public ObjectID getId() {
		return id;
	}

	public void setId(ObjectID id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "TargetObject [id=" + id + "]";
	}
}
