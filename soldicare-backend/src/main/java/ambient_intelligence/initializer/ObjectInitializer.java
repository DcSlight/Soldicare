// FULLY HARDCODED INITIALIZER WITH 3 UNITS AND 6 NAMED SOLDIERS PER UNIT
package ambient_intelligence.initializer;

import java.util.*;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import ambient_intelligence.domain.boundary.ObjectBoundary;
import ambient_intelligence.id.CreatedBy;
import ambient_intelligence.id.ObjectID;
import ambient_intelligence.id.UserID;
import ambient_intelligence.logic.ObjectsService;

@Component("ObjectInitializer")
@Order(1)
public class ObjectInitializer implements CommandLineRunner {

    private final ObjectsService objectService;

    public ObjectInitializer(ObjectsService objectService) {
        this.objectService = objectService;
    }

    @Override
    public void run(String... args) throws Exception {
        List<ObjectBoundary> objects = new ArrayList<>();
        Map<String, ObjectBoundary> createdObjects = new HashMap<>();

        // Units
        ObjectBoundary unitA = createBoundary("armyUnit", "Unit A", Map.of(
            "name", "Unit A",
            "color", "blue",
            "polygon", List.of(
                List.of(31.5408, 34.46205),
                List.of(31.55023, 34.47793),
                List.of(31.54504, 34.48746),
                List.of(31.53641, 34.48196)
            ),
            "severelyWounded", 2,
            "lightlyWounded", 3,
            "immobileUnconscious", 0,
            "dead", 1,
            "combatReadyTroops", 6
        ));

        ObjectBoundary unitB = createBoundary("armyUnit", "Unit B", Map.of(
            "name", "Unit B",
            "color", "red",
            "polygon", List.of(
                List.of(31.5500, 34.4700),
                List.of(31.5600, 34.4800),
                List.of(31.5550, 34.4850),
                List.of(31.5450, 34.4750)
            ),
            "severelyWounded", 1,
            "lightlyWounded", 2,
            "immobileUnconscious", 1,
            "dead", 2,
            "combatReadyTroops", 6
        ));

        ObjectBoundary unitC = createBoundary("armyUnit", "Unit C", Map.of(
            "name", "Unit C",
            "color", "green",
            "polygon", List.of(
                List.of(31.5600, 34.4900),
                List.of(31.5700, 34.5000),
                List.of(31.5650, 34.5050),
                List.of(31.5550, 34.4950)
            ),
            "severelyWounded", 0,
            "lightlyWounded", 1,
            "immobileUnconscious", 2,
            "dead", 3,
            "combatReadyTroops", 6
        ));

        // Soldiers
        ObjectBoundary soldier1 = createBoundary("soldier", "Avi Biton", Map.of("serial", "7654321", "bloodType", "A", "name", "Avi Biton", "age", "22", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier2 = createBoundary("soldier", "Sarah Levi", Map.of("serial", "7654322", "bloodType", "O", "name", "Sarah Levi", "age", "24", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier3 = createBoundary("soldier", "Mike Cohen", Map.of("serial", "7654323", "bloodType", "B", "name", "Mike Cohen", "age", "26", "alergies", List.of("Latex"), "MedicalCondition", List.of("Hypertension"), "Medications", List.of("Lisinopril")));
        ObjectBoundary soldier4 = createBoundary("soldier", "Emma Peretz", Map.of("serial", "7654324", "bloodType", "AB", "name", "Emma Peretz", "age", "23", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier5 = createBoundary("soldier", "Noa Shalev", Map.of("serial", "7654325", "bloodType", "A", "name", "Noa Shalev", "age", "25", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier6 = createBoundary("soldier", "Itay Mor", Map.of("serial", "7654326", "bloodType", "O", "name", "Itay Mor", "age", "23", "alergies", List.of("Penicillin"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));

        ObjectBoundary soldier7 = createBoundary("soldier", "Tamar Gold", Map.of("serial", "7654327", "bloodType", "A", "name", "Tamar Gold", "age", "22", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier8 = createBoundary("soldier", "Omer Bar", Map.of("serial", "7654328", "bloodType", "B", "name", "Omer Bar", "age", "21", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier9 = createBoundary("soldier", "Roni Avraham", Map.of("serial", "7654329", "bloodType", "AB", "name", "Roni Avraham", "age", "23", "alergies", List.of("Shellfish"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier10 = createBoundary("soldier", "Daniel Mizrahi", Map.of("serial", "7654330", "bloodType", "O", "name", "Daniel Mizrahi", "age", "24", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier11 = createBoundary("soldier", "Maya Sharabi", Map.of("serial", "7654331", "bloodType", "B", "name", "Maya Sharabi", "age", "22", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
        ObjectBoundary soldier12 = createBoundary("soldier", "David Hazan", Map.of("serial", "7654332", "bloodType", "A", "name", "David Hazan", "age", "26", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));

        objects.addAll(List.of(unitA, unitB, unitC,
            soldier1, soldier2, soldier3, soldier4, soldier5, soldier6,
            soldier7, soldier8, soldier9, soldier10, soldier11, soldier12));

        ObjectBoundary soldier13 = createBoundary("soldier", "Yossi Azulay", Map.of("serial", "7654333", "bloodType", "AB", "name", "Yossi Azulay", "age", "24", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
ObjectBoundary soldier14 = createBoundary("soldier", "Gal Hadad", Map.of("serial", "7654334", "bloodType", "A", "name", "Gal Hadad", "age", "23", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
ObjectBoundary soldier15 = createBoundary("soldier", "Nir Ami", Map.of("serial", "7654335", "bloodType", "B", "name", "Nir Ben Ami", "age", "25", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
ObjectBoundary soldier16 = createBoundary("soldier", "Dana Azulay", Map.of("serial", "7654336", "bloodType", "O", "name", "Dana Azulay", "age", "22", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
ObjectBoundary soldier17 = createBoundary("soldier", "Yarden Moyal", Map.of("serial", "7654337", "bloodType", "AB", "name", "Yarden Moyal", "age", "21", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));
ObjectBoundary soldier18 = createBoundary("soldier", "Shiran Avital", Map.of("serial", "7654338", "bloodType", "O", "name", "Shiran Avital", "age", "26", "alergies", List.of("None"), "MedicalCondition", List.of("None"), "Medications", List.of("None")));

objects.addAll(List.of(soldier13, soldier14, soldier15, soldier16, soldier17, soldier18));

// Save all
        for (ObjectBoundary ob : objects) {
            ObjectBoundary created = objectService.create(ob);
            createdObjects.put(created.getAlias(), created);
        }

        UserID userId = new UserID("joanna@demo.org", "2025b.Itay.Chabra");
        bindObjects(unitA, soldier1, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitA, soldier2, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitA, soldier3, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitA, soldier4, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitA, soldier5, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitA, soldier6, createdObjects, userId.getSystemID(), userId.getEmail());

        bindObjects(unitB, soldier7, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitB, soldier8, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitB, soldier9, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitB, soldier10, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitB, soldier11, createdObjects, userId.getSystemID(), userId.getEmail());
        bindObjects(unitB, soldier12, createdObjects, userId.getSystemID(), userId.getEmail());
bindObjects(unitC, soldier13, createdObjects, userId.getSystemID(), userId.getEmail());
bindObjects(unitC, soldier14, createdObjects, userId.getSystemID(), userId.getEmail());
bindObjects(unitC, soldier15, createdObjects, userId.getSystemID(), userId.getEmail());
bindObjects(unitC, soldier16, createdObjects, userId.getSystemID(), userId.getEmail());
bindObjects(unitC, soldier17, createdObjects, userId.getSystemID(), userId.getEmail());
bindObjects(unitC, soldier18, createdObjects, userId.getSystemID(), userId.getEmail());
    }

    private void bindObjects(ObjectBoundary parent, ObjectBoundary child, Map<String, ObjectBoundary> createdObjects,
                              String userSystemID, String userEmail) {
        ObjectBoundary parentObj = createdObjects.get(parent.getAlias());
        ObjectBoundary childObj = createdObjects.get(child.getAlias());
        if (parentObj != null && childObj != null) {
            objectService.bindObjects(
                parentObj.getId().getSystemID(), parentObj.getId().getObjectId(),
                childObj.getId().getSystemID(), childObj.getId().getObjectId(),
                userSystemID, userEmail);
        }
    }

    private ObjectBoundary createBoundary(String type, String alias, Map<String, Object> details) {
        ObjectBoundary boundary = new ObjectBoundary();
        ObjectID objectId = new ObjectID();
        objectId.setObjectId(generateObjectId());
        objectId.setSystemID("2025b.Itay.Chabra");
        boundary.setId(objectId);
        boundary.setType(type);
        boundary.setAlias(alias);
        boundary.setStatus("ACTIVE");
        boundary.setActive(true);
        boundary.setCreationTimestamp(new Date());
        CreatedBy createdBy = new CreatedBy();
        createdBy.setUserId(new UserID("joanna@demo.org", "2025b.Itay.Chabra"));
        boundary.setCreatedBy(createdBy);
        boundary.setObjectDetails(details);
        return boundary;
    }

    private String generateObjectId() {
        return "obj-" + System.currentTimeMillis() + "-" + (int) (Math.random() * 1000);
    }
}
