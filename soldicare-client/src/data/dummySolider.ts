import SoldierRoleType from "../types/soldierRoleType";

export type SoldierData = {
    id: string;
    name: string;
    roleType: SoldierRoleType;
    unitId: string;
    sensors : any;// TODO: create a type for the sensors
    medicalHistory: string; //for now string 
    riskLevel: String // for now string
  }

  export const dummySoldierData: SoldierData =   {
    id: 'S001',
    name: 'John Carter',
    roleType: SoldierRoleType.INFANTRY,
    unitId: 'UnitAlpha',
    sensors: {
      "temperatureSensor": {
        "temperature": 39.2,
        "isHeatStress": true,
        "isHypothermia": false
      },
      "bloodPressureSensor": {
        "systolic": 130,
        "diastolic": 85
      },
      "heartRateSensor": {
        "heartRate": 85.0
      },
      "gpsSensor": {
        "longitude": "34.7818",
        "latitude": "32.0853"
      },
      "hydrationSensor": {
        "isDehydrate": 0.25  // 25% dehydrated
      },
      "bloodOxygenSensor": {
        "oxygenLevel": 97.5
      },
      "ecgSensor": {
        "cardiacActivity": "NORMAL"
      },
      "eegSensor": {
        "brainActivity": "ALPHA"  // could be ALPHA, BETA, THETA, etc.
      }
    },
    medicalHistory: 'Mild dehydration reported last week.',
    riskLevel: 'Moderate'
  
  }
