package ambient_intelligence.domain.enums;

public enum ObjectType {
	dummyObject, medicalHistory, medicalEvent, soldier, user, sensor,armyUnit;

	public static ObjectType fromString(String type) {
		try {
			ObjectType objectType = ObjectType.valueOf(type);
			if (objectType != dummyObject)
				return objectType;
			throw new IllegalArgumentException("Unknown object type: " + type);
		} catch (IllegalArgumentException e) {
			throw new IllegalArgumentException("Unknown object type: " + type);
		}
	}
}
