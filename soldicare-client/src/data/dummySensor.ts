export interface SensorData {
    temperatureSensor: {
      temperature: number;
      isHeatStress: boolean;
      isHypothermia: boolean;
    };
  
    bloodPressureSensor: {
      systolic: number;
      diastolic: number;
    };
  
    heartRateSensor: {
      heartRate: number;
    };
  
    gpsSensor: {
      longitude: string;
      latitude: string;
    };
  
    hydrationSensor: {
      isDehydrate: number; // 0–1 or percentage
    };
  
    bloodOxygenSensor: {
      oxygenLevel: number;
    };
  
    
  }
  