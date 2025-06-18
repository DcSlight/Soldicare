package ambient_intelligence.logic;

import ambient_intelligence.domain.boundary.ObjectBoundary;

public interface ObjectsService {

	ObjectBoundary create(ObjectBoundary object);

	void updateObject(String systemID, String objectId, String userSystemID, String userEmail, ObjectBoundary update);

	void deleteAllObjects(String systemID, String userEmail);

	void bindObjects(String parentSystemID, String parentObjectId, String childSystemID, String childObjectId,
			String userSystemID, String userEmail);
}
