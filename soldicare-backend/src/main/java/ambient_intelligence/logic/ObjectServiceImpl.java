package ambient_intelligence.logic;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

import ambient_intelligence.data.ObjectEntity;
import ambient_intelligence.data.UserEntity;
import ambient_intelligence.data.UserRole;
import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.exception.BadRequestException;
import ambient_intelligence.exception.NotFoundException;
import ambient_intelligence.exception.UnauthorizedException;
import ambient_intelligence.id.ObjectID;
import ambient_intelligence.repository.ObjectRepository;
import ambient_intelligence.repository.UserRepository;
import ambient_intelligence.utils.ObjectConverter;

@Service("ObjectService")
public class ObjectServiceImpl implements SearchingObjectService {

	private ObjectConverter converter;
	private final ObjectRepository objectRepository;
	private final UserRepository userRepository;

	private String springApplicationName;

	// support for logging using inherent logger initialized by Spring Boot
	private Log logger = LogFactory.getLog(ObjectServiceImpl.class);

	public ObjectServiceImpl(ObjectConverter converter, ObjectRepository objectRepository,
			UserRepository userRepository) {
		this.converter = converter;
		this.objectRepository = objectRepository;
		this.userRepository = userRepository;
	}

	@Value("${spring.application.name:dummyApplication}")
	public void setSpringApplicationName(String springApplicationName) {
		this.springApplicationName = springApplicationName;
		this.logger.trace("*** " + this.springApplicationName);
	}

	@Override
	@Transactional(readOnly = false)
	public ObjectBoundary create(ObjectBoundary boundary) {
		String userSystemID = boundary.getCreatedBy().getUserId().getSystemID();
		String userEmail = boundary.getCreatedBy().getUserId().getEmail();
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();
		
		if (!UserRole.OPERATOR.equals(role)) {
			throw new UnauthorizedException("Only OPERATOR can create an object");
		}
		
		// logging a DEBUG message indicating service invocation
		this.logger.debug("create(" + "boundary: " + boundary + ")");

		ObjectID objectID = new ObjectID();
		if (boundary.getId() == null)
			objectID.setSystemID(springApplicationName);
		boundary.setId(null);

		if (boundary.getAlias() == null || boundary.getAlias().trim().isEmpty()) {
			throw new BadRequestException("Alias must not be null or empty");
		}

		if (boundary.getType() == null || boundary.getType().trim().isEmpty()) {
			throw new BadRequestException("Type must not be null");
		}

		if (boundary.getObjectDetails() == null)
			throw new BadRequestException("Details must not be null");

		if (boundary.getCreationTimestamp() == null)
			boundary.setCreationTimestamp(new Date());

		if (boundary.getStatus() == null || boundary.getStatus().trim().isEmpty())
			throw new BadRequestException("Status must not be null");

		if (boundary.getCreatedBy() == null)
			throw new BadRequestException("Object's CreatedBy must not be null");
		if (boundary.getCreatedBy().getUserId() == null) {
			throw new BadRequestException("CreatedBy's UserID must not be null");
		}
		if (boundary.getCreatedBy().getUserId().getEmail() == null
				|| boundary.getCreatedBy().getUserId().getEmail().trim().isEmpty()) {
			throw new BadRequestException("UserID's email must not be null");
		}
		if (boundary.getCreatedBy().getUserId().getSystemID() == null
				|| boundary.getCreatedBy().getUserId().getSystemID().trim().isEmpty()) {
			throw new BadRequestException("UserID's SystemID must not be null");
		}

		ObjectEntity entity = this.converter.toEntity(boundary);

		ObjectID newId = new ObjectID(UUID.randomUUID().toString(), springApplicationName);
		entity.setId(newId);

		ObjectBoundary rv = this.converter.toBoundary(this.objectRepository.save(entity));

		this.logger.trace(rv); // logging stored information

		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getAllObjects(String systemID, String email, int size, int page) {
		String systemIDEmail = systemID + "#" + email;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new RuntimeException("User not found"));

		UserRole role = user.getRole();

		Pageable pageable = PageRequest.of(page, size, Direction.ASC, "creationTimestamp", "objectId");

		Page<ObjectEntity> allEntities = this.objectRepository.findAll(pageable);

		// Filter by role
		if (UserRole.END_USER.equals(role)) {
			return allEntities.stream().filter(ObjectEntity::getActive) // assuming there is a `isActive()` getter
					.map(converter::toBoundary).toList();

		} else if (UserRole.OPERATOR.equals(role)) {
			return allEntities.stream().map(converter::toBoundary).toList();

		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch objects.");
		} else {
			throw new BadRequestException("Unknown role: " + role.toString());
		}
	}

