import { Box, Paper, Typography } from "@mui/material";
import {
  LineChart,
  Line,
  XAxis,
  YAxis,
  Tooltip,
  ResponsiveContainer,
} from "recharts";
import {
  DeviceThermostat,
  Favorite,
  Opacity,
  MonitorHeart,
  WaterDrop,
  ShowChart,
  Psychology,
} from "@mui/icons-material";
import type { Snapshot } from "../../interface/alert.interface";
import type { ChartDataItem, ChartPoint } from "../../types/chart";

interface ChartsGridProps {
  snapshotHistory: Snapshot[];
  commandAttributes?: Record<string, string | object>; // Optional
  flatStyle?: boolean;
}

// Default list of fallback metrics (in case no commandAttributes are given)
const fallbackMetrics = [
  "Temperature",
  "HeartRate",
  "BloodOxygen",
  "BloodPressure",
  "Hydration",
  "ECG",
  "EEG",
];

// Mapping of metric to icon
const iconMap: Record<string, React.ReactNode> = {
  Temperature: <DeviceThermostat fontSize="small" sx={{ mr: 1 }} />,
  HeartRate: <Favorite fontSize="small" sx={{ mr: 1 }} />,
  BloodOxygen: <Opacity fontSize="small" sx={{ mr: 1 }} />,
  BloodPressure: <MonitorHeart fontSize="small" sx={{ mr: 1 }} />,
  Hydration: <WaterDrop fontSize="small" sx={{ mr: 1 }} />,
  ECG: <ShowChart fontSize="small" sx={{ mr: 1 }} />,
  EEG: <Psychology fontSize="small" sx={{ mr: 1 }} />,
};

// Mapping of metric to color
const colorMap: Record<string, string> = {
  Temperature: "#ff7043",
  HeartRate: "#d32f2f",
  BloodOxygen: "#43a047",
  BloodPressure: "#e53935",
  Hydration: "#1e88e5",
  ECG: "#5e35b1",
  EEG: "#fb8c00",
};

// Chooses up to 4 metrics: prioritizes critical ones from commandAttributes
const getFinalMetricsToShow = (
  commandAttributes?: Record<string, string | object>
): string[] => {
  const critical = commandAttributes
    ? Object.keys(commandAttributes).filter((key) => key !== "relatedSoldier")
    : [];

  if (critical.length >= 4) return critical.slice(0, 4);

  const additional = fallbackMetrics.filter((m) => !critical.includes(m));
  return [...critical, ...additional].slice(0, 4);
};

// Converts raw snapshot data into chart-friendly format
const formatChartData = (
  snapshotHistory: Snapshot[],
  metrics: string[]
): ChartDataItem[] => {
  const dataMap: Record<string, ChartPoint[]> = {};

  for (const metric of metrics) {
    dataMap[metric] = snapshotHistory.map((snapshot) => {
      const raw = snapshot.objectDetails[metric];
      const value =
        metric === "EEG"
          ? raw === "Unstable"
            ? 2
            : raw === "Normal"
            ? 1
            : 0
          : metric === "ECG"
          ? raw === "Irregular"
            ? 2
            : raw === "Normal"
            ? 1
            : 0
          : Number(raw);

      return {
        time: new Date(snapshot.creationTimestamp).toLocaleTimeString([], {
          hour: "2-digit",
          minute: "2-digit",
        }),
        value: isNaN(value) ? 0 : value,
      };
    });
  }

  return metrics.map((metric) => ({
    title: metric,
    data: dataMap[metric],
  }));
};

const ChartsGrid = ({
  snapshotHistory,
  commandAttributes,
  flatStyle,
}: ChartsGridProps) => {
  const metricsToShow = getFinalMetricsToShow(commandAttributes);
  const chartData = formatChartData(snapshotHistory, metricsToShow);

  return (
    <Box
      sx={{
        display: "grid",
        gridTemplateColumns: "repeat(2, 1fr)",
        gap: 2,
        height: "100%",
      }}
    >
      {chartData.map((chart) => (
        <Paper
          key={chart.title}
          elevation={flatStyle ? 0 : 1}
          sx={{
            p: 2,
            height: "100%",
            boxShadow: flatStyle ? "none" : undefined,
            border: flatStyle ? "none" : undefined,
            backgroundColor: "#fff",
            transition: "0.2s",
            "&:hover": {
              backgroundColor: "#f0f0f0",
            },
          }}
        >
          <Box display="flex" alignItems="center" mb={1}>
            {iconMap[chart.title] || null}
            <Typography variant="subtitle1">{chart.title}</Typography>
          </Box>
          <Box sx={{ width: "100%", height: 130 }}>
            <ResponsiveContainer width="100%" height="100%">
              <LineChart data={chart.data}>
                <XAxis dataKey="time" tick={{ fontSize: 12 }} />
                <YAxis
                  tick={{ fontSize: 12 }}
                  width={30}
                  tickMargin={6}
                  axisLine={false}
                  tickLine={false}
                />
                <Tooltip />
                <Line
                  type="monotone"
                  dataKey="value"
                  stroke={colorMap[chart.title] || "#3f51b5"}
                  strokeWidth={2}
                  dot={false}
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </LineChart>
            </ResponsiveContainer>
          </Box>
        </Paper>
      ))}
    </Box>
  );
};

export default ChartsGrid;
