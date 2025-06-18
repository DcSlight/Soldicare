package ambient_intelligence.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import ambient_intelligence.data.ObjectEntity;
import ambient_intelligence.id.ObjectID;

public interface ObjectRepository extends MongoRepository<ObjectEntity, ObjectID> {

	// new search by exact value operation with pagination support
	List<ObjectEntity> findAllByAlias(@Param("alias") String alias, Pageable pageable);

	// new search by pattern operation with pagination support
	List<ObjectEntity> findAllByAliasLike(@Param("pattern") String pattern, Pageable pageable);

	List<ObjectEntity> findAllByType(@Param("type") String type, Pageable pageable);

	List<ObjectEntity> findAllByStatus(@Param("status") String status, Pageable pageable);

	List<ObjectEntity> findAllByTypeAndStatus(@Param("type") String type, @Param("status") String status,
			Pageable pageable);
}