	@Override
	@Transactional(readOnly = true)
	public Optional<ObjectBoundary> getSpecificObject(String objectSystemID, String objectId, String userSystemID,
			String userEmail) {
		ObjectID id = new ObjectID(objectId, objectSystemID);

		if (!objectSystemID.equals(userSystemID)) {
			throw new UnauthorizedException("User not allowed to access objects from another system");
		}

		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		// Filter by role
		if (UserRole.END_USER.equals(role)) {
			return Optional.of(objectRepository.findById(id).filter(ObjectEntity::getActive).map(converter::toBoundary)
					.orElseThrow(() -> new NotFoundException("Object not found")));

		} else if (UserRole.OPERATOR.equals(role)) {
			return objectRepository.findById(id).map(converter::toBoundary);

		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch objects.");
		} else {
			throw new BadRequestException("Unknown role: " + role.toString());
		}
	}

	@Override
	@Transactional(readOnly = false)
	public void updateObject(String systemID, String objectId, String userSystemID, String userEmail,
			ObjectBoundary update) {

		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!UserRole.OPERATOR.equals(role)) {
			throw new UnauthorizedException("Only OPERATOR can update an object");
		}

		if (!userSystemID.equals(systemID)) {
			throw new UnauthorizedException("User not authorized to view parents of objects in another system");
		}

		if (update == null) {
			throw new BadRequestException("Object data must not be null");
		}

		ObjectID id = new ObjectID(objectId, systemID);
		// Fetch existing object entity
		ObjectEntity oe = objectRepository.findById(id).orElseThrow(() -> new NotFoundException(
				"Object not found with objectId: " + objectId + " and systemID: " + systemID));

		if (update.getAlias() != null)
			oe.setAlias(update.getAlias());

		if (update.getType() != null && !update.getType().trim().isEmpty())
			oe.setType(update.getType());

		if (update.getStatus() != null && !update.getStatus().trim().isEmpty())
			oe.setStatus(update.getStatus());

		if (update.getObjectDetails() != null) {
			Map<String, Object> currentDetails = oe.getObjectDetails();
			if (currentDetails == null) {
				currentDetails = new HashMap<>();
			}
			currentDetails.putAll(update.getObjectDetails()); // merge updates into existing map
			oe.setObjectDetails(currentDetails);
		}

