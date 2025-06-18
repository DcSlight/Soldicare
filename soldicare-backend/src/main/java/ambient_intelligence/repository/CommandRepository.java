package ambient_intelligence.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import ambient_intelligence.data.CommandEntity;
import ambient_intelligence.id.CommandID;
import org.springframework.data.domain.Pageable;
public interface CommandRepository extends MongoRepository<CommandEntity, CommandID> {
	List<CommandEntity> findByCommand(String command, Pageable pageable);
}
