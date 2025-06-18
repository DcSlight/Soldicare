
export interface DataPoint {
  time: string;
  value: number;
}

export interface SensorSeries {
  id: string;
  label: string;
  unit: string;
  color: string;
  data: DataPoint[];
  span?: 1 | 2;             // <-- grid column span
  alert?: boolean;          // <-- should it have a red border?
}
