import React from "react";
import { Box, Typography } from "@mui/material";
import type { UnitData } from "../../interface/UnitData.interface";
import soldierGreen from "../../assets/green body.svg";
import soldierRed from "../../assets/red body.svg";
import soldierYellow from "../../assets/yellow body.svg";
import soldierBlue from "../../assets/blue body.svg";
import soldierDarkGray from "../../assets/dark gray body.svg";

interface Props {
  unit: UnitData;
}

const UnitAccordionDetails: React.FC<Props> = ({ unit }) => {
  const UNIT_SIZE = 6;

  const getSoldierImage = (index: number) => {
    const { severelyWounded, lightlyWounded, immobileUnconscious, dead } = unit;

    if (index < severelyWounded) return soldierRed;
    if (index < severelyWounded + lightlyWounded) return soldierYellow;
    if (index < severelyWounded + lightlyWounded + immobileUnconscious)
      return soldierBlue;
    if (index < severelyWounded + lightlyWounded + immobileUnconscious + dead)
      return soldierDarkGray;

    return soldierGreen;
  };

  return (
    <Box>
      {/* Combat ready troops */}
      <Typography
        variant="h6"
        sx={{ mt: 0, mb: 1, fontWeight: 600, textAlign: "center" }}
      >
        Combat ready troops: {unit.combatReadyTroops}/{UNIT_SIZE}
      </Typography>

      {/* Soldiers grid */}
      <Box display="grid" gridTemplateColumns="repeat(6, 1fr)" gap={1} mb={2}>
        {unit.soldiers.map((soldier, i) => (
          <Box
            key={soldier.id.objectId}
            display="flex"
            flexDirection="column"
            alignItems="center"
          >
            <img
              src={getSoldierImage(i)}
              alt="Soldier"
              width={30}
              height={50}
            />
            <Typography variant="caption">
              {soldier.objectDetails.name}
            </Typography>
          </Box>
        ))}
      </Box>

      {/* Status summary */}
      <Box display="flex" justifyContent="space-between" gap={1}>
        <Box
          flex={1}
          bgcolor="#D84343"
          borderRadius="8px"
          textAlign="center"
          p={1}
        >
          <Typography fontSize={13} fontWeight={600} color="white">
            {unit.severelyWounded}/{UNIT_SIZE}
          </Typography>
          <Typography fontSize={11} fontWeight={600} color="white">
            Critical Condition
          </Typography>
        </Box>

        <Box
          flex={1}
          bgcolor="#E7A65F"
          borderRadius="8px"
          textAlign="center"
          p={1}
        >
          <Typography fontSize={13} fontWeight={600} color="white">
            {unit.lightlyWounded}/{UNIT_SIZE}
          </Typography>
          <Typography fontSize={11} fontWeight={600} color="white">
            Minor Injuries
          </Typography>
        </Box>

        <Box
          flex={1}
          bgcolor="#648CA6"
          borderRadius="8px"
          textAlign="center"
          p={1}
        >
          <Typography fontSize={13} fontWeight={600} color="white">
            {unit.immobileUnconscious}/{UNIT_SIZE}
          </Typography>
          <Typography fontSize={11} fontWeight={600} color="white">
            Disabled
          </Typography>
        </Box>

        <Box
          flex={1}
          bgcolor="#4A4A4A"
          borderRadius="8px"
          textAlign="center"
          p={1}
        >
          <Typography fontSize={13} fontWeight={600} color="white">
            {unit.dead}/{UNIT_SIZE}
          </Typography>
          <Typography fontSize={11} fontWeight={600} color="white">
            Fallen
          </Typography>
        </Box>
      </Box>
    </Box>
  );
};

export default UnitAccordionDetails;
