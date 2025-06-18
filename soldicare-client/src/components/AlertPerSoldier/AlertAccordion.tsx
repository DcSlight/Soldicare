import { Accordion } from "@mui/material";
import AlertSummary from "./AlertSummary";
import AlertDetails from "./AlertDetails";
import { useState } from "react";
import type { RelatedSoldier, Snapshot } from "../../interface/alert.interface";

interface AlertAccordionProps {
  soldierName: string;
  soldierSerial?: string;
  unitName: string;
  timestamp: string;
  status: string;
  severity: string;
  profileImage: string;
  commandAttributes: Record<string, string | object>;
  snapshotHistory: Snapshot[];
  relatedSoldier: RelatedSoldier;
}

const AlertAccordion = (props: AlertAccordionProps) => {
  const [expanded, setExpanded] = useState(false);
  return (
    <Accordion
      sx={{
        marginBottom: 2,
        borderRadius: 3,
        "&::before": {
          display: "none",
        },
      }}
      expanded={expanded}
      onChange={(_, isExpanded) => setExpanded(isExpanded)}
    >
      <AlertSummary {...props} expanded={expanded} />
      <AlertDetails
        relatedSoldier={props.relatedSoldier}
        commandAttributes={props.commandAttributes}
        snapshotHistory={props.snapshotHistory}
      />
    </Accordion>
  );
};

export default AlertAccordion;
