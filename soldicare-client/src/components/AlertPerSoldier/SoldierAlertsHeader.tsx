import { Box, Typography } from "@mui/material";
import AssignmentLateIcon from "@mui/icons-material/AssignmentLate";
import RoomIcon from "@mui/icons-material/Room";
import FilterAltIcon from "@mui/icons-material/FilterAlt";
import { useNavigate } from "react-router-dom";

const SoldierAlertsHeader = () => {
  const navigate = useNavigate();

  return (
    <Box
      display="flex"
      alignItems="center"
      justifyContent="space-between"
      mb={3}
    >
      {/* Left side: Icon + title */}
      <Box display="flex" alignItems="center" gap={1.5}>
        <AssignmentLateIcon sx={{ fontSize: 28, color: "text.primary" }} />
        <Typography
          variant="h6"
          fontWeight="bold"
          sx={{ fontSize: "1.25rem", lineHeight: 1.3 }}
        >
          Alert per soldier
        </Typography>
      </Box>

      {/* Right side: Open map + Filters */}
      <Box display="flex" alignItems="center" gap={3}>
        <Box
          display="flex"
          alignItems="center"
          gap={0.5}
          sx={{
            cursor: "pointer",
            transition: "0.3s ease",
            "&:hover": {
              transform: "scale(1.05)",
            },
          }}
          onClick={() => {
            navigate("/map-view");
          }}
        >
          <RoomIcon />
          <Typography variant="body2" fontWeight="medium">
            Open map
          </Typography>
        </Box>
        <Box
          display="flex"
          alignItems="center"
          gap={0.5}
          sx={{ cursor: "pointer" }}
        >
          <FilterAltIcon />
          <Typography variant="body2" fontWeight="medium">
            Filters
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default SoldierAlertsHeader;
