import { AccordionDetails, Box } from "@mui/material";
import VitalsSection from "./VitalsSection";
import ChartsGrid from "./ChartsGrid";
import MapSection from "./MapSection";
import type { RelatedSoldier, Snapshot } from "../../interface/alert.interface";

interface AlertDetailsProps {
  relatedSoldier: RelatedSoldier;
  commandAttributes: Record<string, string | object>;
  snapshotHistory: Snapshot[];
}

const AlertDetails = ({
  relatedSoldier,
  commandAttributes,
  snapshotHistory,
}: AlertDetailsProps) => {
  const lastSnapshot = snapshotHistory.at(-1);
  const location = lastSnapshot?.objectDetails.location;

  return (
    <AccordionDetails>
      <Box
        sx={{
          display: "grid",
          gridTemplateColumns: "1fr 2fr 1fr",
          alignItems: "stretch",
          gap: 2,
        }}
      >
        <VitalsSection
          lastSnapshot={snapshotHistory.at(-1)!}
          relatedSoldier={relatedSoldier}
          commandAttributes={commandAttributes}
        />
        <ChartsGrid
          commandAttributes={commandAttributes}
          snapshotHistory={snapshotHistory}
        />
        {location && <MapSection location={location} />}
      </Box>
    </AccordionDetails>
  );
};

export default AlertDetails;
