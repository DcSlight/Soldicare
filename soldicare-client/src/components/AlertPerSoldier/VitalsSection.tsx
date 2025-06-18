import { Box, Paper, Typography, Stack, Tooltip } from "@mui/material";
import DeviceThermostatIcon from "@mui/icons-material/DeviceThermostat";
import FavoriteIcon from "@mui/icons-material/Favorite";
import OpacityIcon from "@mui/icons-material/Opacity";
import MonitorHeartIcon from "@mui/icons-material/MonitorHeart";
import WaterDropIcon from "@mui/icons-material/WaterDrop";
import ShowChartIcon from "@mui/icons-material/ShowChart";
import PsychologyIcon from "@mui/icons-material/Psychology";
import ManImg from "../../assets/man.svg";
import type { RelatedSoldier, Snapshot } from "../../interface/alert.interface";

interface VitalsSectionProps {
  relatedSoldier: RelatedSoldier;
  lastSnapshot: Snapshot;
  commandAttributes?: Record<string, string | object>;
}

const VitalsSection = ({
  relatedSoldier,
  commandAttributes,
  lastSnapshot,
}: VitalsSectionProps) => {
  const snapshotData = lastSnapshot.objectDetails;

  const fields = [
    {
      key: "Temperature",
      label: "Temperature",
      icon: <DeviceThermostatIcon sx={{ color: "orange" }} />,
    },
    {
      key: "HeartRate",
      label: "Heart Rate",
      icon: <FavoriteIcon sx={{ color: "red" }} />,
    },
    {
      key: "BloodOxygen",
      label: "Blood Oxygen",
      icon: <OpacityIcon sx={{ color: "blue" }} />,
    },
    {
      key: "BloodPressure",
      label: "Blood Pressure",
      icon: <MonitorHeartIcon sx={{ color: "purple" }} />,
    },
    {
      key: "Hydration",
      label: "Hydration",
      icon: <WaterDropIcon sx={{ color: "teal" }} />,
    },
    {
      key: "ECG",
      label: "ECG",
      icon: <ShowChartIcon sx={{ color: "darkgreen" }} />,
    },
    {
      key: "EEG",
      label: "EEG",
      icon: <PsychologyIcon sx={{ color: "indigo" }} />,
    },
  ];

  return (
    <Box sx={{ height: "100%" }}>
      <Paper
        sx={{
          p: 2,
          height: "100%",
          transition: "0.2s",
          "&:hover": {
            backgroundColor: "#f0f0f0",
          },
        }}
      >
        <Typography variant="h6" gutterBottom>
          {relatedSoldier.name}'s Medical Status
        </Typography>

        <Box display="flex" gap={1}>
          {/* Left: image */}
          <Box sx={{ display: "flex", alignItems: "center" }}>
            <img
              src={ManImg}
              alt="Human"
              style={{
                height: "200px",
                width: "auto",
                maxHeight: "100%",
                display: "block",
                objectFit: "contain",
              }}
            />
          </Box>

          {/* Right: vitals */}
          <Stack spacing={1} flex={1}>
            {fields.map(({ key, label, icon }) => {
              const isFromCommand = commandAttributes?.hasOwnProperty(key);
              const value =
                (commandAttributes?.[key] as string) ??
                (snapshotData[key] as string) ??
                "N/A";

              return (
                <Tooltip
                  key={label}
                  title={isFromCommand ? "From Command" : "From Sensor"}
                  arrow
                >
                  <Box
                    display="flex"
                    alignItems="center"
                    gap={1}
                    minWidth={0}
                    sx={{ cursor: "default" }}
                  >
                    {icon}
                    <Typography
                      variant="body2"
                      noWrap
                      sx={{
                        overflow: "hidden",
                        textOverflow: "ellipsis",
                        color: isFromCommand ? "error.main" : "text.primary",
                        fontWeight: isFromCommand ? "bold" : "normal",
                      }}
                    >
                      {`${label}: ${value}`}
                    </Typography>
                  </Box>
                </Tooltip>
              );
            })}
          </Stack>
        </Box>
      </Paper>
    </Box>
  );
};

export default VitalsSection;
