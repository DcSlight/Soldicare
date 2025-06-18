const SensorType ={
    HEART_RATE : 'HEART_RATE',
    TEMPERATURE : 'TEMPERATURE',
    GPS : 'GPS',
    BLOOD_PRESSURE : 'BLOOD_PRESSURE',
    BLOOD_OXYGEN : 'BLOOD_OXYGEN',
    ECG : 'ECG',
    EEG : 'EEG',
    HYDRATION : 'HYDRATION'
}as const;
type SensorType = typeof SensorType[keyof typeof SensorType];
  
export { SensorType, SensorType as default };

