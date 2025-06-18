import React from "react";
import { Chip } from "@mui/material";
import { useTheme } from "@mui/material/styles";

interface StatusChipProps {
  status: string;
}

const StatusChip: React.FC<StatusChipProps> = ({ status }) => {
  const theme = useTheme();

  const getStyles = (status: string) => {
    switch (status.toLowerCase()) {
      case "active":
        return {
          color: theme.palette.success.main,
          border: `2px solid ${theme.palette.success.main}`,
          backgroundColor: "transparent",
        };
      case "inactive":
        return {
          color: theme.palette.error.dark,
          border: `2px solid ${theme.palette.error.dark}`,
          backgroundColor: "transparent",
        };
      case "in treatment":
        return {
          color: theme.palette.info.main,
          border: `2px solid ${theme.palette.info.main}`,
          backgroundColor: "transparent",
        };
      default:
        return {
          backgroundColor: "#eee",
          color: theme.palette.text.primary,
        };
    }
  };

  return (
    <Chip
      label={status}
      sx={{
        maxWidth: 100,
        height: 28,
        borderRadius: "16px",
        px: 1.5,
        fontWeight: "bold",
        fontSize: "0.75rem",
        whiteSpace: "nowrap",
        ...getStyles(status),
      }}
    />
  );
};

export default StatusChip;
