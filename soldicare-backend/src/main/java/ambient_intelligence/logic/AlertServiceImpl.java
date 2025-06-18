package ambient_intelligence.logic;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.stereotype.Service;

import ambient_intelligence.domain.boundary.CommandBoundary;
import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.id.CreatedBy;
import ambient_intelligence.id.ObjectID;

@Service("alertService")
public class AlertServiceImpl implements AlertService{
	
	private final CommandsService commandService;
	private final SearchingObjectService searchingObjectService;
	
	public AlertServiceImpl(CommandsService commandService , SearchingObjectService searchingObjectService) {
		this.commandService = commandService;
		this.searchingObjectService = searchingObjectService;
	}

	@Override
	public List<ObjectBoundary> getAll(String userSystemID, String userEmail, int size, int page) {
	    List<CommandBoundary> alertCommands = this.commandService
	        .getAllAlertCriticalCommands(userSystemID, userEmail, size, page);

	    List<ObjectBoundary> allSensors = this.searchingObjectService
	        .searchByExactAlias(userSystemID, userEmail, "allSensor", 1000, 0); 

	    List<ObjectBoundary> result = new ArrayList<>();

	    for (CommandBoundary command : alertCommands) {
	        Map<String, Object> attributes = command.getCommandAttributes();


	        @SuppressWarnings("unchecked")
			Map<String, Object> relatedSoldier = (Map<String, Object>) attributes.get("relatedSoldier");
	        @SuppressWarnings("unchecked")
			Map<String, Object> soldierIdMap = (Map<String, Object>) relatedSoldier.get("id");
	        String soldierObjectId = (String) soldierIdMap.get("objectId");
	        String soldierSystemId = (String) soldierIdMap.get("systemID");

	        Date commandTime = command.getInvocationTimestamp();

	        List<ObjectBoundary> relevantSnapshots = new ArrayList<>();

	        for (ObjectBoundary sensor : allSensors) {
	            Date sensorTime = sensor.getCreationTimestamp();
	            if (sensorTime == null || !sensorTime.before(commandTime)) continue;


	            List<ObjectBoundary> parents = this.searchingObjectService.getParents(
	                sensor.getId().getSystemID(),
	                sensor.getId().getObjectId(),
	                userSystemID,
	                userEmail,
	                10, 0 
	            );

	            boolean match = parents.stream().anyMatch(parent ->
	                soldierObjectId.equals(parent.getId().getObjectId()) &&
	                soldierSystemId.equals(parent.getId().getSystemID())
	            );

	            if (match) {
	                relevantSnapshots.add(sensor);
	            }
	        }

	     
	        ObjectBoundary alertObject = new ObjectBoundary();
	        ObjectID id =new ObjectID(UUID.randomUUID().toString(), userSystemID);
	        alertObject.setId(id);;
	        alertObject.setType("alert");
	        alertObject.setAlias("critical");
	        alertObject.setStatus("ACTIVE");
	        alertObject.setActive(true);
	        alertObject.setCreationTimestamp(new Date());

	        CreatedBy createdBy = new CreatedBy(command.getInvokedBy().getUserId());
	        alertObject.setCreatedBy(createdBy);

	        Map<String, Object> alertDetails = new LinkedHashMap<>();
	        alertDetails.put("command", command);
	        alertDetails.put("snapshotHistory", relevantSnapshots);

	        alertObject.setObjectDetails(alertDetails);
	        result.add(alertObject);
	    }

	    return result;
	}



	
	
	
}
