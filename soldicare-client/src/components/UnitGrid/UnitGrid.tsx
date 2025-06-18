import React from "react";
import { Grid, Box } from "@mui/material";
import type { UnitData } from "../../interface/UnitData.interface";
import UnitCard from "./UnitCard";

interface Props {
  units: UnitData[];
  expandedIndices: number[];
  onToggle: (index: number) => void;
  displaySpecificUnit: (unitId: string) => void;
}

const UnitGrid: React.FC<Props> = ({
  units,
  expandedIndices,
  onToggle,
  displaySpecificUnit,
}) => {
  return (
    <Box sx={{ flexGrow: 1 }}>
      <Grid container spacing={2}>
        {units.map((unit, i) => (
          <Grid key={unit.id.objectId} size={{ xs: 12, sm: 6, md: 4 }}>
            <UnitCard
              unit={unit}
              isExpanded={expandedIndices.includes(i)}
              onToggle={() => onToggle(i)}
              displaySpecificUnit={displaySpecificUnit}
            />
          </Grid>
        ))}
      </Grid>
    </Box>
  );
};

export default UnitGrid;
