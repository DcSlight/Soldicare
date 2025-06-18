package ambient_intelligence.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;

import ambient_intelligence.data.UserEntity;

public interface UserRepository extends MongoRepository<UserEntity, String> {
	Optional<UserEntity> findBySystemIDEmail(@Param("systemIDEmail") String systemIDEmail);
}
