package ambient_intelligence.domain.boundary;

import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;

import ambient_intelligence.id.CreatedBy;
import ambient_intelligence.id.ObjectID;

public class ObjectBoundary {

	@Id
	protected ObjectID id;
	protected String type;
	protected String alias;
	protected String status;
	protected Boolean active;
	protected Date creationTimestamp;
	protected CreatedBy createdBy;
	protected Map<String, Object> objectDetails;

	public ObjectBoundary() {
	}

	public ObjectBoundary(ObjectID id, String type, String alias, String status, Boolean active,
			Date creationTimestamp, CreatedBy createdBy, Map<String, Object> objectDetails) {
		super();
		this.id = id;
		this.type = type;
		this.alias = alias;
		this.status = status;
		this.active = active;
		this.creationTimestamp = creationTimestamp;
		this.createdBy = createdBy;
		this.objectDetails = objectDetails;
	}

	public ObjectID getId() {
		return id;
	}

	public void setId(ObjectID id) {
		this.id = id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public CreatedBy getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(CreatedBy createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	@Override
	public String toString() {
		return "BaseBoundary [id=" + id + ", type=" + type + ", alias=" + alias + ", status=" + status + ", active="
				+ active + ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + "]";
	}

}
