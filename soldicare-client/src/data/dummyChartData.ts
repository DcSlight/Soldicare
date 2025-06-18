import type { ChartDataItem } from "../types/chart";

export const chartData: ChartDataItem[] = [
  {
    title: "Blood Pressure",
    data: [
      { time: "00:00", value: 120 },
      { time: "01:00", value: 125 },
      { time: "02:00", value: 118 },
      { time: "03:00", value: 130 },
    ],
  },
  {
    title: "Hydration",
    data: [
      { time: "00:00", value: 70 },
      { time: "01:00", value: 72 },
      { time: "02:00", value: 74 },
      { time: "03:00", value: 73 },
    ],
  },
  {
    title: "EEG",
    data: [
      { time: "00:00", value: 5 },
      { time: "01:00", value: 6 },
      { time: "02:00", value: 4 },
      { time: "03:00", value: 7 },
    ],
  },
  {
    title: "Blood Oxygen",
    data: [
      { time: "00:00", value: 97 },
      { time: "01:00", value: 98 },
      { time: "02:00", value: 98 },
      { time: "03:00", value: 99 },
    ],
  },
];
