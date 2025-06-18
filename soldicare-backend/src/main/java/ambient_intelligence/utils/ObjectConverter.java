package ambient_intelligence.utils;

import org.springframework.stereotype.Component;

import ambient_intelligence.data.ObjectEntity;
import ambient_intelligence.domain.boundary.ObjectBoundary;

@Component
public class ObjectConverter {

	public ObjectBoundary toBoundary(ObjectEntity entity) {
		ObjectBoundary boundary = new ObjectBoundary();
		if (entity.getId() != null)
			boundary.setId(entity.getId());

		boundary.setType(entity.getType().toString());
		boundary.setAlias(entity.getAlias());
		if (entity.getStatus() != null)
			boundary.setStatus(entity.getStatus());
		else
			boundary.setStatus("INACTIVE");
		boundary.setActive(entity.getActive());
		boundary.setCreationTimestamp(entity.getCreationTimestamp());
		boundary.setCreatedBy(entity.getCreatedBy());
		boundary.setObjectDetails(entity.getObjectDetails());
		return boundary;
	}

	public ObjectEntity toEntity(ObjectBoundary boundary) {
		ObjectEntity rv = new ObjectEntity();
		if (boundary.getId() != null)
			rv.setId(boundary.getId());
		rv.setType(boundary.getType());
		rv.setAlias(boundary.getAlias());
		if (boundary.getStatus() != null)
			rv.setStatus(boundary.getStatus());
		else
			rv.setStatus("INACTIVE");
		rv.setActive(boundary.getActive());
		rv.setCreationTimestamp(boundary.getCreationTimestamp());
		rv.setCreatedBy(boundary.getCreatedBy());
		rv.setObjectDetails(boundary.getObjectDetails());
		return rv;
	}

}