		// Save and return updated boundary
		objectRepository.save(oe);
	}

	@Override
	@Transactional(readOnly = false)
	public void deleteAllObjects(String userSystemID, String userEmail) {
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("Only ADMIN can delete all objects");
		}

		this.objectRepository.deleteAll();
	}

	@Override
	@Transactional(readOnly = false)
	public void bindObjects(String parentSystemID, String parentObjectId, String childSystemID, String childObjectId,
			String userSystemID, String userEmail) {
		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		if (!UserRole.OPERATOR.equals(role)) {
			throw new UnauthorizedException("Only OPERATOR user can bind objects");
		}

		if (!userSystemID.equals(parentSystemID)) {
			throw new UnauthorizedException("User not authorized to bind objects in another system");
		}

		ObjectID childId = new ObjectID(childObjectId, childSystemID);
		ObjectEntity childEntity = this.objectRepository.findById(childId)
				.orElseThrow(() -> new NotFoundException("Object child with ID " + childObjectId + " was not found"));

		ObjectID parentId = new ObjectID(parentObjectId, parentSystemID);
		ObjectEntity parentEntity = this.objectRepository.findById(parentId)
				.orElseThrow(() -> new NotFoundException("Object parent with ID " + parentObjectId + " was not found"));

		childEntity.getParents().add(parentEntity);
		parentEntity.getChildren().add(childEntity);

		this.objectRepository.save(childEntity);
		this.objectRepository.save(parentEntity);
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getChildren(String parentSystemID, String parentObjectId, String userSystemID,
			String userEmail, int size, int page) {

		// Authorization check example
		if (!userSystemID.equals(parentSystemID)) {
			throw new UnauthorizedException("User not authorized to view children of objects in another system");
		}

		// Get user role
		String systemIDEmail = userSystemID + "#" + userEmail;
		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));
		UserRole role = user.getRole();

		ObjectID parentId = new ObjectID(parentObjectId, parentSystemID);
		ObjectEntity parentEntity = this.objectRepository.findById(parentId)
				.orElseThrow(() -> new NotFoundException("Parent object with ID " + parentObjectId + " not found"));
		
		if (UserRole.END_USER.equals(role)) {
			if(!parentEntity.getActive()) {
				throw new NotFoundException("Object not found");
			}
		}

		// Filter children based on role
		Stream<ObjectEntity> childrenStream = parentEntity.getChildren().stream();

		if (UserRole.END_USER.equals(role)) {
			childrenStream = childrenStream.filter(ObjectEntity::getActive);
		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch children.");
		} else if (!UserRole.OPERATOR.equals(role)) {
			throw new BadRequestException("Unknown role: " + role.toString());
		}

		List<ObjectEntity> filteredChildren = childrenStream.toList();

		if (filteredChildren.isEmpty()) {
			throw new NotFoundException("No children objects found");
		}

		// Apply pagination on filtered list
		int fromIndex = Math.min(page * size, filteredChildren.size());
		int toIndex = Math.min(fromIndex + size, filteredChildren.size());

		return filteredChildren.subList(fromIndex, toIndex).stream().map(converter::toBoundary).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> getParents(String childSystemID, String childObjectId, String userSystemID,
			String userEmail, int size, int page) {

		if (!userSystemID.equals(childSystemID)) {
			throw new UnauthorizedException("User not authorized to view parents of objects in another system");
		}

		String systemIDEmail = userSystemID + "#" + userEmail;
		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));
		UserRole role = user.getRole();

		ObjectID childId = new ObjectID(childObjectId, childSystemID);
		ObjectEntity childEntity = this.objectRepository.findById(childId)
				.orElseThrow(() -> new NotFoundException("Child object with ID " + childObjectId + " not found"));
		
		//check user role and child active/inActive for permissions
		if (UserRole.END_USER.equals(role)) {
			if(!childEntity.getActive()) {
				throw new NotFoundException("Object not found");
			}
		}

		Stream<ObjectEntity> parentsStream = childEntity.getParents().stream();

		if (UserRole.END_USER.equals(role)) {
			parentsStream = parentsStream.filter(ObjectEntity::getActive);
		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch parents.");
		} else if (!UserRole.OPERATOR.equals(role)) {
			throw new BadRequestException("Unknown role: " + role.toString());
		}

		List<ObjectEntity> filteredParents = parentsStream.toList();

		if (filteredParents.isEmpty()) {
			throw new NotFoundException("No parent objects found");
		}

		int fromIndex = Math.min(page * size, filteredParents.size());
		int toIndex = Math.min(fromIndex + size, filteredParents.size());

		return filteredParents.subList(fromIndex, toIndex).stream().map(converter::toBoundary).toList();
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> searchByExactAlias(String userSystemID, String userEmail, String alias, int size,
			int page) {
		this.logger.debug("searchByExactAlias(" + " alias: " + alias + ", size: " + size + ", page: " + page + ")");

		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		List<ObjectBoundary> rv;
		// Filter by role
		if (UserRole.END_USER.equals(role)) {
			rv = this.objectRepository.findAllByAlias(alias, PageRequest.of(page, size, Direction.ASC, "id")).stream()
					.filter(ObjectEntity::getActive).map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.OPERATOR.equals(role)) {
			rv = this.objectRepository.findAllByAlias(alias, PageRequest.of(page, size, Direction.ASC, "id")).stream()
					.map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch objects.");
		} else {
			throw new BadRequestException("Unknown role: " + role.toString());
		}
		this.logger.trace(rv);
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> searchByAliasPattern(String userSystemID, String userEmail, String pattern, int size,
			int page) {
		this.logger
				.debug("searchByAliasPattern(" + " pattern: " + pattern + ", size: " + size + ", page: " + page + ")");

		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		List<ObjectBoundary> rv;
		// Filter by role
		if (UserRole.END_USER.equals(role)) {
			rv = this.objectRepository
					.findAllByAliasLike("*" + pattern + "*", PageRequest.of(page, size, Direction.ASC, "alias", "id"))
					.stream().filter(ObjectEntity::getActive).map(this.converter::toBoundary).toList();

			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.OPERATOR.equals(role)) {
			rv = this.objectRepository
					.findAllByAliasLike("*" + pattern + "*", PageRequest.of(page, size, Direction.ASC, "alias", "id"))
					.stream().map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch objects.");
		} else {
			throw new BadRequestException("Unknown role: " + role.toString());
		}
		this.logger.trace(rv);
		return rv;
	}

	@Override
	@Transactional(readOnly = true)
	public List<ObjectBoundary> searchByType(String userSystemID, String userEmail, String type, int size,
			int page) {
		this.logger.debug("searchByType(" + " type: " + type + ", size: " + size + ", page: " + page + ")");

		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		List<ObjectBoundary> rv;
		// Filter by role
		if (UserRole.END_USER.equals(role)) {
			rv = this.objectRepository.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "id")).stream()
					.filter(ObjectEntity::getActive).map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.OPERATOR.equals(role)) {
			rv = this.objectRepository.findAllByType(type, PageRequest.of(page, size, Direction.ASC, "id")).stream()
					.map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch objects.");
		} else {
			throw new BadRequestException("Unknown role: " + role.toString());
		}
		this.logger.trace(rv);
		return rv;
	}

	@Override
	public List<ObjectBoundary> searchByStatus(String userSystemID, String userEmail, String status, int size,
			int page) {
		this.logger.debug("searchByStatus(" + " status: " + status + ", size: " + size + ", page: " + page + ")");

		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		List<ObjectBoundary> rv;
		// Filter by role
		if (UserRole.END_USER.equals(role)) {
			rv = this.objectRepository.findAllByStatus(status, PageRequest.of(page, size, Direction.ASC, "id"))
					.stream().filter(ObjectEntity::getActive).map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.OPERATOR.equals(role)) {
			rv = this.objectRepository.findAllByStatus(status, PageRequest.of(page, size, Direction.ASC, "id"))
					.stream().map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch objects.");
		} else {
			throw new BadRequestException("Unknown role: " + role.toString());
		}
		this.logger.trace(rv);
		return rv;
	}

	@Override
	public List<ObjectBoundary> searchByTypeAndStatus(String userSystemID, String userEmail, String type,
			String status, int size, int page) {
		this.logger.debug("searchByStatusAndType(" + " status: " + status + ", type: " + type + ", size: " + size
				+ ", page: " + page + ")");

		String systemIDEmail = userSystemID + "#" + userEmail;

		UserEntity user = this.userRepository.findBySystemIDEmail(systemIDEmail)
				.orElseThrow(() -> new NotFoundException("User not found"));

		UserRole role = user.getRole();

		List<ObjectBoundary> rv;
		// Filter by role
		if (UserRole.END_USER.equals(role)) {
			rv = this.objectRepository
					.findAllByTypeAndStatus(type, status, PageRequest.of(page, size, Direction.ASC, "id"))
					.stream().filter(ObjectEntity::getActive).map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.OPERATOR.equals(role)) {
			rv = this.objectRepository
					.findAllByTypeAndStatus(type, status, PageRequest.of(page, size, Direction.ASC, "id"))
					.stream().map(this.converter::toBoundary).toList();
			if (rv.isEmpty()) {
				throw new NotFoundException("Object not found");
			}

		} else if (UserRole.ADMIN.equals(role)) {
			throw new UnauthorizedException("ADMINs are not allowed to fetch objects.");
		} else {
			throw new BadRequestException("Unknown role: " + role.toString());
		}
		this.logger.trace(rv);
		return rv;
	}

}
