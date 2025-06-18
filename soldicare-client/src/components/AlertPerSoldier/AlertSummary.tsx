import { useState } from "react";
import {
  AccordionSummary,
  Typography,
  Avatar,
  Chip,
  Box,
  IconButton,
} from "@mui/material";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import AccessTimeIcon from "@mui/icons-material/AccessTime";
import RoomIcon from "@mui/icons-material/Room";
import PhotoLibraryIcon from "@mui/icons-material/PhotoLibrary";
import { useTheme } from "@mui/material/styles";
import StatusChip from "./StatusChip";
import armyUnitIcon from "../../assets/army unit.svg";
import LiveClock from "../LiveLock";
import ImageGallery from "./ImageGallery";

interface AlertSummaryProps {
  soldierName: string;
  unitName: string;
  status: string;
  severity: string;
  profileImage: string;
  expanded: boolean;
  timestamp: string;
  soldierSerial?: string;
}

const AlertSummary = ({
  soldierName,
  unitName,
  status,
  severity,
  profileImage,
  expanded,
  timestamp,
  soldierSerial,
}: AlertSummaryProps) => {
  const theme = useTheme();
  const [openGallery, setOpenGallery] = useState(false);

  const severityColor =
    theme.palette.severity[
      severity.toLowerCase() as keyof typeof theme.palette.severity
    ] || "#ccc";

  return (
    <>
      <AccordionSummary expandIcon={<ExpandMoreIcon />}>
        <Box
          display="grid"
          gridTemplateColumns="auto auto auto auto auto"
          gap={2}
          alignItems="center"
          width="100%"
        >
          <Chip
            label={severity}
            sx={{
              backgroundColor: severityColor,
              color: "#fff",
              fontWeight: "bold",
              fontSize: "0.75rem",
              height: 28,
              whiteSpace: "nowrap",
              maxWidth: 100,
            }}
          />

          <Box display="flex" alignItems="center" gap={1}>
            <Avatar src={profileImage} />
            <Box
              sx={{
                minWidth: 80,
                maxWidth: 100,
                overflow: "hidden",
                textOverflow: "ellipsis",
                whiteSpace: "nowrap",
              }}
            >
              <Typography>{soldierName}</Typography>
            </Box>
          </Box>

          <Box display="flex" alignItems="center" gap={1}>
            <img
              src={armyUnitIcon}
              alt="Unit Logo"
              style={{ width: 24, height: 24, objectFit: "contain" }}
            />
            <Typography>{unitName}</Typography>
          </Box>

          <Box display="flex" alignItems="center" gap={1}>
            <StatusChip status={status} />
            <Box display="flex" alignItems="center" gap={0.5}>
              <AccessTimeIcon fontSize="small" />
              <LiveClock initialTimestamp={timestamp} />
            </Box>
            <RoomIcon />
          </Box>

          <Box display="flex" alignItems="center" gap={1}>
            <Typography
              variant="body2"
              sx={{
                color: "text.secondary",
                fontStyle: "italic",
              }}
            >
              {expanded ? "Less info" : "More info"}
            </Typography>

            <IconButton
              size="small"
              onClick={(e) => {
                e.stopPropagation();
                setOpenGallery(true);
              }}
            >
              <PhotoLibraryIcon />
            </IconButton>
          </Box>
        </Box>
      </AccordionSummary>

      {/* Dialog for gallery */}
      <ImageGallery
        openGallery={openGallery}
        soldierSerial={soldierSerial ?? "Unknown"}
        setOpenGallery={setOpenGallery}
      />
    </>
  );
};

export default AlertSummary;
