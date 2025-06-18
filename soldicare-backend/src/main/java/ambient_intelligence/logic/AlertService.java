package ambient_intelligence.logic;

import java.util.List;

import ambient_intelligence.domain.boundary.ObjectBoundary;

public interface AlertService {
	List<ObjectBoundary> getAll(String userSystemID, String userEmail,int size,int page);
}