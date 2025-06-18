import React from "react";
import { Typography, Box } from "@mui/material";
import type { UnitData } from "../../interface/UnitData.interface";
import armyUnitIcon from "../../assets/army unit.svg";
import StatusIndicator from "./StatusIndicator";
import RoomIcon from "@mui/icons-material/Room";

interface Props {
  unit: UnitData;
  displaySpecificUnit: (unitId: string) => void;
}

const UnitAccordionHeader: React.FC<Props> = ({
  unit,
  displaySpecificUnit,
}) => {
  return (
    <Box
      display="grid"
      alignItems="center"
      sx={{
        width: "100%",
        gridTemplateColumns: "2fr 2fr 2fr 1fr",
        gap: 0.2,
      }}
    >
      {/* Unit name + icon */}
      <Box display="flex" alignItems="center" gap={1}>
        <img
          src={armyUnitIcon}
          alt="unit icon"
          style={{ width: 20, height: 20 }}
        />
        <Typography sx={{ fontSize: "0.6rem" }} fontWeight={600}>
          {unit.name}
        </Typography>
      </Box>

      {/* Status */}
      <StatusIndicator
        status={6 - unit.dead - unit.immobileUnconscious - unit.severelyWounded}
        max={6}
      />

      {/* Location */}
      <Box display="flex" alignItems="center" gap={0.5}>
        <RoomIcon
          fontSize="small"
          sx={{ cursor: "pointer" }}
          onClick={() => displaySpecificUnit(unit.id.objectId)}
        />
      </Box>

      {/* See less */}
      <Typography variant="caption" color="primary" sx={{ justifySelf: "end" }}>
        See less
      </Typography>
    </Box>
  );
};

export default UnitAccordionHeader;
