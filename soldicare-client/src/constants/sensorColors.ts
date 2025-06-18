export const sensorColors = {
    temperature: "#f97316",   // orange-500
    heartRate:   "#ef4444",   // red-500
    bloodOxygen: "#10b981",   // emerald-500
    bloodPressure:"#dc2626",  // red-600
    ecg:         "#f59e0b",   // amber-500
    eeg:         "#8b5cf6",   // violet-500
    hydration:   "#3b82f6",   // blue-500
  };
  export type SensorColorKey = keyof typeof sensorColors;