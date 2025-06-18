package ambient_intelligence.logic;

import java.util.List;
import java.util.Optional;

import ambient_intelligence.domain.boundary.ObjectBoundary;

public interface SearchingObjectService extends ObjectsService {
	
	List<ObjectBoundary> getAllObjects(String systemID, String email, int size, int page);
	
	Optional<ObjectBoundary> getSpecificObject(String objectSystemID, String objectId, String userSystemID,
			String userEmail);
	
	List<ObjectBoundary> getChildren(String parentSystemID, String parentObjectId, String userSystemID,
			String userEmail, int size, int page);

	List<ObjectBoundary> getParents(String childSystemID, String childObjectId, String userSystemID, String userEmail,
			int size, int page);
	
	List<ObjectBoundary> searchByExactAlias(String systemID, String email, String alias, int size, int page);
	
	List<ObjectBoundary> searchByAliasPattern(String systemID, String email, String pattern, int size, int page);
	
	List<ObjectBoundary> searchByType(String systemID, String email, String type, int size, int page);
	
	List<ObjectBoundary> searchByStatus(String systemID, String email, String status, int size, int page);
	
	List<ObjectBoundary> searchByTypeAndStatus(String systemID, String email, String type, String status, int size, int page);
}
