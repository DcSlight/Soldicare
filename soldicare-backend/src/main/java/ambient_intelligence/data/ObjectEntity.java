package ambient_intelligence.data;

import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import ambient_intelligence.id.CreatedBy;
import ambient_intelligence.id.ObjectID;

@Document(collection = "OBJECTS")
public class ObjectEntity {
	@Id
	private ObjectID id;
	private String alias;
	private String status;
	private String type;
	private boolean active;
	private Date creationTimestamp;
	private CreatedBy createdBy;
	private Map<String, Object> objectDetails;

	// Using DBRef to reference other ObjectEntity documents
	@DBRef(lazy = true)
	private Set<ObjectEntity> parents = new HashSet<>();

	@DBRef(lazy = true)
	private Set<ObjectEntity> children = new HashSet<>();

	public ObjectEntity() {
	}

	public ObjectEntity(ObjectID id, String type, String alias, String status, Boolean active,
			Date creationTimestamp, CreatedBy createdBy) {
		this.id = id;
		this.type = type;
		this.alias = alias;
		this.status = status;
		this.active = active;
		this.creationTimestamp = creationTimestamp;
		this.createdBy = createdBy;
	}

	public void addParent(ObjectEntity parent) {
		this.parents.add(parent);
		parent.children.add(this); // manually add this object to the parent's children
	}

	public void removeParent(ObjectEntity parent) {
		this.parents.remove(parent);
		parent.children.remove(this); // remove this object from the parent's children
	}

	public void addChild(ObjectEntity child) {
		this.children.add(child);
		child.parents.add(this); // ensure child also knows about the parent
	}

	public void removeChild(ObjectEntity child) {
		this.children.remove(child);
		child.parents.remove(this); // bidirectional removal
	}

	// Getters and setters
	public Set<ObjectEntity> getParents() {
		return parents;
	}

	public void setParents(Set<ObjectEntity> parents) {
		this.parents = parents;
	}

	public Set<ObjectEntity> getChildren() {
		return children;
	}

	public void setChildren(Set<ObjectEntity> children) {
		this.children = children;
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
		return "ObjectEntity [id=" + id + ", type=" + type + ", alias=" + alias + ", status=" + status + ", active="
				+ active + ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + ", parents=" + parents + ", children=" + children + "]";
	}
}
