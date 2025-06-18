export type ChartPoint = {
  time: string;
  value: number;
};

export type ChartDataItem = {
  title: string;
  data: ChartPoint[];
};
